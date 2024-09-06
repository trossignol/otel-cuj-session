package fr.rossi.backend;

import fr.rossi.backend.LabelClient.LabelResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class Resource {

    private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class);

    @Autowired
    LabelClient labelClient;

    @GetMapping("/api/{key}")
    public Mono<Result> getAsync(@PathVariable("key") String key) {
        LOGGER.info("Get for key={}", key);
        return Mono.zip(this.labelClient.getAsync(key), this.labelClient.getAsync(key), this.labelClient.getAsync(key))
                .map(labels -> List.of(labels.getT1(), labels.getT2(), labels.getT3()))
                .map(labels -> Result.build(key, labels));
    }

    public record Result(String key, List<String> labels) {
        static Result build(String key, List<LabelResult> results) {
            return new Result(key, results.stream().map(LabelResult::label).toList());
        }
    }
}
