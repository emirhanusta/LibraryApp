package com.mindtech.library.repository;

import com.mindtech.library.entity.Book;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn13(String isbn13);

    @Query("""
            select b from Book b
            join fetch b.publisher
            left join fetch b.author
            """)
    Page<Book> findAllWithPublisherAndAuthor(Pageable pageable);

    @Query("""
            select b from Book b
            join fetch b.publisher
            left join fetch b.author
            where b.id = :id
            """)
    Optional<Book> findByIdWithPublisherAndAuthor(@Param("id") Long id);

    @Query("""
            select b from Book b
            join fetch b.publisher
            left join fetch b.author
            where b.publicationDate > :date
            """)
    Page<Book> findBooksPublishedAfter(@Param("date") LocalDate date, Pageable pageable);

    @NotNull List<Book> findAll();

    @Modifying
    @Query("delete from Book b where b.id = :id")
    void deleteBookById(@Param("id") Long id);
}
