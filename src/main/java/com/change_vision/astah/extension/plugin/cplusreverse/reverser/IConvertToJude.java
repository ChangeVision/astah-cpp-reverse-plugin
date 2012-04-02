package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IElement;

public interface IConvertToJude {
	
	/**
	 * get the IElement Which Implements IConvertToJude
	 * @param parent, the parent of the current Element
	 * @param files, the files of the xmls
	 * @return the created Element
	 */
	IElement convertToJudeModel(IElement parent, File[] files) throws InvalidEditingException, ClassNotFoundException, ProjectNotFoundException, IOException, SAXException;
}