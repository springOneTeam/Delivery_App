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

    List<Review> findByStoreId(Long storeId);

    List<Review> findByStoreIdAndRatingBetween(Long storeId, int minRating, int maxRating);

}
