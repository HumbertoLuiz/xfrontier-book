package com.nofrontier.book.domain.services;

import com.nofrontier.book.core.filters.DailySalesFilter;

public interface SalesReportService {

	byte[] issueDailySales(DailySalesFilter filter, String timeOffset);
	
}
