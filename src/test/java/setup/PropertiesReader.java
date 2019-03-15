package setup;

import runner.TestRunner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesReader.class);

    private PropertiesReader() {
    }

    public static Map<String, String> read(String path) throws IOException {

        Map<String, String> propertiesMap = new HashMap<>();
        File file = new File(TestRunner.class.getResource(path).getPath());

        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            Properties properties = new Properties();
            properties.load(fileInputStream);

            Enumeration oauthKeysFile = properties.keys();
            Map<String, String> values = new HashMap<>();
            while (oauthKeysFile.hasMoreElements()) {
                String key = (String) oauthKeysFile.nextElement();
                String value = properties.getProperty(key);
                values.put(key, value);
            }

            if (!values.isEmpty()) {
                propertiesMap = values;
            }
        } catch (IOException e) {
            LOGGER.error("Error while reading properties file. {}", e);
            throw e;
        }
        return propertiesMap;
    }
}
