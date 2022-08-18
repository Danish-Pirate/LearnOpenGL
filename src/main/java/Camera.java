import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelMatrix;
    private final Matrix4f viewMatrix;


    private final Vector3f cameraPosition;
    private Vector3f cameraFront;
    private final Vector3f cameraUp;

    private float yaw;
    private float pitch;
    private boolean firstTimeMoved = true;

    public Camera() {
        projectionMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();

        setProjection();

        modelMatrix.translate(0.0f, 0.0f, -500.0f);
        modelMatrix.scale(50.0f);

        cameraPosition = new Vector3f(0.0f, 0.0f, 3.0f);
        cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        yaw = -90.0f;
        pitch = 0.0f;
    }

    private void setProjection() {
        projectionMatrix.identity();
        projectionMatrix.perspective((float) Math.toRadians(45), Window.getWidth() / Window.getHeight(), 0.1f, 10000.0f);
    }

    public Matrix4f getViewMatrix() {

        viewMatrix.identity();
        viewMatrix.lookAt(cameraPosition, cameraPosition.add(cameraFront, new Vector3f()), cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public void processPlayerInput(float dt) {
        float cameraMoveSpeed = 200.0f * dt;

        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            cameraPosition.add(cameraFront.mul(cameraMoveSpeed, new Vector3f()));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            cameraPosition.sub(cameraFront.mul(cameraMoveSpeed, new Vector3f()));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            cameraPosition.add(cameraFront.cross(cameraUp, new Vector3f()).mul(cameraMoveSpeed));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            cameraPosition.sub(cameraFront.cross(cameraUp, new Vector3f()).mul(cameraMoveSpeed));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraPosition.sub(cameraUp.mul(cameraMoveSpeed, new Vector3f()));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraPosition.add(cameraUp.mul(cameraMoveSpeed, new Vector3f()));
        }

        float mouseSensitivity = 0.1f;
        float xOffset = MouseListener.getDx();
        float yOffset = MouseListener.getDy();
        xOffset *= mouseSensitivity;
        yOffset *= mouseSensitivity;
        yaw += xOffset;
        pitch += yOffset;
        MouseListener.endFrame();
        if (pitch > 89.0f)
            pitch = 89.0f;
        if (pitch < -89.0f)
            pitch = -89.0f;
        Vector3f front = new Vector3f();
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cameraFront.set(front).normalize();
        }
}
