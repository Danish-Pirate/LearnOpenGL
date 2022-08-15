#version 330

in vec3 fColor;
in vec2 TexCoords;

out vec4 FragColor;

uniform sampler2D Texture0;
uniform sampler2D Texture1;
uniform sampler2D Texture2;

void main() {
    FragColor = mix(texture(Texture1, TexCoords), texture(Texture2, TexCoords), 0.2);
    //FragColor = vec4(1.0f, 0.0f, 1.0f, 1.0f);
}
