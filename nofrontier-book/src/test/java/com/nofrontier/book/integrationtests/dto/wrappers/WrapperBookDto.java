package com.nofrontier.book.integrationtests.dto.wrappers;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperBookDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @JsonProperty("_embedded")
    private BookEmbeddedDto embedded;

    public WrapperBookDto() {}

    public BookEmbeddedDto getEmbedded() {
        return embedded;
    }

    public void setEmbedded(BookEmbeddedDto embedded) {
        this.embedded = embedded;
    }
}