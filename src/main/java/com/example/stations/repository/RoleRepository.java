package com.example.stations.repository;

import com.example.stations.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole,Long> {
    Optional<UserRole> findByName(String role);
    @Query(value = "select r.* from user_roles as ur\n" +
            "inner join roles as r on r.id=ur.role_id\n" +
            "where ur.user_id=?1", nativeQuery = true)
    List<UserRole> findByUserId(Long id);
}
