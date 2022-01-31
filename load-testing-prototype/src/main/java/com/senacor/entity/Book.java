package com.senacor.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class Book {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String title;

    @Getter
    @Setter
    @Column
    private String subtitle;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String isbn;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    private Author author;

    public Book(String title, String subtitle, String isbn, Author author) {
        this.title = title;
        this.subtitle = subtitle;
        this.isbn = isbn;
        this.author = author;
    }
}
