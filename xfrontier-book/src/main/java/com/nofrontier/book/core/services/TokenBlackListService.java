package com.nofrontier.book.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nofrontier.book.domain.exceptions.TokenBlackListException;
import com.nofrontier.book.domain.model.TokenBlackList;
import com.nofrontier.book.domain.repository.TokenBlackListRepository;

@Service
public class TokenBlackListService {

    @Autowired
    private TokenBlackListRepository repository;

    public void checkToken(String token) {
        if (repository.existsByToken(token)) { throw new TokenBlackListException(); }
    }

    public void putTokenOnBlackList(String token) {
        if (!repository.existsByToken(token)) {
            var tokenBlackList= new TokenBlackList();
            tokenBlackList.setToken(token);
            repository.save(tokenBlackList);
        }
    }

}
