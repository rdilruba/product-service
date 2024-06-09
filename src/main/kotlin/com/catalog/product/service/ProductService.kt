package com.catalog.product.service

import com.amazonaws.services.s3.model.ObjectMetadata
import com.catalog.product.model.Product
import com.catalog.product.repository.ProductRepository
import com.catalog.product.s3.S3Service
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.io.InputStream


@Service
class ProductService(private val productRepository: ProductRepository, private val s3Service: S3Service) {

    @Cacheable("productcache")
    //burada tum paginated result i cache lemis oluyoruz, ancak normalde product by product cache yapmaliyiz.
    //bu serviste oyle bir endpoint olmadigi icin su anlik, Cacheable i gosterme amacli burada kullandik.
    fun getAllProducts(pageNumber: Int, pageSize: Int): Page<Product> {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        return productRepository.findAll(pageRequest)
    }

    fun saveProduct(product: Product, inputStream: InputStream, metadata: ObjectMetadata): Product {
        // key can be adjusted to have timestamp
        s3Service.uploadImage(product.name, inputStream, metadata)
        val imageUrl = s3Service.getImageUrl(product.name)
        product.photoUri = imageUrl
        return productRepository.save(product)
    }
}