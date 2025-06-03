import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageFrame extends JFrame {

    private ImagePanel imagePanel;
    private EffectsPanel effectsPanel;

    public ImageFrame(BufferedImage image) {
        setTitle("Image Processing App");
        setLayout(new BorderLayout());

        imagePanel = new ImagePanel();
        imagePanel.setImage(image);
        effectsPanel = new EffectsPanel(imagePanel);

        add(imagePanel, BorderLayout.CENTER);
        add(effectsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
