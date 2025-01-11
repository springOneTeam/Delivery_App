package com.example.outsourcingproject.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.user.entity.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
	Optional<Store> findByStoreNameAndIsOperatingTrue(String storeName);
	List<Store> findAllByStoreNameAndIsOperatingTrue(String storeName);
	List<Store> findAllByIsOperatingTrue();;
	long countByOwnerAndIsOperatingTrue(User owner);  // 운영 중인 가게만 카운트

}
