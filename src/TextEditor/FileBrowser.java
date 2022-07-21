package TextEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

public class FileBrowser extends JPanel implements ActionListener {

    JLabel label = new JLabel("File List: \n");
    JButton newFile = new JButton("New File");
    JButton openFile = new JButton("Open");
    JButton deleteFile = new JButton("Delete");
    JTextField newFileTextField = new JTextField(10);
    JSeparator separator = new JSeparator();
    ButtonGroup bg;
    File directory;

    public FileBrowser(String dir){
        directory = new File(dir);
        directory.mkdir();
        JPanel fileList = new JPanel(new GridLayout(Objects.requireNonNull(directory.listFiles()).length + 3, 1));
        fileList.add(label);
        separator.setMaximumSize(new Dimension());
        fileList.add(separator);
        bg = new ButtonGroup();
        for (File file : Objects.requireNonNull(directory.listFiles())){
            JRadioButton radioButton = new JRadioButton(file.getName());
            radioButton.setActionCommand(file.getName());
            bg.add(radioButton);
            fileList.add(radioButton);
        }

        JPanel newPanel = new JPanel();
        newPanel.add(newFileTextField);
        newPanel.add((newFile));
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        deleteFile.addActionListener(this);
        fileList.add(openFile);
        fileList.add(deleteFile);
        fileList.add(newPanel);
        add(fileList);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Login login = (Login) getParent();

        if (e.getSource() == openFile){
            login.add(new Editor(directory.getName()+"/"+bg.getSelection().getActionCommand()), "editor");
            login.cardLayout.show(login, "editor");
        }

        if (e.getSource() == newFile){
            String file = directory.getName()+"/"+newFileTextField.getText()+".txt";
            if (newFileTextField.getText().length() > 0 && !(new File(file).exists())){
                login.add(new Editor(file), "editor");
                login.cardLayout.show(login, "editor");
            }
        }

        if (e.getSource() == deleteFile){
            File file = new File(directory.getName()+"/"+bg.getSelection().getActionCommand());
            System.out.println("1 " + file);
            if(file.delete()){
                login.add(new FileBrowser(directory.getName()), "fileBrowser");
                login.cardLayout.show(login, "fileBrowser");
                System.out.println("2 " + file);
            }

        }
    }
}
