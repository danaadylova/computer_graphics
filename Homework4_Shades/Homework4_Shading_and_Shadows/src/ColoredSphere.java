
public class ColoredSphere {

	private vector center;
	private color color;
	private double radius;
	
	public ColoredSphere() {
		vector cen = new vector(0,0,0);
		center = cen;
		radius = 0;
	}
	
	public ColoredSphere(vector cen, color col, double rad) {
		center = cen;
		color = col;
		radius = rad;
	}
	
	public vector getCenter() {
		return center;
	}
	
	public color getColor() {
		return color;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setCenter(vector cen) {
		center = cen;
	}
	
	public void setColor(color col) {
		color = col;
	}
	
	public void setRadius(double rad) {
		radius = rad;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
