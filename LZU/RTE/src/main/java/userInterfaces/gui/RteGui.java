package userInterfaces.gui;

import component.Component;
import component.Config;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RteGui extends JFrame {
    private JPanel pDeployableComponents;
    private JPanel pRte;
    private JPanel pDeployedComponentsList;

    private JLabel lRteStatus;
    private JLabel lDeployedComponents;
    private JLabel lActiveComponents;

    private JButton bRteStartStop;

    private IRuntimeEnvironment rte = null;
    private JPanel mainPanel = null;

    public RteGui(IRuntimeEnvironment rte) {
        super("Runtime Environment");
        this.rte = rte;
        //JFrame frame = new JFrame("Runtime Environment");
        this.setSize(1000,600);
        this.setLayout(null);
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setOpaque(true);
        this.setVisible(true);

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

        this.validate();
        this.repaint();
    }

    private void initRTEStatusPanel(){ //infos about RTE state
        pRte = new JPanel();
        pRte.setLayout(null);
        pRte.setBounds(10,10,450,150);
        pRte.setOpaque(true);
        pRte.setBackground(Color.GRAY);

        mainPanel.add(pRte);

        JButton bLoad = new JButton("Load Config");
        ComponentBuilder load = new ComponentBuilder(bLoad)
                .place(10,10,150,20)
                .build(pRte);
        JFrame thisFrame = this;

        JButton bSave = new JButton("Save Config");
        bSave.setEnabled(false); //default
        bSave.addActionListener(e -> {
            String path = rte.saveConfiguration();
            if(path != null)
                JOptionPane.showMessageDialog(null, "Configuration saved under:\n"+path);
            else
                JOptionPane.showMessageDialog(null, "Ups, something went wrong :( ");
        });
        new ComponentBuilder(bSave)
                .placeRight(load)
                .build(pRte);


        bLoad.addActionListener(e -> {
            FileDialog fd = new FileDialog(thisFrame, "Choose a file", FileDialog.LOAD);
            fd.setFile("*.json");
            fd.setDirectory(Config.CONFIG_DIRECTORY);
            fd.setVisible(true);
            try {
                String file= fd.getFile();
                if(file==null)
                    return;
                rte.restoreConfiguration(file);
                bLoad.setEnabled(false);
                bSave.setEnabled(true);
                updateGUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Loading failed.");
                throw new RuntimeException(ex);
            }
        });

        ComponentBuilder rteStatus= new LabelBuilder("RTE Status:").
                placeUnder(load).
                build(pRte);
        ComponentBuilder c = new LabelBuilder("offline").
            placeRight(rteStatus).
                build(pRte);
        lRteStatus = (JLabel) c.getComponent();

        bRteStartStop = (JButton) new ComponentBuilder(new JButton("start")).
                placeRight(c).
                setWidth(100).
                build(pRte).
                getComponent();
        bRteStartStop.addActionListener(e -> {
            if(rte.rteGetState().running){
                rte.rteStop();
                bLoad.setEnabled(true);
                bSave.setEnabled(false);
            }else{
                rte.rteStart();
                bLoad.setEnabled(false);
                bSave.setEnabled(true);
            }
            updateGUI();
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
        ArrayList<String> deployAbleComponents = rte.getDeployableComponents(Config.JAR_DIRECTORY);

        //placeholder
        ComponentBuilder ref= new LabelBuilder("").
                place(10,10,200,20);
        for (String c : deployAbleComponents) {
            ref = new LabelBuilder(c)
                    .placeUnder(ref)
                    .build(pDeployableComponents);

            JButton but = new JButton("Deploy");
            JFrame thisFrame = this;
            but.addActionListener(e -> {
                try {
                    rte.deployComponent(Config.JAR_DIRECTORY,c);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(thisFrame, ex.getMessage());
                    ex.printStackTrace();
                }
                updateGUI();
            });
            new ComponentBuilder(but).placeRight(ref)
                    .setWidth(100)
                    .build(pDeployableComponents);
        }


    }

    private void initDeployedComponentsPanel(){
        JPanel pDeployedComponents = new JPanel(); //maxOut
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
        //pDeployedComponentsList.setBounds(0,0,500,280); //
        pDeployedComponentsList.setPreferredSize(new Dimension(470,260));
        pDeployedComponentsList.setBorder(null);
        pDeployedComponentsList.setLayout(null);
        pDeployedComponentsList.setOpaque(true);
        pDeployedComponentsList.setBackground(Color.LIGHT_GRAY);

        JPanel layoutManagedPanel = new JPanel(); //to be scrollable layoutmanger mustn't be null
        layoutManagedPanel.setOpaque(true);
        layoutManagedPanel.setBackground(Color.LIGHT_GRAY);
        layoutManagedPanel.add(pDeployedComponentsList);


        JScrollPane scrollPane = new JScrollPane(layoutManagedPanel); //ScrollPane
        //scrollPane.setLayout(null);
        scrollPane.setOpaque(true);
        scrollPane.setBounds(0,20, 500, 280);

        pDeployedComponents.add(scrollPane);
    }

    private void updateGUI(){
        if(rte.rteGetState().running){
            bRteStartStop.setText("stop");
            lRteStatus.setText("online");
            lRteStatus.setForeground(Color.GREEN);
        }else{
            bRteStartStop.setText("start");
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

        pDeployedComponentsList.setPreferredSize(new Dimension(pDeployedComponentsList.getWidth(), (ref.height+ref.y+20)));
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
                .setWidth(80)
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
