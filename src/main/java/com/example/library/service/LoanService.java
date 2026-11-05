package com.example.library.service;

import java.util.List;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    private final BookRepository bookRepository;

    @Transactional
    public void borrow(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + id);
        }
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book already borrowed: " + book.getTitle());
        }
        book.setAvailable(false);
        bookRepository.save(book);
        log.info("Borrowed: {}", book.getTitle());
    }

    // All or nothing: if one book fails, none of them is borrowed (rollback)
    @Transactional
    public void borrowAll(List<Long> ids) {
        for (Long id : ids) {
            borrow(id);
        }
    }

    // Every book goes back to the shelf
    @Transactional
    public int returnAll() {
        return bookRepository.markAllAvailable();
    }
}
