package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
/**
 * 
 * this class is the tag of **.xml
 *  the tag is <derivedcompoundref>.the class is named Derivedcompoundref
 *  the tag is <derivedcompoundref refid>.it is named "refid"
 *  the tag is <derivedcompoundref prot>.it is named "prot"
 *  the tag is <derivedcompoundref virt>.it is named "virt"
 *  the tag content is <derivedcompoundref></derivedcompoundref>.it is named "value"
 */
public class DerivedCompoundRef {
	String refid;
	String prot;
	String virt;
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