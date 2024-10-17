package br.com.xfrontier.book.dto.v1;

import java.io.Serializable;
import java.util.Objects;

public class LinkResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;
	
	private String rel;
	
	private String uri;

	public LinkResponse() {}

	public LinkResponse(String type, String rel, String uri) {
		this.type = type;
		this.rel = rel;
		this.uri = uri;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rel, type, uri);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkResponse other = (LinkResponse) obj;
		return Objects.equals(rel, other.rel)
				&& Objects.equals(type, other.type)
				&& Objects.equals(uri, other.uri);
	}
	
}
