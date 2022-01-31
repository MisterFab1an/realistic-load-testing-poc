package com.senacor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class Author {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @Column
    private String firstName;

    @Getter
    @Setter
    @Column
    private String lastName;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.ALL})
    @Getter
    private List<Book> books;

    public Author(String firstName, String lastName, List<Book> books) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = books;
    }

    public Author(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    public void addBook(Book book) {
        if (book != null) {
            book.setAuthor(this);

            if (books == null)
                books = new ArrayList<>();

            books.add(book);
        }
    }
}
