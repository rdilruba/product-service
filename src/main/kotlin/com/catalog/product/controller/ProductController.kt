package com.catalog.product.controller

import com.amazonaws.services.s3.model.ObjectMetadata
import com.catalog.product.model.Product
import com.catalog.product.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ProductController(private val productService: ProductService) {

    @GetMapping("/products")
    fun getProducts(@RequestParam("page") pageNumber: Int, @RequestParam("size") pageSize: Int ): Page<Product> {
        return productService.getAllProducts(pageNumber, pageSize)
    }

    @PostMapping("/products")
    fun createProduct(@RequestPart("product") product: Product, @RequestPart("image") imageFile: MultipartFile): ResponseEntity<Product> {
        val inputStream = imageFile.inputStream
        val metadata = ObjectMetadata()
        metadata.contentType = imageFile.contentType
        val savedProduct = productService.saveProduct(product, inputStream, metadata)
        return ResponseEntity(savedProduct, HttpStatus.CREATED)
    }

}