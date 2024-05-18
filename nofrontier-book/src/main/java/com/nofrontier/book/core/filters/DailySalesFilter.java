package com.nofrontier.book.core.filters;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DailySalesFilter {

	private Long orderId;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime initialCreationDate;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime exitCreationDate;
	
}
