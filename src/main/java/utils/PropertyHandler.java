package utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 *
 * @author Benedikt Zönnchen
 */
public class PropertyHandler
{
	public static String PROPERTY_FILE_NAME = "/config.properties";
	public static Properties loadProperties() throws IOException
	{
		Properties properties = new Properties();
		InputStream inputStream = PropertyHandler.class.getResourceAsStream(PROPERTY_FILE_NAME);
		properties.load(inputStream);
		inputStream.close();
		return properties;
	}
}
