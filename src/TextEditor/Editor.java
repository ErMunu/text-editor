package TextEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Editor extends JPanel implements ActionListener {

    File file;
    JButton save = new JButton("Save");
    JButton saveClose = new JButton("Save and Close");
    JTextArea textArea = new JTextArea(20, 40);

    public Editor(String filePath){
        file = new File(filePath);
        save.addActionListener(this);
        saveClose.addActionListener(this);
        if(file.exists()){
            try {
                BufferedReader input = new BufferedReader(new FileReader(file));
                String line = input.readLine();
                while (line != null){
                    textArea.append(line+"\n");
                    line = input.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        add(save);
        add(saveClose);
        add(textArea);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            FileWriter out = new FileWriter(file);
            out.write(textArea.getText());
            out.close();
            if(e.getSource() == saveClose) {
                Login login = (Login) getParent();
                login.add(new FileBrowser(file.getPath().split("/")[0]), "fileBrowser");
                login.cardLayout.show(login, "fileBrowser");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
