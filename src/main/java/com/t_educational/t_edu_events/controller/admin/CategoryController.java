package com.t_educational.t_edu_events.controller.admin;

import com.t_educational.t_edu_events.model.Category;
import com.t_educational.t_edu_events.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    // GET /admin/categories — получение списка всех направлений
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    // POST /admin/categories — создание нового направления
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // Если id генерируется вручную, убедитесь, что он уникален.
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    // PUT /admin/categories/{categoryID} — обновление данных направления
    @PutMapping("/{categoryID}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryID") UUID categoryID,
                                                   @RequestBody Category updatedCategory) {
        return categoryRepository.findById(categoryID)
                .map(existingCategory -> {
                    existingCategory.setName(updatedCategory.getName());
                    Category saved = categoryRepository.save(existingCategory);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoryID}")
    public ResponseEntity<Object> deleteCategory(@PathVariable("categoryID") UUID categoryID) {
        return categoryRepository.findById(categoryID)
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

}
