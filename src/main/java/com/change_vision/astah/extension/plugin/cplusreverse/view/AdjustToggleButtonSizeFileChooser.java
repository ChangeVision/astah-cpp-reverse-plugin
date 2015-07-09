package com.change_vision.astah.extension.plugin.cplusreverse.view;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

public class AdjustToggleButtonSizeFileChooser extends JFileChooser {

    public AdjustToggleButtonSizeFileChooser() {
        super();
        adjustToggleButtonSize(this);
    }

    public AdjustToggleButtonSizeFileChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
        adjustToggleButtonSize(this);
    }

    public AdjustToggleButtonSizeFileChooser(File currentDirectory) {
        super(currentDirectory);
        adjustToggleButtonSize(this);
    }

    public AdjustToggleButtonSizeFileChooser(FileSystemView fsv) {
        super(fsv);
        adjustToggleButtonSize(this);
    }

    public AdjustToggleButtonSizeFileChooser(File currentDirectory, FileSystemView fsv) {
        super(currentDirectory, fsv);
        adjustToggleButtonSize(this);

    }

    public AdjustToggleButtonSizeFileChooser(String currentDirectoryPath, FileSystemView fsv) {
        super(currentDirectoryPath, fsv);
        adjustToggleButtonSize(this);
    }

    private void adjustToggleButtonSize(Component component) {
        if (!isWindowsLookAndFeel() && !isWindowsClassicLookAndFeel()) {
            return;
        }
        List<JToggleButton> toggleButtons = getToggleButtons(component);
        Dimension maxDimension = getMaxDimension(toggleButtons);
        for (JToggleButton toggleButton : toggleButtons) {
            setSize(toggleButton, maxDimension);
        }
    }

    private List<JToggleButton> getToggleButtons(Component component) {
        if (component instanceof JToggleButton) {
            return Arrays.asList(JToggleButton.class.cast(component));
        }
        ArrayList<JToggleButton> toggleButtons = new ArrayList<JToggleButton>();
        if (!(component instanceof JComponent)) {
            return toggleButtons;
        }
        Component[] components = JComponent.class.cast(component).getComponents();
        for (Component c : components) {
            toggleButtons.addAll(getToggleButtons(c));
        }
        return toggleButtons;
    }

    private Dimension getMaxDimension(List<JToggleButton> toggleButtons) {
        int maxWidth = 0;
        int maxHeight = 0;
        for (JToggleButton toggleButton : toggleButtons) {
            String text = toggleButton.getText();
            JToggleButton newToggleButton = new JToggleButton(text);
            Icon icon = toggleButton.getIcon();
            maxWidth = Math.max(maxWidth, getPreferredWidth(newToggleButton, icon));
            maxHeight = Math.max(maxHeight, getPreferredHeight(newToggleButton, icon));
        }
        return new Dimension(maxWidth, maxHeight);
    }

    private int getPreferredWidth(JToggleButton newToggleButton, Icon icon) {
        int iconWidth = icon.getIconWidth();
        String text = newToggleButton.getText();
        if (text == null || text.isEmpty()) {
            return iconWidth;
        }
        int textWidth = newToggleButton.getPreferredSize().width;
        return Math.max(textWidth, iconWidth);
    }

    private int getPreferredHeight(JToggleButton newToggleButton, Icon icon) {
        int iconHeight = icon.getIconHeight();
        String text = newToggleButton.getText();
        if (text == null || text.isEmpty()) {
            return iconHeight;
        }
        int textHeight = newToggleButton.getPreferredSize().height;
        return textHeight + iconHeight;
    }

    private void setSize(JToggleButton toggleButton, Dimension dimension) {
        toggleButton.setMaximumSize(dimension);
        toggleButton.setMinimumSize(dimension);
        toggleButton.setPreferredSize(dimension);
    }

    private boolean isWindowsClassicLookAndFeel() {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        if (lookAndFeel == null) {
            return false;
        }
        String lookAndFeelName = lookAndFeel.getName();
        return lookAndFeelName.equals("Windows Classic");
    }

    private boolean isWindowsLookAndFeel() {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        if (lookAndFeel == null) {
            return false;
        }
        String lookAndFeelName = lookAndFeel.getName();
        return lookAndFeelName.equals("Windows");
    }
}