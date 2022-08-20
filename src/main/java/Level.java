import static org.lwjgl.opengl.GL13.*;

public class Level {
    private Shader shader;
    private Camera camera;

    public Level() {
        shader = new Shader();
    }

    public void init() {
        shader.init();
        shader.compile();

        Texture t1 = new Texture("src/main/resources/images/grass.png");
        glActiveTexture(GL_TEXTURE0);

        shader.uploadTexture("Texture1", 1);
        shader.uploadTexture("Texture2", 2);

        camera = new Camera();
    }

    public void update(float dt) {
        shader.update(dt);

        camera.processPlayerInput(dt);
        shader.uploadMat4f("Projection", camera.getProjectionMatrix());
        shader.uploadMat4f("View", camera.getViewMatrix());
        shader.uploadMat4f("Model", camera.getModelMatrix());
    }
}
