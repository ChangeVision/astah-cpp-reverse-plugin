package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.xml.sax.SAXException;

import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IClassifierTemplateParameter;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IGeneralization;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IParameter;
import com.change_vision.jude.api.inf.model.ISubsystem;
import com.change_vision.jude.api.inf.model.ITemplateBinding;
import com.change_vision.jude.api.inf.model.IUsage;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

/**
 * 
 * this class is the tag of **.xml the tag is <memberdef>. the class is named Member the tag is
 * <memberdef kind>.it is named "kind" the tag is <memberdef id>.it is named "id" the kind type is
 * "function","variable","property","enum" the tag is <memberdef prot>.it is named "prot" the tag is
 * <memberdef static>.it is named "staticBoolean" the tag is <memberdef const>.it is named
 * "constBoolean" the tag is <memberdef virt>.it is named "virt" the sub-tag is <name>.it is named
 * "name" the sub-tag is <type>.it is named "type" the type's sub-tag is <type ref>.it is named
 * "typeRef" the sub-tag is <argsstring>.it is named "argsstring" the sub-tag is <initializer>.it is
 * named "initializer" the initializer's sub-tag is <initializer ref">.it is named "initializerRef"
 * the sub-tag is <detaileddescription>.it is named "detaileddescription" the tag is <memberdef
 * gettable>.it is named "gettable" the tag is <memberdef settable>.it is named "settable"
 * memberParaList is the list of the Param.tag is <param> enumValues is the list of the
 * EnumValue.tag is <enumvalue> Parent is the Section relation it's have Astah C#'field.
 * const,override,readonly,delegate,sealed,internal,unsafe,virtual,abstract
 */
public abstract class Member implements IConvertToJude {
    String kind;
    String id;
    String prot;
    String staticBoolean;
    String constBoolean;
    String virt;
    String explicit;
    String inline;
    String mutable;
    String name;
    String type;
    List<Ref> typeRefs = new ArrayList<Ref>();
    String argsstring;
    String initializer;
    Ref initializerRef;
    StringBuilder detaileddescriptionParas = new StringBuilder();
    String briefdescriptionPara;
    String gettable;
    String settable;
    List<Param> memberParaList;
    List<EnumValue> enumValues;
    Section parent;

    public static final Map<String, String> TYPEDEFS = new HashMap<String, String>();

    public static final String KIND_FUNCTION = "function";

    public static final String KIND_SIGNAL = "signal";

    public static final String KIND_SLOT = "slot";

    public static final String KIND_ATTRIBUTE = "variable";

    public static final String KIND_PROPERTY = "property";

    public static final String KIND_ENUM = "enum";

    public static final String KIND_EVENT = "event";

    public static final String KIND_FRIEND = "friend";

    public static final String CONST = "const";

    public static final String AND = "&";

    public static final String STAR = "*";

    public static final String KEYWORD_ARRAY = "array";

    private static final String[] SORTED_COLLECTION_NAMES = { "list", "queue", "vector", "stack" };
    private static final String[] UNIQUE_COLLECTION_NAMES = { "set" };
    private static final String[] KEY_VALUE_COLLECTION_NAMES = { "map" };

    public Section getParent() {
        return parent;
    }

    public void setParent(Section parent) {
        this.parent = parent;
    }

    public Member() {
        enumValues = new ArrayList<EnumValue>();
        memberParaList = new ArrayList<Param>();
    }

    public void addEnum(EnumValue newEnum) {
        enumValues.add(newEnum);
    }

    public List<EnumValue> getEnums() {
        return enumValues;
    }

    public void setEnums(List<EnumValue> enums) {
        this.enumValues = enums;
    }

    public String getConstBoolean() {
        return constBoolean;
    }

    public void setConstBoolean(String constBoolean) {
        this.constBoolean = constBoolean;
    }

    public String getArgsstring() {
        return argsstring;
    }

    public void setArgsstring(String argsstring) {
        this.argsstring = argsstring;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getProt() {
        return prot;
    }

    public void setProt(String prot) {
        this.prot = prot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaticBoolean() {
        return staticBoolean;
    }

    public void setStaticBoolean(String staticBoolean) {
        this.staticBoolean = staticBoolean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInitializer() {
        return initializer;
    }

    public void setInitializer(String initializer) {
        this.initializer = initializer;
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

    public String getGettable() {
        return gettable;
    }

    public void setGettable(String gettable) {
        this.gettable = gettable;
    }

    public String getSettable() {
        return settable;
    }

    public void setSettable(String settable) {
        this.settable = settable;
    }

    public Ref getInitializerRef() {
        return initializerRef;
    }

    public void setInitializerRef(Ref initializerRef) {
        this.initializerRef = initializerRef;
    }

    public List<Param> getMemberParaList() {
        return memberParaList;
    }

    public void setMemberParaList(List<Param> memberParaList) {
        this.memberParaList = memberParaList;
    }

    public void addMemberParam(Param memberParam) {
        memberParaList.add(memberParam);
    }

    public void addTypeRef(Ref typeRef) {
        this.typeRefs.add(typeRef);
    }

    public List<Ref> getTypeRefs() {
        return typeRefs;
    }

    public String getVirt() {
        return virt;
    }

    public void setVirt(String virt) {
        this.virt = virt;
    }

    public String getExplicit() {
        return explicit;
    }

    public void setExplicit(String explicit) {
        this.explicit = explicit;
    }

    public String getInline() {
        return inline;
    }

    public void setInline(String inline) {
        this.inline = inline;
    }

    public String getMutable() {
        return mutable;
    }

    public void setMutable(String mutable) {
        this.mutable = mutable;
    }

    public List<EnumValue> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<EnumValue> enumValues) {
        this.enumValues = enumValues;
    }

    public String getIClassFullName(IClass cls) {
        StringBuilder fullname = new StringBuilder();
        IElement owner = cls.getOwner();
        while (true) {
            if (owner instanceof IModel || owner == null) {
                break;
            } else {
                fullname.insert(0, ((INamedElement) owner).getName());
                fullname.append("::");
                owner = owner.getOwner();
            }
        }
        fullname.append(cls.getName());
        return fullname.toString();
    }

    public IElement convertToJudeModel(IElement parent, File[] files)
            throws InvalidEditingException, ClassNotFoundException, ProjectNotFoundException,
            IOException, SAXException {
        BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();
        if (KIND_ATTRIBUTE.equals(this.getKind())) {
            convertToAttribute(parent, basicModelEditor);
        } else if (KIND_FUNCTION.equals(this.getKind()) || KIND_SIGNAL.equals(this.getKind())
                || KIND_SLOT.equals(this.getKind())) {
            convertToFunction(parent, files, basicModelEditor);
        } else if (KIND_PROPERTY.equals(this.getKind())) {
            convertToAttribute(parent, basicModelEditor);
        } else if (KIND_ENUM.equals(this.getKind())) {
            convertToEnum(parent, files);
        } else if (KIND_EVENT.equals(this.getKind())) {
            convertToFunction(parent, files, basicModelEditor);
        } else if (KIND_FRIEND.equals(this.getKind()) && argsstring.indexOf("(") != -1) {
            convertToFunction(parent, files, basicModelEditor);
        } else if ("typedef".equals(this.getKind())) {
            if (type.indexOf("<") != -1) {
                String[] namespace = new String[] {};
                Tool.getAnonimousClass(parent, namespace, type, getTypeRefs());
                TYPEDEFS.put(this.name, Tool.filterAnonimousString(type, getTypeRefs()));
            } else {
                String[] split = getType().split(" ");
                String filterName = Tool.filterInvalidChar(split[split.length - 1]);
                if (!getTypeRefs().isEmpty()) {
                    filterName = getTypeRefs().get(0).getValue().trim();
                }
                if (!"*".equals(filterName) && !"&".equals(filterName) && !"**".equals(filterName)
                        && !"".equals(filterName)) {
                    TYPEDEFS.put(this.name, filterName);
                }
            }
        } else {
            System.out.println("NO DEAL(KIND)= " + this.getKind());
        }
        return parent;
    }

    protected void convertToEnum(IElement parent, File[] files) throws ProjectNotFoundException,
            ClassNotFoundException, InvalidEditingException, IOException, SAXException {
        IClass enumClass = Tool.getClass(((IClass) parent), getName());
        enumClass.addStereotype("enum");
        EnumValue[] theEnumValues = enumValues.toArray(new EnumValue[enumValues.size()]);
        for (int i = 0; i < theEnumValues.length; ++i) {
            EnumValue enumValue = theEnumValues[i];
            enumValue.convertToJudeModel(enumClass, files);
        }
        CompoundDef.compounddef.put(this.id, enumClass);
    }

    protected void convertToFunction(IElement parent, File[] files,
            BasicModelEditor basicModelEditor) throws InvalidEditingException,
            ClassNotFoundException, ProjectNotFoundException {
        String type = getTypeFromTypeDef(getType());
        type = dealWithConstForOperation(type);
        FilterKeyword result = filterKeyword(type);
        Set<String> keywords = result.keywords;
        type = result.toType.trim();
        String array = "";
        if (keywords.contains(KEYWORD_ARRAY)) {
            array = getType().substring(getType().indexOf("[")).trim();
        }
        if ("".equals(type) && !getTypeRefs().isEmpty()) {
            type = getTypeRefs().get(0).value.trim();
        }
        String filteredType = getTypeFromTypeDef(type).trim() + array;
        IOperation fun = null;
        if (LanguageManager.getCurrentLanguagePrimitiveType().contains(filteredType)) {
            fun = Tool.getOperation((IClass) parent, name, Tool.filterInvalidChar(filteredType));
        } else {
            String[] path;
            if (filteredType.indexOf("<") != -1) {
                path = new String[1];
                path[0] = filteredType;
            } else {
                path = filteredType.split("::");
            }
            filteredType = path.length > 0 ? path[path.length - 1].trim() : filteredType.trim();
            String[] namespace = new String[] {};
            IClass classType = null;
            if (path.length > 1) {
                namespace = new String[path.length - 1];
                System.arraycopy(path, 0, namespace, 0, path.length - 1);
                classType = Tool.getClass(namespace, filteredType);
            }
            if (classType != null) {
                fun = Tool.getOperation((IClass) parent, name, classType);
            } else {
                if (filteredType.indexOf("<") != -1) {
                    fun = createOperationWithAnonimousboundclass(parent, namespace,
                            basicModelEditor, filteredType);
                }
                if (fun == null) {
                    fun = Tool.getOperation((IClass) parent, name,
                            Tool.filterInvalidChar(filteredType));
                }
            }
        }
        if (null == fun) {
            return;
        }
        fun.setVisibility(this.getProt());
        fun.setStatic(!"no".equals(this.getStaticBoolean()));
        String definition = getDefinition();
        if (!"".equals(definition)) {
            fun.setDefinition(definition);
        }

        dealOperationKeyword(basicModelEditor, keywords, fun);

        int index = 0;
        Param[] memberParas = memberParaList.toArray(new Param[memberParaList.size()]);
        for (int i = 0; i < memberParas.length; ++i) {
            Param param = memberParas[i];
            IParameter iParameter = (IParameter) param.convertToJudeModel(fun, files);
            if (null != iParameter && this.argsstring != null && param.declname == null) {
                String[] paramName = this.argsstring.split(",");
                if (index < paramName.length) {
                    dealParamName(iParameter, paramName[index]);
                }
            }
            index++;
        }
    }

    private String dealWithConstForOperation(String type) {
        if (type.trim().indexOf(CONST) == 0) {
            type = type.replaceFirst(CONST, "").trim();
        }
        if (argsstring.indexOf(" " + CONST) != -1) {
            type = CONST + " " + type;
        }
        return type;
    }

    private void dealParamName(IParameter iParameter, String paramName)
            throws InvalidEditingException {
        dealCharacter(iParameter, paramName, "&");
        dealCharacter(iParameter, paramName, "*");
    }

    private void dealCharacter(IParameter iParameter, String paramName, String character)
            throws InvalidEditingException {
        if (paramName.indexOf(character) != -1 && !paramName.endsWith(character + ")")
                && !paramName.endsWith(character)) {
            int indexOf = paramName.indexOf(character);
            if (paramName.endsWith(")")) {
                iParameter.setName(paramName.substring(indexOf + 1, paramName.length() - 1));
            } else {
                iParameter.setName(paramName.substring(indexOf + 1, paramName.length()));
            }
        }
    }

    private IOperation createOperationWithAnonimousboundclass(IElement parent, String[] allPath,
            BasicModelEditor basicModelEditor, String type) throws ProjectNotFoundException,
            ClassNotFoundException, InvalidEditingException {
        FilterKeyword result = filterKeyword(type);
        Set<String> keywords = result.keywords;
        IClass anonimousClass = Tool.getAnonimousClass(allPath, null, type, getTypeRefs());
        if (anonimousClass != null) {
            IOperation fun = Tool.getOperation((IClass) parent, name, anonimousClass);
            dealOperationKeyword(basicModelEditor, keywords, fun);
            return fun;
        }
        return null;
    }

    public static String getTypeFromTypeDef(String name) {
        String type = name;
        while ((type = (String) TYPEDEFS.get(type)) != null && !type.equals(name)) {
            name = type;
        }
        return name;
    }

    protected void convertToAttribute(IElement parent, BasicModelEditor basicModelEditor)
            throws InvalidEditingException, ProjectNotFoundException, ClassNotFoundException,
            IOException {

        String paramString = "";
        if (getType().indexOf("<") != -1 && getType().indexOf(">") != -1) {
            paramString = getType().substring(getType().indexOf("<") + 1,
                    getType().lastIndexOf(">"));
        }
        String[] params = paramString.split(",");
        for (int i = 0; i < params.length; i++) {
            params[i] = params[i].trim();
        }
        IClass templateClass = getTemplateClass(params.length);
        boolean isConvertToAss = true;
        if (KIND_ATTRIBUTE.equals(this.getKind()) && isConvertToAss
                && isCollectionClass(templateClass)) {
            IClass qualifierType = null;
            IClass iChild = null;
            String[][] multiplicity = null;
            if (isKeyValueCollectionClass(templateClass)) {
                // IMap<K,V> etc.
                // case: Map<Key, Value> get value
                // 1.5.8
                // type="IMap<String, Child>", typeRefs={}
                // 1.7.0 & 1.7.1
                // type="IMap<String, >", typeRefs={ref(Value)}
                // type="IMap<, String>", typeRefs={ref(Key)}
                // type="IMap< , >", typeRefs={ref(Key), ref(Value)}
                // 1.7.1:
                // type="< , >", typeRefs={ref(MyMap), ref(Key), ref(Value)}
                // type="<String, >", typeRefs={ref(MyMap), ref(Value)}
                // type="< , String>", typeRefs={ref(MyMap), ref(Key)}
                if (params.length > 1) {
                    int paramIndex = (getType().indexOf("<") == 0) ? 1 : 0;
                    if (params[0].equals("") && paramIndex < typeRefs.size()) {
                        qualifierType = (IClass) CompoundDef.compounddef.get(typeRefs.get(
                                paramIndex++).getRefid());
                    } else {
                        qualifierType = getClassByName(params[0]);
                    }
                    if (params[1].equals("")) {
                        iChild = (IClass) CompoundDef.compounddef.get(typeRefs.get(paramIndex)
                                .getRefid());
                    } else {
                        iChild = getClassByName(params[1]);
                    }
                }
            } else {
                // ICollection<E> etc.
                // case:List<Child> get Child
                if (getType().indexOf("<") == 0 && typeRefs.size() > 1) {
                    // 1.7.1: MyList<MyChild>, type="< >", typeRefs={MyList, MyChild}
                    iChild = (IClass) CompoundDef.compounddef.get(typeRefs.get(1).getRefid());
                } else if (getType().indexOf("<") > 0 && !typeRefs.isEmpty()) {
                    // 1.7.0 & 1.7.1, IList<MyChild>, type="IList<>", typeRefs={MyChild}
                    iChild = (IClass) CompoundDef.compounddef.get(typeRefs.get(0).getRefid());
                } else {
                    // type="IList<String>", typeRefs={}
                    iChild = getClassByName(params[0]);
                }
                multiplicity = new String[][] { { "*" } };
            }
            if (iChild != null && !isCppPrimitive(iChild) && !isCppString(iChild)
                    && !isTemplateParameter(iChild)) {
                // create association and set name, constraint, multiplicity, qualifier
                IAssociation ass = generateAssoication((IClass) parent, basicModelEditor, iChild);
                IAttribute[] ends = ass.getMemberEnds();
                if (qualifierType != null) {
                    basicModelEditor.createQualifier(ends[1], "key", qualifierType);
                }
                if (isSortedCollectionClass(templateClass)) {
                    basicModelEditor.createConstraint(ends[1], "ordered");
                }
                if (isUniqueCollectionClass(templateClass)) {
                    basicModelEditor.createConstraint(ends[1], "unique");
                }
                if (multiplicity != null) {
                    ends[1].setMultiplicityStrings(multiplicity);
                }
                return;
            }
        }

        if (getType() != null && !"".equals(getType()) && getType().indexOf("<") != 0) {
            // all typerefs are actual parameters
            FilterKeyword result = filterKeyword(type);
            Set<String> keywords = result.keywords;
            String type = result.toType.trim();
            if ("".equals(type)) {
                if (1 == typeRefs.size() && "member".equalsIgnoreCase(typeRefs.get(0).getKindref())) {
                    type = typeRefs.get(0).getValue();
                    System.out.println(type);
                }
            }
            // like : IDictionary<String, >, ref={Child}
            // or: IDictionary<, >, ref={Key, Child}
            for (int i = 0, index = 0; i < params.length && index < typeRefs.size(); i++) {
                if (params[i].equals("")) {
                    params[i] = typeRefs.get(index++).getValue();
                }
            }
            if (templateClass != null) {
                type = templateClass.getName();
                if (getType().indexOf("<") != -1 && getType().indexOf(">") != -1
                        && params.length > 0) {
                    type += "<";
                    for (int i = 0; i < params.length; i++) {
                        type += params[i];
                        if (i < params.length - 1) {
                            type += ",";
                        }
                    }
                    type += ">";
                }
            }
            String filteredType = getTypeFromTypeDef(type).trim();
            String[] typepaths;
            if (filteredType.indexOf("<") != -1) {
                typepaths = new String[1];
                typepaths[0] = filteredType;
            } else {
                typepaths = filteredType.split("::");
                if (typepaths != null && typepaths.length == 1 && typepaths[0].equals("string")) {
                    typepaths = new String[] { "std", "string" };
                }
            }

            IAttribute attr = generateAttri(parent, basicModelEditor, typepaths);
            if (null != attr) {
                dealAttributeKeywords(basicModelEditor, keywords, attr);

                IClass attrType = attr.getType();
                if (!isKeepAttribute(attrType)) {
                    // convert attribute to association
                    generateAssoication((IClass) parent, basicModelEditor, attrType);
                    basicModelEditor.delete(attr);
                }
            }
        } else if (!getTypeRefs().isEmpty()) {
            // if is static, create attribute
            IClass anotherCls = Tool.getClass(type, getTypeRefs());
            if (anotherCls != null) {
                if (isKeepAttribute(anotherCls)) {
                    generateAttri(parent, basicModelEditor, anotherCls);
                } else {
                    // if not, create association
                    generateAssoication((IClass) parent, basicModelEditor, anotherCls);
                }
            }
        }
    }

    private boolean isTemplateParameter(IClass iChild) {
        if (iChild.getOwner() instanceof IClassifierTemplateParameter) {
            return true;
        }
        return false;
    }

    private boolean isKeepAttribute(IClass attrType) {
        if (!"no".equals(this.getStaticBoolean()) || isCppPrimitive(attrType)
                || isCppString(attrType) || isCppSystemClass(attrType)) {
            return true;
        }
        return false;
    }

    private boolean isCppSystemClass(IClass type) {
        for (IElement element = type; element != null;) {
            IElement owner = element.getOwner();
            if (owner instanceof IModel) {
                if (element instanceof IPackage && ((IPackage) element).getName().equals("std")) {
                    return true;
                }
                break;
            } else {
                element = owner;
            }
        }
        return false;
    }

    private boolean isCppString(IClass iChild) {

        if (iChild.getName().equals("string") && iChild.getOwner() instanceof IPackage) {
            IPackage pkg = (IPackage) iChild.getOwner();
            if (pkg.getName().equals("std") && pkg.getOwner() instanceof IModel // project model
                    && pkg.getOwner().getOwner() == null) {
                return true;
            }
        }
        return false;
    }

    private boolean isCppPrimitive(IClass iChild) {

        for (Object obj : LanguageManager.getCurrentLanguagePrimitiveType()) {
            if (iChild.getName().equals(obj)) {
                return true;
            }
        }
        return false;
    }

    private IClass getClassByName(String name) throws ProjectNotFoundException,
            ClassNotFoundException {
        IModel project = ProjectAccessorFactory.getProjectAccessor().getProject();
        if (project == null || name == null || name.equals("")) {
            return null;
        }

        if (name.equals("string")) {
            for (INamedElement child : project.getOwnedElements()) {
                if (child instanceof IPackage && child.getName().equals("std")) {
                    for (INamedElement child2 : ((IPackage) child).getOwnedElements()) {
                        if (child2 instanceof IClass && child2.getName().equals(name)) {
                            return (IClass) child2;
                        }
                    }
                }
            }
        }

        for (IClass cls : getClasses(project)) {
            if (cls.getName().equals(name)) {
                return cls;
            }
        }

        return null;
    }

    private List<IClass> getClasses(INamedElement namespace) {
        List<IClass> results = new ArrayList<IClass>();
        INamedElement[] ownedElements = null;
        if (namespace instanceof IPackage) {
            ownedElements = ((IPackage) namespace).getOwnedElements();
        } else if (namespace instanceof IClass) {
            ownedElements = ((IClass) namespace).getNestedClasses();
        }
        for (int i = 0; i < ownedElements.length; ++i) {
            INamedElement child = ownedElements[i];
            if (child instanceof IPackage || child instanceof IClass) {
                results.addAll(getClasses(child));
            }
            if (child instanceof IClass && !(child instanceof ISubsystem)) {
                results.add((IClass) child);
            }
        }

        return results;
    }

    private IClass getTemplateClass(int paramSize) throws ProjectNotFoundException,
            ClassNotFoundException {
        if (getType() != null && !"".equals(getType()) && getType().indexOf("<") > 0) {
            String templateName = getType().substring(0, getType().indexOf("<")).trim();
            String[] splits = templateName.split("::");
            templateName = splits[splits.length - 1];
            if (Arrays.asList(SORTED_COLLECTION_NAMES).contains(templateName)) {
                // list<T, allocator=allocator<T>>
                paramSize = Math.max(paramSize, 2);
            } else if (Arrays.asList(UNIQUE_COLLECTION_NAMES).contains(templateName)) {
                // set<Key, Compare, Allocator=allocator<T>>
                paramSize = Math.max(paramSize, 3);
            } else if (Arrays.asList(KEY_VALUE_COLLECTION_NAMES).contains(templateName)) {
                // map<Key, T, Compare, Allocator=allocator<T>>
                paramSize = Math.max(paramSize, 4);
            }

            IModel project = ProjectAccessorFactory.getProjectAccessor().getProject();
            List<IClass> classList = getClasses(project);
            IClass[] classes = classList.toArray(new IClass[classList.size()]);
            for (int i = 0; i < classes.length; ++i) {
                IClass cls = classes[i];
                if (cls.getName().equals(templateName)
                        && cls.getTemplateParameters().length == paramSize) {
                    return cls;
                }
            }
        } else if (!getTypeRefs().isEmpty()) {
            IClass iClass = (IClass) CompoundDef.compounddef.get(typeRefs.get(0).getRefid());
            return iClass;
        }
        return null;
    }

    private boolean isKeyValueCollectionClass(IClass cls) throws ProjectNotFoundException,
            ClassNotFoundException {
        return isCppCollectionClass(cls, KEY_VALUE_COLLECTION_NAMES);
    }

    private boolean isSortedCollectionClass(IClass cls) throws ProjectNotFoundException,
            ClassNotFoundException {
        return isCppCollectionClass(cls, SORTED_COLLECTION_NAMES);
    }

    private boolean isUniqueCollectionClass(IClass cls) throws ProjectNotFoundException,
            ClassNotFoundException {
        return isCppCollectionClass(cls, UNIQUE_COLLECTION_NAMES);
    }

    private boolean isCollectionClass(IClass cls) throws ProjectNotFoundException,
            ClassNotFoundException {
        return isKeyValueCollectionClass(cls) || isSortedCollectionClass(cls)
                || isUniqueCollectionClass(cls);
    }

    private boolean isCppCollectionClass(IClass cls, String[] collectionNames)
            throws ProjectNotFoundException, ClassNotFoundException {
        if (isCollectionByNames(cls, new String[] { "std" }, collectionNames)) {
            return true;
        }
        return false;
    }

    private boolean isCollectionByNames(IClass cls, String[] namespaces, String[] collectionNames)
            throws ProjectNotFoundException, ClassNotFoundException {
        if (cls == null || namespaces == null || collectionNames == null) {
            return false;
        }
        // judge is builtin collection class
        if (Arrays.asList(collectionNames).contains(cls.getName())) {
            List<String> namespaceStrings = getNamespaceStrings(cls);
            if (namespaceStrings.equals(Arrays.asList(namespaces))) {
                return true;
            }
        }

        // user defined Collection class, e.g. class MyList extends ArrayList{}
        List<Object> theAncestors = getAncestors(cls, new ArrayList<Object>());
        Object[] ancestors = theAncestors.toArray(new Object[theAncestors.size()]);
        for (int i = 0; i < ancestors.length; ++i) {
            Object obj = ancestors[i];
            if (obj instanceof IClass
                    && isCollectionByNames((IClass) obj, namespaces, collectionNames)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getNamespaceStrings(IClass cls) throws ProjectNotFoundException,
            ClassNotFoundException {
        IModel project = ProjectAccessorFactory.getProjectAccessor().getProject();

        List<String> namespaces = new ArrayList<String>();
        for (IElement owner = cls.getOwner(); owner != project; owner = owner.getOwner()) {
            if (owner instanceof INamedElement) {
                namespaces.add(0, ((INamedElement) owner).getName());
            } else {
                break;
            }
        }
        return namespaces;
    }

    private List<Object> getAncestors(IClass child, List<Object> ancestors) {
        // template binding
        {
            ITemplateBinding[] templateBindings = child.getTemplateBindings();
            for (int i = 0; i < templateBindings.length; ++i) {
                IClass template = templateBindings[i].getTemplate();
                if (template != child && !ancestors.contains(template)) {
                    ancestors.add(template);
                    ancestors = getAncestors(template, ancestors);
                }
            }
        }
        // generalizations
        {
            IGeneralization[] generalizations = child.getGeneralizations();
            for (int i = 0; i < generalizations.length; ++i) {
                IClass superType = generalizations[i].getSuperType();
                if (!ancestors.contains(superType)) {
                    ancestors.add(superType);
                    ancestors = getAncestors(superType, ancestors);
                }
            }
        }
        // realizations
        {
            IDependency[] clientDependencies = child.getClientDependencies();
            for (int i = 0; i < clientDependencies.length; ++i) {
                IDependency dep = clientDependencies[i];
                if (dep instanceof IUsage
                        && Arrays.asList(dep.getStereotypes()).contains("realize")) {
                    INamedElement o = dep.getSupplier();
                    if (o instanceof IClass && o != child && !ancestors.contains(o)) {
                        ancestors.add(o);
                        ancestors = getAncestors((IClass) o, ancestors);
                    }
                }
            }
        }

        return ancestors;
    }

    abstract void dealOperationKeyword(BasicModelEditor basicModelEditor, Set<String> keywords,
            IOperation fun) throws InvalidEditingException;

    abstract void dealAttributeKeywords(BasicModelEditor basicModelEditor, Set<String> keywords,
            IAttribute attr) throws InvalidEditingException;

    abstract FilterKeyword filterKeyword(String type);

    void generateAttri(IElement parent, BasicModelEditor basicModelEditor, IClass type)
            throws InvalidEditingException, ProjectNotFoundException, ClassNotFoundException {
        IAttribute attr = Tool.getAttribute((IClass) parent, name, type);
        if (null == attr) {
            return;
        }
        int[][] range = getMultiRange();
        if (range != null) {
            attr.setMultiplicity(range);
        } else {
            String[][] rangeStrings = getMultiRangeStrings();
            if (rangeStrings != null) {
                attr.setMultiplicityStrings(rangeStrings);
            }
        }
        attr.setChangeable(!"no".equals(this.getConstBoolean()));
        attr.setVisibility(this.getProt());
        attr.setStatic(!"no".equals(this.getStaticBoolean()));
        String definition = getDefinition();
        if (!"".equals(definition)) {
            attr.setDefinition(definition);
        }
        attr.setInitialValue(this.getInitializer());
    }

    IAttribute generateAttri(IElement parent, BasicModelEditor basicModelEditor, String[] path)
            throws InvalidEditingException, ProjectNotFoundException, ClassNotFoundException {
        IAttribute attr = null;
        if (LanguageManager.getCurrentLanguagePrimitiveType().contains(path[0])) {
            attr = Tool.getAttribute((IClass) parent, name, path[0]);
        } else {
            String[] namespace = new String[] {};
            IClass classType = null;
            String type = path.length > 0 ? path[path.length - 1].trim() : getType().trim();
            if ("".equals(type) && !getTypeRefs().isEmpty()) {
                type = getTypeRefs().get(0).value.trim();
            }
            if (path.length > 1) {
                namespace = new String[path.length - 1];
                System.arraycopy(path, 0, namespace, 0, path.length - 1);
                classType = Tool.getClass(namespace, type);
            }
            if (classType != null) {
                attr = Tool.getAttribute((IClass) parent, name, classType);
            } else {
                if (type.indexOf("<") != -1) {
                    attr = createAttrWithAnonimousboundclass(parent, basicModelEditor, namespace,
                            type);
                } else {
                    type = Tool.filterInvalidChar(type);
                }
                if (attr == null) {
                    attr = Tool.getAttribute((IClass) parent, name, type);
                }
            }
        }
        if (null == attr) {
            return null;
        }
        if (!"property".equals(getKind())) {
            int[][] range = getMultiRange();
            if (range != null) {
                attr.setMultiplicity(range);
            } else {
                String[][] rangeStrings = getMultiRangeStrings();
                if (rangeStrings != null) {
                    attr.setMultiplicityStrings(rangeStrings);
                }
            }
        }
        attr.setChangeable(!"no".equals(this.getConstBoolean()));
        attr.setVisibility(this.getProt());
        attr.setStatic(!"no".equals(this.getStaticBoolean()));
        String definition = getDefinition();
        if (!"".equals(definition)) {
            attr.setDefinition(definition);
        }
        attr.setInitialValue(this.getInitializer());

        return attr;
    }

    private IAttribute createAttrWithAnonimousboundclass(IElement parent,
            BasicModelEditor basicModelEditor, String[] namespace, String type)
            throws ProjectNotFoundException, ClassNotFoundException, InvalidEditingException {
        FilterKeyword result = filterKeyword(type);
        Set<String> keywords = result.keywords;
        IClass anonimousClass = Tool.getAnonimousClass(namespace, null, type, getTypeRefs());
        if (anonimousClass != null) {
            IAttribute attr = Tool.getAttribute((IClass) parent, name, anonimousClass);
            dealAttributeKeywords(basicModelEditor, keywords, attr);
            return attr;
        }
        return null;
    }

    IAssociation generateAssoication(IClass parent, BasicModelEditor basicModelEditor,
            IClass assocEnd) throws InvalidEditingException, ProjectNotFoundException,
            ClassNotFoundException {
        IAssociation attr = basicModelEditor.createAssociation(parent, assocEnd, "", "", this.name);
        attr.setVisibility(this.getProt());
        String definition = getDefinition();
        if (!"".equals(definition)) {
            attr.getMemberEnds()[1].setDefinition(definition);
        }
        return attr;
    }

    int[][] getMultiRange() {
        StringBuilder buffer = new StringBuilder();
        int length = 0;
        String arrayString;
        if (type.indexOf("[") >= 0) {
            arrayString = type;
        } else if (this.argsstring.indexOf("[") >= 0) {
            arrayString = this.argsstring;
        } else {
            return null;
        }
        while (arrayString.indexOf("[") >= 0) {
            int beginIndex = arrayString.indexOf("[");
            int endIndex = arrayString.indexOf("]");
            if (endIndex - beginIndex >= 1) {
                buffer.append(arrayString.substring(beginIndex + 1, endIndex));
                buffer.append(" ,");
                length++;
            }
            arrayString = arrayString.substring(endIndex + 1);
        }
        if (length == 0) {
            return null;
        }
        String[] mliti = buffer.toString().split(",");
        int[][] range = new int[length][1];
        for (int i = 0; i < length; i++) {
            int value = -100;
            try {
                value = Integer.valueOf(mliti[i].trim()).intValue();
            } catch (NumberFormatException e) {
                return null;
            }
            range[i][0] = value;
        }
        return range;
    }

    String[][] getMultiRangeStrings() {
        StringBuilder buffer = new StringBuilder();
        int length = 0;
        String arrayString;
        if (type.indexOf("[") >= 0) {
            arrayString = type;
        } else if (this.argsstring.indexOf("[") >= 0) {
            arrayString = this.argsstring;
        } else {
            return null;
        }
        while (arrayString.indexOf("[") >= 0) {
            int beginIndex = arrayString.indexOf("[");
            int endIndex = arrayString.indexOf("]");
            if (endIndex - beginIndex >= 1) {
                buffer.append(arrayString.substring(beginIndex + 1, endIndex));
                buffer.append(" ,");
                length++;
            }
            arrayString = arrayString.substring(endIndex + 1);
        }
        if (length == 0) {
            return null;
        }
        String[] mliti = buffer.toString().split(",");
        String[][] range = new String[length][1];
        for (int i = 0; i < length; i++) {
            range[i][0] = mliti[i].trim();
        }
        return range;
    }

    private String getDefinition() {
        StringBuilder definition = new StringBuilder();
        if (this.getBriefdescriptionPara() != null) {
            definition.append("\\brief ");
            definition.append(getBriefdescriptionPara());
        }
        if (this.getDetaileddescriptionPara() != null) {
            definition.append(this.getDetaileddescriptionPara().trim());
        }
        return definition.toString();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}