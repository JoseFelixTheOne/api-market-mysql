package com.platzi.market.domain.service;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll(){
        return productRepository.getAll();
    }
    public Optional<Product> getProduct(int productId){
        return productRepository.getProduct(productId);
    }
    public Optional<List<Product>> getByCategory(int categoryId){
        return  productRepository.getByCategory(categoryId);
    }
    public Product save(Product product){
        return productRepository.save(product);
    }
    public Product update(Product product){
        int productId = product.getProductId();
        Product prod = getProduct(productId).map(p -> {
            BeanUtils.copyProperties(product,p);
            return p;
        }).orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
        return productRepository.save(prod);
    }
    /*
    otra manera de hacer el update, pero en teoría no es tan buena si se trata con datos importantes
    en mi gusto preferiría usar la segunda opción pero usaré la primera por si acaso
    public Product update(Product product) {
        int productId = product.getProductId();
        Product prod = getProduct(productId).orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
        return productRepository.save(product);
    }
     */

    public boolean delete(int productId){
        return getProduct(productId).map(product -> {
            productRepository.delete(productId);
            return true;
        }).orElse(false);
    }
    /*
        otra forma de hacer el delete
        if (getProduct(productId).isPresent()){
            productRepository.delete(productId);
            return true;
        } else{
            return false;
        }

        pero la mejor forma de ahorrar recursos es usando un try catch
        try {
            productRepository.delete(productId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
     */

}
