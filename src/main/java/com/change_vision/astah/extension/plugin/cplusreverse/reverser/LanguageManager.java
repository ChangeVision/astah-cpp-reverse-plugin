package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LanguageManager {
	public static final String LANGUAGE_CPLUS = "cplus_primitivite_type";

	public static final String LANGUAGE_JAVA = "java_primitivite_type";

	public static final String LANGUAGE_CSHARP = "csharp_primitivite_type";

	final static Set<String> Java_PRIMITIVE_TYPE = new HashSet<String>();

	final static Set<String> CSHARP_PRIMITIVE_TYPE = new HashSet<String>();

	final static Set<String> C_PRIMITIVE_TYPE = new HashSet<String>();

	static {
		try {
			Java_PRIMITIVE_TYPE.addAll(
					Arrays.asList(new Config(Config.CONFIG_PROPERTIES).getValue(LANGUAGE_JAVA).split(",")));
			CSHARP_PRIMITIVE_TYPE.addAll(
					Arrays.asList(new Config(Config.CONFIG_PROPERTIES).getValue(LANGUAGE_CSHARP).split(",")));
			C_PRIMITIVE_TYPE.addAll(
					Arrays.asList(new Config(Config.CONFIG_PROPERTIES).getValue(LANGUAGE_CPLUS).split(",")));
		} catch (IOException e) {
		}
	}

	static Set<String> currentLanguagePrimitiveType;

	/**
	 * 
	 * @param null
	 * @return Set
	 */
	public static Set<String> getCurrentLanguagePrimitiveType() {
		return currentLanguagePrimitiveType;
	}

	/**
	 * 
	 * @param type: String
	 * @return void
	 */
	public static void setCurrentLanguagePrimitiveType(String type) {
		if (LANGUAGE_CPLUS.equals(type)) {
			currentLanguagePrimitiveType = C_PRIMITIVE_TYPE;
		} else if (LANGUAGE_CSHARP.equals(type)) {
			currentLanguagePrimitiveType = CSHARP_PRIMITIVE_TYPE;
		} else if (LANGUAGE_JAVA.equals(type)) {
			currentLanguagePrimitiveType = Java_PRIMITIVE_TYPE;
		}
	}

	/**
	 * 
	 * @param null
	 * @return boolean
	 */
	public static boolean isCSHARP() {
		return currentLanguagePrimitiveType == CSHARP_PRIMITIVE_TYPE;
	}

	/**
	 * 
	 * @param null
	 * @return boolean
	 */
	public static boolean isCPlus() {
		return currentLanguagePrimitiveType == C_PRIMITIVE_TYPE;
	}

	/**
	 * 
	 * @param null
	 * @return boolean
	 */
	public static boolean isJAVA() {
		return currentLanguagePrimitiveType == Java_PRIMITIVE_TYPE;
	}
}