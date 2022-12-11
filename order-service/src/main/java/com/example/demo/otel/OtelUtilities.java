package com.example.demo.otel;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapSetter;

public class OtelUtilities {

	private static String THIS_COMPONENT_NAME = OtelUtilities.class.getName();

	public static Span doInitiationForHttpRequestServlet(String spanName, HttpServletRequest httpRequest) {
		Context context = null;
		Span span = null;
		Tracer tracerObj = null;
		Span currentSpan = null;

		currentSpan = getCurrentSpanFromThreadAttribute();
		tracerObj = getTracerObjectDetailFromThreadAttribute();

		if (currentSpan != null && tracerObj != null) {
			span = tracerObj.spanBuilder(spanName).setParent(Context.current().with(currentSpan)).startSpan();
			setAllOtelDetailsInThreadAttributeOfChild(span);
		} else {

			tracerObj = OtelInitializerSingleton.getUniqueInstance().getOtelTracerObj(THIS_COMPONENT_NAME);

			if (httpRequest != null) {
				context = getContextFromHttpServletRequest(httpRequest);
			}

			if (context != null) {
				span = tracerObj.spanBuilder(spanName).setParent(context).startSpan();
			} else {
				span = tracerObj.spanBuilder(spanName).startSpan();
			}

			setAllOtelDetailsInThreadAttribute(span, tracerObj);
		}

		return span;
	}

	private static void setAllOtelDetailsInThreadAttributeOfChild(Span span) {
		OtelThreadManager.set(OtelConstants.CURRENT_SPAN, span);
	}

	private static void setAllOtelDetailsInThreadAttribute(Span span, Tracer tracerObj) {
		OtelThreadManager.set(OtelConstants.CURRENT_SPAN, span);
		OtelThreadManager.set(OtelConstants.TRACE_OBJECT, tracerObj);
	}

	public static Span getChildSpan(String spanName) {
		Span span = null;
		Span currentSpan = null;
		Tracer tracerObj = null;

		currentSpan = getCurrentSpanFromThreadAttribute();
		tracerObj = getTracerObjectDetailFromThreadAttribute();

		if (currentSpan != null && tracerObj != null) {
			span = tracerObj.spanBuilder(spanName).setParent(Context.current().with(currentSpan)).startSpan();
			setAllOtelDetailsInThreadAttributeOfChild(span);
		} else {
			span = doInitiationForHttpRequestServlet(spanName, null);
		}

		return span;
	}

	public static void finishSpan(Span span) {
		if (span != null) {
			span.end();
		}
	}

	public static String getTraceParent() {
		Span currentSpan = getCurrentSpanFromThreadAttribute();
		if (currentSpan == null) {
			currentSpan = doInitiationForHttpRequestServlet("OtelUtilities.getTraceParent", null);
		}
		String traceParent = "00-" + currentSpan.getSpanContext().getTraceId() + "-"
				+ currentSpan.getSpanContext().getSpanId() + "-" + currentSpan.getSpanContext().getTraceFlags();
		return traceParent;
	}

	private static TextMapSetter<HttpHeaders> setterForHttpHeaders = new TextMapSetter<HttpHeaders>() {
		@Override
		public void set(HttpHeaders carrier, String key, String value) {
			// Insert the context as Header
			carrier.set(key, value);
		}
	};

	public static HttpHeaders setTraceIdInHttpHeaders(HttpHeaders httpHeaders) {
		OpenTelemetry openTelemetry = OtelInitializerSingleton.getUniqueInstance().getOpentelemetryObj();
		Span currentSpan = null;
		OtelLogManagerUtil.otelLogInfo(THIS_COMPONENT_NAME, "init - Setting traceId in HttpHeaders");
		try {
			currentSpan = getCurrentSpanFromThreadAttribute();
			if (currentSpan == null) {
				currentSpan = doInitiationForHttpRequestServlet("OtelUtilities.setTraceIdInHttpHeaders", null);
			}
			openTelemetry.getPropagators().getTextMapPropagator().inject(
					io.opentelemetry.context.Context.current().with(currentSpan), httpHeaders, setterForHttpHeaders);
		} catch (Throwable e) {
			OtelLogManagerUtil.otelLogFatalWithException(THIS_COMPONENT_NAME,
					"Exception while setting traceId in HttpHeaders", e);
		}
		OtelLogManagerUtil.otelLogInfo(THIS_COMPONENT_NAME, "end - Setting traceId in HttpHeaders");
		return httpHeaders;
	}

	private static TextMapGetter<HttpServletRequest> getterForHttpHeaders = new TextMapGetter<HttpServletRequest>() {
		@Override
		public String get(HttpServletRequest carrier, String key) {
			Enumeration<String> headerNames = carrier.getHeaderNames();
			if (headerNames != null) {
				while (headerNames.hasMoreElements()) {
					String headerName = headerNames.nextElement();
					if (headerName.equals(key)) {
						String headerValue = carrier.getHeader(headerName);
						return headerValue;
					}
				}
			}

			return null;
		}

		@Override
		public Iterable<String> keys(HttpServletRequest carrier) {
			Set<String> set = new HashSet<String>();
			Enumeration<String> headerNames = carrier.getHeaderNames();
			if (headerNames != null) {
				while (headerNames.hasMoreElements()) {
					set.add(headerNames.nextElement());
				}
			}
			return set;
		}
	};

	private static Span getCurrentSpanFromThreadAttribute() {
		return ((Span) OtelThreadManager.get(OtelConstants.CURRENT_SPAN));
	}

	private static Tracer getTracerObjectDetailFromThreadAttribute() {
		return ((Tracer) OtelThreadManager.get(OtelConstants.TRACE_OBJECT));
	}

	private static Context getContextFromHttpServletRequest(HttpServletRequest request) {
		OpenTelemetry openTelemetry = OtelInitializerSingleton.getUniqueInstance().getOpentelemetryObj();
		Context context = null;
		OtelLogManagerUtil.otelLogInfo(THIS_COMPONENT_NAME, "init - Getting traceId from HttpServletRequest");
		try {
			context = openTelemetry.getPropagators().getTextMapPropagator().extract(Context.current(), request,
					getterForHttpHeaders);
		} catch (Throwable e) {
			System.out.println("00000-----" + e);
			OtelLogManagerUtil.otelLogFatalWithException(THIS_COMPONENT_NAME,
					"Exception while getting traceId from HttpServletRequst", e);
		}
		OtelLogManagerUtil.otelLogInfo(THIS_COMPONENT_NAME, "end - Getting traceId from HttpServletRequest");
		return context;
	}

	public static String getOtelConfigurationsProp(String key) {
		return OtelConfigurationSingleton.getUniqueInstance().getOtelConfigurationProp(key);
	}

	public static boolean log4j2Decider() {
		String typeOfLogger = getOtelConfigurationsProp(OtelConstants.OTEL_LOGGER_TYPE);
		if (typeOfLogger != null && typeOfLogger.equalsIgnoreCase(OtelConstants.LOG4J2_LOGGER_TYPE)) {
			return true;
		} else {
			return false;
		}
	}

}
