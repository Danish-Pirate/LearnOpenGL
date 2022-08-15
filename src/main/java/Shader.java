import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import org.joml.*;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20C.glCreateShader;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Shader {
    private boolean beingUsed = false;
    private int shaderProgramID;
    private int vboID, vaoID, eboID;
    private String vertexSrc, fragmentSrc;

    public void init() {
        float[] vertices = {
                // Position             Color                   Tex coords
                0.5f,  0.5f, 0.0f,      0.0f, 1.0f, 0.0f,       1.0f, 1.0f,// top right 0
                0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f,       1.0f, 0.0f,// bottom right 1  Front quad
                0.0f, -0.5f, 0.0f,      0.0f, 0.0f, 1.0f,       0.0f, 0.0f,// bottom left 2
                0.0f,  0.5f, 0.0f,      0.5f, 0.0f, 0.5f,       0.0f, 1.0f,// top left 3

                0.5f,  0.5f, -0.5f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,// top right 4
                0.5f, -0.5f, -0.5f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,// bottom right 5  Back quad
                0.0f, -0.5f, -0.5f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,// bottom left 6
                0.0f,  0.5f, -0.5f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,// top left 7

                0.5f,  0.5f, -0.5f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,// top right 8
                0.5f, -0.5f, -0.5f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,// bottom right 9  Right quad *
                0.5f, -0.5f,  0.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,// bottom left 10 = bottom right FQ *
                0.5f,  0.5f,  0.0f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,// top left 11 = top right FQ *

                0.0f,  0.5f,  0.0f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,// top right 12 = top left FQ *
                0.0f, -0.5f,  0.0f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,// bottom right 13 = bottom left FQ  Left quad *
                0.0f, -0.5f, -0.5f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,// bottom left 14 = bottom left BQ *
                0.0f,  0.5f, -0.5f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,// top left 15 = top left BQ *

                0.5f,  0.5f, -0.5f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,// top right 16 = top right BQ *
                0.5f,  0.5f,  0.0f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,// bottom right 17 = top right FQ * Top quad
                0.0f,  0.5f,  0.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,// bottom left 18 = top left FQ *
                0.0f,  0.5f, -0.5f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,// top left 19 = top left BQ *

                0.5f, -0.5f, -0.5f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,// top right 20 = bottom right BQ *
                0.5f, -0.5f,  0.0f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,// bottom right 21 = bottom right FQ * Bottom quad
                0.0f, -0.5f,  0.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,// bottom left 22 = bottom left FQ *
                0.0f, -0.5f, -0.5f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,// top left 23 = bottom left BQ
        };

        int[] indices = {
                2, 1, 0,
                2, 0, 3, //Front quad

                6, 5, 4, // Back quad
                6, 4, 7,

                10, 9, 8, // Right quad
                10, 8, 11,

                14, 13, 12,
                14, 12, 15, // Left quad

                18, 17, 16, // Top quad
                18, 16, 19,

                22, 21, 20, // Bottom quad
                22, 20, 23,
        };

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        vertexSrc = null;
        fragmentSrc = null;
        try {
            vertexSrc = Files.readString(Path.of("src/main/resources/shaders/VertexShader.glsl"));
            fragmentSrc = Files.readString(Path.of("src/main/resources/shaders/FragmentShader.glsl"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }

    public void update() {
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
    }

    public void compile() {
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
    }

    public void use() {
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadTexture(String varName, int textureSlot) {
        use();
        glUniform1i(glGetUniformLocation(shaderProgramID, varName), textureSlot);
    }

    public void uploadMat4f(String varName, Matrix4f matrix) {
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        matrix.get(matBuffer);
        glUniformMatrix4fv(glGetUniformLocation(shaderProgramID, varName), false, matBuffer);
    }
}
