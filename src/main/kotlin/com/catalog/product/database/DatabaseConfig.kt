package com.catalog.product.database

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import javax.sql.DataSource

@Configuration
class DatabaseConfig {

    @Value("\${aws.secretsmanager.secretId}")
    private lateinit var secretId: String

    @Value("\${db.url}")
    private lateinit var url: String

    @Bean
    @Primary
    fun dataSource(): DataSource {
        val secret = getSecret()
        val dbSecret = parseSecret(secret)

        val hikariDataSource = HikariDataSource()
        hikariDataSource.jdbcUrl = url
        hikariDataSource.username = dbSecret.username
        hikariDataSource.password = dbSecret.password
        hikariDataSource.driverClassName = "com.mysql.cj.jdbc.Driver"
        return hikariDataSource
    }

    private fun getSecret(): String {
        val client = SecretsManagerClient.builder()
            .region(Region.of("eu-central-1"))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()

        val getSecretValueRequest = GetSecretValueRequest.builder()
            .secretId(secretId)
            .build()

        val getSecretValueResponse = client.getSecretValue(getSecretValueRequest)
        return getSecretValueResponse.secretString()
    }

    private fun parseSecret(secret: String): DbSecret {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(secret)
    }

    data class DbSecret(
    val username: String,
    val password: String,
    )
}