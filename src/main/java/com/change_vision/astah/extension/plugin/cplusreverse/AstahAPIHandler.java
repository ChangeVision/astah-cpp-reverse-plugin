package com.change_vision.astah.extension.plugin.cplusreverse;

import javax.swing.JFrame;

import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public class AstahAPIHandler {

	public JFrame getMainFrame() {
		return getProjectAccessor().getViewManager().getMainFrame();
	}

	public String getEdition() {
		return getProjectAccessor().getAstahEdition();
	}

	private ProjectAccessor getProjectAccessor() {
		ProjectAccessor projectAccessor = null;
		try {
			projectAccessor = ProjectAccessorFactory.getProjectAccessor();
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		if (projectAccessor == null)
			throw new IllegalStateException("projectAccessor is null.");
		return projectAccessor;
	}
}
