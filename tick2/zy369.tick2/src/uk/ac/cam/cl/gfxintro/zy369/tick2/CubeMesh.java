package uk.ac.cam.cl.gfxintro.zy369.tick2;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3f;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Class inheriting from Mesh class
 * Defines a Cube mesh by overloading Mesh's 3D position, UV texture coordiantes and normals
 *
 */
public class CubeMesh extends Mesh {

	private Mesh parent = null;

	private Vector3f scale = null;

	private Vector3f translate = null;

	private Vector3f hinge = null;

	private Vector3f angle = null;
	public CubeMesh() {
		super();
		initialize();
	}

	public CubeMesh(Mesh _parent){
		super();
		initialize();
		parent = _parent;
	}

	public void setScale(Vector3f s){
		scale = s;
	}

	public void setTranslate(Vector3f t){
		translate = t;
	}

	public void setRotate(Vector3f h, Vector3f a){
		hinge = h;
		angle = a;
	}

	public void setRotateAngle(Vector3f a){
		angle = a;
	}

	public Matrix4f getpTransform(){
		Matrix4f transform = new Matrix4f();
		if(parent != null){
			transform = parent.getpTransform();
		}

		if(translate != null)
			transform.translate(translate);

		if(hinge != null) {
			transform.translate(hinge);
			transform.rotateAffineXYZ(angle.x, angle.y, angle.z);
			transform.translate(-hinge.x, -hinge.y, -hinge.z);
		}
		return transform;
	}

	public Matrix4f getTransform(){
		if(scale != null)
			return getpTransform().scale(scale);
		else
			return getpTransform();
	}
	@Override
	float[] initializeVertexPositions() {
		float[] vertPositions = new float[] {
				-1,  1, -1, -1,  1,  1,  1,  1, -1,
				-1,  1,  1,  1,  1,  1,  1,  1, -1,
				-1, -1,  1, -1, -1, -1,  1, -1,  1,
				-1, -1, -1,  1, -1, -1,  1, -1,  1,
				-1,  1,  1, -1, -1,  1,  1,  1,  1,
				-1, -1,  1,  1, -1,  1,  1,  1,  1,
				-1, -1, -1, -1,  1, -1,  1, -1, -1,
				-1,  1, -1,  1,  1, -1,  1, -1, -1,
				-1, -1, -1, -1, -1,  1, -1,  1, -1,
				-1, -1,  1, -1,  1,  1, -1,  1, -1,
				1,  1, -1,  1,  1,  1,  1, -1, -1,
				1,  1,  1,  1, -1,  1,  1, -1, -1
		};
		return vertPositions;
	}

	@Override
	int[] initializeVertexIndices() {

		int[] indices = new int[] {
				0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11,
				12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
				24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
		};
		return indices;
	}

	@Override
	float[] initializeVertexNormals() {

		float[] vertNormals = new float[] {
				0,  1,  0,  0,  1,  0,  0,  1,  0,
				0,  1,  0,  0,  1,  0,  0,  1,  0,
				0, -1,  0,  0, -1,  0,  0, -1,  0,
				0, -1,  0,  0, -1,  0,  0, -1,  0,
				0,  0,  1,  0,  0,  1,  0,  0,  1,
				0,  0,  1,  0,  0,  1,  0,  0,  1,
				0,  0, -1,  0,  0, -1,  0,  0, -1,
				0,  0, -1,  0,  0, -1,  0,  0, -1,
				-1,  0,  0, -1,  0,  0, -1,  0,  0,
				-1,  0,  0, -1,  0,  0, -1,  0,  0,
				1,  0,  0,  1,  0,  0,  1,  0,  0,
				1,  0,  0,  1,  0,  0,  1,  0,  0
		};
		return vertNormals;
	}

	@Override
	float[] initializeTextureCoordinates() {
		float[] texCoors = new float[] {
				1/4f,0, 	1/4f,1/3f, 	1/2f,0,
				1/4f,1/3f,	1/2f,1/3f,	1/2f,0,
				1/4f,2/3f,	1/4f,1,		1/2f,2/3f,
				1/4f,1,		1/2f,1,		1/2f,2/3f,
				1/4f,1/3f, 	1/4f,2/3f,	1/2f,1/3f,
				1/4f,2/3f,	1/2f,2/3f,	1/2f,1/3f,
				1,2/3f,		1,1/3f, 	3/4f,2/3f,
				1,1/3f,		3/4f,1/3f,	3/4f,2/3f,
				0,2/3f,		1/4f,2/3f,	0f,1/3f,
				1/4f,2/3f,	1/4f,1/3f,	0f,1/3f,
				3/4f,1/3f,	1/2f,1/3f,	3/4f,2/3f,
				1/2f,1/3f,	1/2f,2/3f,	3/4f,2/3f
		};
		return texCoors;
	}
}
