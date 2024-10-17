package br.com.xfrontier.book.core.services.gatewaypayment.providers;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.xfrontier.book.core.enums.PaymentStatus;
import br.com.xfrontier.book.core.services.gatewaypayment.adpaters.GatewayPaymentService;
import br.com.xfrontier.book.core.services.gatewaypayment.providers.dtos.PayMeBackRequest;
import br.com.xfrontier.book.core.services.gatewaypayment.providers.dtos.PayMeBackResponse;
import br.com.xfrontier.book.core.services.gatewaypayment.providers.dtos.PayMeTransactionRequest;
import br.com.xfrontier.book.core.services.gatewaypayment.providers.dtos.PayMeTransactionResponse;
import br.com.xfrontier.book.domain.exceptions.GatewayPaymentException;
import br.com.xfrontier.book.domain.exceptions.PaymentMethodNotFoundException;
import br.com.xfrontier.book.domain.model.Book;
import br.com.xfrontier.book.domain.model.PaymentMethod;
import br.com.xfrontier.book.domain.repository.PaymentMethodRepository;

@Service
public class PayMeService implements GatewayPaymentService {

    private static final String BASE_URL = "https://api.pay.me/1";

    private final RestTemplate clientHttp;
    private final String apiKey;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ObjectMapper objectMapper;

    public PayMeService(
            @Value("${com.nofrontier.payme.apiKey}") String apiKey,
            PaymentMethodRepository paymentMethodRepository,
            ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.paymentMethodRepository = paymentMethodRepository;
        this.objectMapper = objectMapper;
        this.clientHttp = new RestTemplate();
    }

    public PaymentMethod pay(Book book, Set<Book> books, String cardHash) {
        try {
            return tryPay(book, books, cardHash);
        } catch (HttpClientErrorException.BadRequest exception) {
            var responseBody = exception.getResponseBodyAsString();
            var jsonNode = getJsonNode(responseBody);
            var message = jsonNode.path("errors").path(0).path("message")
                    .asText();
            throw new GatewayPaymentException(message);
        }
    }


    public PaymentMethod makeTotalRefund(Book book, Set<Book> books) {
        try {
            return tryMakeTotalRefund(book, books);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new GatewayPaymentException(exception.getLocalizedMessage());
        }
    }

    public PaymentMethod makePartialRefund(Book book, Set<Book> books) {
        try {
            return tryMakePartialRefund(book, books);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new GatewayPaymentException(exception.getLocalizedMessage());
        }
    }

    private PaymentMethod tryMakePartialRefund(Book book, Set<Book> books) {
        var request = PayMeBackRequest.builder()
                .amount(convertRealToCents(book.getPrice().divide(new BigDecimal(2))))
                .apiKey(apiKey).build();
        return makeRefund(book, books, request);
    }

    private PaymentMethod tryMakeTotalRefund(Book book, Set<Book> books) {
        var request = PayMeBackRequest.builder().apiKey(apiKey).build();
        return makeRefund(book, books, request);
    }

    private PaymentMethod makeRefund(Book book, Set<Book> books, PayMeBackRequest request) {
        validateBookForRefund(book);
        var payment = getPaymentBook(book);
        var url = BASE_URL + "/transactions/{transaction_id}/refund";
        var response = clientHttp.postForEntity(url, request, PayMeBackResponse.class, payment.getTransactionId());
        return createPayment(book, books, response.getBody());
    }

    private void validateBookForRefund(Book book) {
        var isNotPaidOrNotRated = !(book.isPaid() || book.isRated());
        if (isNotPaidOrNotRated) {
            throw new IllegalArgumentException("No refunds can be made for books that have not been paid for");
        }
    }

    private PaymentMethod getPaymentBook(Book book) {
        return book.getPaymentMethods().stream()
                .filter(this::isPaymentAccepted)
                .findFirst()
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment Method Not Found"));
    }

    private boolean isPaymentAccepted(PaymentMethod payment) {
        return payment.getPaymentStatus().equals(PaymentStatus.ACCEPTED);
    }

    private PaymentMethod tryPay(Book book, Set<Book> books, String cardHash) {
        var transactionRequest = createTransactionRequest(book, cardHash);
        var url = BASE_URL + "/transactions";
        var response = clientHttp.postForEntity(url, transactionRequest, PayMeTransactionResponse.class);
        return createPayment(book, books, response.getBody());
    }

    private PaymentMethod createPayment(Book book, Set<Book> books, PayMeTransactionResponse body) {
        var payment = PaymentMethod.builder()
                .value(book.getPrice())
                .transactionId(body.getId())
                .books(books)
                .paymentStatus(createPaymentStatus(body.getStatus()))
                .build();
        return paymentMethodRepository.save(payment);
    }

    private PaymentMethod createPayment(Book book, Set<Book> books, PayMeBackResponse body) {
        var payment = PaymentMethod.builder()
                .value(convertCentsToReal(body.getRefundedAmount()))
                .transactionId(body.getId())
                .books(books)
                .paymentStatus(PaymentStatus.REFUNDED)
                .build();
        return paymentMethodRepository.save(payment);
    }

    private PaymentStatus createPaymentStatus(String transactionStatus) {
        return transactionStatus.equals("paid") ? PaymentStatus.ACCEPTED : PaymentStatus.REJECTED;
    }

    private PayMeTransactionRequest createTransactionRequest(Book book, String cardHash) {
        return PayMeTransactionRequest.builder()
                .amount(convertRealToCents(book.getPrice()))
                .cardHash(cardHash)
                .async(false)
                .apiKey(apiKey)
                .build();
    }

    private Integer convertRealToCents(BigDecimal price) {
        return price.multiply(new BigDecimal(100)).intValue();
    }

    private BigDecimal convertCentsToReal(Integer price) {
        return new BigDecimal(price).divide(new BigDecimal(100));
    }

    private JsonNode getJsonNode(String responseBody) {
        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException exception) {
            throw new GatewayPaymentException(exception.getLocalizedMessage());
        }
    }
}