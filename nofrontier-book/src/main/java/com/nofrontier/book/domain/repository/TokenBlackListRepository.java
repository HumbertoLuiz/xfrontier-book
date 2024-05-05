package com.nofrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.TokenBlackList;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

    boolean existsByToken(String token);

}
