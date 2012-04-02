package com.change_vision.astah.extension.plugin.cplusreverse.reverser;


import org.apache.commons.digester.BeanPropertySetterRule;
import org.xml.sax.Attributes;

/**
 * Rule implements sets initializer. it will get All value between tag<initializer> and </initializer>,
 * 
 *
 * The property set:
 * can be specified when the rule is created
 * or can match the current element when the rule is called.
 *
 */
public class InitBeanPropertySetterRule extends BeanPropertySetterRule {
	
	private String content;

	public InitBeanPropertySetterRule() {
		this.propertyName = "initializer";
	}
	
	@Override
	public void body(String namespace, String name, String text)
			throws Exception {
		String currentContent = DoxygenDigester.current.trim();
		if (! text.trim().equals(currentContent)) {
			int startIndex = currentContent.indexOf("<ref");
			int midIndex = currentContent.indexOf(">", startIndex);
			int endIndex = currentContent.indexOf("</ref>", startIndex);
			
			if (startIndex != -1
					&& midIndex != -1
					&& endIndex != -1) {
				currentContent.substring(midIndex, endIndex);
				this.content = currentContent.substring(0, startIndex)
				+ currentContent.substring(midIndex + ">".length(), endIndex)
				+ currentContent.substring(endIndex + "</ref>".length(), currentContent.length());
			}
		}
		super.body(namespace, name, text);
	}

	@Override
	public void begin(String namespace, String name, Attributes attributes)
			throws Exception {
		super.begin(namespace, name, attributes);
	}
	
	@Override
	public void end(String namespace, String name) throws Exception {
		if (content != null) {
			this.bodyText = content;
			content = null;
		}
		super.end(namespace, name);
		DoxygenDigester.current = "";
	}
}