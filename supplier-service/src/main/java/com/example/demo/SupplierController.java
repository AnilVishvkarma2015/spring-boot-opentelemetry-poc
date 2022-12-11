package com.example.demo;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.otel.OtelUtilities;

import io.opentelemetry.api.trace.Span;

@RestController
public class SupplierController {
	private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

	@Value("${spring.application.name}")
	private String applicationName;

	@GetMapping(value = "/supply")
	public ResponseEntity<SupplierEntity> receiveSomething(HttpServletRequest request) throws InterruptedException {
		logger.info("Supplier Controller: Started");

		String spanName = "Supplier Controller Span";
		Span span = OtelUtilities.doInitiationForHttpRequestServlet(spanName, request);
		span.setAttribute("Order Supplied", "custom");
		span.addEvent("order supplied");
		OtelUtilities.finishSpan(span);
		logger.info("Supplier Controller: Finished");
		doWork();
		return ResponseEntity.ok(new SupplierEntity("Flipkart"));
	}

	public void doWork() throws InterruptedException {
		Span span = OtelUtilities.getChildSpan("child 1");
		span.setAttribute("child-1", "child-one");
		try {
			Thread.sleep(1000);
			parentOne();
		} finally {
			span.end();
		}
	}

	void parentOne() {
		Span span = OtelUtilities.getChildSpan("child 2");
		span.setAttribute("child-2", "child-two");
		try {
			childOne(span);
		} finally {
			span.end();
		}
	}

	void childOne(Span parentSpan) {
		Span span = OtelUtilities.getChildSpan("child 3");
		span.setAttribute("child-3", "child-three");
		try {

		} finally {
			span.end();
		}
	}
}
