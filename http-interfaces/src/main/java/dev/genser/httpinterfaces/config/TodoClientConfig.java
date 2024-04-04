package dev.genser.httpinterfaces.config;

import dev.genser.httpinterfaces.client.TodoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
@Slf4j
public class TodoClientConfig {

    @Bean
    public TodoClient todoClient() {
        final var webClient = WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .filter(retryFilter())
                .filter(logRequest())
                .build();

        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter
                        .create(webClient))
                .build()
                .createClient(TodoClient.class);
    }

    private ExchangeFilterFunction retryFilter() {
        return (request, next) -> next.exchange(request)
                .retryWhen(
                        Retry.fixedDelay(3, Duration.ofSeconds(30))
                                .doAfterRetry(_ -> log.warn("Retrying"))
                );
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    @Bean
    public TodoClient todoRestClient() {
        final var restClient = RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .requestInterceptor(
                        ((request, body, execution) -> {
                            log.info("Request: {}", request.getURI());
                            return execution.execute(request, body);
                        }))
                .build();

        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build()
                .createClient(TodoClient.class);

    }

}
