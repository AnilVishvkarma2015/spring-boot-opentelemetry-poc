package com.example.demo.otel;

import java.util.HashMap;
//import java.util.Locale.Category;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class OtelLog4jPropertiesSingleton {

	private static OtelLog4jPropertiesSingleton instance = null;
	private static boolean INITIALIZED = false;
	private HashMap loggerMap = new HashMap();

	private OtelLog4jPropertiesSingleton() {
		if (!INITIALIZED) {
			loadLog4JPropertiesFile();
		}
	}

	public static OtelLog4jPropertiesSingleton getUniqueInstance() {
		if (instance == null) {
			synchronized (OtelLog4jPropertiesSingleton.class) {
				if (instance == null) {
					instance = new OtelLog4jPropertiesSingleton();
					INITIALIZED = true;
				}
			}
		}
		return instance;
	}

	private void loadLog4JPropertiesFile() {
		String userHome = OtelConstants.userHome;
		if (userHome == null) {
			System.out.println("Undefined system property user.home in OtelLog4jPropertiesSingleton");
			return;
		}

		if (!userHome.endsWith(OtelConstants.fileSeparator)) {
			userHome = userHome + OtelConstants.fileSeparator;
		}

		String p_config_file = userHome
				+ OtelUtilities.getOtelConfigurationsProp(OtelConstants.OTEL_LOG4J_FILE_LOCATION);

		PropertyConfigurator.configure(p_config_file);

	}

	public Logger getLog4JLogger(String p_category) {
		Logger logger = (Logger) loggerMap.get(p_category);
		if (logger == null) {
			synchronized (loggerMap) {
				logger = (Logger) loggerMap.get(p_category);
				if (logger == null) {
					logger = (Logger) Category.getInstance(p_category);
					loggerMap.put(p_category, logger);
				}
			}
		}
		return logger;
	}

}
