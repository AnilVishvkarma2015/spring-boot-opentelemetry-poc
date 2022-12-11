package com.example.demo.otel;

import java.util.HashMap;
import java.util.Map;

public final class OtelThreadManager {

	private static final ThreadLocal attributeMapHolder = new ThreadLocal() {
		protected Object initialValue() {
			return new HashMap();
		}
	};

	public static final Object get(String key) {
		return attributeMap().get(key);
	}

	public static final void set(String key, Object value) {
		attributeMap().put(key, value);
	}

	public static final void setIfNull(String key, Object value) {
		if (attributeMap().get(key) == null) {
			attributeMap().put(key, value);
		}

	}

	public static final Object clear(String key) {
		return attributeMap().remove(key);
	}

	public static final void clearAll() {
		attributeMap().clear();
	}

	private static Map attributeMap() {
		return ((Map) attributeMapHolder.get());
	}

	private OtelThreadManager() {
	}

}
