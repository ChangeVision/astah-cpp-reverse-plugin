package com.change_vision.astah.extension.plugin.cplusreverse;

import javax.swing.JFrame;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class AstahAPIHandler {

	public JFrame getMainFrame() {
		try {
			return getProjectAccessor().getViewManager().getMainFrame();
		} catch (Exception e) {
			return null;
		}
	}

	public String getEdition() {
		return getProjectAccessor().getAstahEdition();
	}

	private ProjectAccessor getProjectAccessor() {
		ProjectAccessor projectAccessor = null;
		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		if (projectAccessor == null)
			throw new IllegalStateException("projectAccessor is null.");
		return projectAccessor;
	}
}
