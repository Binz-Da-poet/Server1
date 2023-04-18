package com.example.ShopeeClone.controller;

import com.example.ShopeeClone.entity.Category;
import com.example.ShopeeClone.entity.Product;
import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.repositories.CategoryRepositories;
import com.example.ShopeeClone.repositories.ProductsRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;


@Controller
@CrossOrigin
@RequestMapping(path = "/api/v1/Products")

public class ProductController {
    @Autowired
    private ProductsRepositories productsRepositories;

    @Autowired
    private CategoryRepositories categoryRepository;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort_by,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) Long categoryId, // thêm tham số category_id
            @RequestParam(required = false) Integer rating, // thêm tham số rating
            @RequestParam(required = false) Integer price_min, // thêm tham số price_min
            @RequestParam(required = false) Integer price_max // thêm tham số price_max
    ) {
        Sort sort;
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "cannot found category", "")
                );
            }
            sort = order.equals("asc") ? Sort.by(sort_by).ascending().and(Sort.by("category.name").ascending()) :
                    Sort.by(sort_by).descending().and(Sort.by("category.name").ascending());

        } else if (rating != null) {
            sort = order.equals("asc") ? Sort.by("rating").ascending() : Sort.by("rating").descending();
        } else if (price_min != null && price_max != null) {
            sort = order.equals("asc") ? Sort.by("price").ascending() : Sort.by("price").descending();
        } else {
            sort = order.equals("asc") ? Sort.by(sort_by).ascending() : Sort.by(sort_by).descending();
        }


        Page<Product> productPage;
        if (price_min != null && price_max != null) {
            Pageable pageable = PageRequest.of(page - 1, limit, sort);
            productPage = categoryId != null ?
                    productsRepositories.findAllByCategoryAndPriceBetween(categoryRepository.findById(categoryId).orElse(null), price_min, price_max, pageable) :
                    productsRepositories.findAllByPriceBetween(price_min, price_max, pageable);
        } else if (rating != null) {
            Pageable pageable = PageRequest.of(page - 1, limit, sort);
            productPage = categoryId != null ?
                    productsRepositories.findAllByCategoryAndRatingGreaterThanEqual(categoryRepository.findById(categoryId).orElse(null), rating, pageable) :
                    productsRepositories.findAllByRatingGreaterThanEqual(rating, pageable);
        } else {
            Pageable pageable = PageRequest.of(page - 1, limit, sort);
            productPage = categoryId != null ?
                    productsRepositories.findAllByCategory(categoryRepository.findById(categoryId).orElse(null), pageable) :
                    productsRepositories.findAll(pageable);
        }
        ResponseObject response = new ResponseObject("success", "Retrieved all products", productPage);
        return ResponseEntity.ok(response);

    }


    @PostMapping(value = "/add")
    public ResponseEntity<ResponseObject> addProduct(@RequestBody Product newProduct) {
        newProduct.setCreatedAt(new Date());
        productsRepositories.save(newProduct);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok ", " đã add product thành công ", newProduct)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateProduct(@PathVariable Long id, @RequestBody @Valid Product updatedProduct) {
        Optional<Product> optionalProduct = productsRepositories.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "cannot found product", "")
            );
        }

        Product product = optionalProduct.get();
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setDiscount_Price(updatedProduct.getDiscount_Price());
        product.setRating(updatedProduct.getRating());
        product.setSold(updatedProduct.getSold());
        product.setQuantity(updatedProduct.getQuantity());
        product.setView(updatedProduct.getView());
        product.setDescription(updatedProduct.getDescription());


        productsRepositories.save(product);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update successfully", "")
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteProductByid(@PathVariable Long id) {
        Optional<Product> existProduct = productsRepositories.findById(id);
        return existProduct.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Deleted Product", existProduct)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "cannot found product", "")
        );
    }
}
