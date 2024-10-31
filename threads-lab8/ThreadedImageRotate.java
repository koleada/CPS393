import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageRotate {

    public static BufferedImage rotateImageSection(BufferedImage src, double angle, int startX, int endX) {
        double radians = Math.toRadians(angle);
        int width = src.getWidth();
        int height = src.getHeight();
        
        // Calculate the new dimensions for the rotated section
        int newWidth = (int) Math.abs(width * Math.cos(radians)) + (int) Math.abs(height * Math.sin(radians));
        int newHeight = (int) Math.abs(height * Math.cos(radians)) + (int) Math.abs(width * Math.sin(radians));

        // Create a new image for this section
        BufferedImage rotatedSection = new BufferedImage(newWidth, newHeight, src.getType());
        Graphics2D g2d = rotatedSection.createGraphics();

        // Set rotation around the center of the entire image, not the section
        g2d.translate(newWidth / 2, newHeight / 2);
        g2d.rotate(radians);
        g2d.translate(-width / 2, -height / 2);

        // Draw the original section onto the rotated section image
        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();

        // Return only the portion of the rotated section that corresponds to the startX and endX positions
        return rotatedSection.getSubimage(startX, 0, endX - startX, newHeight);
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        try {
            // Load the image
            BufferedImage image = ImageIO.read(new File(args[0]));
            int width = image.getWidth();
            int height = image.getHeight();
            int threadCount = 4; // Number of threads
            int sectionWidth = width / threadCount;
            BufferedImage[] rotatedSections = new BufferedImage[threadCount];
            Thread[] threads = new Thread[threadCount];

            // Create and start threads for each section of the image
            for (int i = 0; i < threadCount; i++) {
                final int startX = i * sectionWidth;
                final int endX = (i == threadCount - 1) ? width : startX + sectionWidth;
                final int index = i;

                threads[i] = new Thread(() -> {
                    rotatedSections[index] = rotateImageSection(image, 90, startX, endX);
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Create the final rotated image with the correct dimensions
            BufferedImage finalImage = new BufferedImage(rotatedSections[0].getWidth() * threadCount, rotatedSections[0].getHeight(), image.getType());
            Graphics g = finalImage.getGraphics();
            
            // Draw each section at the appropriate horizontal position
            for (int i = 0; i < threadCount; i++) {
                g.drawImage(rotatedSections[i], i * rotatedSections[i].getWidth(), 0, null);
            }
            g.dispose();

            // Save the final rotated image
            ImageIO.write(finalImage, "png", new File("rotated_" + args[0].split("\\.")[0] + ".png"));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1_000_000_000 + " seconds");
    }
}

