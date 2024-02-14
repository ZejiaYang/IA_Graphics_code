package uk.ac.cam.cl.gfxintro.zy369.tick1;

import java.util.Vector;

public class Sphere extends SceneObject {

	// Sphere coefficients
	private final double SPHERE_KD = 0.8;
	private final double SPHERE_KS = 1.2;
	private final double SPHERE_ALPHA = 10;
	private final double SPHERE_REFLECTIVITY = 0.3;

	private final double SPHERE_REFRACTION = 0.1;
	// The world-space position of the sphere
	protected Vector3 position;

	public Vector3 getPosition() {
		return position;
	}

	// The radius of the sphere in world units
	private double radius;

	public Sphere(Vector3 position, double radius, ColorRGB colour) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = SPHERE_KD;
		this.phong_kS = SPHERE_KS;
		this.phong_alpha = SPHERE_ALPHA;
		this.reflectivity = SPHERE_REFLECTIVITY;
		this.refractive_index = SPHERE_REFRACTION;
	}

	public Sphere(Vector3 position, double radius, ColorRGB colour, double kD, double kS, double alphaS, double reflectivity, ColorRGB transmittance) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = kD;
		this.phong_kS = kS;
		this.phong_alpha = alphaS;
		this.reflectivity = reflectivity;
		this.transmittance = transmittance;
	}

	/*
	 * Calculate intersection of the sphere with the ray. If the ray starts inside the sphere,
	 * intersection with the surface is also found.     
	 */
	public RaycastHit intersectionWith(Ray ray) {

		// Get ray parameters
		Vector3 O = ray.getOrigin();
		Vector3 D = ray.getDirection();
		
		// Get sphere parameters
		Vector3 C = position;
		double r = radius;

		// Calculate quadratic coefficients
		double a = D.dot(D);
		double b = 2 * D.dot(O.subtract(C));
		double c = (O.subtract(C)).dot(O.subtract(C)) - Math.pow(r, 2);

		double intersect = b * b - 4 * a * c;

		if(intersect < 0)
			return new RaycastHit();
		else if(intersect == 0){
			double s = -b/(2*a);

			if(s < 0)
				return new RaycastHit();
			else{
				Vector3 vdist = D.scale(s);
				double distance = Math.sqrt(vdist.dot(vdist));
				Vector3 location =  O.add(vdist);
				return new RaycastHit(this, distance, location, getNormalAt(location));
			}
		}else{
			double s1 = (-b + Math.sqrt(intersect))/(2*a);
			double s2 = (-b - Math.sqrt(intersect))/(2*a);

			Vector3 vdist1 = D.scale(s1);
			Vector3 vdist2 = D.scale(s2);

			double distance1 = Math.sqrt(vdist1.dot(vdist1));
			double distance2 = Math.sqrt(vdist2.dot(vdist2));

			Vector3 location1 = O.add(vdist1);
			Vector3 location2 = O.add(vdist2);

			Vector3 normal1 = location1.subtract(C).normalised();
			Vector3 normal2 = location2.subtract(C).normalised();

			//There is no intersection
			if (s1 < 0 && s2 < 0)
				return new RaycastHit();

			//There is only one intersection
			//ray, s2 doesn't exist
			else if (s1 >= 0 && s2 < 0)
				return new RaycastHit(this, distance1, location1, normal1);
			// since s1 >= 0 and s2 >= 0 and s2 < s1, thus s2 is more close to origin
			else
				return new RaycastHit(this, distance2, location2, normal2);
		}

	}

	// Get normal to surface at position
	public Vector3 getNormalAt(Vector3 position) {
		return position.subtract(this.position).normalised();
	}
}
