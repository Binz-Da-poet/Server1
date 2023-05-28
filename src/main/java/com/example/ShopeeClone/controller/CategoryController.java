package com.example.ShopeeClone.controller;

import com.example.ShopeeClone.Request.CategoryRequest;
import com.example.ShopeeClone.entity.Category;
import com.example.ShopeeClone.entity.Product;
import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.repositories.CategoryRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path = "/api/v1/category")
public class CategoryController {


    @Autowired
    private CategoryRepositories categoryRepositories;


    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllCategory() {
        List<Category> categories = categoryRepositories.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok ", " get All category ", categories)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/{name}")
    public ResponseEntity<ResponseObject> createCategory(@PathVariable String name) {
        Optional<Category> foundCategory = categoryRepositories.findByName(name);
        if (foundCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Category is already", "")
            );
        }
        Category category = new Category();
        category.setName(name);
        categoryRepositories.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add category Successfully", category)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteCategory(@RequestBody CategoryRequest categoryRequest) {
        List<Long> CategoryIds = categoryRequest.getCategoryIds();
        List<Category> CategoryToDelete = new ArrayList<>();
        for (Long categoryId : CategoryIds) {
            Optional<Category> optionalCategory = categoryRepositories.findById(categoryId);
            if (optionalCategory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find category with ID " + categoryId, "")
                );
            }
            Category category = optionalCategory.get();
            CategoryToDelete.add(category);
        }
        categoryRepositories.deleteAll(CategoryToDelete);
        return ResponseEntity.ok().body(
                new ResponseObject("success", "Category's deleted successfully", "")
        );
    }

}

