package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;

/**
 * 
 * this class is the tag of **.xml
 *  the tag is <compounddef>. the class is named Compounddef
 *  the tag is <compounddef id>.it is named "compounddefId"
 *  the tag is <compounddef kind>.it is named "compounddefKind"
 *  the tag kind type is "class","namespace","union","struct"
 *  the tag is <compounddef port>.it is named "compounddefVisible"
 *  the sub-tag is <compoundname>.it is named "compoundName"
 *  the sub-tag is <location>.it is named "locationFile"
 *  the sub-tag is <detaileddescription><para>.it is named "detaileddescriptionPara"
 *  templateParamList is list of the templateParam'class.tag is <templateparamlist><param>
 *  sections is the list of section'class.tag is <sectiondef>.
 *  basecompoundList is the list of basecompound'class.tag is <basecompoundref>.
 *  derivedcompoundList is the list of derivedcompound'class.tag is <derivedcompoundref>
 *  innerclass is the list of innerclass'class.tag is <innerclass>
 *  innernamespace is the list of innernamespace'class.tag is <innernamespace>
 */

public class CompoundDef implements IConvertToJude {
	String compounddefId;

	String compounddefVisible;

	String compounddefKind;
	
	String compoundName;

	Vector templateParamList = new Vector();

	Vector sections = new Vector();
	
	StringBuilder detaileddescriptionParas = new StringBuilder();
	
	String briefdescriptionPara;

	String locationFile;
	
	// #205 #219
	int locationLine;

	String locationBodyFile;

	int locationBodyStart;

	int locationBodyEnd;
	//
	
	Vector basecompoundList = new Vector();

	Vector derivedcompoundList = new Vector();

	Vector innerclass = new Vector();

	Vector innernamespace = new Vector();

	public static Map compounddef = new HashMap();

	public static final String KIND_CLASS = "class";

	public static final String KIND_NAMESPACE = "namespace";
	
	public static final String KIND_INTERFACE = "interface";

	public Vector getSections() {
		return sections;
	}

	public void setSections(Vector sections) {
		this.sections = sections;
	}
	
	/**
	 * add Section class to the list
	 * and set the Parent relation
	 */
	public void addSection(Section newSection) {
		sections.add(newSection);
		newSection.setParent(this);
	}

	public String getCompounddefVisible() {
		return compounddefVisible;
	}

	public void setCompounddefVisible(String compounddefVisible) {
		this.compounddefVisible = compounddefVisible;
	}

	public String getCompoundName() {
		return compoundName;
	}

	public void setCompoundName(String compoundName) {
		this.compoundName = compoundName;
	}

	public String getCompounddefId() {
		return compounddefId;
	}

	public void setCompounddefId(String compounddefId) {
		this.compounddefId = compounddefId;
	}

	public String getCompounddefKind() {
		return compounddefKind;
	}

	public void setCompounddefKind(String compounddefKind) {
		this.compounddefKind = compounddefKind;
	}
	
	public String getBriefdescriptionPara() {
		return briefdescriptionPara;
	}

	public void setBriefdescriptionPara(String briefdescriptionPara) {
		this.briefdescriptionPara = briefdescriptionPara + "\n\n";
	}

	public String getDetaileddescriptionPara() {
		return detaileddescriptionParas.toString();
	}

	public void setDetaileddescriptionPara(String detaileddescriptionPara) {
		this.detaileddescriptionParas.append(detaileddescriptionPara + "\n\n");
	}

	public String getLocationFile() {
		return locationFile;
	}

	public void setLocationFile(String locationFile) {
		this.locationFile = locationFile;
	}
	
	// #205 #219
	public int getLocationLine() {
		return locationLine;
	}

	public void setLocationLine(int locationLine) {
		this.locationLine = locationLine;
	}

	public String getLocationBodyFile() {
		return locationBodyFile;
	}

	public void setLocationBodyFile(String locationBodyFile) {
		this.locationBodyFile = locationBodyFile;
	}

	public int getLocationBodyStart() {
		return locationBodyStart;
	}

	public void setLocationBodyStart(int locationBodyStart) {
		this.locationBodyStart = locationBodyStart;
	}

	public int getLocationBodyEnd() {
		return locationBodyEnd;
	}

	public void setLocationBodyEnd(int locationBodyEnd) {
		this.locationBodyEnd = locationBodyEnd;
	}
	//

	public void setDerivedCompound(String derivedCompound) {
		derivedcompoundList.add(derivedCompound);
	}

	public void setBaseCompound(String baseCompound) {
		basecompoundList.add(baseCompound);
	}

	public Vector getDerivedcompoundList() {
		return derivedcompoundList;
	}

	public void setDerivedcompoundList(Vector derivedcompoundList) {
		this.derivedcompoundList = derivedcompoundList;
	}

	public void addDerivedcompoundList(DerivedCompoundRef derivedcompoundList) {
		this.derivedcompoundList.add(derivedcompoundList);
	}

	public Vector getBasecompoundList() {
		return basecompoundList;
	}

	public void setBasecompoundList(Vector basecompoundList) {
		this.basecompoundList = basecompoundList;
	}

	public void addBasecompoundList(BaseCompoundDefRef basecompoundList) {
		this.basecompoundList.add(basecompoundList);
	}

	public Vector getInnerclass() {
		return innerclass;
	}

	public void addInnerclass(InnerClass innerclass) {
		this.innerclass.add(innerclass);
	}

	public void setInnerclass(Vector innerclass) {
		this.innerclass = innerclass;
	}

	public Vector getInnernamespace() {
		return innernamespace;
	}

	public void addInnernamespace(InnerNameSpace innernamespace) {
		this.innernamespace.add(innernamespace);
	}

	public void setInnernamespace(Vector innernamespace) {
		this.innernamespace = innernamespace;
	}
	
	public Vector getTemplateParamList() {
		return templateParamList;
	}

	public void setTemplateParamList(Vector templateParamList) {
		this.templateParamList = templateParamList;
	}

	public void addTemplateParam(TempleParam templeParam) {
		templateParamList.add(templeParam);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CompoundDef) {
			return ((CompoundDef) obj).getCompounddefId().equals(this.getCompounddefId());
		} else {
			return false;
		}
	}
	
	public IElement convertToJudeModel(IElement parent, File[] files) throws InvalidEditingException,
			ClassNotFoundException, ProjectNotFoundException, IOException, SAXException {
		if (compounddef.get(this.getCompounddefId()) != null) {
			return parent;
		}
		if (KIND_CLASS.equals(this.getCompounddefKind())||KIND_INTERFACE.equals(this.getCompounddefKind())) {
			convertClass(parent, files);
		} else if (KIND_NAMESPACE.equals(this.getCompounddefKind())) {
			convertPackage(files);
		} else {
			if (parent instanceof IPackage) {
				dealWithGlobalElements((IPackage) parent, files);
			}
		}
		return parent;
	}

	private void convertPackage(File[] files) throws ProjectNotFoundException,
			ClassNotFoundException, InvalidEditingException, IOException,
			SAXException {
		//deal with the kind is namespace
		//new pkg
		IPackage pkg = Tool.getPackage(getCompoundName().split("::"));
		dealWithGlobalElements(pkg, files);
		dealCompounddefInnerClass(files, pkg);
		Vector innernamespaces = getInnernamespace();
		for (Iterator iter = innernamespaces.iterator(); iter.hasNext();) {
			InnerNameSpace innerNamespace = (InnerNameSpace) iter.next();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(innerNamespace.getRefid() + ".xml")) {
					DoxygenXmlParser.parserCompounddefXML(files[i]).convertToJudeModel(pkg, files);
				}
			}
		}
		compounddef.put(this.getCompounddefId(), pkg);
	}

	protected IClass convertClass(IElement parent, File[] files)
			throws ProjectNotFoundException, ClassNotFoundException,
			InvalidEditingException, SAXException, IOException {
		IClass iclass = getIElement(parent);
		String definition = "";
		if (this.getBriefdescriptionPara() != null) {
			definition += "\\brief ";
			definition += getBriefdescriptionPara();
		}
		if(this.getDetaileddescriptionPara()!= null){
			definition += this.getDetaileddescriptionPara().trim();	
		}
		if (!"".equals(definition)) {
			iclass.setDefinition(definition);
		}
		if(this.getCompounddefVisible()!=null){
			iclass.setVisibility(this.getCompounddefVisible());
		}
		dealWithTemplatePara(iclass, files);
		dealCompounddefInnerClass(files, iclass);
		compounddef.put(this.getCompounddefId(), iclass);		
		return iclass;
	}

	void dealWithTemplatePara(IClass parent, File[] files) throws InvalidEditingException,
			ClassNotFoundException, ProjectNotFoundException, SAXException {
		for (Iterator iterator = templateParamList.iterator(); iterator.hasNext();) {
			TempleParam templeParam = (TempleParam) iterator.next();
			templeParam.convertToJudeModel(parent, files);
		}
	}

	protected void dealWithGlobalElements(IPackage pkg, File[] files) throws ProjectNotFoundException,
			ClassNotFoundException, InvalidEditingException, IOException, SAXException {}

	public void convertChildren(File[] files) throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, IOException, SAXException {

		//new generalization
		for (Iterator iterator = basecompoundList.iterator(); iterator.hasNext();) {
			BaseCompoundDefRef baseCompound = (BaseCompoundDefRef) iterator.next();
			IClass baseClass = (IClass) compounddef.get(baseCompound.getRefid());
			if (baseClass != null) {
				Tool.getGeneralization((IClass) compounddef.get(getCompounddefId()), baseClass);
			} else {
				String[] names = baseCompound.getValue().split("::");
				List path = convertPath(names);
				Tool.getGeneralization((IClass) compounddef.get(getCompounddefId()), Tool.getClass((String[]) path
						.toArray(new String[]{}), names[names.length - 1]));
			}
		}

		for (Iterator iterator = derivedcompoundList.iterator(); iterator.hasNext();) {
			DerivedCompoundRef derivedCompound = (DerivedCompoundRef) iterator.next();
			IClass derivedClass = (IClass) compounddef.get(this.getCompounddefId());
			if (derivedClass != null) {
				Tool.getGeneralization((IClass) compounddef.get(derivedCompound.getRefid()), derivedClass);
			} else {
				String[] names = derivedCompound.getValue().split("::");
				List path = convertPath(names);
				Tool.getGeneralization((IClass) compounddef.get(derivedCompound.getRefid()), Tool.getClass(
						(String[]) path.toArray(new String[]{}), names[names.length - 1]));
			}
		}

		if (!KIND_NAMESPACE.equals(getCompounddefKind())) {
			for (Iterator iterator = this.getSections().iterator(); iterator.hasNext();) {
				Section next = (Section) iterator.next();
				IElement element = (IElement) compounddef.get(this.getCompounddefId());
				if (element != null) {
					next.convertToJudeModel(element, files);
				}
			}
		}
	}
	
	void dealCompounddefInnerClass(File[] files, IElement pkg) throws IOException,
			SAXException, InvalidEditingException, ClassNotFoundException, ProjectNotFoundException {
		// new class in the pkg
		Vector innerclasses = this.getInnerclass();
		for (Iterator iter = innerclasses.iterator(); iter.hasNext();) {
			InnerClass innerCls = (InnerClass) iter.next();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(innerCls.getRefid() + ".xml")) {
					CompoundDef newCompounddef = DoxygenXmlParser.parserCompounddefXML(files[i]);
					newCompounddef.convertToJudeModel(pkg, files);
				}
			}
		}
	}

	List convertPath(String[] names) {
		List path = new ArrayList();
		if (names.length > 1) {
			for (int i = 0; i < (names.length - 1); i++) {
				path.add(names[i]);
			}
		}
		return path;
	}

	public IClass getIElement(IElement parent) throws ProjectNotFoundException, ClassNotFoundException,
			InvalidEditingException {
		String[] names = compoundName.split("::");
		List path = convertPath(names);

		IClass iclass = null;
		if (parent instanceof IModel) {
			IClass nestClass = getNestClass(path);
			if (nestClass != null) {
				iclass = Tool.getClass(nestClass, names[names.length - 1]);
			} else {
				iclass = Tool.getClass((IModel) parent, names[names.length - 1]);
			}
		} else if (parent instanceof IPackage) {
			iclass = Tool.getClass(((IPackage) parent), names[names.length - 1].toString());
		} else if (parent instanceof IClass) {
			iclass = Tool.getClass(((IClass) parent), names[names.length - 1].toString());
		}
		return iclass;
	}

	IClass getNestClass(List paths) throws ProjectNotFoundException, ClassNotFoundException, InvalidEditingException {
		IClass current = null;
		for (int i = 0; i < paths.size(); i++) {
			String path = (String) paths.get(i);
			if (i == 0) {
				current = Tool.getClass(new String[]{}, path);
			} else {
				current = Tool.getClass(current, path);
			}
		}
		return current;
	}
}