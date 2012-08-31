package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IPackage;

public class CompoundDefCPlus extends CompoundDef {

    public static final String KIND_STRUCT = "struct";

    public static final String KIND_UNION = "union";

    @Override
    public IElement convertToJudeModel(IElement parent, File[] files)
            throws InvalidEditingException, ClassNotFoundException, ProjectNotFoundException,
            IOException, SAXException {
        if (compounddef.get(this.getCompounddefId()) != null) {
            return parent;
        }
        if (KIND_STRUCT.equals(this.getCompounddefKind())) {
            IClass iclass = this.convertClass(parent, files);
            iclass.addStereotype("struct");
        } else if (KIND_UNION.equals(this.getCompounddefKind())) {
            IClass iclass = this.convertClass(parent, files);
            iclass.addStereotype("union");
        } else {
            super.convertToJudeModel(parent, files);
        }
        return parent;
    }

    @Override
    protected void dealWithGlobalElements(IPackage pkg, File[] files)
            throws ProjectNotFoundException, ClassNotFoundException, InvalidEditingException,
            IOException, SAXException {
        int i = 0;
        String name = "Global";
        IClass globalClass = null;
        int index = 0;
        while (i < 100) {
            globalClass = Tool.getGlobalClass(pkg, name);
            if (globalClass == null && index == 0) {
                index = i - 1;
                break;
            }
            if (!Tool.GlobalList.contains(globalClass)) {
                globalClass = null;
            } else {
                break;
            }
            name = "Global" + "_" + i++;
        }

        if (globalClass == null) {
            String className = "Global";
            if (index >= 0) {
                className += "_" + index;
            }
            globalClass = Tool.setLanguage(ModelEditorFactory.getBasicModelEditor().createClass(
                    pkg, className));
            Tool.GlobalList.add(globalClass);
        }

        Section[] theSections = this.sections.toArray(new Section[this.sections.size()]);
        for (int j = 0; j < theSections.length; ++j) {
            Section next = theSections[j];
            next.convertToJudeModel(globalClass, files);
        }
    }
}