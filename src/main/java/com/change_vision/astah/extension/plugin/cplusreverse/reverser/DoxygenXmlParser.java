package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.change_vision.astah.extension.plugin.cplusreverse.exception.IndexXmlNotFoundException;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

/**
 *	Parse Doxygen's xml to generate jude model 
 *
 */
public class DoxygenXmlParser {
	//for Installer
	private static final String FILE_NAME = ".\\C++.asta";
	
	private static final Logger logger = LoggerFactory.getLogger(DoxygenXmlParser.class);
	
	List compounds = new ArrayList();
	static CompoundDef lastCompoundDef;
	
	public void addCompound(Compound compound) {
		compounds.add(compound);
	}

	public static String parser(String path) throws LicenseNotFoundException, ProjectLockedException, IndexXmlNotFoundException, Throwable {
   	    ProjectAccessor prjAccessor = ProjectAccessorFactory.getProjectAccessor();
	    String iCurrentProject = prjAccessor.getProjectPath();
		
		File indexFile;
		if ((indexFile = findFile(path, "index.xml")) == null) {
            throw new IndexXmlNotFoundException();
		}

		// check template project
//        File file = new File(FILE_NAME);
//        System.out.println(file.getAbsolutePath());
        String astahModelPath = getTempAstahModelPath();
//        boolean isUseTemplate = file.exists();
//        if (isUseTemplate) {
////        	prjAccessor.open(FILE_NAME);
//        	
//        	prjAccessor.open();
//        } else {
//        	prjAccessor.create(astahModelPath, false);
//        }
        InputStream stream = DoxygenXmlParser.class.getResourceAsStream("C++.asta");
		prjAccessor.open(stream);
		logger.info("Importing... please wait..");
		
		//get current jude project's root
		IModel project = ProjectAccessorFactory.getProjectAccessor().getProject();

		DoxygenXmlParser doxygenXmlParser = new DoxygenXmlParser();

		//begin transaction
		TransactionManager.beginTransaction();
		
		//get the availability the concourse
		List compounddefes = doxygenXmlParser.parserIndexXml(indexFile);

		for (Iterator iterator = compounddefes.iterator(); iterator.hasNext();) {
			//first deal namespace
			CompoundDef compounddef = (CompoundDef) iterator.next();
			logger.debug(compounddef.toString());
			if (CompoundDef.KIND_NAMESPACE.equals(compounddef.getCompounddefKind())||CompoundDef.KIND_INTERFACE.equals(compounddef.getCompounddefKind())) {
				// #205 #219
				// (compounddef).convertToJudeModel(project, indexFile.getParentFile().listFiles());
				lastCompoundDef = compounddef;
				compounddef.convertToJudeModel(project, indexFile.getParentFile().listFiles());
				//
			}
		}
		
		for (Iterator iterator = compounddefes.iterator(); iterator.hasNext();) {
			//second deal all the compound
			// #205 #219
			// ((CompoundDef) iterator.next()).convertToJudeModel(project, indexFile.getParentFile().listFiles());
			CompoundDef compounddef = (CompoundDef) iterator.next();
			lastCompoundDef = compounddef;
			compounddef.convertToJudeModel(project, indexFile.getParentFile().listFiles());
			//
		}
		
		for (Iterator iterator = compounddefes.iterator(); iterator.hasNext();) {
			//third convert all children
			// #205 #219
			// ((CompoundDef) iterator.next()).convertChildren(indexFile.getParentFile().listFiles());
			CompoundDef compounddef = (CompoundDef) iterator.next();
			lastCompoundDef = compounddef;
			compounddef.convertChildren(indexFile.getParentFile().listFiles());
			//
		}
		
		CompoundDef.compounddef.clear();
				
		//end transaction
		TransactionManager.endTransaction();
		
		//save the project
//		if (isUseTemplate) {
			prjAccessor.saveAs(astahModelPath);
//		} else {
//			prjAccessor.save();
//		}
        
		prjAccessor.close();

		logger.info("Import Done.");
                        
        return astahModelPath;
	}
	
	public String getErrorLocationFile() {
		if (lastCompoundDef != null) {
			return lastCompoundDef.getLocationFile();
		} else {
			return null;
		}
	}
		
	public int getErrorLocationLine() {
		if (getErrorLocationFile() != null) {
			return lastCompoundDef.getLocationLine();
		} else {
			return -1;
		}
	}

	public String getErrorLocationBodyFile() {
		if (getErrorLocationFile() != null) {
			return lastCompoundDef.getLocationBodyFile();
		} else {
			return null;
		}
	}
	
	public int getErrorLocationBodyStart() {
		if (getErrorLocationFile() != null) {
			return lastCompoundDef.getLocationBodyStart();
		} else {
			return -1;
		}
	}
	
	public int getErrorLocationBodyEnd() {
		if (getErrorLocationFile() != null) {
			return lastCompoundDef.getLocationBodyEnd();
		} else {
			return -1;
		}
	}
	
	
	private static String getTempAstahModelPath() throws IOException {
		Calendar cal = Calendar.getInstance();  
		String year = Integer.toString(cal.get(Calendar.YEAR));        
		String month = Integer.toString(cal.get(Calendar.MONTH) + 1);  
		String day = Integer.toString(cal.get(Calendar.DATE));         
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY)); 
		String minute = Integer.toString(cal.get(Calendar.MINUTE));    
		String second = Integer.toString(cal.get(Calendar.SECOND));    

		String tempFileName = year + month + day + hour + minute + second;
		File tempFile = File.createTempFile(tempFileName, ".asta");   
		tempFile.deleteOnExit();
		return tempFile.getAbsolutePath();   
	}

	/**
	 * 
	 * @param fileName: target indexFile
	 * @return the Set
	 * @throws IOException
	 * @throws SAXException
	 */
	private List parserIndexXml(File indexFile) throws IOException, SAXException
			, ProjectNotFoundException, ClassNotFoundException, InvalidEditingException {
		// init digester for parser index.xml...
		Digester digester = new Digester();
		digester.setValidating(false);
		//create the DoxygenXmlParser
		digester.addObjectCreate("doxygenindex", DoxygenXmlParser.class);
		//create the Compound
		digester.addObjectCreate("doxygenindex/compound", Compound.class);
		digester.addSetProperties("doxygenindex/compound", "refid", "compoundRefid");
		digester.addSetProperties("doxygenindex/compound", "kind", "compoundKind");
		digester.addBeanPropertySetter("doxygenindex/compound/name", "name");

		digester.addSetNext("doxygenindex/compound", "addCompound");
		//init digester for parser  index.xml
		DoxygenXmlParser doxygenXmlParser = (DoxygenXmlParser) digester.parse(indexFile);
		
		for (Iterator iterator = doxygenXmlParser.compounds.iterator(); iterator.hasNext();) {
			Compound compound = (Compound) iterator.next();
			if ("file".equals(compound.getKind())) {
				//setLanguage
				String type = getFileType(compound.getName());
				LanguageManager.setCurrentLanguagePrimitiveType(type);
				
				//get current jude project's root
				IModel project = ProjectAccessorFactory.getProjectAccessor().getProject();
				
				BasicModelEditor basicModelEditor = ModelEditorFactory.getBasicModelEditor();
				//set the project type
				if (LanguageManager.LANGUAGE_CSHARP.equals(type)) {
					basicModelEditor.setLanguageCSharp(project, true);
				} else if (LanguageManager.LANGUAGE_JAVA.equals(type)) {
					basicModelEditor.setLanguageJava(project, true);
				} else if (LanguageManager.LANGUAGE_CPLUS.equals(type)) {
					basicModelEditor.setLanguageCPlus(project, true);
				}
			}
		}
		
		List compounddefs = new ArrayList();
		for (Iterator iterator = doxygenXmlParser.compounds.iterator(); iterator.hasNext();) {
			Compound compound = (Compound) iterator.next();
			//parse the kindName is class or interface or struct or union
			if ("namespace".equals(compound.getKind())
					|| "class".equals(compound.getKind())
					|| "interface".equals(compound.getKind())
					|| "struct".equals(compound.getKind())
					|| "union".equals(compound.getKind())
					|| "file".equals(compound.getKind())) {
				File file = findFile(indexFile.getParent(), compound.getRefid() + ".xml");
				compounddefs.add(parserCompounddefXML(file));
			}
		}
		return compounddefs;
	}
	
	/**
	 * 
	 * @param file: target file
	 * @return the Compounddef
	 * @throws IOException
	 * @throws SAXException
	 */
	public static CompoundDef parserCompounddefXML(File file) throws IOException, SAXException {
		// init digester for parser class.xml or interface.xml...
		Digester digester = new DoxygenDigester();
		digester.setValidating(false);
		//create the Compounddef
		digester.addRule("doxygen/compounddef", new ObjectCreateAfterLanguageRule(CompoundDef.class));
//		digester.addObjectCreate("doxygen/compounddef", Compounddef.class);
		digester.addSetProperties("doxygen/compounddef", "id", "compounddefId");
		digester.addSetProperties("doxygen/compounddef", "prot", "compounddefVisible");
		digester.addSetProperties("doxygen/compounddef", "kind", "compounddefKind");
		digester.addBeanPropertySetter("doxygen/compounddef/compoundname", "compoundName");
		
		//crreate the TempleParam
		digester.addRule("doxygen/compounddef/templateparamlist/param"
				, new ObjectCreateAfterLanguageRule(TempleParam.class));
//		digester.addObjectCreate("doxygen/compounddef/templateparamlist/param", TempleParam.class);
		digester.addBeanPropertySetter("doxygen/compounddef/templateparamlist/param/type", "type");
		digester.addBeanPropertySetter("doxygen/compounddef/templateparamlist/param/declname", "declname");
		digester.addBeanPropertySetter("doxygen/compounddef/templateparamlist/param/defname", "defname");
		digester.addBeanPropertySetter("doxygen/compounddef/templateparamlist/param/defval", "defval");
		digester.addRule("doxygen/compounddef/templateparamlist/param/defval/ref"
				, new ObjectCreateAfterLanguageRule(Ref.class));
		digester.addSetProperties("doxygen/compounddef/templateparamlist/param/defval/ref", "refid", "refid");
		digester.addSetProperties("doxygen/compounddef/templateparamlist/param/defval/ref", "refid", "kindref");
		digester.addBeanPropertySetter("doxygen/compounddef/templateparamlist/param/defval/ref", "value");
		digester.addSetNext("doxygen/compounddef/templateparamlist/param/defval/ref", "setDefaultValue");
		
		digester.addSetNext("doxygen/compounddef/templateparamlist/param", "addTemplateParam");
		
		//create the BaseCompounddefref
		digester.addRule("doxygen/compounddef/basecompoundref"
				, new ObjectCreateAfterLanguageRule(BaseCompoundDefRef.class));
//		digester.addObjectCreate("doxygen/compounddef/basecompoundref", BaseCompounddefref.class);
		digester.addSetProperties("doxygen/compounddef/basecompoundref", "refid", "refid");
		digester.addSetProperties("doxygen/compounddef/basecompoundref", "prot", "prot");
		digester.addSetProperties("doxygen/compounddef/basecompoundref", "virt", "virt");
		digester.addBeanPropertySetter("doxygen/compounddef/basecompoundref", "value");
		digester.addSetNext("doxygen/compounddef/basecompoundref", "addBasecompoundList");
		
		//create the Derivedcompoundref
		digester.addRule("doxygen/compounddef/derivedcompoundref"
				, new ObjectCreateAfterLanguageRule(DerivedCompoundRef.class));
//		digester.addObjectCreate("doxygen/compounddef/derivedcompoundref", Derivedcompoundref.class);
		digester.addSetProperties("doxygen/compounddef/derivedcompoundref", "refid", "refid");
		digester.addSetProperties("doxygen/compounddef/derivedcompoundref", "prot", "prot");
		digester.addSetProperties("doxygen/compounddef/derivedcompoundref", "virt", "virt");
		digester.addBeanPropertySetter("doxygen/compounddef/derivedcompoundref", "value");
		digester.addSetNext("doxygen/compounddef/derivedcompoundref", "addDerivedcompoundList");
		
		//create the InnerClass
		digester.addRule("doxygen/compounddef/innerclass"
				, new ObjectCreateAfterLanguageRule(InnerClass.class));
//		digester.addObjectCreate("doxygen/compounddef/innerclass", InnerClass.class);
		digester.addSetProperties("doxygen/compounddef/innerclass", "refid", "refid");
		digester.addSetProperties("doxygen/compounddef/innerclass", "prot", "prot");
		digester.addSetNext("doxygen/compounddef/innerclass", "addInnerclass");
		
		//create the Innernamespace
		digester.addRule("doxygen/compounddef/innernamespace"
				, new ObjectCreateAfterLanguageRule(InnerNameSpace.class));
//		digester.addObjectCreate("doxygen/compounddef/innernamespace", Innernamespace.class);
		digester.addSetProperties("doxygen/compounddef/innernamespace", "refid", "refid");
		digester.addSetNext("doxygen/compounddef/innernamespace", "addInnernamespace");
		
		digester.addSetProperties("doxygen/compounddef/location", "file", "locationFile");
		// #205 #219
		digester.addSetProperties("doxygen/compounddef/location", "line", "locationLine");
		digester.addSetProperties("doxygen/compounddef/location", "bodyfile", "locationBodyFile");
		digester.addSetProperties("doxygen/compounddef/location", "bodystart", "locationBodyStart");
		digester.addSetProperties("doxygen/compounddef/location", "bodyend", "locationBodyEnd");
		//
		digester.addBeanPropertySetter("doxygen/compounddef/briefdescription/para", "briefdescriptionPara");
		digester.addBeanPropertySetter("doxygen/compounddef/detaileddescription/para", "detaileddescriptionPara");
		
		//create the Section
		digester.addRule("doxygen/compounddef/sectiondef"
				, new ObjectCreateAfterLanguageRule(Section.class));
//		digester.addObjectCreate("doxygen/compounddef/sectiondef", Section.class);
		digester.addSetProperties("doxygen/compounddef/sectiondef", "kind", "kind");
		digester.addSetNext("doxygen/compounddef/sectiondef", "addSection");
		
		//create the Member
//		digester.addObjectCreate("doxygen/compounddef/sectiondef/memberdef", Member.class);
		digester.addRule("doxygen/compounddef/sectiondef/memberdef"
				, new ObjectCreateAfterLanguageRule(Member.class));
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "kind", "kind");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "id", "id");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "prot", "prot");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "const", "constBoolean");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "static", "staticBoolean");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "gettable", "gettable");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "settable", "settable");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "virt", "virt");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "explicit", "explicit");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "inline", "inline");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef", "mutable", "mutable");
		
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/type", "type");
		digester.addRule("doxygen/compounddef/sectiondef/memberdef/type/ref"
				, new ObjectCreateAfterLanguageRule(Ref.class));
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef/type/ref", "refid", "refid");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef/type/ref", "refid", "kindref");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/type/ref", "value");
		digester.addSetNext("doxygen/compounddef/sectiondef/memberdef/type/ref", "addTypeRef");

		//resolve special initializer
		digester.addRule("doxygen/compounddef/sectiondef/memberdef/initializer", new InitBeanPropertySetterRule());

		digester.addRule("doxygen/compounddef/sectiondef/memberdef/enumvalue"
				, new ObjectCreateAfterLanguageRule(EnumValue.class));
//		digester.addObjectCreate("doxygen/compounddef/sectiondef/memberdef/enumvalue", EnumValue.class);
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/enumvalue/name", "name");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/enumvalue/initializer", "initializer");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/enumvalue/briefdescription", "briefdescription");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/enumvalue/detaileddescription", "detaileddescription");
		digester.addSetNext("doxygen/compounddef/sectiondef/memberdef/enumvalue", "addEnum");

		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/name", "name");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/argsstring", "argsstring");
		
		//create the Param
		digester.addRule("doxygen/compounddef/sectiondef/memberdef/param"
				, new ObjectCreateAfterLanguageRule(Param.class));
//		digester.addObjectCreate("doxygen/compounddef/sectiondef/memberdef/param", Param.class);
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/param/type", "type");
		digester.addRule("doxygen/compounddef/sectiondef/memberdef/param/type/ref"
				, new ObjectCreateAfterLanguageRule(Ref.class));
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef/param/type/ref", "refid", "refid");
		digester.addSetProperties("doxygen/compounddef/sectiondef/memberdef/param/type/ref", "refid", "kindref");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/param/type/ref", "value");
		digester.addSetNext("doxygen/compounddef/sectiondef/memberdef/param/type/ref", "addTypeRef");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/param/declname", "declname");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/param/defname", "defname");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/param/array", "array");
		digester.addSetNext("doxygen/compounddef/sectiondef/memberdef/param", "addMemberParam");
				
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/briefdescription/para",
				"briefdescriptionPara");
		digester.addBeanPropertySetter("doxygen/compounddef/sectiondef/memberdef/detaileddescription/para",
				"detaileddescriptionPara");
		digester.addSetNext("doxygen/compounddef/sectiondef/memberdef", "addMember");
		
		return (CompoundDef) digester.parse(file);
	}

	/**
	 * 
	 * @param path: target Path
	 * @param fileName: target file's name
	 * @return the target file
	 * @throws IOException
	 * @throws SAXException
	 */
	public static File findFile(String path, String fileName) throws IOException, SAXException {
		File dir = new File(path);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (fileName.equals(files[i].getName())) {
					return files[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * analyze fileName to judge Language. 
	 * *.cs: LANGUAGE_CSHARP
	 * *.java: LANGUAGE_JAVA
	 * *.cpp: LANGUAGE_CPLUS
	 * *.h: LANGUAGE_CPLUS
	 * *.cc: LANGUAGE_CPLUS
	 * @return language type
	 */

	public static String getFileType(String fileName) {
		String[] fileNames = fileName.split("\\.");

		if ("cs".equals(fileNames[fileNames.length - 1])) {
			return LanguageManager.LANGUAGE_CSHARP;
		}
		if ("java".equals(fileNames[fileNames.length - 1])) {
			return LanguageManager.LANGUAGE_JAVA;
		}
		if ("cpp".equals(fileNames[fileNames.length - 1])) {
			return LanguageManager.LANGUAGE_CPLUS;
		}
		if ("h".equals(fileNames[fileNames.length - 1])) {
			return LanguageManager.LANGUAGE_CPLUS;
		}
		if ("hpp".equals(fileNames[fileNames.length - 1])) {
			return LanguageManager.LANGUAGE_CPLUS;
		}
		if ("cc".equals(fileNames[fileNames.length - 1])) {
			return LanguageManager.LANGUAGE_CPLUS;
		}		
		return null;
	}
}