package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * this class is the tag of **.xml
 *  the tag is <innernamespace>. the class is named Innernamespace.
 *  the tag is <innernamespace refid>.it is named "refid"
 *  the tag content is <innernamespace></innernamespace>.it is named "value"
 */
public class InnerNameSpace {
	private String refid;

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}