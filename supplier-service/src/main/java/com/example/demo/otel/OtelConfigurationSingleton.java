package com.example.demo.otel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class OtelConfigurationSingleton {

	private static Properties otelConfigurations = null;
	private static OtelConfigurationSingleton instance = null;
	private static boolean INITIALIZED = false;

	private OtelConfigurationSingleton() {
		if (!INITIALIZED) {
			loadOtelConfigurations();
		}
	}

	public static OtelConfigurationSingleton getUniqueInstance() {
		if (instance == null) {
			synchronized (OtelConfigurationSingleton.class) {
				if (instance == null) {
					instance = new OtelConfigurationSingleton();
					INITIALIZED = true;
				}
			}
		}
		return instance;
	}

	private void loadOtelConfigurations() {
		String userHome = OtelConstants.userHome;
		if (userHome == null) {
			System.out.println("Undefined system property user.home in OtelConfigurationSingleton");
			return;
		}

		if (!userHome.endsWith(OtelConstants.fileSeparator)) {
			userHome = userHome + OtelConstants.fileSeparator;
		}

		String filePath = userHome + "otelcfg" + OtelConstants.fileSeparator + "otelconfig.properties";

		System.out.println("FilePath of otelconfig.properties configuration file - " + filePath);

		otelConfigurations = new Properties();
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			try {
				BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				otelConfigurations.load(bufferedInputStream);
			} catch (Exception e) {
				System.out.println("Failure while reading otel configuraton file");
				e.printStackTrace();
			}
		} else {
			System.out.println("Otel configuration file does not exist for loading config properties");
		}

	}

	public String getOtelConfigurationProp(String key) {
		return otelConfigurations.getProperty(key, null);
	}
}
