package br.com.xfrontier.book.core.services.gatewaypayment.adpaters;

import java.util.Set;

import br.com.xfrontier.book.domain.model.Book;
import br.com.xfrontier.book.domain.model.PaymentMethod;

public interface GatewayPaymentService {

	PaymentMethod pay(Book book, Set<Book> books, String cardHash);

	PaymentMethod makeTotalRefund(Book book, Set<Book> books);

	PaymentMethod makePartialRefund(Book book, Set<Book> books);

}
