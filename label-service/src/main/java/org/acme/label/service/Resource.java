package org.acme.label.service;

import java.time.Duration;
import java.util.Random;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api")
public class Resource {

    private final Random random = new Random();

    @GET
    @Path("/{key}")
    public Uni<Result> get(String key) {
        Span.current().getSpanContext().getTraceState().forEach((k, v) -> System.out.println("State: " + k + "=" + v));
        Baggage.current().asMap().forEach((k, v) -> System.out.println("Baggage: " + k + "=" + v.getValue()));
        Span.current().setAttribute("tmp-tro", 1234);

        return Uni.createFrom().item(new Result(key, "label-for-key-" + key))
                .onItem().delayIt().by(Duration.ofMillis(100L + random.nextInt(0, 100)));
    }

    public record Result(String key, String label) {
    }
}