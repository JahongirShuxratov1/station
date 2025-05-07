package com.example.stations.repository;

import com.example.stations.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findByUserId(Long id);

    @Query(value = "select * from token as t where t.token=?1",nativeQuery = true)
    Optional<Token> findByToken(String token);
}
