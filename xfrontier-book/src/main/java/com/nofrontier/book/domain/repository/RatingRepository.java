package com.nofrontier.book.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nofrontier.book.domain.model.Book;
import com.nofrontier.book.domain.model.Rating;
import com.nofrontier.book.domain.model.User;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	
	boolean existsByBookAndRater(Book book, User rater);

    @Query(
        """
        SELECT
            AVG(r.score)
        FROM
            Rating r
        WHERE
            r.book = :rater
        """
    )
    Double getRatingAverage(User rater);

    Page<Rating> findByRater(User rater, Pageable pageable);

    default List<Rating> getLastReviews(User rater) {
        var ratingSort = Sort.sort(Rating.class);
        var sort = ratingSort.by(Rating::getCreatedAt).descending();
        var pageable = PageRequest.of(0, 2, sort);
        return findByRater(rater, pageable).getContent();
    }
    
    @Query(
            """
            SELECT
                COUNT(*) = 2
            FROM
                Rating r
            WHERE
                r.book = :book
            """
        )
    
        boolean isCustomerRatedBook(Book book);

        boolean existsByRaterAndBookId(User rater, Long bookId);

}
