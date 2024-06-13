package com.nofrontier.book.dto.v1.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

//	private Boolean authenticated;
//	private Date created;
//	private Date expiration;
	
    private String access;
    private String refresh;

}