package com.example.demo;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.otel.OtelUtilities;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;

@RestController
public class ReceiverController {
	private static final Logger logger = LoggerFactory.getLogger(ReceiverController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.application.name}")
	private String applicationName;

	@GetMapping(value = "/receive")
	public ResponseEntity<ReceiverEntity> receiveSomething(HttpServletRequest request) {
		logger.info("Receiver Controller: Started");
		ResponseEntity<SupplierEntity> supplierName = null;

		String spanName = "Receiver Controller Span";
		Span span = OtelUtilities.doInitiationForHttpRequestServlet(spanName, request);
		span.setAttribute("Order Received", "custom");
		span.addEvent("order received");

		HttpHeaders headers = OtelUtilities.setTraceIdInHttpHeaders(new HttpHeaders());

		try {
			supplierName = restTemplate.exchange("http://localhost:8083/supply", HttpMethod.GET,
					new HttpEntity<>(headers), SupplierEntity.class);
		} catch (Exception e) {
			logger.error("Receiver Controller: Error" + e);
			span.addEvent("order received error" + e);
			span.setStatus(StatusCode.ERROR, "exception occurred");
		} finally {
			OtelUtilities.finishSpan(span);
		}

		ReceiverEntity receiver = new ReceiverEntity("Indian Mart", supplierName.getBody().getSupplierName());

		logger.info("Receiver Controller: Finished");
		return ResponseEntity.ok(receiver);
	}
}
