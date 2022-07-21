package TextEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

public class SignUp extends JPanel implements ActionListener {
    JLabel userLabel = new JLabel("Username: ");
    JTextField userTextField = new JTextField();
    JLabel passwordLabel = new JLabel("Password: ");
    JPasswordField passwordField = new JPasswordField();
    JLabel confirmPasswordLabel = new JLabel("Confirm Password: ");
    JPasswordField confirmPasswordField = new JPasswordField();
    JButton signUpButton = new JButton("Sign Up");
    JButton backButton = new JButton("Back");

    public SignUp(){
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4,2));
        loginPanel.add(userLabel);
        loginPanel.add(userTextField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(confirmPasswordLabel);
        loginPanel.add(confirmPasswordField);
        signUpButton.addActionListener(this);
        backButton.addActionListener(this);
        loginPanel.add(signUpButton);
        loginPanel.add(backButton);
        add(loginPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == signUpButton && passwordField.getPassword().length > 0 && userTextField.getText().length() > 0){
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            if(password.equals(confirmPassword)){
                try {
                    BufferedReader input = new BufferedReader(new FileReader("passwords.txt"));
                    String line = input.readLine();
                    while (line != null){
                        StringTokenizer st = new StringTokenizer(line);
                        if (userTextField.getText().equals(st.nextToken())){
                            System.out.println("User Already Exists");
                            return;
                        }
                        line = input.readLine();
                    }
                    input.close();
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(password.getBytes());
                    byte[] byteData = md.digest();
                    StringBuilder sb = new StringBuilder();
                    for (byte byteDatum : byteData) {
                        sb.append(Integer.toString((byteDatum & 0xFF) + 0x100, 16).substring(1));
                    }
                    BufferedWriter output = new BufferedWriter(new FileWriter("passwords.txt", true));
                    output.write(userTextField.getText() + " " + sb.toString() + "\n");
                    output.close();
                    Login login = (Login) getParent();
                    login.cardLayout.show(login, "login");
                } catch (IOException | NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
        if(e.getSource() == backButton){
            Login login = (Login) getParent();
            login.cardLayout.show(login, "login");
        }
    }
}