package br.com.xfrontier.book.domain.services;

import br.com.xfrontier.book.core.filters.DailySalesFilter;

public interface SalesReportService {

	byte[] issueDailySales(DailySalesFilter filter, String timeOffset);
	
}
