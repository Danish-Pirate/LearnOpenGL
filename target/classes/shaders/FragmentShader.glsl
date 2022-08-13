#version 330

in vec3 fColor;
in vec2 TexCoords;

out vec4 color;

uniform sampler2D ourTexture;

void main() {
    color = texture(ourTexture, TexCoords);
}
