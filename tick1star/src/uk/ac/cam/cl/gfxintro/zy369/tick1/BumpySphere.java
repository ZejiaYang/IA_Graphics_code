package uk.ac.cam.cl.gfxintro.zy369.tick1;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class BumpySphere extends Sphere {

	private float BUMP_FACTOR = 5f;
	private float[][] heightMap;
	private int bumpMapHeight;
	private int bumpMapWidth;

	public BumpySphere(Vector3 position, double radius, ColorRGB colour, String bumpMapImg, String textMapImg) {
		super(position, radius, colour, textMapImg);
		try {
			BufferedImage inputImg = ImageIO.read(new File(bumpMapImg));
			bumpMapHeight = inputImg.getHeight();
			bumpMapWidth = inputImg.getWidth();
			heightMap = new float[bumpMapHeight][bumpMapWidth];
			for (int row = 0; row < bumpMapHeight; row++) {
				for (int col = 0; col < bumpMapWidth; col++) {
					float height = (float) (inputImg.getRGB(col, row) & 0xFF) / 0xFF;
					heightMap[row][col] = BUMP_FACTOR * height;
				}
			}
		} catch (IOException e) {
			System.err.println("Error creating bump map");
			e.printStackTrace();
		}
	}

	// Get normal to surface at position
	@Override
	public Vector3 getNormalAt(Vector3 position) {
		Vector3 R = position.subtract(this.position).normalised();

		double a = Math.atan2(R.z, R.x);
		if(a < 0)
			a = 2 * Math.PI + a;

		double b = Math.acos(R.y);

		int u = (int) (a /(2 * Math.PI) *bumpMapWidth);
		int v = (int) (b / Math.PI * bumpMapHeight);


		Vector3 Pu = new Vector3(- Math.sin(a) * Math.sin(b), 0, Math.cos(a) * Math.sin(b));
		Vector3 Pv = new Vector3( Math.cos(a) * Math.cos(b), -Math.sin(b), Math.cos(b) * Math.sin(a));
		float Bu = heightMap[v][(u + 1)%bumpMapWidth] - heightMap[v][u];
		float Bv = heightMap[(v + 1)%bumpMapHeight][u] - heightMap[v][u];

		return R.add(Pu.scale(Bu)).add(Pv.scale(Bv)).normalised();
	}

}
