package br.com.xfrontier.book.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xfrontier.book.domain.model.TokenBlackList;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {
	
	boolean existsByToken(String token);

}
