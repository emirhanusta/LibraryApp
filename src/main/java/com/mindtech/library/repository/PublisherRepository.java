package com.mindtech.library.repository;

import com.mindtech.library.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);

    @Query("""
            select distinct p from Publisher p
            left join fetch p.books b
            left join fetch b.author
            where p.id in :ids
            """)
    Page<Publisher> findPublishersWithBooksAndAuthors(@Param("ids") Iterable<Long> ids, Pageable pageable);

    @Query("select p.id from Publisher p")
    Page<Long> findAllPublisherIds(Pageable pageable);
}
