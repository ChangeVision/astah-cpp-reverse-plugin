package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IOperation;

public class MemberCPlus extends Member {
    public static final String KEYWORD_VIRTUAL = "virtual";
    public static final String KEYWORD_PURE_VIRTUAL = "pure-virtual";

    public static final String KEYWORD_EXPLICIT = "explicit";

    public static final String KEYWORD_INLINE = "inline";

    public static final String KEYWORD_MUTABLE = "mutable";

    public static final String KEYWORD_VOLATILE = "volatile";

    public static final String KEYWORD_FRIEDN = "friend";

    private static int operaNum = 0;

    private static int paramNum = 0;

    @Override
    protected void convertToAttribute(IElement parent, BasicModelEditor basicModelEditor)
            throws InvalidEditingException, ProjectNotFoundException, ClassNotFoundException,
            IOException {
        // deal with case like: int (*pf)(int);
        if (getType() != null && type.indexOf("(*") > 0 && argsstring.indexOf(")(") == 0) {
            // create a attribute named pf : int (*pf)()
            createAttributeForPointer(((IClass) parent), type);
        } else {
            super.convertToAttribute(parent, basicModelEditor);
        }
    }

    @Override
    void dealOperationKeyword(BasicModelEditor basicModelEditor, Set<String> keywords, IOperation fun)
            throws InvalidEditingException {
        if (keywords.contains(CONST)) {
            // fun.setLeaf(true);//until 5.4
            basicModelEditor.createTaggedValue(fun, "jude.c_plus.const", "true");
        }
        if (KEYWORD_VIRTUAL.equals(this.getVirt()) || KEYWORD_PURE_VIRTUAL.equals(this.getVirt())) {
            basicModelEditor.createTaggedValue(fun, "jude.c_plus.virtual", "true");
        }
        if ("yes".equals(this.getExplicit())) {
            basicModelEditor.createTaggedValue(fun, "jude.c_plus.explicit", "true");
        }
        if ("yes".equals(this.getInline())) {
            basicModelEditor.createTaggedValue(fun, "jude.c_plus.inline", "true");
        }
        if (keywords.contains(KEYWORD_FRIEDN)) {
            basicModelEditor.createTaggedValue(fun, "jude.c_plus.friend", "true");
        }
        if (keywords.contains(AND)) {
            fun.setTypeModifier(AND);
        } else if (keywords.contains(STAR + STAR)) {
            fun.setTypeModifier(STAR + STAR);
        } else if (keywords.contains(STAR)) {
            fun.setTypeModifier(STAR);
        }
    }

    @Override
    void dealAttributeKeywords(BasicModelEditor basicModelEditor, Set<String> keywords, IAttribute attr)
            throws InvalidEditingException {
        if (keywords.contains(CONST)) {
            // attr.setChangeable(false);//until 5.4
            basicModelEditor.createTaggedValue(attr, "jude.c_plus.const", "true");
        }
        if (keywords.contains(KEYWORD_VOLATILE)) {
            basicModelEditor.createTaggedValue(attr, "jude.c_plus.volatile", "true");
        }
        if ("yes".equals(this.getMutable())) {
            basicModelEditor.createTaggedValue(attr, "jude.c_plus.mutable", "true");
        }
        if (keywords.contains(AND)) {
            attr.setTypeModifier(AND);
        } else if (keywords.contains(STAR + STAR)) {
            attr.setTypeModifier(STAR + STAR);
        } else if (keywords.contains(STAR)) {
            attr.setTypeModifier(STAR);
        }
    }

    @Override
    FilterKeyword filterKeyword(String type) {
        Set<String> keywords = new HashSet<String>();
        if (type.indexOf("[") != -1) {
            type = type.substring(0, type.indexOf("["));
            keywords.add(KEYWORD_ARRAY);
        }
        if (type.trim().indexOf(CONST) == 0) {
            type = type.replaceFirst(CONST, "").trim();
            keywords.add(CONST);
        }
        if (type.trim().indexOf("::") == 0) {
            type = type.replaceFirst("::", "").trim();
            keywords.add("::");
        }
        if (type.trim().endsWith(AND)) {
            type = type.replaceFirst(AND, "").trim();
            keywords.add(AND);
        }
        if (type.trim().endsWith(STAR + STAR)) {
            type = type.replaceFirst("\\*" + "\\*", "").trim();
            keywords.add(STAR + STAR);
        }

        if (type.trim().endsWith(STAR)) {
            type = type.replaceFirst("\\*", "").trim();
            keywords.add(STAR);
        }
        if (type.indexOf(KEYWORD_VIRTUAL) != -1) {
            type = type.replaceFirst(KEYWORD_VIRTUAL, "").trim();
            keywords.add(KEYWORD_VIRTUAL);
        }
        if (type.indexOf(KEYWORD_PURE_VIRTUAL) != -1) {
            type = type.replaceFirst(KEYWORD_PURE_VIRTUAL, "").trim();
            keywords.add(KEYWORD_PURE_VIRTUAL);
        }
        if (type.indexOf(KEYWORD_VOLATILE) != -1) {
            type = type.replaceFirst(KEYWORD_VOLATILE, "").trim();
            keywords.add(KEYWORD_VOLATILE);
        }
        if (type.indexOf(KEYWORD_FRIEDN) != -1) {
            type = type.replaceFirst(KEYWORD_FRIEDN, "").trim();
            keywords.add(KEYWORD_FRIEDN);
        }
        return new FilterKeyword(keywords, type);
    }

    private void createAttributeForPointer(IClass parentClass, String type)
            throws ProjectNotFoundException, ClassNotFoundException, InvalidEditingException {
        String className = type + argsstring;
        IClass newClass = Tool.getClass(parentClass, className);
        Tool.getAttribute(parentClass, name, newClass);
        // create operation in class like: int func0 ( int arg)
        createOperationForPointer(newClass, type);
    }

    private void createOperationForPointer(IClass parentClass, String type)
            throws ProjectNotFoundException, ClassNotFoundException, InvalidEditingException {
        String returnType = type.substring(0, type.indexOf("("));
        IOperation newOperation = Tool.getOperation(parentClass, "operation" + operaNum++,
                returnType);
        String[] params = getParameters();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].trim().split(" ");
            if ("".equals(param[0])) {
                continue;
            }
            String paramName = param.length == 1 ? "param" + paramNum++ : param[1];
            if (null != newOperation) {
                Tool.getOperationParameter(newOperation, paramName, param[0]);
            }
        }
    }

    private String[] getParameters() {
        argsstring = argsstring.replaceAll("\\(", "");
        argsstring = argsstring.replaceAll("\\)", "");
        return argsstring.split(",");
    }
}