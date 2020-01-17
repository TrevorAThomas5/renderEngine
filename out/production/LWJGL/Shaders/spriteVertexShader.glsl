#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;

out vec2 pass_textureCoords;

void main()
{
    pass_textureCoords = textureCoords;
    gl_Position = vec4(position.x, -position.y, position.z, 1);
}
