package com.change_vision.astah.extension.plugin.cplusreverse.reverser;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * service for Config.properties
 */
public class Config {
	// for Installer
    public static final String CONFIG_PROPERTIES = "reverser\\Config.properties";
    // from Eclipse
    // public static final String CONFIG_PROPERTIES =
    // "api_sample\\sample_doxygen_cplus\\doxygen_cplus\\Config.properties";

    private Properties propertie;
    private InputStream inputFile;

    public Config() {
        propertie = new Properties();
    }

    public Config(String filePath) throws IOException {
        propertie = new Properties();
        inputFile = Config.class.getResourceAsStream("Config.properties");
        // inputFile = new FileInputStream(filePath);
        propertie.load(inputFile);
        inputFile.close();
    }

    public Config(InputStream input) throws IOException {
        propertie = new Properties();
        inputFile = input;
        propertie.load(inputFile);
        inputFile.close();
    }

    public String getValue(String key) {

        if (propertie.containsKey(key)) {
            String value = propertie.getProperty(key);
            return value;
        } else
            return "";
    }

    public String getValue(String fileName, String key) throws IOException {
        String value = "";
        inputFile = new FileInputStream(fileName);
        propertie.load(inputFile);
        inputFile.close();
        if (propertie.containsKey(key)) {
            value = propertie.getProperty(key);
            return value;
        } else
            return value;
    }

    public void clear() {
        propertie.clear();
    }

    public void setValue(String key, String value) {
        propertie.setProperty(key, value);
    }

    public static List<String> getClassNameAboutForbidCreateAssociation() throws IOException {
        if (LanguageManager.isCPlus()) {
            return Arrays.asList(new Config(CONFIG_PROPERTIES)
                    .getValue("cplus_types_for_attribute").split(","));
        } else if (LanguageManager.isCSHARP()) {
            return Arrays.asList(new Config(CONFIG_PROPERTIES).getValue(
                    "csharp_types_for_attribute").split(","));
        } else if (LanguageManager.isJAVA()) {
            return Arrays.asList(new Config(CONFIG_PROPERTIES).getValue("java_types_for_attribute")
                    .split(","));
        }
        return null;
    }
}