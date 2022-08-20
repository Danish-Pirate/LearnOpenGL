#version 330

in vec3 fColor;
in vec2 TexCoords;
in float fTexId;

out vec4 FragColor;

uniform sampler2D Texture0;
uniform sampler2D Texture1;
uniform sampler2D Texture2;
uniform sampler2D Texture3;

void main() {
    if (fTexId == 1.0) {
        FragColor = texture(Texture1, TexCoords);
    }
    else if (fTexId == 2.0) {
        FragColor = texture(Texture2, TexCoords);
    }
    else if (fTexId == 3.0) {
        FragColor = texture(Texture3, TexCoords);
    }
    else {
        FragColor = vec4(1.0f, 0.0f, 1.0f, 1.0f);
    }
}
