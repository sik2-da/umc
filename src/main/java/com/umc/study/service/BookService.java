package com.umc.study.service;

import com.umc.study.dto.BookResponse;
import com.umc.study.repository.BookRepository;
import com.umc.study.repository.JpaBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import com.umc.study.dto.CreateBookRequest;
import com.umc.study.entity.Book;
import com.umc.study.entity.Category;
import com.umc.study.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final JpaBookRepository jpaBookRepository;
    private final CategoryRepository categoryRepository;
    @Transactional(readOnly = true)
    public List<BookResponse> getBooks() {
        return jpaBookRepository.findAllByOrderByBookIdDesc().stream()
                .map(BookResponse::from)
                .toList();
    }

    @Transactional
    public BookResponse createBook(CreateBookRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "존재하지 않는 카테고리입니다."
                ));

        Book book = new Book(
                category,
                request.title(),
                request.description()
        );

        return BookResponse.from(jpaBookRepository.save(book));
    }

    public List<Map<String, Object>> getBooksByCategoryId(int categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }
}