#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normal;

out vec2 pass_textureCoords;
out vec3 pass_normal;
out vec3 fragPosition;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

/**
 * Runs the vertex shader. Exexcutes once for each vertex.
 */
void main(void)
{
    fragPosition = vec3(transformationMatrix * vec4(position, 1.0));
    pass_normal = normal;

    // pass the UVs to the fragment shader
    pass_textureCoords = textureCoords;

    gl_Position = projectionMatrix * viewMatrix * vec4(fragPosition, 1.0);
}
