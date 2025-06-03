import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Random;

public class EffectsPanel extends JPanel {

    private ImagePanel imagePanel;
    private boolean isEffectApplied = false;

    public EffectsPanel(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        setLayout(new GridLayout(2, 5));

        String[] effectNames = {"Black-White", "Negative", "Sepia", "Add Noise", "Pixelate",
                "Mirror", "Tint", "Posterize", "Solarize","Vignette"};

        for (String effectName : effectNames) {
            JButton effectButton = new JButton(effectName);
            effectButton.addActionListener(new EffectButtonListener(effectName));
            add(effectButton);
        }
    }

    private class EffectButtonListener implements ActionListener {

        private String effectName;

        public EffectButtonListener(String effectName) {
            this.effectName = effectName;
        }

        public void actionPerformed(ActionEvent e) {
            BufferedImage img = imagePanel.getCurrentImage();
            if (img == null) return;

            if (isEffectApplied) {
                imagePanel.resetImage();
            } else {
                BufferedImage modifiedImage = imagePanel.getCurrentImage();

                // apply effect to the left part of the image only
                int divisionX = imagePanel.divisionX > 0 ? imagePanel.divisionX : modifiedImage.getWidth();

                BufferedImage leftPart = modifiedImage.getSubimage(0, 0, divisionX, modifiedImage.getHeight());
                BufferedImage effectedLeftPart = deepCopy(leftPart);

                switch (effectName) {
                    case "Black-White":
                        applyBlackWhiteEffect(effectedLeftPart);
                        break;
                    case "Negative":
                        applyNegativeEffect(effectedLeftPart);
                        break;
                    case "Sepia":
                        applySepiaEffect(effectedLeftPart);
                        break;
                    case "Add Noise":
                        addNoise(effectedLeftPart);
                        break;
                    case "Pixelate":
                        applyPixelateEffect(effectedLeftPart);
                        break;
                    case "Mirror":
                        applyMirrorEffect(effectedLeftPart);
                        break;
                    case "Tint":
                        applyTintEffect(effectedLeftPart);
                        break;
                    case "Posterize":
                        applyPosterizeEffect(effectedLeftPart);
                        break;
                    case "Solarize":
                        applySolarizeEffect(effectedLeftPart);
                        break;
                    case "Vignette":
                        applyVignette(effectedLeftPart);
                        break;
                    default:
                        System.out.println("Unknown effect: " + effectName);
                }

                Graphics g = modifiedImage.getGraphics();
                g.drawImage(effectedLeftPart, 0, 0, null);
                g.dispose();

                imagePanel.applyEffect(modifiedImage);
            }
            isEffectApplied = !isEffectApplied;
        }

        private BufferedImage deepCopy(BufferedImage bi) {
            ColorModel cm = bi.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = bi.copyData(null);
            return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        }

        private void applyBlackWhiteEffect(BufferedImage img) {
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int rgba = img.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    int gray = (col.getRed() + col.getGreen() + col.getBlue()) / 3;
                    int newColor = (gray > 128) ? Color.WHITE.getRGB() : Color.BLACK.getRGB();
                    img.setRGB(x, y, newColor);
                }
            }
        }

        private void applyNegativeEffect(BufferedImage img) {
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int rgba = img.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());
                    img.setRGB(x, y, col.getRGB());
                }
            }
        }

        private void applySepiaEffect(BufferedImage img) {
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int rgba = img.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    int tr = (int) (0.393 * col.getRed() + 0.769 * col.getGreen() + 0.189 * col.getBlue());
                    int tg = (int) (0.349 * col.getRed() + 0.686 * col.getGreen() + 0.168 * col.getBlue());
                    int tb = (int) (0.272 * col.getRed() + 0.534 * col.getGreen() + 0.131 * col.getBlue());
                    col = new Color(Math.min(tr, 255), Math.min(tg, 255), Math.min(tb, 255));
                    img.setRGB(x, y, col.getRGB());
                }
            }
        }

        private void addNoise(BufferedImage img) {
            Random random = new Random();
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int rgba = img.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    int noise = (int) (random.nextGaussian() * 30);
                    col = new Color(
                            clamp(col.getRed() + noise),
                            clamp(col.getGreen() + noise),
                            clamp(col.getBlue() + noise));
                    img.setRGB(x, y, col.getRGB());
                }
            }
        }

        private int clamp(int value) {
            return Math.max(0, Math.min(value, 255));
        }

        private void applyPixelateEffect(BufferedImage img) {
            int pixelSize = 10;
            for (int y = 0; y < img.getHeight(); y += pixelSize) {
                for (int x = 0; x < img.getWidth(); x += pixelSize) {
                    int averageR = 0, averageG = 0, averageB = 0;
                    int count = 0;

                    for (int dy = 0; dy < pixelSize; dy++) {
                        for (int dx = 0; dx < pixelSize; dx++) {
                            if (x + dx < img.getWidth() && y + dy < img.getHeight()) {
                                int rgba = img.getRGB(x + dx, y + dy);
                                Color col = new Color(rgba, true);
                                averageR += col.getRed();
                                averageG += col.getGreen();
                                averageB += col.getBlue();
                                count++;
                            }
                        }
                    }

                    averageR /= count;
                    averageG /= count;
                    averageB /= count;
                    Color averageColor = new Color(averageR, averageG, averageB);

                    for (int dy = 0; dy < pixelSize; dy++) {
                        for (int dx = 0; dx < pixelSize; dx++) {
                            if (x + dx < img.getWidth() && y + dy < img.getHeight()) {
                                img.setRGB(x + dx, y + dy, averageColor.getRGB());
                            }
                        }
                    }
                }
            }
        }

        private void applyMirrorEffect(BufferedImage img) {
            int width = img.getWidth();
            int height = img.getHeight();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width / 2; x++) {
                    int leftPixel = img.getRGB(x, y);
                    int rightPixel = img.getRGB(width - 1 - x, y);
                    img.setRGB(x, y, rightPixel);
                    img.setRGB(width - 1 - x, y, leftPixel);
                }
            }
        }


        private void applyTintEffect(BufferedImage img) {
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int rgba = img.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    col = new Color(col.getRed(), (int) (col.getGreen() * 0.7), (int) (col.getBlue() * 0.7));
                    img.setRGB(x, y, col.getRGB());
                }
            }
        }

        private void applyPosterizeEffect(BufferedImage img) {
            int posterizationLevels = 4;
            int[] levels = new int[256];
            for (int i = 0; i < 256; i++) {
                levels[i] = 255 * (posterizationLevels * i / 256) / (posterizationLevels - 1);
            }

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int rgba = img.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    col = new Color(levels[col.getRed()], levels[col.getGreen()], levels[col.getBlue()]);
                    img.setRGB(x, y, col.getRGB());
                }
            }
        }

        private void applySolarizeEffect(BufferedImage img) {
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int rgba = img.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    col = new Color(col.getRed() < 128 ? 255 - col.getRed() : col.getRed(),
                            col.getGreen() < 128 ? 255 - col.getGreen() : col.getGreen(),
                            col.getBlue() < 128 ? 255 - col.getBlue() : col.getBlue());
                    img.setRGB(x, y, col.getRGB());
                }
            }
        }

        private void applyVignette(BufferedImage img) {
            Graphics2D g = img.createGraphics();
            float radius = img.getWidth() / 2f;
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 150)};
            RadialGradientPaint p = new RadialGradientPaint(
                    new Point2D.Float(img.getWidth() / 2f, img.getHeight() / 2f), radius, dist, colors);
            g.setPaint(p);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.dispose();
        }
    }
}
