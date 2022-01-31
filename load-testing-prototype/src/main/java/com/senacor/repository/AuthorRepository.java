package com.senacor.repository;

import com.senacor.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "authors")
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
