package com.mindtech.library.repository;

import com.mindtech.library.entity.Publisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);

    @Query("select p.id from Publisher p order by p.id")
    List<Long> findFirstNPublisherIds(Pageable pageable);

    @Query("""
            select distinct p from Publisher p
            left join fetch p.books b
            left join fetch b.author a
            where p.id in :ids
            order by p.id
            """)
    List<Publisher> findPublishersByIdsWithBooksAndAuthors(@Param("ids") List<Long> ids);
}