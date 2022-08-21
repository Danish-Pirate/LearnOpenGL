import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private int texID;

    public Texture(String filepath) {
        this.filepath = filepath;

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


        // Loads the texture
        int[] width = {1};
        int[] height = {1};
        int[] channels = {1};
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image != null) {
            if (channels[0] == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0],
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels[0] == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0],
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                throw new IllegalStateException("Unknown number of channels " + this.filepath);
            }
        } else {
            throw new IllegalStateException("Could not load image " + this.filepath);
        }

        stbi_image_free(image);
    }
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
