import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class CommandLineInterface extends JFrame {
    private static CommandLineInterface instance = null;

    private ArrayList<ICommandLineInterpreter> subscriber = new ArrayList<>();

    private JPanel mainPanel;
    private JTextArea textArea;
    private JTextField inputText;
    private JScrollPane scrollPane;

    private CommandLineInterface(){
        super("view.CommandLineInterface");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(630,330);
        mainPanel = new JPanel();
        mainPanel.setLayout(null); //absolut positioning

        textArea = new JTextArea ("...");
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setSize(600,250);
        scrollPane.setLocation(10,10);
        mainPanel.add(scrollPane);

        inputText = new JTextField();
        inputText.setSize(600,30);
        inputText.setLocation(10,260);
        inputText.setBackground(Color.BLACK);
        inputText.setForeground(Color.WHITE);
        inputText.setText("click to write...");
        inputText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                inputText.setText("");
            }
        } );
        inputText.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for( ICommandLineInterpreter s : subscriber ) {
                    s.handleCommand(inputText.getText());
                }
                print(inputText.getText());
                inputText.setText("");
            }
        });
        mainPanel.add(inputText);

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setResizable(false);
    }

    public void print(String msg){
        //scroll to the end
        textArea.append("\n"+msg);
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    public void close(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        this.dispose();
    }

    public void subscribe(ICommandLineInterpreter cli) {
        subscriber.add(cli);
    }

    public void unsubscribe(ICommandLineInterpreter cli) {
        subscriber.remove(cli);
    }

    public static CommandLineInterface getInstance() {
        if( instance==null)
            instance = new CommandLineInterface();
        return instance;
    }

}
