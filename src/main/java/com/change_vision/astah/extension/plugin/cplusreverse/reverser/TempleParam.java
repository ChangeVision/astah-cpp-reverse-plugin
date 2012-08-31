package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
import java.io.File;

import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;

/**
 * this class extend Param
 *  the sub-tag is <defval>.it is named "defval"
 */
public class TempleParam extends Param {
	private String defval;	//for doxygen1.5.8
	private Ref defaultValue;	//for doxygen1.7.0

	public String getDefval() {
		return defval;
	}

	public void setDefval(String defval) {
		this.defval = defval;
	}
	
	public void setDefaultValue(Ref defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public IElement convertToJudeModel(IElement parent, File[] files) throws InvalidEditingException,
			ClassNotFoundException, ProjectNotFoundException {
		String paramName = null;
		BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();
		if (declname != null) {
			paramName = declname;
		}
		if (defname != null) {
			paramName = defname;
		}
		Object defValObj = defval;
		if (defaultValue != null) {
			defValObj = CompoundDef.compounddef.get(defaultValue.getRefid());
		}
		
		String[] result = type.split(" ");
		if (paramName != null) {
			if ("class".equals(result[0])) {
				basicModelEditor.createTemplateParameter(((IClass) parent), paramName, (IClass) null, defValObj);
			} else if (defValObj instanceof IClass) { // #205 #219
				basicModelEditor.createTemplateParameter(((IClass) parent), paramName, type, null);
				                                      //
			} else {
                if (!"".equals(type)) {
                    basicModelEditor.createTemplateParameter(((IClass) parent), paramName, type,
                            defValObj);
                }
			}
		} else {
			if ("class".equals(result[0])) {
				paramName = result[result.length - 1];
				try {
                    basicModelEditor.createTemplateParameter(((IClass) parent), paramName, (IClass) null, defValObj);
                } catch (InvalidEditingException e) {
                    // do nothing
                }
			}
		}
		return parent;
	}
}
