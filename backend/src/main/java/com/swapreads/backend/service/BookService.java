package com.swapreads.backend.service;

import com.swapreads.backend.dto.BookRequest;
import com.swapreads.backend.dto.BookResponse;
import com.swapreads.backend.entity.Book;
import com.swapreads.backend.entity.User;
import com.swapreads.backend.repository.BookRepository;
import com.swapreads.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookResponse createBook(BookRequest request, String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setGenre(request.getGenre());
        book.setCondition(request.getCondition());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setAvailableForExchange(request.isAvailableForExchange());
        book.setImageUrl(request.getImageUrl());
        book.setOwner(owner);

        return BookResponse.fromEntity(bookRepository.save(book));
    }

    public Page<BookResponse> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        return bookRepository.findAll(pageable)
                .map(BookResponse::fromEntity);
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found with id: " + id));
        return BookResponse.fromEntity(book);
    }

    public Page<BookResponse> searchBooks(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        keyword, keyword, pageable)
                .map(BookResponse::fromEntity);
    }

    public Page<BookResponse> filterBooks(String genre, String condition,
                                          Double minPrice, Double maxPrice,
                                          Boolean availableForExchange,
                                          int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        return bookRepository.findWithFilters(
                        genre, condition, minPrice, maxPrice,
                        availableForExchange, pageable)
                .map(BookResponse::fromEntity);
    }

    public List<BookResponse> getMyBooks(String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookRepository.findByOwner(owner)
                .stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public BookResponse updateBook(Long id, BookRequest request, String email) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        if (!book.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("You can only update your own books");
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setGenre(request.getGenre());
        book.setCondition(request.getCondition());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setAvailableForExchange(request.isAvailableForExchange());
        book.setImageUrl(request.getImageUrl());

        return BookResponse.fromEntity(bookRepository.save(book));
    }

    public void deleteBook(Long id, String email) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        if (!book.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own books");
        }

        bookRepository.deleteById(id);
    }
//    public List<Book> getAllBooks() {
//        return bookRepository.findAll();
//    }
//
//    public Book getBookById(Long id) {
//        return bookRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
//    }
//
//    public Book createBook(Book book) {
//        return bookRepository.save(book);
//    }

//    public Book updateBook(Long id, Book updatedBook) {
//        Book existing = getBookById(id);
//        existing.setTitle(updatedBook.getTitle());
//        existing.setAuthor(updatedBook.getAuthor());
//        existing.setGenre(updatedBook.getGenre());
//        existing.setPrice(updatedBook.getPrice());
//        existing.setCondition(updatedBook.getCondition());
//        existing.setDescription(updatedBook.getDescription());
//        existing.setAvailableForExchange(updatedBook.isAvailableForExchange());
//        return bookRepository.save(existing);
//    }
//
//    public void deleteBook(Long id) {
//        bookRepository.deleteById(id);
//    }
//
//    public List<Book> searchByTitle(String title) {
//        return bookRepository.findByTitleContainingIgnoreCase(title);
//    }

//    public List<Book> getBooksByGenre(String genre) {
//        return bookRepository.findByGenre(genre);
//    }
//
//    public List<Book> getBooksAvailableForExchange() {
//        return bookRepository.findByAvailableForExchangeTrue();
//    }
}
