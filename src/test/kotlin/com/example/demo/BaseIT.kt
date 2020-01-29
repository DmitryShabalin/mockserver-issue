package com.example.demo

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.verify.VerificationTimes
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.MockServerContainer

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseIT {

    @Rule
    @JvmField
    final val mockServer = MockServerContainer()

    val mockServerClient by lazy { MockServerClient(mockServer.containerIpAddress, mockServer.serverPort) }

    var restTemplate = RestTemplate()

    @Before
    fun setup() {
        mockServerClient
            .`when`(
                request()
                    .withMethod("GET")
                    .withPath("/unused1")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient
            .`when`(
                request()
                    .withMethod("GET")
                    .withPath("/unused2")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient
            .`when`(
                request()
                    .withMethod("GET")
                    .withPath("/unused3")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient
            .`when`(
                request()
                    .withMethod("GET")
                    .withPath("/unused4")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient
            .`when`(
                request()
                    .withMethod("GET")
                    .withPath("/unused5")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient
            .`when`(
                request()
                    .withMethod("GET")
                    .withPath("/unused6")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient.clear(
            request()
                .withMethod("GET")
                .withPath("/")
        )
        mockServerClient
            .`when`(
                request()
                    .withMethod("GET")
                    .withPath("/")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient
            .`when`(
                request()
                    .withMethod("PUT")
                    .withPath("/")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient
            .`when`(
                request()
                    .withMethod("POST")
                    .withPath("/")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )

        mockServerClient
            .`when`(
                request()
                    .withMethod("DELETE")
                    .withPath("/")
            )
            .respond(
                response()
                    .withStatusCode(200)
            )
    }

    @After
    fun resetState() {
        mockServerClient.reset()
    }

    fun verifyHttpRequest(times: Int = 1) {
        mockServerClient.verify(
            request()
                .withMethod("GET")
                .withPath("/"),
            VerificationTimes.exactly(times)
        )
    }

    fun ping() {
        restTemplate.getForEntity(
            "http://${mockServer.containerIpAddress}:${mockServer.serverPort}",
            String::class.java
        )
    }
}
