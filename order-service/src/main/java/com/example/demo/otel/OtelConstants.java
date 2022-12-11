package com.example.demo.otel;

public class OtelConstants {

	public static final String fileSeparator = System.getProperty("file.separator");
	public static final String userHome = System.getProperty("user.home");

	public static final String OTEL_SYSTEM_NAME = "OTEL.SYSTEM.NAME";
	public static final String OTEL_COLLECTOR_URL = "OTEL.COLLECTOR.URL";
	public static final String OTEL_SERVICE_NAME = "OTEL.SERVICE.NAME";
	public static final String OTEL_LOGGER_TYPE = "OTEL.LOGGER.TYPE";

	public static final String OTEL_LOG4J_FILE_LOCATION = "OTEL.LOG4J.PROPERTIES.FILE.LOCATION";
	public static final String OTEL_LOG4J2_FILE_LOCATION = "OTEL.LOG4J2.PROPERTIES.FILE.LOCATION";

	public static final String LOG4J_LOGGER_TYPE = "LOG4J";
	public static final String LOG4J2_LOGGER_TYPE = "LOG4J2";

	public static final String OTEL_JSON_MESSAGE_LOGGER = "OtelJSONMessageLog";

	public static final String SPAN_ID = "SpanID";
	public static final String TRACE_ID = "TraceID";
	public static final String TRACE_STATE = "TraceState";
	public static final String TRACE_FLAGS = "TraceFlags";
	public static final String TRACE_OBJECT = "TracerObject";
	public static final String OTEL_MESSAGE_DTO = "OtelMessageDTO";
	public static final String CURRENT_SPAN = "CurrentSpan";

}
