package com.example.ShopeeClone.controller;

import com.example.ShopeeClone.entity.Product;
import com.example.ShopeeClone.entity.ResponseObject;
import com.example.ShopeeClone.repositories.ProductsRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
@CrossOrigin
@RequestMapping(path = "/api/v1/Products")

public class ProductController {
    @Autowired
    private ProductsRepositories productsRepositories;


    @GetMapping("")

    public ResponseEntity<ResponseObject> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort_by,
            @RequestParam(defaultValue = "desc") String order
    ) {
        Sort sort = order.equals("asc") ? Sort.by(sort_by).ascending() : Sort.by(sort_by).descending();
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Product> productPage = productsRepositories.findAll(pageable);
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
        if (!optionalProduct.isPresent()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
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
        return  ResponseEntity.status(HttpStatus.OK).body(
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
