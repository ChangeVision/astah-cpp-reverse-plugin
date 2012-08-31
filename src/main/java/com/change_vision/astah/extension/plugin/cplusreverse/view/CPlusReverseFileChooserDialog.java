package com.change_vision.astah.extension.plugin.cplusreverse.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.cplusreverse.Activator;
import com.change_vision.astah.extension.plugin.cplusreverse.Messages;
import com.change_vision.astah.extension.plugin.cplusreverse.exception.IndexXmlNotFoundException;
import com.change_vision.astah.extension.plugin.cplusreverse.reverser.DoxygenXmlParser;
import com.change_vision.astah.extension.plugin.cplusreverse.util.ConfigUtil;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IMessageDialogHandler;
import com.change_vision.jude.api.inf.view.IViewManager;

public class CPlusReverseFileChooserDialog extends JDialog implements ProjectEventListener {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory
            .getLogger(CPlusReverseFileChooserDialog.class);

    private static final String NAME = "cplus_reverse";
    private static int WIDTH = 520;
    private static int HEIGHT = 120;
    private IMessageDialogHandler util = Activator.getMessageHandler();

    private JButton reverseButton;
    private CPlusReverseFileChooserPanel fileChooserPanel;
    private String resultTempModel = null;

    public CPlusReverseFileChooserDialog(JFrame window) {
        super(window, true);
        setName(NAME);
        setTitle(Messages.getMessage("reverse_dialog.title"));
        createContents();
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(window);
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
        helpPanel.add(new HelpButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    URL url = new URL(Messages.getMessage("help.url"));
                    desktop.browse(url.toURI());
                } catch (MalformedURLException e1) {
                    logger.error(e1.getMessage(), e1);
                } catch (IOException e1) {
                    logger.error(e1.getMessage(), e1);
                } catch (URISyntaxException e1) {
                    logger.error(e1.getMessage(), e1);
                }
            }
        }));
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
        DoxygenXmlParser dxp = new DoxygenXmlParser();
        try {
            // String doxygenXml = "C:/doxygen-test/1.5.8_xml";
            // String astahModelName = "C:/doxygen-test/astah_model/test.asta";

            String doxygenXml = fileChooserPanel.getXmlFileChooseText();
            if (!("".equals(doxygenXml.trim()))) {
                ProjectAccessor prjAccessor = ProjectAccessorFactory.getProjectAccessor();
                String iCurrentProject = prjAccessor.getProjectPath();
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                resultTempModel = DoxygenXmlParser.parser(doxygenXml);
                setVisible(false);

                ConfigUtil.saveCPlusXmlPath(doxygenXml);
                prjAccessor.addProjectEventListener(this);
                prjAccessor.open(iCurrentProject);

                // openが終わってから、projectOpened()によってeasyMergeが実行される

                // debug
                // throw new IOException();
                //

            } else {
                util.showWarningMessage(getMainFrame(),
                        Messages.getMessage("reverse_dialog.xml_folder_input_message"));
            }
        } catch (LicenseNotFoundException e1) {
            logger.error(e1.getMessage(), e1);
        } catch (ProjectLockedException e1) {
            logger.error(e1.getMessage(), e1);
        } catch (IndexXmlNotFoundException e1) {
            util.showWarningMessage(getMainFrame(),
                    Messages.getMessage("reverse_dialog.xml_folder_input_message"));
            logger.error(e1.getMessage(), e1);
        } catch (UTFDataFormatException e1) {
            String messageStr = "";
            String errorLocationFile = dxp.getErrorLocationFile();
            if (errorLocationFile != null) {
                int errorLocationLine = dxp.getErrorLocationLine();
                String errorLocationBodyFile = dxp.getErrorLocationBodyFile();
                int errorLocationBodyStart = dxp.getErrorLocationBodyStart();
                int errorLocationBodyEnd = dxp.getErrorLocationBodyEnd();
                String line_separator = System.getProperty("line.separator");
                messageStr = Messages.getMessage("doxygen_utf_exception_detail.error_message")
                        + line_separator + "File:" + errorLocationFile + " Line:"
                        + errorLocationLine + line_separator + "Body File:" + errorLocationBodyFile
                        + " Start:" + errorLocationBodyStart + " End:" + errorLocationBodyEnd;
            } else {
                messageStr = Messages.getMessage("doxygen_utf_exception.error_message");
            }
            //
            logger.error(messageStr);
            logger.error(e1.getMessage(), e1);
            util.showWarningMessage(getMainFrame(), messageStr);
        } catch (Throwable e1) {
            String messageStr = "";
            String errorLocationFile = dxp.getErrorLocationFile();
            if (errorLocationFile != null) {
                int errorLocationLine = dxp.getErrorLocationLine();
                String errorLocationBodyFile = dxp.getErrorLocationBodyFile();
                int errorLocationBodyStart = dxp.getErrorLocationBodyStart();
                int errorLocationBodyEnd = dxp.getErrorLocationBodyEnd();
                String line_separator = System.getProperty("line.separator");
                messageStr = Messages.getMessage("doxygen_parse_exception_detail.error_message")
                        + line_separator + "File:" + errorLocationFile + " Line:"
                        + errorLocationLine + line_separator + "Body File:" + errorLocationBodyFile
                        + " Start:" + errorLocationBodyStart + " End:" + errorLocationBodyEnd;
            } else {
                messageStr = Messages.getMessage("doxygen_parse_exception.error_message");
            }
            logger.error(messageStr);
            logger.error(e1.getMessage(), e1);
            util.showWarningMessage(getMainFrame(), messageStr);
        } finally {
            if (TransactionManager.isInTransaction()) {
                TransactionManager.abortTransaction();
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    // TODO AstahAPIHandlerと全く同じ内容の処理？
    private JFrame getMainFrame() {
        JFrame parent = null;
        try {
            ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
            if (projectAccessor == null)
                return null;
            IViewManager viewManager = projectAccessor.getViewManager();
            if (viewManager == null)
                return null;
            parent = viewManager.getMainFrame();
        } catch (ClassNotFoundException ex) {
            return null;
        } catch (InvalidUsingException e) {
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
        ProjectAccessor prjAccessor = null;
        try {
            prjAccessor = ProjectAccessorFactory.getProjectAccessor();
            if (resultTempModel != null) {
                prjAccessor.easyMerge(resultTempModel, true);
            }
            resultTempModel = null;
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (ProjectNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidEditingException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (prjAccessor != null) {
                prjAccessor.removeProjectEventListener(this);
            }
        }
    }
}
