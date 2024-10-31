
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageRotate
 {

    public static BufferedImage rotateImage(BufferedImage src, double angle)
	{
        // Calculate the new dimensions of the rotated image
        double radians = Math.toRadians(angle);
        int width = src.getWidth();
        int height = src.getHeight();
        
        // Calculate the size of the new image
        int newWidth = (int) Math.abs(width * Math.cos(radians)) + (int) Math.abs(height * Math.sin(radians));
        int newHeight = (int) Math.abs(height * Math.cos(radians)) + (int) Math.abs(width * Math.sin(radians));

        // Create a new image with the calculated dimensions
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, src.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        // Set the rotation point to the center of the new image
        g2d.translate(newWidth / 2, newHeight / 2);
        g2d.rotate(radians);
        g2d.translate(-width / 2, -height / 2);

        // Draw the original image onto the rotated image
        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

    public static void main(String[] args)
	{
        try 
		{
            // Load the image
            BufferedImage image = ImageIO.read(new File(args[0]));

            // Rotate the image by 90 degrees
            BufferedImage rotatedImage = rotateImage(image, 90);

            // Save the rotated image
            ImageIO.write(rotatedImage, "png", new File("rotated_"+args[0].split("\\.")[0]+".png"));

        } catch (IOException e) 
		{
            e.printStackTrace();
        }
    }
}

