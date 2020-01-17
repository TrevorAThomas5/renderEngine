#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;

out vec4 worldPosition;
out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

/**
 * Runs the vertex shader
 */
void main(void)
{
    worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    pass_textureCoords = textureCoords;
}
