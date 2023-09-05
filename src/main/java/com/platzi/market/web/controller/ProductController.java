package com.platzi.market.web.controller;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    @Operation(summary = "Get all supermarket products")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<Product>> getAll(){
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Search a product with an ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
    })
    public ResponseEntity<Product> getProduct(@Parameter(description = "The id of the product", required = true, example = "7")
                                                  @PathVariable("id") int productId){
        return productService.getProduct(productId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("categoryId") int categoryId){
        return  productService.getByCategory(categoryId)
                .map(products -> new ResponseEntity<>(products, !products.isEmpty() ?  HttpStatus.OK : HttpStatus.NOT_FOUND))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /*
        otra forma de hacer el getByCategory, pero no tan buena como la que se usa
        return  productService.getByCategory(categoryId)
                .map(products -> !products.isEmpty() ? new ResponseEntity<>(products, HttpStatus.OK) : new ResponseEntity<List<Product>>(HttpStatus.NOT_FOUND))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
     */
    @PostMapping("/")
    public ResponseEntity<Product> save(@RequestBody Product product){
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }
    @PutMapping("/")
    public ResponseEntity<Product> update(@RequestBody Product product){return new ResponseEntity<>(productService.update(product),HttpStatus.OK);}
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int productId){
        return new ResponseEntity(productService.delete(productId) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
    /*
        forma larga de hacer el delete, pero mas facil de leer
        if (productService.delete(productId)){
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
     */
}
