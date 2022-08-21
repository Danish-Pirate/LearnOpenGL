public class Scene {
    private Shader shader;
    private Camera camera;
    private Renderer renderer;

    public Scene() {
        shader = new Shader();
        renderer = new Renderer();
    }

    public void init() {
        shader.init();
        shader.compile();

        shader.uploadTexture("Texture1", 1);
        shader.uploadTexture("Texture2", 2);
        shader.uploadTexture("Texture3", 3);

        camera = new Camera();
    }

    public void update(float dt) {
        shader.update(dt);
        renderer.render();
        camera.processPlayerInput(dt);
        shader.uploadMat4f("Projection", camera.getProjectionMatrix());
        shader.uploadMat4f("View", camera.getViewMatrix());
        shader.uploadMat4f("Model", camera.getModelMatrix());
    }
}
