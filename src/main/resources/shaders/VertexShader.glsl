#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aColor;
layout(location = 2) in vec2 aTexCoords;
layout(location = 3) in float aTexId;

out vec3 fColor;
out vec2 TexCoords;

uniform mat4 Projection;
uniform mat4 View;
uniform mat4 Model;

void main() {
    TexCoords = aTexCoords;
    fColor = aColor;
    gl_Position = Projection * View *  Model * vec4(aPos, 1.0);
}

