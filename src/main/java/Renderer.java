import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private List<Mesh> meshes;

    public Renderer() {
        meshes = new ArrayList<>();
        meshes.add(new Mesh());
    }

    public void render() {
        for (Mesh mesh : meshes) {
            mesh.render(0);
        }
    }
}
