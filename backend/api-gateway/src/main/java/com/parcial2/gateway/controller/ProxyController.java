package com.parcial2.gateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.Duration;

@RestController
public class ProxyController {

  private final RestTemplate restTemplate;
  private final String authUrl;
  private final String taxiUrl;
  private final String tripUrl;

  public ProxyController(
      RestTemplateBuilder restTemplateBuilder,
      @Value("${app.services.auth-url}") String authUrl,
      @Value("${app.services.taxi-url}") String taxiUrl,
      @Value("${app.services.trip-url}") String tripUrl
  ) {
    this.restTemplate = restTemplateBuilder
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(30))
        .build();
    this.authUrl = normalize(authUrl);
    this.taxiUrl = normalize(taxiUrl);
    this.tripUrl = normalize(tripUrl);
    this.restTemplate.setErrorHandler(new ResponseErrorHandler() {
      @Override
      public boolean hasError(org.springframework.http.client.ClientHttpResponse response) {
        return false;
      }

      @Override
      public void handleError(org.springframework.http.client.ClientHttpResponse response) {
      }
    });
  }

  @RequestMapping("/api/auth/**")
  public ResponseEntity<byte[]> proxyAuth(HttpServletRequest request) throws Exception {
    return proxy(request, authUrl);
  }

  @RequestMapping("/api/taxis/**")
  public ResponseEntity<byte[]> proxyTaxis(HttpServletRequest request) throws Exception {
    return proxy(request, taxiUrl);
  }

  @RequestMapping("/api/trips/**")
  public ResponseEntity<byte[]> proxyTrips(HttpServletRequest request) throws Exception {
    return proxy(request, tripUrl);
  }

  private ResponseEntity<byte[]> proxy(HttpServletRequest request, String targetBaseUrl) throws Exception {
    URI uri = UriComponentsBuilder
        .fromUriString(targetBaseUrl + request.getRequestURI())
        .query(request.getQueryString())
        .build(true)
        .toUri();

    HttpHeaders headers = new HttpHeaders();
    Collections.list(request.getHeaderNames()).forEach(name -> {
      if (!name.equalsIgnoreCase(HttpHeaders.HOST) && !name.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
        headers.put(name, Collections.list(request.getHeaders(name)));
      }
    });

    byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
    HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);

    try {
      return restTemplate.exchange(uri, HttpMethod.valueOf(request.getMethod()), entity, byte[].class);
    } catch (RuntimeException ex) {
      String message = "{\"message\":\"Servicio no disponible desde el API Gateway\",\"detail\":\""
          + ex.getClass().getSimpleName()
          + "\"}";
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
          .contentType(MediaType.APPLICATION_JSON)
          .body(message.getBytes());
    }
  }

  private String normalize(String url) {
    if (url.startsWith("http://") || url.startsWith("https://")) {
      return removeTrailingSlash(url);
    }
    if (url.contains("onrender.com")) {
      return "https://" + removeTrailingSlash(url);
    }
    return "http://" + removeTrailingSlash(url);
  }

  private String removeTrailingSlash(String value) {
    return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
  }
}
