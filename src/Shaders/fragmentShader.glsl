#version 400 core

in vec2 pass_textureCoords;
in vec3 pass_normal;
in vec3 fragPosition;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightPosition;

const vec3 lightDirection = normalize(vec3(0.4, -1, 0.8));

/**
 * Runs the fragment shader. Executes once for each pixel.
 */
void main(void)
{
    // texture method returns color of pixel based on the texture and
    // its coords via uv system
    vec4 color = texture(textureSampler, pass_textureCoords);

    out_Color = color; //* brightness;
}