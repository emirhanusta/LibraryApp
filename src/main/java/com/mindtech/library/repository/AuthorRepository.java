package com.mindtech.library.repository;

import com.mindtech.library.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("select a from Author a join fetch a.book")
    Page<Author> findAllWithBook(Pageable pageable);

    @Modifying
    @Query("delete from Author a where a.book.id = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);
}
