package com.change_vision.astah.extension.plugin.cplusreverse.actions;

import java.io.IOException;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.cplusreverse.Activator;
import com.change_vision.astah.extension.plugin.cplusreverse.Messages;
import com.change_vision.astah.extension.plugin.cplusreverse.view.CPlusReverseFileChooserDialog;
import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import com.change_vision.jude.api.inf.view.IViewManager;

public class CPlusReverseAction implements IPluginActionDelegate {

	private static final Logger logger = LoggerFactory.getLogger(CPlusReverseAction.class);

	public Object run(IWindow window) throws UnExpectedException {
		try {
			ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			if ("no_title".equals(prjAccessor.getProjectPath())) {
				int result = Activator.getMessageHandler().showComfirmMessage(getMainFrame(),
						Messages.getMessage("warning_message.save_before_reverse_warning"));
				if (result == 0) {
					try {
						prjAccessor.save();
					} catch (IOException e) {
						// 保存の取り消し
						return null;
					}
				} else {
					return null;
				}
			}
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (ProjectNotFoundException e) {
			Activator.getMessageHandler().showWarningMessage(getMainFrame(),
					Messages.getMessage("warning_message.open_existing_model_before_reverse_warning"));
			return null;
		} catch (LicenseNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (ProjectLockedException e) {
			logger.error(e.getMessage(), e);
		} catch (Throwable e) {
			Activator.getMessageHandler().showComfirmMessage(getMainFrame(), Messages.getMessage("message.unknown_error"));
			logger.error(e.getMessage(), e);
		}

		JFrame frame = (JFrame) window.getParent();

		CPlusReverseFileChooserDialog dialog = new CPlusReverseFileChooserDialog(frame);
		dialog.setVisible(true);

		return null;
	}

	// TODO AstahAPIHandlerと全く同じ内容の処理？
	private JFrame getMainFrame() {
		JFrame parent = null;
		try {
			ProjectAccessor projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			if (projectAccessor == null)
				return null;
			IViewManager viewManager = projectAccessor.getViewManager();
			if (viewManager == null)
				return null;
			parent = viewManager.getMainFrame();
		} catch (ClassNotFoundException ex) {
			return null;
		} catch (Exception ex) {
			return null;
		}
		return parent;
	}
}
