package com.change_vision.astah.extension.plugin.cplusreverse.reverser;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;

/**
 * 
 * this class is the tag of index.xml
 *  the tag is <param>. the class is named Param
 *  the sub-tag is <type>.it is named "type"
 *  the sub-tag is <declname>.it is named "declname"
 *  the sub-tag is <defname>.it is named "defname"
 *  the sub-tag is <array>.it is named "array"
 *  it's have Astah C#'s field
 *  ref,out
 */
public class Param implements IConvertToJude {
	
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
		throws InvalidEditingException,	ClassNotFoundException,	ProjectNotFoundException {
		Object[] result = filterKeyword(type);
		String type = ((String) result[1]).trim();
		if ("".equals(type) && !getTypeRefs().isEmpty()) {
			type = getTypeRefs().get(0).value;
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
		dealKeyword(param, (HashSet) result[0]);
		return param;
	}

	/**
	 * 
	 * @param param: IElement
	 * @param keywords : HashSet
	 * @return void
	 * @throws InvalidEditingException
	 */
	void dealKeyword(IElement param, HashSet keywords) throws InvalidEditingException {}

	/**
	 * 
	 * @param toType: String
	 * @return Object[]
	 */
	Object[] filterKeyword(String toType) {
		Set keywords = new HashSet();
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
		if (type.endsWith(AND)) {
			toType = toType.replaceFirst(AND, "").trim();
			keywords.add(AND);
		}
		if (type.trim().endsWith(STAR + STAR)) {
			toType = toType.replaceFirst("\\*" + "\\*", "").trim();
			keywords.add(STAR + STAR);
		}
		
		if (type.trim().endsWith(STAR)) {
			toType = toType.replaceFirst("\\*", "").trim();
			keywords.add(STAR);
		}
		return new Object[] {keywords, toType};
	}
	
	/**
	 * 
	 * @param parent: IElement
	 * @param names : String[]
	 * @param paramArray : String
	 * @param paramName : String
	 * @return IElement
	 * @throws InvalidEditingException
	 * @throws ClassNotFoundException
	 * @throws ProjectNotFoundException
	 */
	protected IElement createParameter(IElement parent, String[] names, String paramArray, String paramName)
		throws InvalidEditingException, ProjectNotFoundException, ClassNotFoundException {
		BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();
		if (LanguageManager.getCurrentLanguagePrimitiveType().contains(names[names.length - 1])) {
			return basicModelEditor.createParameter(((IOperation) parent), paramName
					, names[names.length - 1] + paramArray);
		} else {
			IParameter parameter = null;
			String type = names.length > 0 ? names[names.length -1].trim() : getType().trim();
			
			String[] namespace = new String[] {};
			IClass classType = null;
			if (names.length > 1) {
				namespace = new String[names.length - 1];
				System.arraycopy(names, 0, namespace, 0, names.length - 1);
				classType = Tool.getClass(namespace, type);
			}
			if (classType != null) {
				parameter = basicModelEditor.createParameter((IOperation) parent, paramName, classType);
			} else {
				if (type.indexOf("<") != -1) {				
					parameter = createParamWithAnonimousboundclass(parent, namespace, basicModelEditor, paramName, type);
				} else {
					type = Tool.filterInvalidChar(type);
				}
				if (parameter == null) {				
					parameter = basicModelEditor.createParameter((IOperation) parent, paramName, type + paramArray);
				}
			}
			return parameter;
		}
	}
	
	private IParameter createParamWithAnonimousboundclass(
			IElement parent, String[] allPath, BasicModelEditor basicModelEditor, String paramName, String type) 
			throws ProjectNotFoundException,ClassNotFoundException, InvalidEditingException {
		Object[] result = filterKeyword(type);
		HashSet keywords = (HashSet) result[0];
		IClass anonimousClass = Tool.getAnonimousClass(allPath, null, type, getTypeRefs());
		if (anonimousClass != null) {
			IParameter param = basicModelEditor.createParameter((IOperation) parent, paramName, anonimousClass);
			dealKeyword(param, keywords);
			return param;
		}
		return null;
	}
}