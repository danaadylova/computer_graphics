/* Dana Adylova
 * 02/04/2015
 * This class has methods that are used to produce a spheres ray traced image, including shades.
*/
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class shades {
	
	public static boolean originRayHitSphere(vector ray, ColoredSphere sph) {
		double lengthRay = vector.vectorLength(ray);
		double centerLength = vector.vectorLength(sph.getCenter());
		double cos = (ray.getX()*sph.getCenter().getX()+ray.getY()*sph.getCenter().getY()+ray.getZ()*sph.getCenter().getZ())/(lengthRay*centerLength);
		double d;
		if (cos>=0)
			d = centerLength*Math.sqrt(1-cos*cos);
		else
			return false;
		if (d>=sph.getRadius()) {
			return false;
		}
		return true;
	}
	
	public static vector hitPoint(vector ray, ColoredSphere sph) {
		double lengthRay = vector.vectorLength(ray);
		double originLength = vector.vectorLength(sph.getCenter());
		double cos = (ray.getX()*sph.getCenter().getX()+ray.getY()*sph.getCenter().getY()+ray.getZ()*sph.getCenter().getZ())/(lengthRay*originLength);
		double d = originLength*Math.sqrt(1-cos*cos);
		if (cos<0)
			return null;
		if (d>=sph.getRadius()) {
			return null;
		}
		double t = (Math.sqrt(originLength*originLength - d*d) - Math.sqrt(sph.getRadius()*sph.getRadius() - d*d))/lengthRay;
		if (t<=0.000000001)
			t = 0;
		vector hit = new vector (t*ray.getX(), t*ray.getY(), t*ray.getZ());
		return hit;
	}
	
	public static boolean sphereInFront(vector ray, ColoredSphere[] spheres, int currentSphere) {
		vector currentHit = hitPoint(ray, spheres[currentSphere]);
		if (currentHit == null)
			return false;
		double currentHitLength = vector.vectorLength(currentHit);
		for (int i = 0; i<4; i++) {
			if (i!= currentSphere) {
				if (normal(ray, spheres[i])!=null) {
					vector hitPoint = hitPoint(ray, spheres[i]);
					double hitLength = vector.vectorLength(hitPoint);
					if (hitLength<currentHitLength)  // DOES NOT INCLUDE SPHERES THAT ARE NOT SEEN
						return true;
				}
			}
		}
		return false;
	}
	
	public static boolean sphereUp (vector ray, ColoredSphere[] spheres, int currentSphere) {
		vector hitPoint = hitPoint(ray, spheres[currentSphere]);
		if (hitPoint == null)
			return false;
		vector n = new vector(0, 1, 0);
		for (int i = 0; i<4; i++) {
			if (i!=currentSphere) {
				// hitSph is a vector that starts at normal start and ends at the checking sphere center
				vector hitSph = new vector(spheres[i].getCenter().getX()-hitPoint.getX(), spheres[i].getCenter().getY()-hitPoint.getY(), spheres[i].getCenter().getZ()-hitPoint.getZ());
				double hitSphLength = vector.vectorLength(hitSph);
				double cos = (hitSph.getX()*n.getX()+hitSph.getY()*n.getY()+hitSph.getZ()*n.getZ())/(hitSphLength);
				double d;
				if (cos>=0){
					d = hitSphLength*Math.sqrt(1-cos*cos);
					if (d < spheres[i].getRadius())
						return true;
				}
			}
		}
		return false;
	}
	
	public static vector normal(vector ray, ColoredSphere sph) {
		vector hit = hitPoint(ray,sph);
		if (hit == null)
			return null;
		vector norm = new vector(hit.getX()-sph.getCenter().getX(), hit.getY()-sph.getCenter().getY(), hit.getZ()-sph.getCenter().getZ());
		double normLength = vector.vectorLength(norm);
		norm.setX(norm.getX()/normLength);
		norm.setY(norm.getY()/normLength);
		norm.setZ(norm.getZ()/normLength);
		return norm;
	}
	
	public static void sphereRayTracingShades(BufferedImage image, ColoredSphere[] spheres){
		int width = image.getWidth();
		int height = image.getHeight();
		try {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					vector ray = new vector(-1.0+2.0*j/((double)width), 1.0-2.0*i/((double)height), -1.0);
					color rayColor = new color(0,0,0);
					boolean hitsSomeSphere = false;
					for (int l=0; l<spheres.length; l++){
						if (originRayHitSphere(ray,spheres[l])) {
							hitsSomeSphere = true;
						}
					}
					
					if (hitsSomeSphere) {
						for (int k = 0; k < spheres.length; k++) {
							if (!sphereInFront(ray,spheres,k)) {
								if (originRayHitSphere(ray,spheres[k]) &&!sphereUp(ray, spheres, k)) {
									vector norm = normal(ray, spheres[k]);
									if (norm.getY()>0) {
										rayColor.setRed(spheres[k].getColor().getRed()*norm.getY()*255.0);
										rayColor.setGreen(spheres[k].getColor().getGreen()*norm.getY()*255.0);
										rayColor.setBlue(spheres[k].getColor().getBlue()*norm.getY()*255.0);
									}
									else {
										rayColor.setRed(0);
										rayColor.setBlue(0);
										rayColor.setGreen(0);
									}
								}
								else if (originRayHitSphere(ray,spheres[k]) && sphereUp(ray, spheres, k)) {
									rayColor.setRed(0);
									rayColor.setBlue(0);
									rayColor.setGreen(0);
								}
							}
						}
					}
					else {
						rayColor.setRed(0);
						rayColor.setGreen(0.2*(1.0-ray.getY())*255.0);
						rayColor.setBlue(0.1*255.0);
					}
					int r = (int)rayColor.getRed();
					int g = (int)rayColor.getGreen();
					int b = (int)rayColor.getBlue();
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
		BufferedImage image = new BufferedImage (600, 600, BufferedImage.TYPE_INT_RGB);
		vector oneCenter = new vector(0, 0, -3);
		color oneColor = new color(1, 0.5, 0.5);
		ColoredSphere one = new ColoredSphere(oneCenter, oneColor, 1);
		vector twoCenter = new vector(2, 0, -4);
		color twoColor = new color(0.5, 1, 0.5);
		ColoredSphere two = new ColoredSphere(twoCenter, twoColor, 1);
		vector threeCenter = new vector(-2, 0, -3);
		color threeColor = new color(0.5, 0.5, 1);
		ColoredSphere three = new ColoredSphere(threeCenter, threeColor, 1);
		vector fourCenter = new vector(0, -100, 0);
		color fourColor = new color(1, 1, 1);
		ColoredSphere four = new ColoredSphere(fourCenter, fourColor, 98.5);
		ColoredSphere[] spheres = new ColoredSphere[4];
		spheres[0]=one; 
		spheres[1]=two;
		spheres[2]=three;
		spheres[3]=four;
		sphereRayTracingShades(image, spheres);
//		vector vv = new vector(0,0,1);
//		vector center = new vector(0,0,-4);
//		color col = new color(0,0,0);
//		vector rayOrig = new vector(0,0,0);
//		ColoredSphere spp = new ColoredSphere(center,col,1);
//		if (doesRayHitSphere(rayOrig,vv,spp))
//			System.out.println("URRAAA");
		}
	
	
}