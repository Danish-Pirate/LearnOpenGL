import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Level {
    private Shader shader;

    public Level() {
        shader = new Shader();
    }

    public void init() {
        shader.init();
        shader.compile();

        Texture t1 = new Texture("src/main/resources/images/grass.png", 1);
        Texture t2 = new Texture("src/main/resources/images/goose.jpg", 2);

        shader.uploadTexture("Texture1", 1);
        shader.uploadTexture("Texture2", 2);

        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f viewMatrix = new Matrix4f();

        projectionMatrix.identity();
        projectionMatrix.perspective(1.5708f, Window.getWidth() / Window.getHeight(), 0.0f, 100.0f);

        Vector3f position = new Vector3f(0.0f, 0.0f, 200.0f);
        Vector3f cameraFront = new Vector3f(position.x, position.y, -1.0f);
        Vector3f cameraUp = new Vector3f(position.x, 1.0f, position.y);
        viewMatrix.identity();
        viewMatrix.lookAt(position, cameraFront, cameraUp);

        shader.uploadMat4f("Projection", projectionMatrix);
        shader.uploadMat4f("View", viewMatrix);
    }

    public void update() {
        shader.update();
    }
}
