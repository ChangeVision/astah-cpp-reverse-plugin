package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
/**
 * 
 * this class is the tag of index.xml
 *  the tag is <compound>.the class is named Compound
 *  the tag is <compound refid>.it is named "refid"
 *  the tag is <compound kind>.it is named "kind"
 *  the sub-tag is <name>.it is named "name"
 */
public class Compound {

	private String refid;
	private String kind;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRefid() {
		return refid;
	}

	public void setRefid(String compoundRefid) {
		this.refid = compoundRefid;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String compoundKind) {
		this.kind = compoundKind;
	}
}