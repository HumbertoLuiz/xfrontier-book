package com.nofrontier.book.core.filters;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.OffsetDateTime;

@Setter
@Getter
public class OrderFilter {

	private Long customerId;
	
	private Long orderId;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime initialCreationDate;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime exitCreationDate;
	
}
