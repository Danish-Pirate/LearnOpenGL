import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20C.glCreateShader;

public class Shader {
    private boolean beingUsed = false;
    private int shaderProgramID;
    private String vertexSrc, fragmentSrc;

    public void init() {
        vertexSrc = null;
        fragmentSrc = null;
        try {
            vertexSrc = Files.readString(Path.of("src/main/resources/shaders/VertexShader.glsl"));
            fragmentSrc = Files.readString(Path.of("src/main/resources/shaders/FragmentShader.glsl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(float dt) {

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
