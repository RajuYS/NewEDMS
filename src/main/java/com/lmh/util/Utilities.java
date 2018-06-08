package com.lmh.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utilities
{
  public static String getProperty(String propertyKey)
  {
    String propertyValue = "";
    try
    {
      Properties prop = new Properties();
      InputStream input = new FileInputStream("src/main/resources/application.properties");
      prop.load(input);
      propertyValue = prop.getProperty(propertyKey);
    }
    catch (IOException localIOException) {}
    return propertyValue;
  }
}
