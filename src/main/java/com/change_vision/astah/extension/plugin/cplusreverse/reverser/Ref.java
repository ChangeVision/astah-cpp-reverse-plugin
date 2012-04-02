package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
/**
 * 
 * this class is the tag of **.xml
 *  the tag is <ref>. the class is namde Ref
 *  the tag is <ref refid>.it is named "refid"
 *  the tag is <ref kindref>.it is named "kindref"
 *  the tag content is<ref></ref>.it is  named "value"
 */
public class Ref {
	String refid;
	
	String kindref;
	
	String value;

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getKindref() {
		return kindref;
	}

	public void setKindref(String kindref) {
		this.kindref = kindref;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}