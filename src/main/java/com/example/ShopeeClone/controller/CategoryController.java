package com.example.ShopeeClone.controller;

import com.example.ShopeeClone.entity.Category;
import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.repositories.CategoryRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/v1/category")
public class CategoryController {


        @Autowired
        private CategoryRepositories categoryRepositories;

        @GetMapping("")
        public ResponseEntity<ResponseObject> getAllCategory(){
            List<Category> categories = categoryRepositories.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok ", " đã add product thành công ", categories)
            );
        }
        @PostMapping("/add")
        public ResponseEntity<ResponseObject> createCategory(@RequestBody Category category) {
            Optional<Category> foundCategory = categoryRepositories.findByName( category.getName());
            if (foundCategory.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("failed", "Category is already", "")
                );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Add category Successfully", categoryRepositories.save(category))
            );
        }

    }

