package userInterfaces.gui;

import component.Component;
import dtos.ComponentState;
import exceptions.AlreadyRunningException;
import exceptions.ComponentDelegateException;
import exceptions.ComponentNotFoundException;
import exceptions.ComponentUnavailableException;
import rte.IRuntimeEnvironment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class RteGui {
    private JPanel pDeployableComponents;
    private JPanel pRte;
    private JPanel pDeployedComponentsList;

    private JLabel lRteStatus;
    private JLabel lDeployedComponents;
    private JLabel lActiveComponents;

    private JButton brteStartStop;

    private IRuntimeEnvironment rte = null;
    private JPanel mainPanel = null;

    public RteGui(IRuntimeEnvironment rte) {

        this.rte = rte;
        JFrame frame = new JFrame("Runtime Environment");
        frame.setSize(1000,600);
        frame.setLayout(null);
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setOpaque(true);
        frame.setVisible(true);

        initRTEStatusPanel();
        initDeployableComponentsPanel();
        initDeployedComponentsPanel();

        JButton bReload = new JButton("RELOAD");
        bReload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGUI();
            }
        });
        bReload.setBounds(10,500,100,30);
        mainPanel.add(bReload);

        frame.validate();
        frame.repaint();
    }

    private void initRTEStatusPanel(){ //infos about RTE state
        pRte = new JPanel();
        pRte.setLayout(null);
        pRte.setBounds(10,10,450,150);
        pRte.setOpaque(true);
        pRte.setBackground(Color.GRAY);

        mainPanel.add(pRte);

        ComponentBuilder rteStatus= new LabelBuilder("RTE Status:").
                place(10,10,150,20).
                build(pRte);
        ComponentBuilder c = new LabelBuilder("offline").
            placeRight(rteStatus).
                build(pRte);
        lRteStatus = (JLabel) c.getComponent();

        brteStartStop = (JButton) new ComponentBuilder(new JButton("start")).
                placeRight(c).
                setWidth(100).
                build(pRte).
                getComponent();
        brteStartStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rte.rteGetState().running){
                    rte.rteStop();
                }else{
                    rte.rteStart();
                }
                updateGUI();
            }
        });


        ComponentBuilder deployedComponents= new LabelBuilder("Deployed Components:").
                placeUnder(rteStatus).
                build(pRte);
        lDeployedComponents = (JLabel) new LabelBuilder("0").
                placeRight(deployedComponents).
                build(pRte).getComponent();

        ComponentBuilder activeComponents= new LabelBuilder("Active Components:").
                placeUnder(deployedComponents).
                build(pRte);
        lActiveComponents = (JLabel) new LabelBuilder("0").
                placeRight(activeComponents).
                build(pRte).getComponent();
    }

    private void initDeployableComponentsPanel(){
        pDeployableComponents = new JPanel();
        pDeployableComponents.setLayout(null);
        pDeployableComponents.setOpaque(true);
        pDeployableComponents.setBackground(Color.GRAY);
        pDeployableComponents.setBounds(10,160+20,450,300);
        mainPanel.add(pDeployableComponents);

        JLabel headLine= (JLabel)new LabelBuilder("Deployable Components:").
                place(0,0,450,20).
                build(pDeployableComponents).getComponent();
        headLine.setHorizontalAlignment(SwingConstants.CENTER);
        headLine.setFont(new Font("Serif", Font.PLAIN, 14));
        ArrayList<String> deployAbleComponents = rte.getDeployableComponents(Component.JAR_DIRECTORY);

        //placeholder
        ComponentBuilder ref= new LabelBuilder("").
                place(10,10,200,20);
        for (String c : deployAbleComponents) {
            ref = new LabelBuilder(c)
                    .placeUnder(ref)
                    .build(pDeployableComponents);

            JButton but = new JButton("Deploy");
            but.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        rte.deployComponent(Component.JAR_DIRECTORY,c);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    updateGUI();
                }
            });
            new ComponentBuilder(but).placeRight(ref)
                    .setWidth(100)
                    .build(pDeployableComponents);
        }


    }

    private void initDeployedComponentsPanel(){
        JPanel pDeployedComponents = new JPanel();
        pDeployedComponents.setLayout(null);
        pDeployedComponents.setOpaque(true);
        pDeployedComponents.setBackground(Color.GRAY);
        pDeployedComponents.setBounds(460+20,10,500,300);
        mainPanel.add(pDeployedComponents);

        JLabel headLine= (JLabel)new LabelBuilder("Deployed Components:").
                place(0,0,500,20).
                build(pDeployedComponents).getComponent();
        headLine.setHorizontalAlignment(SwingConstants.CENTER);
        headLine.setFont(new Font("Serif", Font.PLAIN, 14));

        pDeployedComponentsList = new JPanel();
        pDeployedComponentsList.setBounds(0,30,500,300-30);
        pDeployedComponentsList.setLayout(null);
        pDeployedComponentsList.setOpaque(true);
        pDeployedComponentsList.setBackground(Color.GRAY);
        pDeployedComponents.add(pDeployedComponentsList);
    }

    private void updateGUI(){
        if(rte.rteGetState().running){
            brteStartStop.setText("stop");
            lRteStatus.setText("online");
            lRteStatus.setForeground(Color.GREEN);
        }else{
            brteStartStop.setText("start");
            lRteStatus.setText("offline");
            lRteStatus.setForeground(Color.BLACK);
        }


        ArrayList<String> cIDs = rte.getComponentsID();
        HashMap<String, ComponentState> allComponents= new HashMap<>();
        int activeComponents=0;

        pDeployedComponentsList.removeAll(); //Clear DeployedComponents list
        ComponentBuilder ref = new LabelBuilder("").place(10,-20,200,20);
        for ( String id : cIDs)
            try {
                ComponentState cs = rte.getComponentState(id);
                ref = addComponentInstanceToList(id,cs,ref);
                if (cs.isRunning)
                    activeComponents++;
                allComponents.put(id, cs);
            } catch (ComponentDelegateException e) {
                e.printStackTrace();
            } catch (ComponentNotFoundException e) {
                e.printStackTrace();
            } catch (ComponentUnavailableException e) {
                e.printStackTrace();
            }

        lDeployedComponents.setText(""+allComponents.size());
        lActiveComponents.setText(""+activeComponents);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private ComponentBuilder addComponentInstanceToList(String componentID, ComponentState state, ComponentBuilder ref){
        ref = new LabelBuilder(componentID + "")
                .placeUnder(ref)
                .build(pDeployedComponentsList);

        ComponentBuilder cb = new LabelBuilder("")
                .placeRight(ref)
                .setWidth(50)
                .build(pDeployedComponentsList);
        if(state.isRunning) {
            ((JLabel) cb.getComponent()).setText("online");
            ((JLabel) cb.getComponent()).setForeground(Color.GREEN);
        }
        else
            ((JLabel)cb.getComponent()).setText("offline");

        JButton startStop = new JButton();
        cb = new ComponentBuilder(startStop)
                .placeRight(cb)
                .setWidth(100)
                .build(pDeployedComponentsList);
        if(state.isRunning)
            startStop.setText("stop");
        else
            startStop.setText("start");

        startStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!state.isRunning)
                            rte.componentStart(componentID);
                    else
                        rte.componentStop(componentID);
                } catch (ComponentNotFoundException componentNotFoundException) {
                    componentNotFoundException.printStackTrace();
                } catch (AlreadyRunningException alreadyRunningException) {
                    alreadyRunningException.printStackTrace();
                } catch (ComponentUnavailableException componentUnavailableException) {
                    componentUnavailableException.printStackTrace();
                } catch (ComponentDelegateException componentDelegateException) {
                    componentDelegateException.printStackTrace();
                }
                updateGUI();
            }
        });

        ((JButton) new ComponentBuilder(new JButton("remove"))
                .placeRight(cb)
                .setWidth(100)
                .build(pDeployedComponentsList)
                .getComponent())
                .addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            rte.removeComponent(componentID);
                            updateGUI();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });


        return ref;
    }
}
