package com.example.outsourcingproject.domain.review.repository;

import com.example.outsourcingproject.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    default Review findByIdOrElseThrow(Long id ) {
        return findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist id = " + id));
    }

    List<Review> findByStore_StoreId(Long storeId);

    // List<Review> findByStoreIdAndRatingBetween(Long storeId, int minRating, int maxRating);

    // findByStoreIdAndRatingBetween 메서드에서 JPA는 'storeId'를 Store 엔티티의 속성으로 인식하려고 합니다.
    // 하지만 실제로는 Review 엔티티가 Store 엔티티 자체를 참조하고 있는 상황입니다.
    List<Review> findByStore_StoreIdAndRatingBetween(Long storeId, int minRating, int maxRating);

}
