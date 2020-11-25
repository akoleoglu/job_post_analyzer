package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {

    // Bu Classin amaci ==> configuration.properties dosyasindaki verilerini okumaktir.
    private static Properties properties;
    private static Properties propertiesSecrets;
    static {
        String path = "configuration.properties";
        String pathSecrets = "configurationSecrets.properties";
        try {
            FileInputStream file = new FileInputStream(path);
            properties =  new Properties();
            properties.load(file);

            FileInputStream fileSecrets = new FileInputStream(pathSecrets);
            propertiesSecrets =  new Properties();
            propertiesSecrets.load(fileSecrets);

        } catch (IOException e) {
            System.out.println("Configuration file bulunamadi");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getPropertySecrets(String key) {
        return propertiesSecrets.getProperty(key);
    }

}
