import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;


public class Homework2 {

	public static void main(String[] args) {
		BufferedImage image = null;
		try {
			File initialImage = new File("/Users/danaadylova/Desktop/blue-waterfall.jpg");
			image = ImageIO.read(initialImage);
			int w = image.getWidth();
			int h = image.getHeight();
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					int pixel = image.getRGB(j, i);
					int red = 255 - ((pixel >> 16) & 0xff);
				    int green = 255 - ((pixel >> 8) & 0xff);
				    int blue = 255 - ((pixel) & 0xff);
				    int rgb = ((red&0x0ff)<<16)|((green&0x0ff)<<8)|(blue&0x0ff);
				    image.setRGB(j,i,rgb);
			      }
			    }
			ImageIO.write(image, "jpg", new File("/Users/danaadylova/Desktop/blue-waterfall_negate.jpg"));
		} catch (IOException e) {
			System.out.println("Exception occured :" + e.getMessage());
		}
	}
}
