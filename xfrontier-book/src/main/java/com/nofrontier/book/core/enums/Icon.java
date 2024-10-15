package com.nofrontier.book.core.enums;

public enum Icon {

    TWF_CLEANING_1("twf-cleaning-1"),
    TWF_CLEANING_2("twf-cleaning-2"),
    TWF_CLEANING_3("twf-cleaning-3");

    private String name;

    private Icon(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
