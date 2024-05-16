package com.catalog.product.service

import com.catalog.product.model.Product
import com.catalog.product.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service


@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(pageNumber: Int, pageSize: Int): Page<Product> {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        return productRepository.findAll(pageRequest)
    }

    fun saveProduct(product: Product): Product {
        return productRepository.save(product)
    }
}