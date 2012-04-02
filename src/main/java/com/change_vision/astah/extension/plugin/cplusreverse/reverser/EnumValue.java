package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;

/**
 * 
 * this class is the tag of **.xml
 *  the tag is <enumvalue>. the class is named EnumValue
 *  the tag is <enumvalue id>.it is named "id"
 *  the tag is <enumvalue prot>.it is named "prot"
 *  the sub-tag is <name>.it is named "name"
 *  the sub-tag is <initializer>.it is named "initializer"
 *  the sub-tag is <briefdescription>.it is named "briefdescription"
 *  the sub-tag is <detaileddescription>.it is named "detaileddescription"
 */
public class EnumValue implements IConvertToJude {
	String id;
	String prot;
	String name;
	String initializer;
	String briefdescription;
	String detaileddescription;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getProt() {
		return prot;
	}
	
	public void setProt(String prot) {
		this.prot = prot;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBriefdescription() {
		return briefdescription;
	}
	
	public void setBriefdescription(String briefdescription) {
		this.briefdescription = briefdescription;
	}
	
	public String getDetaileddescription() {
		return detaileddescription;
	}
	
	public void setDetaileddescription(String detaileddescription) {
		this.detaileddescription = detaileddescription;
	}
	
	public IElement convertToJudeModel(IElement parent, File[] files)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, IOException, SAXException {
		IAttribute iattr = Tool.getAttribute((IClass) parent, name, "int");
		if (getInitializer() != null)
			iattr.setInitialValue(this.getInitializer());
		iattr.addStereotype("enum constant");
		return parent;
	}

	public String getInitializer() {
		return initializer;
	}

	public void setInitializer(String initializer) {
		this.initializer = initializer;
	}
}