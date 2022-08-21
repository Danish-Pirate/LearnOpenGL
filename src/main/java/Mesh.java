import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    private final float[] vertices;
    private final float[] indices;

    private final byte positionOffset = 0;
    private final byte positionSize = 3;
    private final byte colorSize = 3;
    private final byte texCoordsSize = 2;
    private final byte TEX_ID_SIZE = 1;

    private final int colorOffset = positionSize;
    private final int texCoordsOffset = colorOffset + colorSize;
    private final int TEX_ID_OFFSET = texCoordsOffset + texCoordsSize;
    private final int stride = (positionSize + colorSize + texCoordsSize + TEX_ID_SIZE) * Float.BYTES;

    public Mesh() {
        vertices = new float[]{
                // Position             Color                  Tex coords
                0.5f,  0.5f, 0.0f,      0.0f, 1.0f, 0.0f,      1.0f, 1.0f,      1,// top right 0
                0.0f, -0.5f, 0.0f,      0.0f, 0.0f, 1.0f,      0.0f, 0.0f,      1,// bottom left 1
                0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f,      1.0f, 0.0f,      1,// bottom right 2  Front quad
                0.0f,  0.5f, 0.0f,      0.5f, 0.0f, 0.5f,      0.0f, 1.0f,      1,// top left 3

                0.5f,  0.5f, -0.5f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,      1,// top right 4
                0.0f, -0.5f, -0.5f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,      1,// bottom left 5
                0.5f, -0.5f, -0.5f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,      1,// bottom right 6  Back quad
                0.0f,  0.5f, -0.5f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,      1,// top left 7

                0.5f,  0.5f, -0.5f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,      1,// top right 8
                0.5f, -0.5f,  0.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,      1,// bottom left 9 = bottom right FQ *
                0.5f, -0.5f, -0.5f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,      1,// bottom right 10  Right quad *
                0.5f,  0.5f,  0.0f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,      1,// top left 11 = top right FQ *

                0.0f,  0.5f,  0.0f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,      1,// top right 12 = top left FQ *
                0.0f, -0.5f, -0.5f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,      1,// bottom left 13 = bottom left BQ *
                0.0f, -0.5f,  0.0f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,      1,// bottom right 14 = bottom left FQ  Left quad *
                0.0f,  0.5f, -0.5f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,      1,// top left 15 = top left BQ *

                0.5f,  0.5f, -0.5f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,      3,// top right 16 = top right BQ *
                0.0f,  0.5f,  0.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,      3,// bottom left 17 = top left FQ *
                0.5f,  0.5f,  0.0f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,      3,// bottom right 18 = top right FQ * Top quad
                0.0f,  0.5f, -0.5f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,      3,// top left 19 = top left BQ *

                0.5f, -0.5f, -0.5f,     0.0f, 1.0f, 0.0f,      1.0f, 1.0f,      2,// top right 20 = bottom right BQ *
                0.0f, -0.5f,  0.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f,      2,// bottom left 21 = bottom left FQ *
                0.5f, -0.5f,  0.0f,     1.0f, 0.0f, 0.0f,      1.0f, 0.0f,      2,// bottom right 22 = bottom right FQ * Bottom quad
                0.0f, -0.5f, -0.5f,     0.5f, 0.0f, 0.5f,      0.0f, 1.0f,      2,// top left 23 = bottom left BQ
        };
        indices = new float[]{
                0, 1, 2,
                3, 1, 0, //Front quad

                6, 5, 4, // Back quad
                4, 5, 7,

                8, 9, 10, // Right quad
                11, 9, 8,

                12, 13, 14,
                15, 13, 12, // Left quad

                16, 17, 18, // Top quad
                19, 17, 16,

                22, 21, 20, // Bottom quad
                20, 21, 23,
        };
        // Put the vertices into a buffer and flip them so OpenGL can use them.
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();
        System.out.println(glGenVertexArrays());
        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, stride, positionOffset * Float.BYTES);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, stride, colorOffset * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, texCoordsSize, GL_FLOAT, false, stride, texCoordsOffset * Float.BYTES);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, stride, TEX_ID_OFFSET * Float.BYTES);
        glEnableVertexAttribArray(3);

        glActiveTexture(GL_TEXTURE1);
        Texture t1 = new Texture("src/main/resources/images/grass.png");
        glActiveTexture(GL_TEXTURE2);
        Texture t2 = new Texture("src/main/resources/images/dirt.jpg");
        glActiveTexture(GL_TEXTURE3);
        Texture t3 = new Texture("src/main/resources/images/grass_top.jpg");
    }
    public void render(float dt) {

        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
    }
}
