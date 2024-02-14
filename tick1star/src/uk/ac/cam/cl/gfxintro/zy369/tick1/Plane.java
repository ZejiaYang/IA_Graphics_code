package uk.ac.cam.cl.gfxintro.zy369.tick1;

public class Plane extends SceneObject {
	
	// Plane constants
	private final double DEFAULT_PLANE_KD = 0.6;
	private final double DEFAULT_PLANE_KS = 0.0;
	private final double DEFAULT_PLANE_ALPHA = 0.0;
	private final double DEFAULT_PLANE_REFLECTIVITY = 0.1;

	// A point in the plane
	private Vector3 point;

	// The normal of the plane
	private Vector3 normal;

	public Plane(Vector3 point, Vector3 normal, ColorRGB colour) {
		this.point = point;
		this.normal = normal;
		this.colour = colour;

		this.phong_kD = DEFAULT_PLANE_KD;
		this.phong_kS = DEFAULT_PLANE_KS;
		this.phong_alpha = DEFAULT_PLANE_ALPHA;
		this.reflectivity = DEFAULT_PLANE_REFLECTIVITY;
		this.textured = false;
	}

	public Plane(Vector3 point, Vector3 normal, ColorRGB colour, String textmap) {
		this.point = point;
		this.normal = normal;
		this.colour = colour;

		this.phong_kD = DEFAULT_PLANE_KD;
		this.phong_kS = DEFAULT_PLANE_KS;
		this.phong_alpha = DEFAULT_PLANE_ALPHA;
		this.reflectivity = DEFAULT_PLANE_REFLECTIVITY;
		load_text_map(textmap);
	}

	public Plane(Vector3 point, Vector3 normal, ColorRGB colour, double kD, double kS, double alphaS, double reflectivity) {
		this.point = point;
		this.normal = normal;
		this.colour = colour;
		this.phong_kD = kD;
		this.phong_kS = kS;
		this.phong_alpha = alphaS;
		this.reflectivity = reflectivity;
		this.textured = false;
	}

	public Plane(Vector3 point, Vector3 normal, ColorRGB colour, double kD, double kS, double alphaS, double reflectivity, String textmap) {
		this.point = point;
		this.normal = normal;
		this.colour = colour;

		this.phong_kD = kD;
		this.phong_kS = kS;
		this.phong_alpha = alphaS;
		this.reflectivity = reflectivity;
		load_text_map(textmap);

		this.max_height = 240877.57324478272;
		this.min_height = 0.0011280273352161085;
		this.max_width = 240876.57324498295;
		this.min_width = -240876.57324478272;

		this.height_range = this.max_height - this.min_height;
		this.width_range = this.max_width - this.min_width;
	}

	// Intersect this plane with a ray
	@Override
	public RaycastHit intersectionWith(Ray ray) {
		// Get ray parameters
		Vector3 O = ray.getOrigin();
		Vector3 D = ray.getDirection();
		
		// Get plane parameters
		Vector3 Q = this.point;
		Vector3 N = this.normal;

		double s = Q.subtract(O).dot(N)/(D.dot(N));

		if(s >= 0){
			double distance = D.scale(s).magnitude();
			Vector3 location = O.add(D.scale(s));
			return new RaycastHit(this, distance, location, N );}
		else
			return new RaycastHit();
	}

	// Get normal to the plane   
	@Override
	public Vector3 getNormalAt(Vector3 position) {
		return normal; // normal is the same everywhere on the plane
	}

	public double max_height;
	public double min_height;

	public double max_width;
	public double min_width;

	public double height_range;

	public double width_range;
	public ColorRGB getColour(Vector3 position){
		Vector3 R = position.subtract(this.point);
		int rheight = 35;
		int rwidth = 35;
		if(Math.abs(R.x) > rwidth || Math.abs(R.y) >rheight)
			return this.colour;
		else {
			int v = (int) (R.y /rheight * textMapHeight);
			int u = (int) ((R.x / (2 *rwidth) + 0.5) * textMapWidth);
			return textMap[v][u];
		}
	}
}
