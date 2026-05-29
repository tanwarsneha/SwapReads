package com.swapreads.backend.repository;

import com.swapreads.backend.entity.Book;
import com.swapreads.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByOwner(User owner);
    Page<Book> findByAvailableForExchangeTrue(Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title, String author, Pageable pageable);

    Page<Book> findByGenreIgnoreCase(String genre, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE " +
            "(:genre IS NULL OR LOWER(b.genre) = LOWER(:genre)) AND " +
            "(:condition IS NULL OR LOWER(b.condition) = LOWER(:condition)) AND " +
            "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR b.price <= :maxPrice) AND " +
            "(:availableForExchange IS NULL OR " +
            "b.availableForExchange = :availableForExchange)")
    Page<Book> findWithFilters(
            @Param("genre") String genre,
            @Param("condition") String condition,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("availableForExchange") Boolean availableForExchange,
            Pageable pageable);

}
