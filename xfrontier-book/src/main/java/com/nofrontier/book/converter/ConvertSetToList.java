package com.nofrontier.book.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ConvertSetToList {

	public static final <T> List<T> convertSetToList(Set<T> entitySet) {
		return new ArrayList<>(entitySet);
	}

}
