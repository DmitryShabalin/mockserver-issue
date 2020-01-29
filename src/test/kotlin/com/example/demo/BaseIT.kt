package com.example.demo

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.verify.VerificationTimes
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import java.util.concurrent.ConcurrentHashMap

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [ContainerInitializer::class])
abstract class BaseIT {

    init {
        ContainerInitializer.start()
    }
    @Autowired
    lateinit var mockServerClient: MockServerClient
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
            "http://${ContainerHolder.container().containerIpAddress}:${ContainerHolder.container().serverPort}",
            String::class.java
        )
    }
}

@Configuration
class MockServerClientConfig {
    @Bean
    fun mockServerClient() = MockServerClient(
        ContainerHolder.container().containerIpAddress,
        ContainerHolder.container().serverPort
    )
}

class ContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(p0: ConfigurableApplicationContext) {
        println("do nothing")
    }

    companion object {
        private val RUNNING_CONTAINERS = ConcurrentHashMap<String, MockServerContainer>()

        fun start() {
            val container = ContainerHolder.container()
            container.start()
            container.followOutput(Slf4jLogConsumer(LoggerFactory.getLogger("mock-container")))
            RUNNING_CONTAINERS["mock-container"] = container
        }
    }
}

object ContainerHolder {
    private val mockServerContainer: MockServerContainer by lazy { MockServerContainer() }

    fun name() = "mockServer"

    fun container() = mockServerContainer
}