package userInterfaces.gui;

import javax.swing.*;
import java.awt.*;

public class ComponentBuilder {

    private JComponent component;
    int x=0;
    int y=0;
    int width=0;
    int height=20;

    private int verticalGap =10;
    private int horizontalGap =5;

    public ComponentBuilder(JComponent component){
        this.component = component;
    }

    public ComponentBuilder place(int x, int y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        return this;
    }

    public ComponentBuilder build(JComponent parent){
        component.setBounds(this.x,this.y,this.width,this.height);
        parent.add(component);
        return this;
    }

    public ComponentBuilder placeUnder(ComponentBuilder reference){
        this.x= reference.x;
        this.y= reference.y + reference.height + verticalGap;
        this.width= reference.width;
        this.height=reference.height;
        return this;
    }

    public ComponentBuilder placeRight(ComponentBuilder reference){
        this.x= reference.x+reference.width+horizontalGap;
        this.y= reference.y;
        this.width= reference.width;
        this.height=reference.height;
        return this;
    }

    public ComponentBuilder setWidth(int width){
        this.width = width;
        return this;
    }

    public JComponent getComponent(){
        return component;
    }
}
