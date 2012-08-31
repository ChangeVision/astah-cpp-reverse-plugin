package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;

/**
 * 
 * this class is the tag of index.xml the tag is <param>. the class is named Param the sub-tag is
 * <type>.it is named "type" the sub-tag is <declname>.it is named "declname" the sub-tag is
 * <defname>.it is named "defname" the sub-tag is <array>.it is named "array" it's have Astah C#'s
 * field ref,out
 */
public class Param implements IConvertToJude {
    private static final Logger logger = LoggerFactory.getLogger(Param.class);

    protected String type;

    protected List<Ref> typeRefs = new ArrayList<Ref>();

    protected String declname;

    protected String defname;

    protected String array;

    protected static int paramNum = 0;

    public static final String REF = "ref";

    public static final String OUT = "out";

    public static final String IN = "in";

    public static final String CONST = "const";

    public static final String PARAMETERS = "params";

    public static final String AND = "&";

    public static final String STAR = "*";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Ref> getTypeRefs() {
        return typeRefs;
    }

    public void addTypeRef(Ref typeRef) {
        typeRefs.add(typeRef);
    }

    public String getDeclname() {
        return declname;
    }

    public void setDeclname(String declname) {
        this.declname = declname;
    }

    public String getArray() {
        return array;
    }

    public void setArray(String array) {
        this.array = array;
    }

    public String getDefname() {
        return defname;
    }

    public void setDefname(String defname) {
        this.defname = defname;
    }

    public IElement convertToJudeModel(IElement parent, File[] files)
            throws InvalidEditingException, ClassNotFoundException, ProjectNotFoundException {
        FilterKeyword result = filterKeyword(type);
        String type = result.toType.trim();
        if ("".equals(type) && !getTypeRefs().isEmpty()) {
            type = getTypeRefs().get(0).value;
        } else if (!getTypeRefs().isEmpty() && 0 <= type.indexOf("<")) {
            type = getTypeRefs().get(0).value + type;
        }
        String filteredType = Member.getTypeFromTypeDef(type).trim();
        String[] typepaths;
        if (filteredType.indexOf("<") != -1) {
            typepaths = new String[1];
            typepaths[0] = filteredType;
        } else {
            typepaths = filteredType.split("::");
        }

        String paramArray = "";
        String paramName = null;

        if (array != null) {
            paramArray = array;
        }
        if (declname != null) {
            paramName = declname;
        }
        if (defname != null) {
            paramName = defname;
        }
        if (paramName == null) {
            paramName = "param" + paramNum++;
        }

        if (typepaths[0].startsWith("\"") && typepaths[typepaths.length - 1].endsWith("\"")) {
            typepaths = new String[1];
            typepaths[0] = filteredType;
        }

        IElement param = createParameter(parent, typepaths, paramArray, paramName);
        if (null != param) {
            dealKeyword(param, result.keywords);
        }
        return param;
    }

    /**
     * 
     * @param param
     *            : IElement
     * @param keywords
     *            : HashSet
     * @return void
     * @throws InvalidEditingException
     */
    void dealKeyword(IElement param, Set<String> keywords) throws InvalidEditingException {
    }

    /**
     * 
     * @param toType
     *            : String
     * @return Object[]
     */
    FilterKeyword filterKeyword(String toType) {
        Set<String> keywords = new HashSet<String>();
        if (toType.indexOf(REF) != -1) {
            toType = toType.replaceFirst(REF + " ", "");
            keywords.add(REF);
        }
        if (type.indexOf(OUT) != -1) {
            toType = toType.replaceFirst(OUT + " ", "");
            keywords.add(OUT);
        }
        if (type.indexOf(IN) != -1) {
            toType = toType.replaceFirst(IN + " ", "");
            keywords.add(IN);
        }
        if (type.indexOf(PARAMETERS) != -1) {
            toType = toType.replaceFirst(PARAMETERS + " ", "");
            keywords.add(PARAMETERS);
        }
        if (type.indexOf(CONST) != -1) {
            toType = toType.replaceFirst(CONST + " ", "");
            keywords.add(CONST);
        }
        if (type.trim().indexOf("::") == 0 || toType.trim().indexOf("::") == 0) {
            toType = toType.replaceFirst("::", "");
            keywords.add("::");
        }
        Pattern p = Pattern.compile(String.format("[\\%s|%s]*$", STAR, AND));
        Matcher m = p.matcher(type);
        if (m.find()) {
            toType = toType.replace(m.group(), "").trim();
            keywords.add(m.group());
        }
        return new FilterKeyword(keywords, toType);
    }

    /**
     * 
     * @param parent
     *            : IElement
     * @param names
     *            : String[]
     * @param paramArray
     *            : String
     * @param paramName
     *            : String
     * @return IElement
     * @throws InvalidEditingException
     * @throws ClassNotFoundException
     * @throws ProjectNotFoundException
     */
    protected IElement createParameter(IElement parent, String[] names, String paramArray,
            String paramName) throws InvalidEditingException, ProjectNotFoundException,
            ClassNotFoundException {
        BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();
        if (LanguageManager.getCurrentLanguagePrimitiveType().contains(names[names.length - 1])) {
            return basicModelEditor.createParameter(((IOperation) parent), paramName,
                    names[names.length - 1] + paramArray);
        } else {
            IParameter parameter = null;
            String type = names.length > 0 ? names[names.length - 1].trim() : getType().trim();
            String[] namespace = new String[] {};
            IClass classType = null;
            if (names.length > 1) {
                namespace = new String[names.length - 1];
                System.arraycopy(names, 0, namespace, 0, names.length - 1);
                classType = Tool.getClass(namespace, type);
            }
            if (classType != null) {
                parameter = basicModelEditor.createParameter((IOperation) parent, paramName,
                        classType);
            } else {
                if (type.indexOf("<") != -1) {
                    parameter = createParamWithAnonimousboundclass(parent, namespace,
                            basicModelEditor, paramName, type);
                } else {
                    type = Tool.filterInvalidChar(type);
                }
                if (parameter == null) {
                    try {
                        parameter = basicModelEditor.createParameter((IOperation) parent,
                                paramName, type + paramArray);
                    } catch (InvalidEditingException e) {
                        // TODO 重複エラー回避
                        if ("An element with the same name already exists.".equals(e.getMessage())) {
                            INamedElement[] classes = null;
                            if (parent.getOwner().getOwner() instanceof IClass) {
                                IClass ownerClass = (IClass) parent.getOwner().getOwner();
                                classes = ownerClass.getNestedClasses();
                            } else if (parent.getOwner().getOwner() instanceof IModel) {
                                IModel model = (IModel) parent.getOwner().getOwner();
                                classes = model.getOwnedElements();
                            }
                            if (null != classes) {
                                List<INamedElement> deleteClasses = new ArrayList<INamedElement>();
                                for (int i = classes.length - 1; i >= 0; --i) {
                                    INamedElement clazz = classes[i];
                                    if (type.equals(clazz.getName())) {
                                        logger.trace(String.format("%s", clazz.toString()));
                                        deleteClasses.add(clazz);
                                    }
                                }
                                Iterator<INamedElement> it = deleteClasses.iterator();
                                while (it.hasNext() && 1 < deleteClasses.size()) {
                                    ModelEditorFactory.getBasicModelEditor().delete(it.next());
                                    it.remove();
                                }
                            }
                        } else {
                            throw e;
                        }
                    }
                }
            }
            return parameter;
        }
    }

    private IParameter createParamWithAnonimousboundclass(IElement parent, String[] allPath,
            BasicModelEditor basicModelEditor, String paramName, String type)
            throws ProjectNotFoundException, ClassNotFoundException, InvalidEditingException {
        FilterKeyword result = filterKeyword(type);
        Set<String> keywords = result.keywords;
        IClass anonimousClass = Tool.getAnonimousClass(allPath, null, type, getTypeRefs());
        if (anonimousClass != null) {
            IParameter param = basicModelEditor.createParameter((IOperation) parent, paramName,
                    anonimousClass);
            dealKeyword(param, keywords);
            return param;
        }
        return null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}