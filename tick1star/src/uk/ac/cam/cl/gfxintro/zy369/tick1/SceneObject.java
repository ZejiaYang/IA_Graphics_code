package uk.ac.cam.cl.gfxintro.zy369.tick1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class SceneObject {
	
	// The diffuse colour of the object
	protected ColorRGB colour;

	// Coefficients for calculating Phong illumination
	protected double phong_kD, phong_kS, phong_alpha;

	// How reflective this object is
	protected double reflectivity;

	// How much light is transmitted through the object (between 0 and 1)
	protected ColorRGB transmittance;
	protected double refractive_index;

	public boolean textured;
	protected ColorRGB[][] textMap;
	protected int textMapHeight;
	protected int textMapWidth;

	protected SceneObject() {
		colour = new ColorRGB(1);
		phong_kD = phong_kS = phong_alpha = reflectivity = 0;
		refractive_index = 1.5;
		transmittance = new ColorRGB(0);
	}

	// Intersect this object with ray
	public abstract RaycastHit intersectionWith(Ray ray);

	// Get normal to object at position
	public abstract Vector3 getNormalAt(Vector3 position);

	public ColorRGB getColour() {
		return colour;
	}

	public ColorRGB getColour(Vector3 position) {
		return colour;
	}

	public void setColour(ColorRGB colour) {
		this.colour = colour;
	}

	public double getPhong_kD() {
		return phong_kD;
	}

	public double getPhong_kS() {
		return phong_kS;
	}

	public double getPhong_alpha() {
		return phong_alpha;
	}

	public double getReflectivity() {
		return reflectivity;
	}

	public void load_text_map(String texture_map){
		if (texture_map == "") {
			textured = false;
		}
		else
		{
			textured = true;
			try {
				BufferedImage inputImg = ImageIO.read(new File(texture_map));
				textMapHeight = inputImg.getHeight();
				textMapWidth = inputImg.getWidth();
				textMap = new ColorRGB[textMapHeight][textMapWidth];
				for (int row = 0; row < textMapHeight; row++) {
					for (int col = 0; col < textMapWidth; col++) {
						double blue = (double) (inputImg.getRGB(col, row) & 0xFF) / 0xFF;
						double green = (double) ((inputImg.getRGB(col, row) & 0xFF00) >> 8) /0xFF;
						double red = (double) ((inputImg.getRGB(col, row) & 0xFF0000) >> 16) /0xFF;
						textMap[row][col] = new ColorRGB(red, green, blue);
					}
				}
			} catch (IOException e) {
				System.err.println("Error creating bump map");
				e.printStackTrace();
			}
		}
	}

	public boolean isTransmissive() { return !transmittance.isZero(); }

	public ColorRGB getTransmittance() { return transmittance; }
	public double getRefractiveIndex() {
		return refractive_index;
	}

	public void setReflectivity(double reflectivity) {
		this.reflectivity = reflectivity;
	}
}
