package com.example.demo.otel;

public class OtelLogManagerUtil {

	private static String THIS_COMPONENT_NAME = OtelLogManagerUtil.class.getName();

	public static void otelLogInfo(String className, String message) {
		if (OtelUtilities.log4j2Decider()) {
			getLog4j2Object(className).info(message);
		} else {
			getLog4jObject(className).info(message);
		}
	}

	public static void otelLogTrace(String className, String message) {
		if (OtelUtilities.log4j2Decider()) {
			getLog4j2Object(className).info(message);
		} else {
			getLog4jObject(className).info(message);
		}
	}

	public static void otelLogDebug(String className, String message) {
		if (OtelUtilities.log4j2Decider()) {
			getLog4j2Object(className).debug(message);
		} else {
			getLog4jObject(className).debug(message);
		}
	}

	public static void otelLogWarn(String className, String message) {
		if (OtelUtilities.log4j2Decider()) {
			getLog4j2Object(className).warn(message);
		} else {
			getLog4jObject(className).warn(message);
		}
	}

	public static void otelLogFatal(String className, String message) {
		if (OtelUtilities.log4j2Decider()) {
			getLog4j2Object(className).fatal(message);
		} else {
			getLog4jObject(className).fatal(message);
		}
	}

	public static void otelLogFatalWithException(String className, String message, Throwable e) {
		if (OtelUtilities.log4j2Decider()) {
			getLog4j2Object(className).warn(message, e);
		} else {
			getLog4jObject(className).warn(message, e);
		}
	}

	private static org.apache.log4j.Logger getLog4jObject(String className) {
		return OtelLog4jPropertiesSingleton.getUniqueInstance().getLog4JLogger(className);
	}

	private static org.apache.logging.log4j.Logger getLog4j2Object(String className) {
		return OtelLog4j2PropertiesSingleton.getUniqueInstance().getLog4J2Logger(className);
	}
}
