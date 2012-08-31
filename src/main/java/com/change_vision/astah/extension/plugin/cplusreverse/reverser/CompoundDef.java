package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.xml.sax.SAXException;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;

/**
 * 
 * this class is the tag of **.xml the tag is <compounddef>. the class is named Compounddef the tag
 * is <compounddef id>.it is named "compounddefId" the tag is <compounddef kind>.it is named
 * "compounddefKind" the tag kind type is "class","namespace","union","struct" the tag is
 * <compounddef port>.it is named "compounddefVisible" the sub-tag is <compoundname>.it is named
 * "compoundName" the sub-tag is <location>.it is named "locationFile" the sub-tag is
 * <detaileddescription><para>.it is named "detaileddescriptionPara" templateParamList is list of
 * the templateParam'class.tag is <templateparamlist><param> sections is the list of
 * section'class.tag is <sectiondef>. basecompoundList is the list of basecompound'class.tag is
 * <basecompoundref>. derivedcompoundList is the list of derivedcompound'class.tag is
 * <derivedcompoundref> innerclass is the list of innerclass'class.tag is <innerclass>
 * innernamespace is the list of innernamespace'class.tag is <innernamespace>
 */

public class CompoundDef implements IConvertToJude {
    String compounddefId;

    String compounddefVisible;

    String compounddefKind;

    String compoundName;

    List<TempleParam> templateParamList = new ArrayList<TempleParam>();

    List<Section> sections = new ArrayList<Section>();

    StringBuilder detaileddescriptionParas = new StringBuilder();

    String briefdescriptionPara;

    String locationFile;

    int locationLine;

    String locationBodyFile;

    int locationBodyStart;

    int locationBodyEnd;

    List<BaseCompoundDefRef> basecompoundList = new ArrayList<BaseCompoundDefRef>();

    List<DerivedCompoundRef> derivedcompoundList = new ArrayList<DerivedCompoundRef>();

    List<InnerClass> innerclass = new ArrayList<InnerClass>();

    List<InnerNameSpace> innernamespace = new ArrayList<InnerNameSpace>();

    public static Map<String, Object> compounddef = new HashMap<String, Object>();

    public static final String KIND_CLASS = "class";

    public static final String KIND_NAMESPACE = "namespace";

    public static final String KIND_INTERFACE = "interface";

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    /**
     * add Section class to the list and set the Parent relation
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

    public List<DerivedCompoundRef> getDerivedcompoundList() {
        return derivedcompoundList;
    }

    public void setDerivedcompoundList(List<DerivedCompoundRef> derivedcompoundList) {
        this.derivedcompoundList = derivedcompoundList;
    }

    public void addDerivedcompoundList(DerivedCompoundRef derivedcompoundList) {
        this.derivedcompoundList.add(derivedcompoundList);
    }

    public List<BaseCompoundDefRef> getBasecompoundList() {
        return basecompoundList;
    }

    public void setBasecompoundList(List<BaseCompoundDefRef> basecompoundList) {
        this.basecompoundList = basecompoundList;
    }

    public void addBasecompoundList(BaseCompoundDefRef basecompoundList) {
        this.basecompoundList.add(basecompoundList);
    }

    public List<InnerClass> getInnerclass() {
        return innerclass;
    }

    public void addInnerclass(InnerClass innerclass) {
        this.innerclass.add(innerclass);
    }

    public void setInnerclass(List<InnerClass> innerclass) {
        this.innerclass = innerclass;
    }

    public List<InnerNameSpace> getInnernamespace() {
        return innernamespace;
    }

    public void addInnernamespace(InnerNameSpace innernamespace) {
        this.innernamespace.add(innernamespace);
    }

    public void setInnernamespace(List<InnerNameSpace> innernamespace) {
        this.innernamespace = innernamespace;
    }

    public List<TempleParam> getTemplateParamList() {
        return templateParamList;
    }

    public void setTemplateParamList(List<TempleParam> templateParamList) {
        this.templateParamList = templateParamList;
    }

    public void addTemplateParam(TempleParam templeParam) {
        templateParamList.add(templeParam);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CompoundDef) {
            return ((CompoundDef) obj).getCompounddefId().equals(this.compounddefId);
        } else {
            return false;
        }
    }

    public IElement convertToJudeModel(IElement parent, File[] files)
            throws InvalidEditingException, ClassNotFoundException, ProjectNotFoundException,
            IOException, SAXException {
        if (compounddef.get(this.compounddefId) != null) {
            return parent;
        }
        if (KIND_CLASS.equals(this.compounddefKind) || KIND_INTERFACE.equals(this.compounddefKind)) {
            convertClass(parent, files);
        } else if (KIND_NAMESPACE.equals(this.compounddefKind)) {
            convertPackage(files);
        } else {
            if (parent instanceof IPackage) {
                dealWithGlobalElements((IPackage) parent, files);
            }
        }
        return parent;
    }

    private void convertPackage(File[] files) throws ProjectNotFoundException,
            ClassNotFoundException, InvalidEditingException, IOException, SAXException {
        // deal with the kind is namespace
        // new pkg
        IPackage pkg = Tool.getPackage(compoundName.split("::"));
        dealWithGlobalElements(pkg, files);
        dealCompounddefInnerClass(files, pkg);
        InnerNameSpace[] innernamespaces = this.innernamespace
                .toArray(new InnerNameSpace[this.innernamespace.size()]);
        int length = innernamespaces.length;
        for (int i = 0; i < length; ++i) {
            InnerNameSpace innerNamespace = innernamespaces[i];
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals(innerNamespace.getRefid() + ".xml")) {
                    DoxygenXmlParser.parserCompounddefXML(files[j]).convertToJudeModel(pkg, files);
                }
            }
        }
        compounddef.put(this.compounddefId, pkg);
    }

    protected IClass convertClass(IElement parent, File[] files) throws ProjectNotFoundException,
            ClassNotFoundException, InvalidEditingException, SAXException, IOException {
        IClass iclass = getIElement(parent);
        StringBuilder definition = new StringBuilder();
        if (this.briefdescriptionPara != null) {
            definition.append("\\brief ");
            definition.append(this.briefdescriptionPara);
        }
        if (this.getDetaileddescriptionPara() != null) {
            definition.append(this.getDetaileddescriptionPara().trim());
        }
        if (!"".equals(definition.toString())) {
            iclass.setDefinition(definition.toString());
        }
        if (this.getCompounddefVisible() != null) {
            iclass.setVisibility(this.getCompounddefVisible());
        }
        dealWithTemplatePara(iclass, files);
        dealCompounddefInnerClass(files, iclass);
        compounddef.put(this.compounddefId, iclass);
        return iclass;
    }

    void dealWithTemplatePara(IClass parent, File[] files) throws InvalidEditingException,
            ClassNotFoundException, ProjectNotFoundException, SAXException {
        TempleParam[] templeParams = templateParamList.toArray(new TempleParam[templateParamList
                .size()]);
        int length = templeParams.length;
        for (int i = 0; i < length; ++i) {
            TempleParam templeParam = templeParams[i];
            templeParam.convertToJudeModel(parent, files);
        }
    }

    protected void dealWithGlobalElements(IPackage pkg, File[] files)
            throws ProjectNotFoundException, ClassNotFoundException, InvalidEditingException,
            IOException, SAXException {
    }

    public void convertChildren(File[] files) throws InvalidEditingException,
            ClassNotFoundException, ProjectNotFoundException, IOException, SAXException {

        // new generalization
        {
            BaseCompoundDefRef[] baseCompoundDefRefs = basecompoundList
                    .toArray(new BaseCompoundDefRef[basecompoundList.size()]);
            int length = baseCompoundDefRefs.length;
            for (int i = 0; i < length; ++i) {
                BaseCompoundDefRef baseCompound = baseCompoundDefRefs[i];
                IClass baseClass = (IClass) compounddef.get(baseCompound.getRefid());
                if (baseClass != null) {
                    Tool.getGeneralization((IClass) compounddef.get(getCompounddefId()), baseClass);
                } else {
                    String[] names = baseCompound.getValue().split("::");
                    List<String> path = convertPath(names);
                    Tool.getGeneralization((IClass) compounddef.get(getCompounddefId()), Tool
                            .getClass((String[]) path.toArray(new String[] {}),
                                    names[names.length - 1]));
                }
            }
        }
        {
            DerivedCompoundRef[] derivedCompoundRefs = derivedcompoundList
                    .toArray(new DerivedCompoundRef[derivedcompoundList.size()]);
            int length = derivedCompoundRefs.length;
            for (int i = 0; i < length; ++i) {
                DerivedCompoundRef derivedCompound = derivedCompoundRefs[i];
                IClass derivedClass = (IClass) compounddef.get(this.getCompounddefId());
                if (derivedClass != null) {
                    Tool.getGeneralization((IClass) compounddef.get(derivedCompound.getRefid()),
                            derivedClass);
                } else {
                    String[] names = derivedCompound.getValue().split("::");
                    List<String> path = convertPath(names);
                    Tool.getGeneralization((IClass) compounddef.get(derivedCompound.getRefid()),
                            Tool.getClass((String[]) path.toArray(new String[] {}),
                                    names[names.length - 1]));
                }
            }
        }
        if (!KIND_NAMESPACE.equals(this.compounddefKind)) {
            Section[] theSections = this.sections.toArray(new Section[this.sections.size()]);
            int length = theSections.length;
            for (int i = 0; i < length; ++i) {
                Section next = theSections[i];
                IElement element = (IElement) compounddef.get(this.compounddefId);
                if (element != null) {
                    next.convertToJudeModel(element, files);
                }
            }
        }
    }

    void dealCompounddefInnerClass(File[] files, IElement pkg) throws IOException, SAXException,
            InvalidEditingException, ClassNotFoundException, ProjectNotFoundException {
        // new class in the pkg
        InnerClass[] innerclasses = this.innerclass.toArray(new InnerClass[this.innerclass.size()]);
        int length = innerclasses.length;
        for (int ii = 0; ii < length; ++ii) {
            InnerClass innerCls = innerclasses[ii];
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(innerCls.getRefid() + ".xml")) {
                    CompoundDef newCompounddef = DoxygenXmlParser.parserCompounddefXML(files[i]);
                    newCompounddef.convertToJudeModel(pkg, files);
                }
            }
        }
    }

    List<String> convertPath(String[] names) {
        List<String> path = new ArrayList<String>();
        if (names.length > 1) {
            for (int i = 0; i < (names.length - 1); i++) {
                path.add(names[i]);
            }
        }
        return path;
    }

    public IClass getIElement(IElement parent) throws ProjectNotFoundException,
            ClassNotFoundException, InvalidEditingException {
        String[] names = compoundName.split("::");
        List<String> path = convertPath(names);

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

    IClass getNestClass(List<String> paths) throws ProjectNotFoundException,
            ClassNotFoundException, InvalidEditingException {
        IClass current = null;
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            if (i == 0) {
                current = Tool.getClass(new String[] {}, path);
            } else {
                current = Tool.getClass(current, path);
            }
        }
        return current;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}