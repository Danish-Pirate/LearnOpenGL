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
    }

    public void update() {
        shader.update();
    }
}
