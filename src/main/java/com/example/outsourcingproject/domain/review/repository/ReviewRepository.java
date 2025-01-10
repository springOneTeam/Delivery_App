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

    List<Review> findByStore_StoreIdOrderByCreatedAtDesc(Long storeId);

    // List<Review> findByStore_IdAndRatingBetween(Long storeId, int minRating, int maxRating);

    List<Review> findByStore_StoreIdAndRatingBetweenOrderByCreatedAtDesc(Long storeId, int minRating, int maxRating);

}
