package com.mindtech.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
public class Author extends BaseEntity {

    @Column(name = "author_name_surname", nullable = false)
    private String nameSurname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private Book book;

    public Author(final String nameSurname, final Book book) {
        this.nameSurname = nameSurname;
        this.book = book;
    }
}