package com.change_vision.astah.extension.plugin.cplusreverse.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.common.AstahModelUtil;
import com.change_vision.astah.extension.plugin.cplusreverse.Activator;
import com.change_vision.astah.extension.plugin.cplusreverse.Messages;
import com.change_vision.astah.extension.plugin.cplusreverse.util.ConfigUtil;
import com.change_vision.astah.extension.plugin.exception.IndexXmlNotFoundException;
import com.change_vision.astah.extension.plugin.exception.XmlParsingException;
import com.change_vision.astah.extension.plugin.reverser.Creator;
import com.change_vision.astah.extension.plugin.reverser.DoxygenXmlParser;
import com.change_vision.astah.extension.plugin.reverser.TypeUtil;
import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IMessageDialogHandler;
import com.change_vision.jude.api.inf.view.IViewManager;

public class CPlusReverseFileChooserDialog extends JDialog implements ProjectEventListener {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CPlusReverseFileChooserDialog.class);

	private static final String NAME = "cplus_reverse";
	private static final String ENTER = "\n";
	private static int WIDTH = 520;
	private static int HEIGHT = 120;
	private IMessageDialogHandler util = Activator.getMessageHandler();

	private JButton reverseButton;
	private CPlusReverseFileChooserPanel fileChooserPanel;
	private String resultTempModel = null;
	private ProjectAccessor projectAccessor;

	public CPlusReverseFileChooserDialog(JFrame window) {
		super(window, true);
		setName(NAME);
		setTitle(Messages.getMessage("reverse_dialog.title"));
		createContents();
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(window);

		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void createContents() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		createMainContent();
		createSouthContent();
		getRootPane().setDefaultButton(reverseButton);
	}

	private void createSouthContent() {
		JPanel sourthContentPanel = new JPanel(new BorderLayout());

		JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        helpPanel.add(createHelpButton());
		sourthContentPanel.add(helpPanel, BorderLayout.WEST);

		JPanel reversePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		reverseButton = new ReverseButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parseXMLandEasyMerge();
			}
		});
		reversePanel.add(reverseButton);
		reversePanel.add(new CancelButton(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		}));

		sourthContentPanel.add(reversePanel, BorderLayout.EAST);

		getContentPane().add(sourthContentPanel);
	}

	private void parseXMLandEasyMerge() {
		try {
			String doxygenXml = fileChooserPanel.getXmlFileChooseText();
			if (null != doxygenXml) {
				doxygenXml = doxygenXml.trim();
			}
			if (!("".equals(doxygenXml))) {
				String iCurrentProject = projectAccessor.getProjectPath();
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				DoxygenXmlParser doxygenXmlParser = new DoxygenXmlParser();
				doxygenXmlParser.setProjectAccessor(projectAccessor);

				Creator creator = createCreator(doxygenXml);
				doxygenXmlParser.setCreator(creator);
				// begin transaction
				doxygenXmlParser.initProject();
				TransactionManager.beginTransaction();
				doxygenXmlParser.parser(doxygenXml);
				// end transaction
				TransactionManager.endTransaction();
				resultTempModel = doxygenXmlParser.saveProject();

				setVisible(false);
				ConfigUtil.saveCPlusXmlPath(doxygenXml);
				projectAccessor.addProjectEventListener(this);
				projectAccessor.open(iCurrentProject);
			} else {
				util.showWarningMessage(getMainFrame(), Messages.getMessage("reverse_dialog.xml_folder_input_message"));
			}
		} catch (LicenseNotFoundException e1) {
			logger.error(e1.getMessage(), e1);
		} catch (ProjectLockedException e1) {
			logger.error(e1.getMessage(), e1);
		} catch (IndexXmlNotFoundException e1) {
			util.showWarningMessage(getMainFrame(), Messages.getMessage("reverse_dialog.xml_folder_input_message"));
			logger.error(e1.getMessage(), e1);
		} catch (UTFDataFormatException e1) {
			String messageStr = Messages.getMessage("doxygen_utf_exception_detail.error_message");
			logger.error(messageStr);
			logger.error(e1.getMessage(), e1);
			util.showWarningMessage(getMainFrame(), messageStr);
		} catch (XmlParsingException e1) {
            String message = Messages.getMessage("doxygen_encoding_exception.error_message",
                    e1.getMessage());
            Object[] options = new Object[] { createDetailButton(),
                    Messages.getMessage("error.message.close.button.label") };
            JOptionPane.showOptionDialog(getMainFrame(), message, "Warning",
                    JOptionPane.WARNING_MESSAGE, JOptionPane.WARNING_MESSAGE, null, options, null);
			logger.error(e1.getMessage(), e1);
		} catch (Throwable e1) {
			String messageStr = Messages.getMessage("doxygen_parse_exception_detail.error_message");
			logger.error(messageStr);
			logger.error(e1.getMessage(), e1);
            JOptionPane.showOptionDialog(getMainFrame(), getMessageString(messageStr, e1.getMessage()), "Warning",
                    JOptionPane.WARNING_MESSAGE, JOptionPane.WARNING_MESSAGE, null, getOptions(), null);
		} finally {
			if (TransactionManager.isInTransaction()) {
				TransactionManager.abortTransaction();
			}
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

    protected Creator createCreator(String doxygenXml) throws InvalidEditingException {
		Creator creator = new Creator();
		creator.setProjectAccessor(projectAccessor);
		creator.setBasicModelEditor(projectAccessor.getModelEditorFactory().getBasicModelEditor());
		AstahModelUtil astahModelUtil = new AstahModelUtil();
		creator.setAstahModelUtil(astahModelUtil);
		TypeUtil typeUtil = new TypeUtil();
		typeUtil.setXmlDir(new File(doxygenXml));
		typeUtil.setAstahModelUtil(astahModelUtil);
		creator.setTypeUtil(typeUtil);
		return creator;
	}

	// TODO AstahAPIHandlerと全く同じ内容の処理？
	private JFrame getMainFrame() {
		JFrame parent = null;
		try {
			if (projectAccessor == null)
				return null;
			IViewManager viewManager = projectAccessor.getViewManager();
			if (viewManager == null)
				return null;
			parent = viewManager.getMainFrame();
		} catch (Exception e) {
			return null;
		}
		return parent;
	}

	private void createMainContent() {
		JPanel mainContentPanel = new JPanel();
		mainContentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		fileChooserPanel = new CPlusReverseFileChooserPanel();
		mainContentPanel.add(fileChooserPanel, gbc);
		getContentPane().add(mainContentPanel);
	}

	class HelpButton extends JButton {
		private static final long serialVersionUID = 1L;

		static final String NAME = "export_dialog.help";

		private HelpButton(ActionListener listener) {
			setName(NAME);
			setText(Messages.getMessage("reverse_dialog.help_button.title"));
			addActionListener(listener);
		}
	}

	class ReverseButton extends JButton {
		private static final long serialVersionUID = 1L;

		static final String NAME = "export_dialog.generate";

		private ReverseButton(ActionListener listener) {
			setName(NAME);
			setText(Messages.getMessage("reverse_dialog.reverse_button.title"));
			addActionListener(listener);
		}
	}

	class CancelButton extends JButton {
		private static final long serialVersionUID = 1L;

		static final String NAME = "export_dialog.cancel";

		private CancelButton(ActionListener listener) {
			setName(NAME);
			setText(Messages.getMessage("reverse_dialog.cancel_button.title"));
			addActionListener(listener);
		}
	}

	@Override
	public void projectChanged(ProjectEvent arg0) {
	}

	@Override
	public void projectClosed(ProjectEvent arg0) {
	}

	@Override
	public void projectOpened(ProjectEvent arg0) {
		try {
			if (resultTempModel != null) {
				projectAccessor.easyMerge(resultTempModel, true);
			}
			resultTempModel = null;
		} catch (ProjectNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidEditingException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (projectAccessor != null) {
				projectAccessor.removeProjectEventListener(this);
			}
		}
	}
	
    private String getMessageString(String preposition, String postposition) {
        StringBuilder sb = new StringBuilder();
        sb.append(preposition);
        sb.append(ENTER);
        sb.append(postposition);
        return sb.toString();
    }
    
    private Object[] getOptions() {
        HelpButton button = createHelpButton();
        Object obj = UIManager.get("OptionPane.buttonFont", getLocale());
        if (obj instanceof Font) {
            Font font = (Font) obj;
            button.setFont(font);
        }
        return new Object[] { Messages.getMessage("error.message.ok.button.label"), button };
    }

    private HelpButton createHelpButton() {
        return new HelpButton(createActionListener("help.url"));
    }

    private JButton createButton(String name, String Text, ActionListener listener) {
        JButton button = new JButton();
        button.setName(name);
        button.setText(Messages.getMessage(Text));
        button.addActionListener(listener);
        Object obj = UIManager.get("OptionPane.buttonFont", getLocale());
        if (obj instanceof Font) {
            Font font = (Font) obj;
            button.setFont(font);
        }
        return button;
    }

    private JButton createDetailButton() {
        return createButton("doxygen_encoding_exception_error.help",
                "doxygen_encoding_exception_error_help.button.label",
                createActionListener("doxygen_encoding_exception_error_help.url"));
    }

    private ActionListener createActionListener(final String urlKeyString) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    URL url = new URL(Messages.getMessage(urlKeyString));
                    desktop.browse(url.toURI());
                } catch (MalformedURLException e1) {
                    logger.error(e1.getMessage(), e1);
                } catch (IOException e1) {
                    logger.error(e1.getMessage(), e1);
                } catch (URISyntaxException e1) {
                    logger.error(e1.getMessage(), e1);
                }
            }
        };
    }
}
