package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
/**
 * 
 * this class is the tag of **.xml
 *  the tag is <basecompoundref>.the class is named BaseCompounddefref
 *  the tag is <basecompoundref refid>.it is named "refid"
 *  the tag is <basecompoundref prot>.it is named "prot"
 *  the tag is <basecompoundref virt>.it is named "virt"
 *  the tag content is <basecompoundref></basecompoundref>.it is named "value"
 */
public class BaseCompoundDefRef {
	
	private String refid;
	private String prot;
	private String virt;
	private String value;
	
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
	public String getVirt() {
		return virt;
	}
	public void setVirt(String virt) {
		this.virt = virt;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}