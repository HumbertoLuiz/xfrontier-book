package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HateoasResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_links")
	private List<LinkResponse> links;
	
	public HateoasResponse() {
		links = new ArrayList<>();
	}
	
	public void addLinks(Link... links) {
		for (Link link : links) {
			var linkResponse = new LinkResponse();
			linkResponse.setUri(link.getHref());
			linkResponse.setType(link.getType());
			linkResponse.setRel(link.getRel().value());
			
			this.links.add(linkResponse);
		}
	}

	public List<LinkResponse> getLinks() {
		return links;
	}

	public void setLinks(List<LinkResponse> links) {
		this.links = links;
	}

	@Override
	public int hashCode() {
		return Objects.hash(links);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HateoasResponse other = (HateoasResponse) obj;
		return Objects.equals(links, other.links);
	}

}


