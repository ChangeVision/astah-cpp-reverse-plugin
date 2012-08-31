package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import org.apache.commons.digester.ObjectCreateRule;
import org.xml.sax.Attributes;

public class ObjectCreateAfterLanguageRule extends ObjectCreateRule {

    public ObjectCreateAfterLanguageRule(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public void begin(Attributes attributes) throws Exception {

        // Identify the name of the class to instantiate
        String realClassName = className;
        if (attributeName != null) {
            String value = attributes.getValue(attributeName);
            if (value != null) {
                realClassName = value;
            }
        }

        String plugClassName = realClassName;
        if (LanguageManager.isCPlus()) {
            plugClassName += "CPlus";
        }

        // Instantiate the new object and push it on the context stack
        try {
            Class<?> clazz = digester.getClassLoader().loadClass(plugClassName);
            Object instance = clazz.newInstance();
            digester.push(instance);
        } catch (ClassNotFoundException e) {
            Class<?> clazz = digester.getClassLoader().loadClass(realClassName);
            Object instance = clazz.newInstance();
            digester.push(instance);
        }
    }
}