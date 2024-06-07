package com.nofrontier.book.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nofrontier.book.core.filters.DailySalesFilter;
import com.nofrontier.book.domain.services.SalesQueryService;
import com.nofrontier.book.domain.services.SalesReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfVendaReportService implements SalesReportService {

	@Autowired
	private SalesQueryService salesQueryService;

	public byte[] issueDailySales(DailySalesFilter filter, String timeOffset) {
		try {
			var inputStream = this.getClass()
					.getResourceAsStream("/reports/daily-sales.jasper");

			var parameters = new HashMap<String, Object>();
			parameters.put("REPORT_LOCALE", Locale.of("pt", "BR"));

			var dailySales = salesQueryService.consultDailySales(filter,
					timeOffset);
			var dataSource = new JRBeanCollectionDataSource(dailySales);

			var jasperPrint = JasperFillManager.fillReport(inputStream,
					parameters, dataSource);

			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Unable to issue daily sales report", e);
		}
	}

}
