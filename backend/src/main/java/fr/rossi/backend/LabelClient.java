package fr.rossi.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LabelClient {
    private final WebClient client;

    public LabelClient(
            WebClient.Builder webClientBuilder,
            @Value("${label-service.url}") final String labelServiceUrl) {
        this.client = webClientBuilder.baseUrl(labelServiceUrl).build();
    }

    public Mono<LabelResult> getAsync(String key) {
        return client.get()
                .uri("/api/" + key)
                .retrieve()
                .bodyToMono(LabelResult.class);
    }

    public record LabelResult(String key, String label) {
    }
}