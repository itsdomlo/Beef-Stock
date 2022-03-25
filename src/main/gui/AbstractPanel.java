package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a typical, abstract panel with default setup
public abstract class AbstractPanel extends JPanel implements ActionListener {

    protected static final String BACK = "back";

    // EFFECTS: constructs the panel with default layout
    public AbstractPanel() {
        super();
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(bl);
        Border border = this.getBorder();
        this.setBorder(new CompoundBorder(border, FrontGUI.MARGIN));
    }

    // MODIFIES: this
    // EFFECTS: adds a button with action listener to this
    protected void addButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(button);
    }

    // MODIFIES: this
    // EFFECTS: adds a label to this
    protected void addStaticLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(label);
    }

    // MODIFIES: c
    // EFFECTS: set size and align left for c
    protected void setLeftAndSize(JComponent c) {
        c.setMaximumSize(new Dimension(200, c.getPreferredSize().height));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // EFFECTS: abstract method for resetting relevant components on this panel
    protected abstract void resetFieldsAndLabels();

    // EFFECTS: abstract handler for action performed on this panel
    @Override
    public abstract void actionPerformed(ActionEvent e);

}
