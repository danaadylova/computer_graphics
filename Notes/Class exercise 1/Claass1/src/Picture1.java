// Dana Adylova
// This code creates an image on the desktop. Image is half-stripes, half- gradient.

import java.io.FileWriter;
import java.io.IOException;

public class Picture1 {
	
	public static void main(String[] args) throws IOException {
		String path = "/Users/danaadylova/Desktop/";
	    FileWriter out = new FileWriter(path+"pic.ppm");
		out.write("P3 \n256 256 \n255\n");
		for (int i = 0; i<256; i++){
			for (int j = 0; j<256; j++) {
				if (j<128){
					out.write(i+" "+i+" "+i+"  ");
					}
				else if ((j>=128) && (i%2 == 0)) {
					out.write(0+" "+0+" "+0+" ");
					}
				else if ((j>=128) && (i%2 == 1)) {
					out.write(255+" "+255+" "+255+" ");
					}
				}
			out.write('\n');
			}
		}
	
	}
