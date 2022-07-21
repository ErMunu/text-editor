package TextEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

public class Login extends JPanel implements ActionListener {

    JLabel userLabel = new JLabel("Username: ");
    JTextField userTextField = new JTextField();
    JLabel passwordLabel = new JLabel("Password: ");
    JPasswordField passwordField = new JPasswordField();
    JPanel loginPanel = new JPanel(new GridLayout(3,2));
    JPanel panel = new JPanel();
    JButton signInButton = new JButton("Sign In");
    JButton signUpButton = new JButton("Sign Up");
    CardLayout cardLayout;

    Login(){
        setLayout(new CardLayout());
        loginPanel.add(userLabel);
        loginPanel.add(userTextField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        signInButton.addActionListener(this);
        signUpButton.addActionListener(this);
        loginPanel.add(signInButton);
        loginPanel.add(signUpButton);
        panel.add(loginPanel);
        add(panel, "login");
        cardLayout = (CardLayout) getLayout();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == signInButton){
            try {
                BufferedReader input = new BufferedReader(new FileReader("passwords.txt"));
                String password = null;
                String line = input.readLine();
                while (line != null){
                    StringTokenizer st = new StringTokenizer(line);
                    if(userTextField.getText().equals(st.nextToken())){
                        password = st.nextToken();
                        line = input.readLine();
                    }
                    input.close();

                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(new String(passwordField.getPassword()).getBytes());
                    byte[] byteData = md.digest();
                    StringBuilder sb = new StringBuilder();
                    for (byte byteDatum : byteData) {
                        sb.append(Integer.toString((byteDatum & 0xFF) + 0x100, 16).substring(1));
                    }
                    assert password != null;
                    if(password.equals(sb.toString())){
                        add(new FileBrowser(userTextField.getText()), "fileBrowser");
                        cardLayout.show(this, "fileBrowser");
                    } else {
                        System.out.println("Wrong Username or Password");
                    }
                }
            } catch (IOException | NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource() == signUpButton){
            add(new SignUp(), "signup");
            cardLayout.show(this, "signup");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        Login login = new Login();
        frame.add(login);
        frame.setVisible(true);

    }
}
