package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * this class is the tag of **.xml
 *  the tag is <innerclass>. the class is named InnerClass.
 *  the tag is <innerclass refid>.it is named "refid"
 *  the tag is <innerclass prot>.it is named "prot"
 *  the tag content is <innerclass></innerclass>.it is named "value"
 */
public class InnerClass {
	String refid;
	String prot;
	String value;

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getProt() {
		return prot;
	}

	public void setProt(String prot) {
		this.prot = prot;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}