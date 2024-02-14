package uk.ac.cam.cl.gfxintro.zy369.tick2;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.lang.Math;
import java.nio.FloatBuffer;;

public class CubeRobot {
	
    // Filenames for vertex and fragment shader source code
    private final static String VSHADER_FN = "resources/cube_vertex_shader.glsl";
    private final static String FSHADER_FN = "resources/cube_fragment_shader.glsl";

	private final static String MSHADER_FN = "resources/fresnel_fragment_shader.glsl";
    // Reference to the skybox of the scene
    public SkyBox skybox;
    
    // Components of this CubeRobot
    
    // Component 1 : Body
	private Mesh body_mesh;				// Mesh of the body
	private ShaderProgram body_shader;	// Shader to colour the body mesh
	private Texture body_texture;		// Texture image to be used by the body shader
	private Matrix4f body_transform;	// Transformation matrix of the body object
	
	// TODO: Add Component 2: Right Arm
	private Mesh right_arm_mesh;				// Mesh of the body
	private ShaderProgram right_arm_shader;	// Shader to colour the body mesh
	private Texture right_arm_texture;		// Texture image to be used by the body shader
	private Matrix4f right_arm_transform;	// Transformation matrix of the body object

	// Left Arm
	private Mesh left_arm_mesh;				// Mesh of the body
	private ShaderProgram left_arm_shader;	// Shader to colour the body mesh
	private Texture left_arm_texture;		// Texture image to be used by the body shader
	private Matrix4f left_arm_transform;	// Transformation matrix of the body object

	// Right Leg
	private Mesh right_leg_mesh;				// Mesh of the body
	private ShaderProgram right_leg_shader;	// Shader to colour the body mesh
	private Texture right_leg_texture;		// Texture image to be used by the body shader
	private Matrix4f right_leg_transform;	// Transformation matrix of the body object

	// Left Leg
	private Mesh left_leg_mesh;				// Mesh of the body
	private ShaderProgram left_leg_shader;	// Shader to colour the body mesh
	private Texture left_leg_texture;		// Texture image to be used by the body shader
	private Matrix4f left_leg_transform;	// Transformation matrix of the body object

	// Head
	private Mesh head_mesh;				// Mesh of the body
	private ShaderProgram head_shader;	// Shader to colour the body mesh
	private Texture head_texture;		// Texture image to be used by the body shader
	private Matrix4f head_transform;	// Transformation matrix of the body object

	// Head
	private Mesh mirror;
	private ShaderProgram mirror_shader;
	private Texture mirror_texture;		// Texture image to be used by the body shader
	private Matrix4f mirror_transform;
	private Vector3f body_scale = new Vector3f(0.75f,1.5f, 0.75f);
	private Vector3f arm_scale = new Vector3f(0.25f, 1.25f, 0.25f);
	private Vector3f leg_scale = new Vector3f(0.2f, 1, 0.2f);
	private Vector3f head_scale = new Vector3f(0.35f,0.35f, 0.35f);
	private Vector3f mirror_scale = new Vector3f(10.0f,0.1f, 10.0f);


/**
 *  Constructor
 *  Initialize all the CubeRobot components
 */
	public CubeRobot() {
		// Create body node
		
		// Initialise Geometry
		body_mesh = new CubeMesh(); 
		
		// Initialise Shader
		body_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		// Tell vertex shader where it can find vertex positions. 3 is the dimensionality of vertex position
		// The prefix "oc_" means object coordinates
		body_shader.bindDataToShader("oc_position", body_mesh.vertex_handle, 3);
		// Tell vertex shader where it can find vertex normals. 3 is the dimensionality of vertex normals
		body_shader.bindDataToShader("oc_normal", body_mesh.normal_handle, 3);
		// Tell vertex shader where it can find texture coordinates. 2 is the dimensionality of texture coordinates
		body_shader.bindDataToShader("texcoord", body_mesh.tex_handle, 2);
		
		// Initialise texturing
		body_texture = new Texture(); 
		body_texture.load("resources/cubemap.png");
		
		// Set Transformation Matrix
		body_mesh.setScale(body_scale);
		body_transform = body_mesh.getTransform();

		
		// TODO: Create right arm node
		right_arm_mesh = new CubeMesh(body_mesh);
		right_arm_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		right_arm_shader.bindDataToShader("oc_position", right_arm_mesh.vertex_handle, 3);
		// Tell vertex shader where it can find vertex normals. 3 is the dimensionality of vertex normals
		right_arm_shader.bindDataToShader("oc_normal", right_arm_mesh.normal_handle, 3);
		// Tell vertex shader where it can find texture coordinates. 2 is the dimensionality of texture coordinates
		right_arm_shader.bindDataToShader("texcoord", right_arm_mesh.tex_handle, 2);

		// TODO: Initialise Texturing
		right_arm_texture = new Texture();
		right_arm_texture.load("resources/cubemap.png");

		// TODO: Build Right Arm's Transformation Matrix (rotate the right arm around its end)
		right_arm_mesh.setScale(arm_scale);
		right_arm_mesh.setRotate(new Vector3f(0.0f,1.0f,0.0f), new Vector3f(0.0f, 0.0f, 0.3f));
		right_arm_mesh.setTranslate(new Vector3f(1f, -0.25f, 0.125f));
		right_arm_transform = right_arm_mesh.getTransform();


		// TODO: Left Arm
		left_arm_mesh = new CubeMesh(body_mesh);
		left_arm_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		left_arm_shader.bindDataToShader("oc_position", left_arm_mesh.vertex_handle, 3);
		// Tell vertex shader where it can find vertex normals. 3 is the dimensionality of vertex normals
		left_arm_shader.bindDataToShader("oc_normal", left_arm_mesh.normal_handle, 3);
		// Tell vertex shader where it can find texture coordinates. 2 is the dimensionality of texture coordinates
		left_arm_shader.bindDataToShader("texcoord", left_arm_mesh.tex_handle, 2);

		left_arm_texture = new Texture();
		left_arm_texture.load("resources/cubemap.png");

		// TODO: Build Left Arm's Transformation Matrix (rotate the left arm around its end)
		left_arm_mesh.setScale(arm_scale);
		left_arm_mesh.setRotate(new Vector3f(0.0f,1.0f,0.0f), new Vector3f(0.0f, 0.0f, -0.3f));
		left_arm_mesh.setTranslate(new Vector3f(-1f, -0.25f, 0.125f));
		left_arm_transform = left_arm_mesh.getTransform();

		// TODO: Right Leg
		right_leg_mesh = new CubeMesh(body_mesh);
		right_leg_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		right_leg_shader.bindDataToShader("oc_position", right_leg_mesh.vertex_handle, 3);
		// Tell vertex shader where it can find vertex normals. 3 is the dimensionality of vertex normals
		right_leg_shader.bindDataToShader("oc_normal", right_leg_mesh.normal_handle, 3);
		// Tell vertex shader where it can find texture coordinates. 2 is the dimensionality of texture coordinates
		right_leg_shader.bindDataToShader("texcoord", right_leg_mesh.tex_handle, 2);

		right_leg_texture = new Texture();
		right_leg_texture.load("resources/cubemap.png");

		right_leg_mesh.setScale(leg_scale);
		right_leg_mesh.setTranslate(new Vector3f(0.5f, -2.5f, 0f));
		right_leg_transform = right_leg_mesh.getTransform();

		// TODO: Left Leg
		left_leg_mesh = new CubeMesh(body_mesh);
		left_leg_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		left_leg_shader.bindDataToShader("oc_position", left_leg_mesh.vertex_handle, 3);
		// Tell vertex shader where it can find vertex normals. 3 is the dimensionality of vertex normals
		left_leg_shader.bindDataToShader("oc_normal", left_leg_mesh.normal_handle, 3);
		// Tell vertex shader where it can find texture coordinates. 2 is the dimensionality of texture coordinates
		left_leg_shader.bindDataToShader("texcoord", left_leg_mesh.tex_handle, 2);

		left_leg_texture = new Texture();
		left_leg_texture.load("resources/cubemap.png");

		left_leg_mesh.setScale(leg_scale);
		left_leg_mesh.setTranslate(new Vector3f(-0.5f, -2.5f, 0f));
		left_leg_transform = left_leg_mesh.getTransform();


		head_mesh = new CubeMesh(body_mesh);
		// Initialise Shader
		head_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		// Tell vertex shader where it can find vertex positions. 3 is the dimensionality of vertex position
		// The prefix "oc_" means object coordinates
		head_shader.bindDataToShader("oc_position", head_mesh.vertex_handle, 3);
		// Tell vertex shader where it can find vertex normals. 3 is the dimensionality of vertex normals
		head_shader.bindDataToShader("oc_normal", head_mesh.normal_handle, 3);
		// Tell vertex shader where it can find texture coordinates. 2 is the dimensionality of texture coordinates
		head_shader.bindDataToShader("texcoord", head_mesh.tex_handle, 2);

		// Initialise texturing
		head_texture = new Texture();
		head_texture.load("resources/cubemap_head.png");

		// Set Transformation Matrix
		head_mesh.setScale(head_scale);
		head_mesh.setTranslate(new Vector3f(0,1.85f,0));
		head_transform = head_mesh.getTransform();

//		mirror = new CubeMesh();
//		mirror_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, MSHADER_FN), "colour");
//		mirror_shader.bindDataToShader("oc_position", mirror.vertex_handle, 3);
//		mirror_shader.bindDataToShader("oc_normal", mirror.normal_handle, 3);
//		mirror_shader.bindDataToShader("texcoord", mirror.tex_handle, 2);
//		mirror_texture = new Texture();
//		mirror.setScale(mirror_scale);
//		mirror.setTranslate(new Vector3f(0f, -4.0f, 0f));
//		mirror_transform = mirror.getTransform();
	}
	

	/**
	 * Updates the scene and then renders the CubeRobot
	 * @param camera - Camera to be used for rendering
	 * @param deltaTime		- Time taken to render this frame in seconds (= 0 when the application is paused)
	 * @param elapsedTime	- Time elapsed since the beginning of this program in millisecs
	 */
	public void render(Camera camera, float deltaTime, long elapsedTime) {
		
		// TODO: Animate Body. Translate the body as a function of time
		float bodyRotationSpeed = 0.5f; // Adjust this value to control the body rotation speed
		float bodyRotationAngle = bodyRotationSpeed * elapsedTime / 500.0f; // Convert elapsed time to seconds

		float armRotationSpeed = 0.5f; // Adjust this value to control the arm rotation speed
		float armRotationAngle = (float) (armRotationSpeed * (Math.sin(elapsedTime /500.0f) + 1.6)); // Convert elapsed time to seconds

		body_mesh.setRotate(new Vector3f(0,0,0), new Vector3f(0.0f, bodyRotationAngle, 0.0f));
		body_transform = body_mesh.getTransform();

		// TODO: Animate Arm. Rotate the left arm around its end as a function of time

		right_arm_mesh.setRotateAngle(new Vector3f(0.0f, 0.0f, armRotationAngle));
		right_arm_transform = right_arm_mesh.getTransform();

		left_arm_mesh.setRotateAngle(new Vector3f(0.0f, 0.0f, -armRotationAngle));
		left_arm_transform = left_arm_mesh.getTransform();

		right_leg_transform = right_leg_mesh.getTransform();
		left_leg_transform = left_leg_mesh.getTransform();
		head_transform = head_mesh.getTransform();

		renderMesh(camera, body_mesh, body_transform, body_shader, body_texture);
		renderMesh(camera, right_arm_mesh, right_arm_transform, right_arm_shader, right_arm_texture);
		renderMesh(camera, left_arm_mesh, left_arm_transform, left_arm_shader, left_arm_texture);
		renderMesh(camera, left_leg_mesh, left_leg_transform, left_leg_shader, left_leg_texture);
		renderMesh(camera, right_leg_mesh, right_leg_transform, right_leg_shader, right_leg_texture);
		renderMesh(camera, head_mesh, head_transform, head_shader, head_texture);
		//renderMesh(camera, mirror, mirror_transform, mirror_shader, mirror_texture);
		
	}
	
	/**
	 * Draw mesh from a camera perspective
	 * @param camera		- Camera to be used for rendering
	 * @param mesh			- mesh to render
	 * @param modelMatrix	- model transformation matrix of this mesh
	 * @param shader		- shader to colour this mesh
	 * @param texture		- texture image to be used by the shader
	 */
	public void renderMesh(Camera camera, Mesh mesh , Matrix4f modelMatrix, ShaderProgram shader, Texture texture) {
		// If shaders modified on the disc, reload them
		shader.reloadIfNeeded(); 
		shader.useProgram();

		// Step 2: Pass relevant data to the vertex shader
		
		// compute and upload MVP
		Matrix4f mvp_matrix = new Matrix4f(camera.getProjectionMatrix()).mul(camera.getViewMatrix()).mul(modelMatrix);
		shader.uploadMatrix4f(mvp_matrix, "mvp_matrix");
		
		// Upload Model Matrix and Camera Location to the shader for Phong Illumination
		shader.uploadMatrix4f(modelMatrix, "m_matrix");
		shader.uploadVector3f(camera.getCameraPosition(), "wc_camera_position");
		
		// Transformation by a nonorthogonal matrix does not preserve angles
		// Thus we need a separate transformation matrix for normals

		//TODO: Calculate normal transformation matrix
		Matrix3f normal_matrix = new Matrix3f(modelMatrix);
		normal_matrix.invert().transpose();

		shader.uploadMatrix3f(normal_matrix, "normal_matrix");
		
		// Step 3: Draw our VertexArray as triangles
		// Bind Textures
		texture.bindTexture();
		shader.bindTextureToShader("tex", 0);
		skybox.bindCubemap();
		shader.bindTextureToShader("skybox", 1);
		// draw
		glBindVertexArray(mesh.vertexArrayObj); // Bind the existing VertexArray object
		glDrawElements(GL_TRIANGLES, mesh.no_of_triangles, GL_UNSIGNED_INT, 0); // Draw it as triangles
		glBindVertexArray(0);             // Remove the binding
		
        // Unbind texture
		texture.unBindTexture();
		skybox.unBindCubemap();
	}
}
