package com.nofrontier.book.domain.exceptions;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date timestamp;
	private String message;
	private String details;
	
}