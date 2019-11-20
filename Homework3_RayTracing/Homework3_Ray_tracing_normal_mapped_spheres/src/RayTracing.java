/* Dana Adylova
 * 01/29/2015
 * This class has methods that are used to produce a sphere ray traced image.
*/
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class RayTracing {
	
	/** This method checks id a ray from the origin hits the sphere
	 * 
	 * @param ray
	 * @param center
	 * @param radius
	 * @return true if the ray hits the sphere
	 */
	public static boolean doesHitSphere(vector ray, vector center, double radius) {
		double lengthRay = vector.vectorLength(ray);
		double lengthCenter = vector.vectorLength(center);
		double cosRayCenter = (ray.getX()*center.getX()+ray.getY()*center.getY()+ray.getZ()*center.getZ())/(lengthRay*lengthCenter);
		double d = lengthCenter*Math.sqrt(1-cosRayCenter*cosRayCenter);
		if (d<=radius) {
			return true;
		}
		return false;
	}
	
	/** This method finds normal to the surface of sphere. The hit point is closer to the head of the ray.
	 * 
	 * @param ray that hits the sphere
	 * @param center of a spere
	 * @return
	 */
	public static vector normal(vector ray, vector center, double radius) {
		double lengthRay = vector.vectorLength(ray);
		double lengthCenter = vector.vectorLength(center);	
		double cosRayCenter = (ray.getX()*center.getX()+ray.getY()*center.getY()+ray.getZ()*center.getZ())/(lengthRay*lengthCenter);
		double d = lengthCenter*Math.sqrt(1-cosRayCenter*cosRayCenter);
		double t = (Math.sqrt(lengthCenter*lengthCenter - d*d) - Math.sqrt(radius*radius - d*d))/lengthRay;
		vector hit = new vector (t*ray.getX(), t*ray.getY(), t*ray.getZ());
		vector norm = new vector(hit.getX()-center.getX(), hit.getY()-center.getY(), hit.getZ()-center.getZ());
		double normLength = vector.vectorLength(norm);
		norm.setX(norm.getX()/normLength);
		norm.setY(norm.getY()/normLength);
		norm.setZ(norm.getZ()/normLength);
		return norm;
	}
	
	/** This method does ray tracing with a ray in direction (x,y,z) =  (-1 + 2*(i/(height-1), -1 + 2*(j/(width-1)), -1).
	 * If the ray hits the sphere, (red, green, blue) = ((nx + 1)/2, (ny+1)/2, (nz+1)/2) where nx, ny, and nz are coordinates of the normal to the surface where the ray hits the sphere.
	 * If the ray misses the sphere, (red, green, blue) = (0, 0.2*(1+y), 0.1) where y is y coordinate of the ray.
	 * 
	 * @param image to be ray traced
	 * @param center of a sphere
	 * @param radius of a sphere
	 */
	public static void sphereRayTracing(BufferedImage image, vector center, double radius){
		int width = image.getWidth();
		int height = image.getHeight();
		try {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					double red, green, blue;
					vector ray = new vector(2.0*i/((double)height-1.0) -1.0, 2.0*j/((double)width-1.0) - 1.0, -1.0);
					if (doesHitSphere(ray, center, radius)) {
						vector norm = normal(ray, center, radius);
						red = (norm.getX() + 1.0)*255.0/2.0;
						green = (norm.getY()+1.0)*255.0/2.0;
						blue = (norm.getZ()+1.0)*255.0/2.0;
					}
					else {
						red = 0;
						green = 0.2*(1.0+ray.getY())*255.0;
						blue = 0.1*255.0;
					}
					int r = (int)red;
					int g = (int)green;
					int b = (int)blue;
				    int rgb = ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
				    image.setRGB(j,i,rgb);
			      }
			    }
			ImageIO.write(image, "jpg", new File("/Users/danaadylova/Desktop/sphereRayTracing.jpg"));
		} catch (IOException e) {
			System.out.println("Exception occured :" + e.getMessage());
		}
	}

	public static void main(String[] args) {
		BufferedImage image = new BufferedImage (256, 256, BufferedImage.TYPE_INT_RGB);
		vector center = new vector(0,0,-3);
		sphereRayTracing(image, center, 2.0);
		}
}