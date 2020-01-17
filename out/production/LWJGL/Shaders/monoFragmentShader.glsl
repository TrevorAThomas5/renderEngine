#version 400 core

in vec4 finalColor;
in vec2 finalTextureCoords;

out vec4 out_Color;

uniform sampler2D textureSampler;

/**
 * Runs the fragment shader.
 */
void main(void)
{
    vec4 textureColor = texture(textureSampler, finalTextureCoords);

    out_Color = finalColor * textureColor;
}