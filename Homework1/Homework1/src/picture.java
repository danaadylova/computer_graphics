// Dana Adylova
// Homework 1

import java.io.FileWriter;
import java.io.IOException;

public class picture {

	public static void main(String[] args) throws IOException {
		String path = "/Users/danaadylova/Desktop/";
	    FileWriter out = new FileWriter(path+"homework1.ppm");
		out.write("P3 \n256 256 \n255\n");
		for (int i = 0; i<256; i++){
			if (i<128) {
				for (int j = 0; j<256; j++) {
					if (i%8==0)
						out.write(255+" "+0+" "+0+"  ");
					else
						out.write(0+" "+255+" "+0+"  ");
					}
			}
			else {
				for (int j = 0; j<256; j++) {
					out.write(0+" "+0+" "+200+"  ");
				}
			}
			out.write('\n');
		}
	}

}
