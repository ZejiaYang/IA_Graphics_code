#version 140

in vec3 wc_frag_normal;        	// fragment normal in world coordinates (wc_)
in vec2 frag_texcoord;			// texture UV coordinates
in vec3 wc_frag_pos;			// fragment position in world coordinates

out vec3 color;			        // pixel colour

uniform sampler2D tex;  		  // 2D texture sampler
uniform samplerCube skybox;		  // Cubemap texture used for reflections
uniform vec3 wc_camera_position;  // Position of the camera in world coordinates

uniform vec3 light_position = vec3(-1,3,-1);
uniform vec3 light_color = vec3(0.941, 0.968, 1);
uniform vec3 ambient_light = vec3(0.16, 0, 0);
uniform vec3 diffuse_color = vec3(1, 0, 0);
uniform vec3 specular_color = vec3(1, 1, 1);
uniform float diffuse_constant = float(0.4);
uniform float specular_constant = float(0.75);
uniform float environment_constant = float(0.2);
uniform float roughness = 32;

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
	// TODO: Calculate colour using Phong illumination model
	// linear_color = ambient + diffuse + specular
	// TODO: Sample the texture and replace diffuse surface colour (C_diff) with texel value
    vec4 texColor = texture(tex, frag_texcoord);
    vec3 diffuse_color_f = texColor.rgb;
    vec3 ambient = ambient_light * diffuse_color_f; // Calculate ambient term

    vec3 light_direction = normalize(light_position - wc_frag_pos);
    float diffuse_factor = max(dot(wc_frag_normal, light_direction), 0.0);
    vec3 diffuse = diffuse_color_f * diffuse_constant * diffuse_factor * light_color; // Calculate diffuse term

    vec3 reflected_ray = reflect(-light_direction, wc_frag_normal);
    vec3 view_direction = normalize(wc_camera_position - wc_frag_pos);
    float specular_factor = pow(max(dot(reflected_ray, view_direction), 0.0), roughness);
    vec3 specular = specular_color * specular_constant * specular_factor * light_color; // Calculate specular term

    vec3 reflected_d = reflect(-view_direction, wc_frag_normal);
    vec3 environment_color = texture(skybox, reflected_d).rgb;
    vec3 environment = environment_color * environment_constant;

    linear_color = ambient + diffuse + specular + environment; // Sum up ambient, diffuse, and specular terms

	color = tonemap(linear_color);
}

