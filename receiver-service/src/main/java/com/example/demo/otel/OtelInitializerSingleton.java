package com.example.demo.otel;

import java.util.HashMap;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

public class OtelInitializerSingleton {

	private static OtelInitializerSingleton instance = null;
	private static boolean INITIALIZED = false;

	private static SpanExporter otlpExporter = null;
	private static OpenTelemetry openTelemetry = null;
	private static HashMap<String, Tracer> tracerObjectMap = new HashMap<String, Tracer>();

	private OtelInitializerSingleton() {
		if (!INITIALIZED) {
			intializeOtel();
		}
	}

	public static OtelInitializerSingleton getUniqueInstance() {
		if (instance == null) {
			synchronized (OtelInitializerSingleton.class) {
				if (instance == null) {
					instance = new OtelInitializerSingleton();
					INITIALIZED = true;
				}
			}
		}
		return instance;
	}

	private void intializeOtel() {
		try {
			otlpExporter = OtlpGrpcSpanExporter.builder()
					.setEndpoint(OtelUtilities.getOtelConfigurationsProp(OtelConstants.OTEL_COLLECTOR_URL)).build();

			SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
					.addSpanProcessor(SimpleSpanProcessor.create(otlpExporter))
					.setResource(Resource.getDefault().toBuilder()
							.put(ResourceAttributes.SERVICE_NAME,
									OtelUtilities.getOtelConfigurationsProp(OtelConstants.OTEL_SERVICE_NAME))
							.build())
					.build();

			openTelemetry = OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
					.setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
					.buildAndRegisterGlobal();
		} catch (Exception e) {
			System.out.println("Failure in initializing opentelemetry");
			e.printStackTrace();
		}
	}

	public Tracer getOtelTracerObj(String tracerObjName) {
		Tracer tracerObj = tracerObjectMap.get(tracerObjName);
		if (tracerObj == null) {
			synchronized (tracerObjectMap) {
				tracerObj = tracerObjectMap.get(tracerObjName);
				if (tracerObj == null) {
					tracerObj = openTelemetry.getTracer(tracerObjName);
					tracerObjectMap.put(tracerObjName, tracerObj);
				}
			}
		}

		return tracerObj;
	}

	public OpenTelemetry getOpentelemetryObj() {
		return openTelemetry;
	}
}
