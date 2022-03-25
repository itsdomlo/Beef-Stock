package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractPanel extends JPanel implements ActionListener {

    protected static final String BACK = "back";

    public AbstractPanel() {
        super();
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(bl);
        Border border = this.getBorder();
        this.setBorder(new CompoundBorder(border, FrontGUI.MARGIN));
    }

    protected void addButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(button);
    }

    protected void addStaticLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(label);
    }

    protected void setLeftAndSize(JComponent c) {
        c.setMaximumSize(new Dimension(200, c.getPreferredSize().height));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    protected abstract void resetFieldsAndLabels();

    @Override
    public abstract void actionPerformed(ActionEvent e);

}
