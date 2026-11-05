package com.example.library.repository;

import java.util.List;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// JpaRepository<Book, Long>: entity Book, primary key of type Long
// save, findAll, findById, count, deleteById... already exist
public interface BookRepository extends JpaRepository<Book, Long> {

    // Derived methods: Spring Data reads the method name and writes the SQL
    List<Book> findByAuthor(String author);

    List<Book> findByPublishedYearGreaterThan(int year);

    List<Book> findByTitleContainingIgnoreCase(String text);

    // JPQL: uses the class (Book) and its attributes (available, title)
    @Query("SELECT b FROM Book b WHERE b.available = true ORDER BY b.title")
    List<Book> findAvailableBooks();

    // Native SQL: uses the table (books) and its columns (published_year)
    @Query(value = "SELECT COUNT(*) FROM books WHERE published_year < :year", nativeQuery = true)
    long countPublishedBefore(@Param("year") int year);

    // UPDATE / DELETE need @Modifying (and a transaction)
    @Modifying
    @Query("UPDATE Book b SET b.available = true")
    int markAllAvailable();
}
