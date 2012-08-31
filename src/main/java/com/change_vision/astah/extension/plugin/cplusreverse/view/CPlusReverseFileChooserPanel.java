package com.change_vision.astah.extension.plugin.cplusreverse.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.change_vision.astah.extension.plugin.cplusreverse.Messages;
import com.change_vision.astah.extension.plugin.cplusreverse.util.ConfigUtil;

public class CPlusReverseFileChooserPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    static final String NAME = "reverse_panel";
    private XmlFileChooseText doxygenXmlFileText;

    public CPlusReverseFileChooserPanel() {
        setName(NAME);
        setLayout(new GridBagLayout());
        setAlignmentX(Component.LEFT_ALIGNMENT);
        createContents();
        setVisible(true);
    }

    private void createContents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(2, 10, 0, 10);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new XmlFileChooseMessageLabel(), gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new Empty(), gbc);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 1;
        add(new XmlFileChooseTextLabel(), gbc);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 2;
        add(doxygenXmlFileText = new XmlFileChooseText(), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 3;
        add(new doxygenXmlReferenceButton(), gbc);
    }

    class Empty extends JLabel {
        private static final long serialVersionUID = 1L;

        static final String NAME = "empty";

        public Empty() {
            setName(NAME);
        }
    }

    class XmlFileChooseMessageLabel extends JLabel {
        private static final long serialVersionUID = 1L;

        static final String NAME = "reverse_panel.explain_xml_path";

        public XmlFileChooseMessageLabel() {
            setText(Messages.getMessage("reverse_dialog.xml_folder_select_message"));
            setName(NAME);
        }
    }

    class XmlFileChooseTextLabel extends JLabel {
        private static final long serialVersionUID = 1L;

        static final String NAME = "reverse_panel.xml_path_title";

        public XmlFileChooseTextLabel() {
            setName(NAME);
            setText(Messages.getMessage("reverse_dialog.xml_folder_select_title"));
        }
    }

    class XmlFileChooseText extends JTextField {
        private static final long serialVersionUID = 1L;

        private static final int WIDTH = 300;
        private static final int HEIGHT = 14;
        static final String NAME = "reverse_panel.xml_path";

        public XmlFileChooseText() {
            setName(NAME);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setMinimumSize(new Dimension(WIDTH, HEIGHT));
            setText(ConfigUtil.getDefaultCPlusXmlDirectoryPath());
        }
    }

    class doxygenXmlReferenceButton extends JButton {
        private static final long serialVersionUID = 1L;

        static final String NAME = "reverse_panel.reference_button";

        public doxygenXmlReferenceButton() {
            setName(NAME);
            setText(Messages.getMessage("reverse_dialog.reference_button_title"));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setCurrentDirectory(new File(ConfigUtil
                            .getDefaultCPlusXmlDirectoryPath()));
                    int option = chooser.showOpenDialog(CPlusReverseFileChooserPanel.this);
                    if (JFileChooser.APPROVE_OPTION == option) {
                        File file = chooser.getSelectedFile();
                        doxygenXmlFileText.setText(file.getAbsolutePath());
                    }
                }
            });
        }
    }

    public String getXmlFileChooseText() {
        return doxygenXmlFileText.getText();
    }
}
