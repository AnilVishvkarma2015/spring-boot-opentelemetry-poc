package com.example.demo.otel;

import java.io.File;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

public class OtelLog4j2PropertiesSingleton {

	private static OtelLog4j2PropertiesSingleton instance = null;
	private static boolean INITIALIZED = false;

	private LoggerContext context = null;
	private HashMap loggerMapLog4j2 = new HashMap();

	private OtelLog4j2PropertiesSingleton() {
		if (!INITIALIZED) {
			loadLog4J2PropertiesFile();
		}
	}

	public static OtelLog4j2PropertiesSingleton getUniqueInstance() {
		if (instance == null) {
			synchronized (OtelLog4j2PropertiesSingleton.class) {
				if (instance == null) {
					instance = new OtelLog4j2PropertiesSingleton();
					INITIALIZED = true;
				}
			}
		}
		return instance;
	}

	private void loadLog4J2PropertiesFile() {
		String userHome = OtelConstants.userHome;
		if (userHome == null) {
			System.out.println("Undefined system property user.home in OtelLog4j2PropertiesSingleton");
			return;
		}

		if (!userHome.endsWith(OtelConstants.fileSeparator)) {
			userHome = userHome + OtelConstants.fileSeparator;
		}

		String p_config_file = userHome
				+ OtelUtilities.getOtelConfigurationsProp(OtelConstants.OTEL_LOG4J2_FILE_LOCATION);

		context = (LoggerContext) LogManager.getContext(false);
		File propertiesFile = new File(p_config_file);
		context.setConfigLocation(propertiesFile.toURI());
	}

	public Logger getLog4J2Logger(String p_category) {
		Logger logger = (Logger) loggerMapLog4j2.get(p_category);
		if (logger == null) {
			synchronized (loggerMapLog4j2) {
				logger = (Logger) loggerMapLog4j2.get(p_category);
				if (logger == null) {
					logger = LogManager.getLogger(p_category);
					loggerMapLog4j2.put(p_category, logger);
				}
			}
		}
		return logger;
	}

}
