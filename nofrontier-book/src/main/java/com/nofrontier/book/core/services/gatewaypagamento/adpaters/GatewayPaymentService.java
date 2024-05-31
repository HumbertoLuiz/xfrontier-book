package com.nofrontier.book.core.services.gatewaypagamento.adpaters;

import java.util.Set;

import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.model.PaymentMethod;

public interface GatewayPaymentService {

	PaymentMethod pay(Book book, Set<Book> books, String cardHash);

	PaymentMethod makeTotalRefund(Book book, Set<Book> books);

	PaymentMethod makePartialRefund(Book book, Set<Book> books);

}
