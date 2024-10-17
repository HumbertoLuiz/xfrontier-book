package br.com.xfrontier.book.domain.services;

import java.util.List;

import br.com.xfrontier.book.core.filters.DailySalesFilter;
import br.com.xfrontier.book.domain.model.dto.DailySales;

public interface SalesQueryService {

	List<DailySales> consultDailySales(DailySalesFilter filter, String timeOffset);
	
}
