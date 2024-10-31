import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThreadedImageBlur {

    public static BufferedImage gaussianBlur(BufferedImage src, int startX, int endX) {
        int radius = 5; // Blur radius
        int size = radius * 2 + 1;

        // Create a Gaussian kernel
        double[][] kernel = new double[size][size];
        double sigma = radius / 3.0;
        double sum = 0.0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                double value = (double) Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
                kernel[x + radius][y + radius] = value;
                sum += value;
            }
        }

        // Normalize the kernel
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                kernel[x][y] /= sum;
            }
        }

        // Create a new image section to store the blurred result
        BufferedImage blurred = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

        // Apply the kernel to each pixel within the assigned section
        for (int x = startX; x < endX; x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                double r = 0, g = 0, b = 0;

                for (int kernelX = -radius; kernelX <= radius; kernelX++) {
                    for (int kernelY = -radius; kernelY <= radius; kernelY++) {
                        int imageX = Math.min(Math.max(x + kernelX, 0), src.getWidth() - 1);
                        int imageY = Math.min(Math.max(y + kernelY, 0), src.getHeight() - 1);

                        Color color = new Color(src.getRGB(imageX, imageY));
                        double kernelValue = kernel[kernelX + radius][kernelY + radius];

                        r += color.getRed() * kernelValue;
                        g += color.getGreen() * kernelValue;
                        b += color.getBlue() * kernelValue;
                    }
                }

                blurred.setRGB(x, y, new Color((int) r, (int) g, (int) b).getRGB());
            }
        }

        return blurred;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        try {
            // Load the image
            BufferedImage image = ImageIO.read(new File(args[0]));

            // Split the image into sections for multithreading
            int width = image.getWidth();
            int threadCount = 4;
            int sectionWidth = width / threadCount;
            BufferedImage[] blurredSections = new BufferedImage[threadCount];
            Thread[] threads = new Thread[threadCount];

            // Create and start threads to blur each section
            for (int i = 0; i < threadCount; i++) {
                final int startX = i * sectionWidth;
                final int endX = (i == threadCount - 1) ? width : startX + sectionWidth;
                final int index = i;

                threads[i] = new Thread(() -> {
                    blurredSections[index] = gaussianBlur(image, startX, endX);
                });
                threads[i].start();
            }

            // Wait for all threads to finish
            for (Thread thread : threads) {
                thread.join();
            }

            // Combine the blurred sections
            BufferedImage finalImage = new BufferedImage(width, image.getHeight(), image.getType());
            for (int i = 0; i < threadCount; i++) {
                int startX = i * sectionWidth;
                for (int x = startX; x < startX + sectionWidth && x < width; x++) {
                    for (int y = 0; y < image.getHeight(); y++) {
                        finalImage.setRGB(x, y, blurredSections[i].getRGB(x, y));
                    }
                }
            }

            // Save the final blurred image
            ImageIO.write(finalImage, "png", new File("blurred_" + args[0].split("\\.")[0] + ".png"));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nano seconds");
    }
}
