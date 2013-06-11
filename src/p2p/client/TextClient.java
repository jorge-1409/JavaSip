package p2p.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import p2p.model.MessageProcessor;
import p2p.model.SipLayer;

public class TextClient extends JFrame implements MessageProcessor {

    private SipLayer sipLayer;
    private String serverNotificer;
    private JTextField fromAddress;
    private JLabel fromLbl;
    private JLabel receivedLbl;
    private JTextArea receivedMessages;
    private JScrollPane receivedScrollPane;
    private JButton sendBtn;
    private JLabel sendLbl;
    private JTextField sendMessages;
    private JTextField toAddress;
    private JLabel toLbl;

    public TextClient(SipLayer sip, String ipServer, int portServer) {
        super();
        sipLayer = sip;
        initWindow();
        String from = "sip:" + sip.getUsername() + "@" + sip.getHost() + ":" + sip.getPort();
        this.fromAddress.setText(from);
        this.serverNotificer = "sip:server@" + ipServer + ":" + portServer;

        try {
            this.sipLayer.sendMessage(this.serverNotificer, "connected");
        } catch (Throwable e) {
            e.printStackTrace();
            this.receivedMessages.append("Error notificando al tracker: " + e.getMessage() + "\n");
        }

    }

    private void initWindow() {
        receivedLbl = new JLabel();
        sendLbl = new JLabel();
        sendMessages = new JTextField();
        receivedScrollPane = new JScrollPane();
        receivedMessages = new JTextArea();
        fromLbl = new JLabel();
        fromAddress = new JTextField();
        toLbl = new JLabel();
        toAddress = new JTextField();
        sendBtn = new JButton();

        getContentPane().setLayout(null);

        setTitle("TextClient");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    sipLayer.sendMessage(serverNotificer, "disconnected");
                } catch (Throwable e) {
                    e.printStackTrace();
                    receivedMessages.append("Error notificando al tracker: " + e.getMessage() + "\n");
                }
                System.exit(0);
            }
        });

        receivedLbl.setText("Received Messages:");
        receivedLbl.setAlignmentY(0.0F);
        receivedLbl.setPreferredSize(new java.awt.Dimension(25, 100));
        getContentPane().add(receivedLbl);
        receivedLbl.setBounds(5, 0, 136, 20);

        sendLbl.setText("Send Message:");
        getContentPane().add(sendLbl);
        sendLbl.setBounds(5, 150, 90, 20);

        getContentPane().add(sendMessages);
        sendMessages.setBounds(5, 170, 270, 20);

        receivedMessages.setAlignmentX(0.0F);
        receivedMessages.setEditable(false);
        receivedMessages.setLineWrap(true);
        receivedMessages.setWrapStyleWord(true);
        receivedScrollPane.setViewportView(receivedMessages);
        receivedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().add(receivedScrollPane);
        receivedScrollPane.setBounds(5, 20, 270, 130);

        fromLbl.setText("From:");
        getContentPane().add(fromLbl);
        fromLbl.setBounds(5, 200, 35, 15);

        getContentPane().add(fromAddress);
        fromAddress.setBounds(40, 200, 235, 20);
        fromAddress.setEditable(false);

        toLbl.setText("To:");
        getContentPane().add(toLbl);
        toLbl.setBounds(5, 225, 35, 15);

        getContentPane().add(toAddress);
        toAddress.setBounds(40, 225, 235, 21);

        sendBtn.setText("Send");
        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        getContentPane().add(sendBtn);
        sendBtn.setBounds(200, 255, 75, 25);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 288) / 2, (screenSize.height - 310) / 2, 288, 320);
    }

    private void sendBtnActionPerformed(ActionEvent evt) {

        try {
            String to = this.toAddress.getText();
            String message = this.sendMessages.getText();
            sipLayer.sendMessage(to, message);
        } catch (Throwable e) {
            e.printStackTrace();
            this.receivedMessages.append("ERROR sending message: " + e.getMessage() + "\n");
        }

    }

    @Override
    public void processMessage(String sender, String message) {
        if (message.charAt(0) == '*') {
            JOptionPane.showMessageDialog(null, "");
        }
        this.receivedMessages.append("De " + sender + ": " + message + "\n");
    }

    @Override
    public void processError(String errorMessage) {
        this.receivedMessages.append("ERROR: "+ errorMessage + "\n");
    }

    @Override
    public void processInfo(String infoMessage) {
        this.receivedMessages.append(infoMessage + "\n");
    }
}
