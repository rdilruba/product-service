package com.catalog.product.s3
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class S3Service(private val amazonS3: AmazonS3) {

    fun uploadImage(key: String, inputStream: InputStream, metadata: ObjectMetadata) {
        amazonS3.putObject("microservice-product", key, inputStream, metadata)
    }

    fun getImageUrl(key: String): String {
        return amazonS3.getUrl("microservice-product", key).toString()
    }
}
