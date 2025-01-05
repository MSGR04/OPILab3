package org.web.lab3.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties;

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties = new Properties();
            if (input == null) {
                throw new RuntimeException("Config file not found");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config file: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
