package calendar.Utils;

import calendar.CustomExceptions.MissingConfigException;
import calendar.CustomExceptions.MissingConfigParameterException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtils {

    /**
     * Get specific property from property file given in parameter
     * @param pathToFile path to property file
     * @param desiredProperty property to be found
     * @return Value of desired property
     * @throws MissingConfigException is thrown when no config file found by specified path
     * @throws MissingConfigParameterException is thrown when desired property is not found in specified file
     */
    public static String getProperty(URI pathToFile, String desiredProperty) throws MissingConfigException, MissingConfigParameterException {
        String propertyValue;

        try (InputStream input = new FileInputStream(new File(pathToFile))) {

            // Load property file
            Properties prop = new Properties();
            prop.load(input);

            // Check that property was found
            if((propertyValue = prop.getProperty(desiredProperty)) == null){
                throw new MissingConfigParameterException(String.format("Missing desired property in configuration file. Looking for property [%s] in file [%s]", desiredProperty, pathToFile));
            }

        } catch (IOException ex) {
            throw new MissingConfigException(String.format("Missing configuration file. Looking for property [%s] in file [%s]", desiredProperty, pathToFile));
        }

        return propertyValue;
    }

    /**
     * Get specific property from property file given in parameter
     * @param pathToFile path to property file
     * @param desiredProperties properties to be found
     * @return Value of desired property
     * @throws MissingConfigException is thrown when no config file found by specified path
     * @throws MissingConfigParameterException is thrown when desired property is not found in specified file
     */
    public static Map<String, String> getProperties(String pathToFile, String[] desiredProperties) throws MissingConfigException, MissingConfigParameterException {
        Map<String, String> propertyValues = new HashMap<>();

        try (InputStream input = new FileInputStream(pathToFile)) {

            // Load property file
            Properties prop = new Properties();
            prop.load(input);

            for (String desiredProperty: desiredProperties) {

                // Check that property was found
                String propertyValue;
                if((propertyValue = prop.getProperty(desiredProperty)) == null){
                    throw new MissingConfigParameterException(String.format("Missing desired property in configuration file. Looking for property [%s] in file [%s]", desiredProperty, pathToFile));
                }

                //saveToFileAsJson property
                propertyValues.put(desiredProperty, propertyValue);
            }
        } catch (IOException ex) {
            throw new MissingConfigException(String.format("Missing configuration file. Looking for property [%s] in file [%s]", desiredProperties, pathToFile));
        }

        return propertyValues;
    }

}
