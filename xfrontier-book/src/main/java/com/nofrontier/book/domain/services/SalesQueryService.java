package com.nofrontier.book.domain.services;

import java.util.List;

import com.nofrontier.book.core.filters.DailySalesFilter;
import com.nofrontier.book.domain.model.dto.DailySales;

public interface SalesQueryService {

	List<DailySales> consultDailySales(DailySalesFilter filter, String timeOffset);
	
}
