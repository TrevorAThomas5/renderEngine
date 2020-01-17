#version 400 core
#extension GL_EXT_geometry_shader4 : enable

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in vec4 worldPosition[];
in vec2 pass_textureCoords[];

out vec4 finalColor;
out vec2 finalTextureCoords;

uniform vec3 cubeColor;

const vec3 lightDirection = normalize(vec3(-1, -0.5, 1));

/**
 * For each triangle, calculate color based on normals direction and light direction.
 */
void main()
{
    // calculate vector for first edge of triangle
    vec3 edge1 = vec3(worldPosition[1].x - worldPosition[0].x,
            worldPosition[1].y - worldPosition[0].y,
            worldPosition[1].z - worldPosition[0].z);

    // calculate vector for second edge of tringle
    vec3 edge2 = vec3(worldPosition[2].x - worldPosition[0].x,
            worldPosition[2].y - worldPosition[0].y,
            worldPosition[2].z - worldPosition[0].z);

    // normal = first edge cross product second edge
    vec3 normal = normalize(cross(edge1, edge2));

    // compare normal to light direction
    float diff = max(dot(normal, lightDirection), 0.15);

    // the greater the angle between the normal and light direction, the darker the triangle
    vec4 color = diff * vec4(cubeColor, 1);

    // retain the same gl_Postion for each vertex
    gl_Position = gl_PositionIn[0];
    finalColor = color;
    finalTextureCoords = pass_textureCoords[0];
    EmitVertex();

    gl_Position = gl_PositionIn[1];
    finalColor = color;
    finalTextureCoords = pass_textureCoords[1];
    EmitVertex();

    gl_Position = gl_PositionIn[2];
    finalColor = color;
    finalTextureCoords = pass_textureCoords[2];
    EmitVertex();
    EndPrimitive();
}
