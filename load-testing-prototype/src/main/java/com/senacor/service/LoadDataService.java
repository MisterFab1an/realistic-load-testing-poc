package com.senacor.service;

import com.senacor.entity.Author;
import com.senacor.entity.Book;
import com.senacor.repository.AuthorRepository;
import com.senacor.repository.BookRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class LoadDataService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    public LoadDataService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;

        loadData();
    }

    @Transactional
    public void loadData() {
        // https://www.goodreads.com/author/show/45372.Robert_C_Martin?from_search=true&from_srp=true
        var martinRobert = new Author("Robert", "Martin");
        // https://www.goodreads.com/author/show/25169.Charles_Petzold?from_search=true&from_srp=true
        var petzoldCharles = new Author("Charles", "Petzold");
        // https://www.goodreads.com/author/show/104368.Eric_Evans
        var evansEric = new Author("Eric", "Evans");

        // save authors and use attached entities
        martinRobert = authorRepository.save(martinRobert);
        petzoldCharles = authorRepository.save(petzoldCharles);
        evansEric = authorRepository.save(evansEric);

        // https://www.goodreads.com/book/show/3735293-clean-code
        var cleanCode = new Book("Clean Code", "A Handbook of Agile Software Craftsmanship", "9780132350884", martinRobert);
        // https://www.goodreads.com/book/show/10284614-the-clean-coder
        var theCleanerCode = new Book("The Clean Coder", "A Code of Conduct for Professional Programmers", "9780137081073", martinRobert);
        // https://www.goodreads.com/book/show/18043011-clean-architecture
        var cleanArchitecture = new Book("Clean Architecture", null, "9780134494166", martinRobert);
        // https://www.goodreads.com/book/show/873375.UML_for_Java_Programmers
        var umlForJavaProgrammers = new Book("UML for Java Programmers", null, "9780131428485", martinRobert);

        // https://www.goodreads.com/book/show/44882.Code
        var code = new Book("Code", "The Hidden Language of Computer Hardware and Software", "9780735611313", petzoldCharles);
        // https://www.goodreads.com/book/show/420643.Programming_Windows
        var programmingWindows = new Book("Programming Windows", null, "9781572319950", petzoldCharles);
        // https://www.goodreads.com/book/show/420650.3D_Programming_for_Windows
        var programmingForWindows3d = new Book("3D Programming for Windows (Pro-Developer)", null, "9780735623941", petzoldCharles);

        // https://www.goodreads.com/book/show/179133.Domain_Driven_Design
        var domainDrivenDesign = new Book("Domain-Driven Design", "Tackling Complexity in the Heart of Software", "9780321125217", evansEric);
        // https://www.goodreads.com/book/show/4885462-so-far-from-home
        var soFarFromHome = new Book("So Far from Home", null, "9780731810680", evansEric);

        var books = new ArrayList<Book>()
        {
            {
                add(cleanCode);
                add(theCleanerCode);
                add(cleanArchitecture);
                add(umlForJavaProgrammers);
                add(code);
                add(programmingWindows);
                add(programmingForWindows3d);
                add(domainDrivenDesign);
                add(soFarFromHome);
            }
        };

        bookRepository.saveAll(books);
    }
}
