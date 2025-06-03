import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class InitialFrame extends JFrame {

    public InitialFrame() {
        setTitle("Select Image");
        setSize(300, 100);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton openButton = new JButton("Open Image");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileLoader fileLoader = new FileLoader();
                BufferedImage image = fileLoader.loadFile(InitialFrame.this);
                if (image != null) {
                    new ImageFrame(image);
                    dispose();
                }
            }
        });

        add(openButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
