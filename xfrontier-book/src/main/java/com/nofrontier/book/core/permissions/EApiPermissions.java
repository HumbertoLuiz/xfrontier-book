package com.nofrontier.book.core.permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

public @interface EApiPermissions {

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isAdminOrCustomer {}

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isCustomer {}

    @PreAuthorize("hasAuthority('ADMIN')")
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface isAdmin {}

}
