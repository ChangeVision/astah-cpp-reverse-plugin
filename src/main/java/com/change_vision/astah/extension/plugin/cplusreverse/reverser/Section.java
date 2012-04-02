package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.xml.sax.SAXException;


import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IElement;

/**
 * 
 * this class is the tag of **.xml
 *  the tag is <sectiondef>. the class is named Section
 *  the tag attribute is <sectiondef kind>.it is named "kind"
 *  members is list of the member'class.tag is <memberdef>
 *  parent is the Compounddef relation
 */
public class Section implements IConvertToJude {
	private String kind;
	private Vector members;
	private CompoundDef parent;

	public CompoundDef getParent() {
		return parent;
	}

	public void setParent(CompoundDef parent) {
		this.parent = parent;
	}

	public Section() {
		members = new Vector();
	}

	public Vector getMembers() {
		return members;
	}

	public void setMembers(Vector members) {
		this.members = members;
	}

	public void addMember(Member member) {
		members.add(member);
		member.setParent(this);
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public IElement convertToJudeModel(IElement parent, File[] files) throws InvalidEditingException,
			ClassNotFoundException, ProjectNotFoundException, IOException, SAXException {
		for (Iterator iterator = members.iterator(); iterator.hasNext();) {
			Member member = (Member) iterator.next();
			member.convertToJudeModel(parent, files);
		}
		return parent;
	}
}