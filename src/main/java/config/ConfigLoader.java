package config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static Config config;

    public static Config get() {
        if (config == null) {
            try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
                Properties p = new Properties();
                p.load(is);
                config = new Config(
                        p.getProperty("base.url"),
                        p.getProperty("browser", "chromium"),
                        Boolean.parseBoolean(p.getProperty("headless", "true")),
                        p.getProperty("trace", "on"),
                        p.getProperty("screenshot", "only-on-failure"),
                        p.getProperty("video", "off"),
                        p.getProperty("device","")
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to load config", e);
            }
        }
        return config;
    }
}
