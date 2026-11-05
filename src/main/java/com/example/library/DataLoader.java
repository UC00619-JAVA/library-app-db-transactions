package com.example.library;

import java.util.List;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import com.example.library.service.LoanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Runs once, right after the application starts
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final LoanService loanService;

    @Override
    public void run(String... args) {
        // The database is a file: the books are only saved the first time
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("Clean Code", "Robert C. Martin", 2008));
            bookRepository.save(new Book("The Clean Coder", "Robert C. Martin", 2011));
            bookRepository.save(new Book("Effective Java", "Joshua Bloch", 2018));
            bookRepository.save(new Book("Head First Java", "Kathy Sierra", 2005));
            log.info("4 books saved");
        }

        log.info("Total books: {}", bookRepository.count());
        for (Book book : bookRepository.findAll()) {
            log.info("{} - {} ({})", book.getId(), book.getTitle(), book.getPublishedYear());
        }

        printTitles("By Robert C. Martin", bookRepository.findByAuthor("Robert C. Martin"));
        printTitles("Published after 2010", bookRepository.findByPublishedYearGreaterThan(2010));
        printTitles("Title contains \"java\"", bookRepository.findByTitleContainingIgnoreCase("java"));

        // Exercise 4 — queries and transactions
        log.info("Books returned: {}", loanService.returnAll());
        printTitles("Available (JPQL)", bookRepository.findAvailableBooks());

        loanService.borrowAll(List.of(1L, 2L));
        try {
            // book 1 is already borrowed: the whole transaction is rolled back
            loanService.borrowAll(List.of(3L, 1L));
        } catch (IllegalStateException e) {
            log.warn("Loan failed: {}", e.getMessage());
        }
        printTitles("Available now", bookRepository.findAvailableBooks());

        log.info("Published before 2010 (native SQL): {}", bookRepository.countPublishedBefore(2010));
    }

    private void printTitles(String label, List<Book> books) {
        log.info("{}:", label);
        for (Book book : books) {
            log.info("  - {}", book.getTitle());
        }
    }
}
