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

/**
 * 
 * @author avishvka
 *
 */

@RestController
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.application.name}")
	private String applicationName;

	@GetMapping(value = "/order")
	public ResponseEntity<OrderEntity> orderSomething(HttpServletRequest request) {
		logger.info("Order Controller: Started");

		String spanName = "Order Controller Span";
		Span span = OtelUtilities.doInitiationForHttpRequestServlet(spanName, request);
		span.setAttribute("Order Type", "custom");
		span.addEvent("Something ordered");

		HttpHeaders headers = OtelUtilities.setTraceIdInHttpHeaders(new HttpHeaders());

		ResponseEntity<ReceiverEntity> receiver = restTemplate.exchange("http://localhost:8082/receive", HttpMethod.GET,
				new HttpEntity<>(headers), ReceiverEntity.class);

		OtelUtilities.finishSpan(span);

		OrderEntity order = new OrderEntity("Apple MacBook Pro M1 Pro", receiver.getBody().getReceiverName(),
				receiver.getBody().getSupplierName());
		logger.info("Order Controller: Finished");
		return ResponseEntity.ok(order);
	}

}
