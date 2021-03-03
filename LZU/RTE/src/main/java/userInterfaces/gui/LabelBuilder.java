package userInterfaces.gui;

import javax.swing.*;
import java.awt.*;

public class LabelBuilder extends ComponentBuilder{

    private int verticalGap =10;
    private int horizontalGap =5;

    public LabelBuilder(String text){
        super(new JLabel());
        JLabel label= (JLabel) super.getComponent();
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setText(text);
    }
}
