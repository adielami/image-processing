import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FileLoader {

    public BufferedImage loadFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                return ImageIO.read(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error loading image", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
}
