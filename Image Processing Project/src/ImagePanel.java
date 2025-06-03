import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImagePanel extends JPanel {

    private BufferedImage originalImage;
    private BufferedImage currentImage;
    int divisionX = -1;

    public ImagePanel() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                divisionX = e.getX();
                repaint();
            }
        });
    }

    public void setImage(BufferedImage image) {
        this.originalImage = deepCopy(image);
        this.currentImage = deepCopy(image);
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        divisionX = -1; // לאפס את הקו האנכי כאשר תמונה חדשה נטענת
        revalidate();
        repaint();
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public void applyEffect(BufferedImage modifiedImage) {
        this.currentImage = deepCopy(modifiedImage);
        repaint();
    }

    public void resetImage() {
        this.currentImage = deepCopy(originalImage);
        divisionX = -1; // לאפס את הקו האנכי כאשר תמונה מתאפסת
        repaint();
    }

    private BufferedImage deepCopy(BufferedImage bi) {
        if (bi == null) return null;
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), this);

            if (divisionX >= 0) {
                // חלק שמאלי
                BufferedImage leftImage = currentImage.getSubimage(0, 0, divisionX, currentImage.getHeight());
                g.drawImage(leftImage, -1, -1, divisionX, getHeight(), this);

                // חלק ימני
                BufferedImage rightImage = originalImage.getSubimage(divisionX, 0, originalImage.getWidth() - divisionX, originalImage.getHeight());
                g.drawImage(rightImage, divisionX, 0, originalImage.getWidth() - divisionX, getHeight(), this);

                // קו אדום
                g.setColor(Color.RED);
                g.drawLine(divisionX, 0, divisionX, getHeight());
            }
        }
    }
}
