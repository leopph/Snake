package hu.alkfejl.config;

import java.io.IOException;
import java.util.Properties;


public class Configuration
{
    private static final Properties s_Properties;

    static
    {
        s_Properties = new Properties();

        try { s_Properties.load(Configuration.class.getResourceAsStream("/application.properties")); }
        catch (IOException eki) { eki.printStackTrace(); }
    }

    public static String getValue(String key) { return s_Properties.getProperty(key); }
}
