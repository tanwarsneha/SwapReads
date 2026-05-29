//package com.swapreads.backend.controller;
//
//import com.swapreads.backend.entity.Book;
//import com.swapreads.backend.service.BookService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/books")
//@CrossOrigin(origins = "http://localhost:3000")
//@RequiredArgsConstructor
//public class BookController {
//
//    private final BookService bookService;
//
//    @GetMapping
//    public List<Book> getAllBooks() {
//        return bookService.getAllBooks();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Book> getBook(@PathVariable Long id) {
//        return ResponseEntity.ok(bookService.getBookById(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<Book> createBook(@RequestBody Book book) {
//        return ResponseEntity.ok(bookService.createBook(book));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Book> updateBook(@PathVariable Long id,
//                                           @RequestBody Book book) {
//        return ResponseEntity.ok(bookService.updateBook(id, book));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
//        bookService.deleteBook(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/search")
//    public List<Book> searchBooks(@RequestParam String title) {
//        return bookService.searchByTitle(title);
//    }
//
//    @GetMapping("/genre/{genre}")
//    public List<Book> getByGenre(@PathVariable String genre) {
//        return bookService.getBooksByGenre(genre);
//    }
//
//    @GetMapping("/exchange")
//    public List<Book> getBooksForExchange() {
//        return bookService.getBooksAvailableForExchange();
//    }
//}

package com.swapreads.backend.controller;

import com.swapreads.backend.dto.BookRequest;
import com.swapreads.backend.dto.BookResponse;
import com.swapreads.backend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getAllBooks(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @RequestBody BookRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(bookService.createBook(request, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(bookService.updateBook(id, request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        bookService.deleteBook(id, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                bookService.searchBooks(keyword, page, size));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<BookResponse>> filterBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean availableForExchange,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.filterBooks(
                genre, condition, minPrice, maxPrice,
                availableForExchange, page, size));
    }

    @GetMapping("/my-books")
    public ResponseEntity<List<BookResponse>> getMyBooks(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(bookService.getMyBooks(email));
    }
}