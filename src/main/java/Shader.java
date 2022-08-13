import org.lwjgl.opengl.GL11C;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20C.glCreateShader;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.stb.STBImage.*;

public class Shader {
    private int shaderProgramID;
    private int vboID, vaoID, eboID;

    public void init() {
        float[] vertices = {
                // Position             Color                   Tex coords
                 0.5f,  0.5f, 0.0f,     0.0f, 1.0f, 0.0f,       1.0f, 1.0f,// top right
                 0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f,       1.0f, 0.0f,// bottom right
                -0.5f, -0.5f, 0.0f,     0.0f, 0.0f, 1.0f,       0.0f, 0.0f,// bottom left
                -0.5f,  0.5f, 0.0f,     0.5f, 0.0f, 0.5f,       0.0f, 1.0f,// top left
        };

        int[] indices = {
                0, 1, 3,   // first triangle
                1, 2, 3    // second triangle
        };

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        String vertexSrc = null;
        String fragmentSrc = null;
        try {
            vertexSrc = Files.readString(Path.of("src/main/resources/shaders/VertexShader.glsl"));
            fragmentSrc = Files.readString(Path.of("src/main/resources/shaders/FragmentShader.glsl"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexSrc);
        glCompileShader(vertexShaderID);

        int fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentSrc);
        glCompileShader(fragmentShaderID);

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexShaderID);
        glAttachShader(shaderProgramID, fragmentShaderID);
        glLinkProgram(shaderProgramID);

        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);


        byte positionSize = 3;
        byte colorSize = 3;
        byte texCoordsSize = 2;
        int colorOffset = positionSize;
        int texCoordsOffset = colorOffset + colorSize;
        int stride = (positionSize + colorSize + texCoordsSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, stride, colorOffset * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, texCoordsSize, GL_FLOAT, false, stride, texCoordsOffset * Float.BYTES);
        glEnableVertexAttribArray(2);

        int texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        int[] width = {1};
        int[] height = {1};
        int[] channels = {1};
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load("src/main/resources/images/grass.jpg", width, height, channels, 0);

        if (image != null)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        else
            throw new IllegalStateException("Image could not be loaded");

        stbi_image_free(image);

        int varLocation = glGetUniformLocation(shaderProgramID, "ourTexture");
        glUniform1i(varLocation, 0);

        glUseProgram(shaderProgramID);
    }

    public void update() {
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }
}
