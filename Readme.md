# OTEL context management

This workspace is a sandbox to play with OTEL baggage and tracestate.

### Context
This workspace is composed by 3 components:
- `label-service`: simple Quarkus microservice with one endpoint returning a simple label
- `backend`: Spring-boot service which call 3 times (in parallel) the `label-service` to compose an agregated response
  - we use 2 different services to test the OTEL context propagation
  - we use 2 different framework to test their compatibility
  - we use non bloking methods to challenge the multi-thread handling
- `docker-compose.yml` and especially 2 services
  - `otel-collector`: otel collector configured by `otel/otel-collector-config.yml` (mostly for debug/logging mode)
  - `jaeger`: simple tool used to visualize the traces

### Run components
Here is the simplest way to run the components. You can also build the docker images for `label-service` and `backend` using the `build` target in each Makefile.

``` bash
cd label-service && make dev
```

``` bash
cd backend && make dev
```

``` bash
docker-compose up otel-collector
```

### HTTP requests

##### HTTP request with a correct `traceparent` and `tracestate`
```bash 
http :8080/api/test baggage:"cuj-session-id=42" traceparent:"00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01" tracestate:"toto=567"
```

##### HTTP request with a `baggage`
```bash 
http :8080/api/test baggage:"cuj-session-id=42"
```


### Conclusion
it appears that
- The baggage is well propagated from `backend` to `label-service` but we can't find it in the collectors logs neither in Jaeger. After inpsecting the [otel collector repository](https://github.com/open-telemetry/opentelemetry-collector), it seems that the baggage is absolutely not handled.
- According to [the specification](https://www.w3.org/TR/trace-context/#tracestate-header), if the `traceparent`is not set ot if it's format is not correct, the `tracestate` header is not handling (by Quarkus for instance). It means, althought we can find the `tracestate` value in the request header, it's not set in the `Span` API.

### Sources
- [Trace context specification](https://www.w3.org/TR/trace-context/)
- [Trace Context HTTP Request Headers Format](https://github.com/w3c/trace-context/blob/main/spec/20-http_request_header_format.md)
- [OTEL Baggage](https://opentelemetry.io/docs/concepts/signals/baggage/)