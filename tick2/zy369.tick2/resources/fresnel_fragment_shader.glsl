#version 140

in vec3 wc_frag_normal;        	// fragment normal in world coordinates (wc_)
in vec2 frag_texcoord;			// texture UV coordinates
in vec3 wc_frag_pos;			// fragment position in world coordinates

out vec3 color;			        // pixel colour

uniform sampler2D tex;  		  // 2D texture sampler
uniform samplerCube skybox;		  // Cubemap texture used for reflections
uniform vec3 wc_camera_position;  // Position of the camera in world coordinates

uniform float environment_constant = float(0.2);
uniform float fresnel_constant = float(1.5);
uniform vec3 light_position = vec3(-1,3,-1);
// Combined tone mapping and display encoding
vec3 tonemap(vec3 linearRGB)
{
    float L_white = 0.7; // Controls the brightness of the image

    float inverseGamma = 1./2.2;
    return pow(linearRGB/L_white, vec3(inverseGamma)); // Display encoding - a gamma
}



void main()
{
    vec3 linear_color = vec3(0, 0, 0);

    vec3 light_direction = normalize(light_position - wc_frag_pos);
    vec3 view_direction = normalize(wc_camera_position - wc_frag_pos);

    vec3 reflected = normalize(reflect(-view_direction, wc_frag_normal));
    vec3 reflect_envir_color = texture(skybox, reflected).rgb;

    vec3 refracted = normalize(refract(-view_direction, wc_frag_normal, 1/fresnel_constant));
    vec3 refracted_envir_color = texture(skybox, refracted).rgb;

    float anglei = dot(reflected,wc_frag_normal);
    float anglet = -dot(refracted,wc_frag_normal);

    float Rs = pow(abs(anglei - anglet * fresnel_constant) / abs(anglei + anglet * fresnel_constant),2);
    float Rp = pow(abs(anglet - anglei * fresnel_constant) / abs(anglet + anglei * fresnel_constant),2);

    float Fr = (Rs + Rp)/2;
    float Ft = 1 - Fr;

    vec3 reflected_color = reflect_envir_color * Fr;
    vec3 refracted_color = refracted_envir_color * Ft;

    linear_color = reflected_color + refracted_color; // Sum up ambient, diffuse, and specular terms

    color = tonemap(linear_color);
}
