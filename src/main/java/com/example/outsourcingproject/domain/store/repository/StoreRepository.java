package com.example.outsourcingproject.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.user.entity.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
	@Query("SELECT COUNT(s) FROM Store s WHERE s.owner = :owner AND s.isOperating = true")
	int countActiveStoresByOwner(@Param("owner") User owner);
}
