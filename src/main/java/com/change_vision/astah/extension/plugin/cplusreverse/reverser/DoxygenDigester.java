package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class DoxygenDigester extends Digester {
	
	static String current = "";

	@SuppressWarnings("unchecked")
    @Override
	public void characters(char[] buffer, int start, int length) throws SAXException {
		super.characters(buffer, start, length);
		if (! bodyText.toString().trim().equals("")) {
			if (this.match.equals("doxygen/compounddef/sectiondef/memberdef/initializer")) {
				String sb = new String(buffer);
				int lastIndexOf = sb.lastIndexOf("<initializer>", start);
				int indexOf = sb.indexOf("</initializer>", start + length);
				if (lastIndexOf != -1 && indexOf != -1) {
					current = sb.substring(lastIndexOf + "<initializer>".length(), indexOf).trim();
				} else if (lastIndexOf != -1 && indexOf == -1) {
					current += sb.substring(lastIndexOf + "<initializer>".length(), buffer.length - 1).trim();
				} else if (lastIndexOf == -1 && indexOf != -1) {
					current += sb.substring(0, indexOf).trim();
				}
			} else if (this.match.equals("doxygen/compounddef/sectiondef/memberdef/type")) {
				String sb = new String(buffer);
				int lastIndexOf = sb.lastIndexOf("<type>", start);
				int indexOf = sb.indexOf("</type>", start + length);
				if (lastIndexOf != -1 && indexOf != -1) {
					current = sb.substring(lastIndexOf + "<type>".length(), indexOf).trim();
				} else if (lastIndexOf != -1 && indexOf == -1) {
					current += sb.substring(lastIndexOf + "<type>".length(), buffer.length - 1).trim();
				} else if (lastIndexOf == -1 && indexOf != -1) {
					current += sb.substring(0, indexOf).trim();
				}
			} else if (this.match.equals("doxygen/compounddef/detaileddescription/para/ref")) {
				//for bug 804
				String sb = new String(buffer);
				int beginIndexOf = sb.lastIndexOf("<ref", start);
				int endIndexOf = sb.indexOf("</ref>", start + length);
				if (beginIndexOf != -1 && endIndexOf != -1) {
					int refNameIndex = sb.indexOf(">", beginIndexOf);
					String refName = sb.substring(refNameIndex + 1, endIndexOf);
					int index = bodyTexts.size() - 1;
					StringBuffer text = new StringBuffer(this.bodyTexts.get(index).toString());
					text.append(refName);
					this.bodyTexts.set(index, text);
				}
			}
		}
	}
}