package br.com.xfrontier.book.domain.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DailySales {

	private Date date;
	private Long totalSales;
	private BigDecimal totalBilled;

	public DailySales(Date data, Long totalSales, BigDecimal totalBilled) {
		this.date = new Date(data.getTime());
		this.totalSales = totalSales;
		this.totalBilled = totalBilled;
	}
}
