package com.example.outsourcingproject.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.user.entity.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
	long countByOwner(User owner);
	// 정확한 가게명으로 단건 조회
	Optional<Store> findByStoreName(String storeName);
	List<Store> findAllByStoreName(String storeName);
	Optional<Store> findByStoreNameAndIsOperatingTrue(String storeName);
	List<Store> findAllByStoreNameAndIsOperatingTrue(String storeName);
	List<Store> findAllByIsOperatingTrue();;
}
