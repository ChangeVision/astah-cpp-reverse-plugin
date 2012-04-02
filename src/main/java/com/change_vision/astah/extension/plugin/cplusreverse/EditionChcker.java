package com.change_vision.astah.extension.plugin.cplusreverse;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.cplusreverse.view.AlertEditionDialog;

public class EditionChcker {
	
	private AstahAPIHandler handler = new AstahAPIHandler();
	private static final Set<String> targetEditon = new HashSet<String>();
	static {
		targetEditon.add("professional");
		targetEditon.add("UML");
	}
	private static final Logger logger = LoggerFactory.getLogger(EditionChcker.class);

	public boolean hasError() {
		String edition = handler.getEdition();
		if(targetEditon.contains(edition))
			return false;
		logger.info(edition);
		showEndOfPeriodDialog();
		return true;
	}
	
	private void showEndOfPeriodDialog() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = handler.getMainFrame();
				AlertEditionDialog dialog = new AlertEditionDialog(frame);
				dialog.setModal(true);
				dialog.setVisible(true);				
			}
		});
	}

}
