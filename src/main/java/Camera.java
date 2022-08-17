import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelMatrix;
    private final Matrix4f viewMatrix;
    private final Vector3f cameraPosition;
    private final Vector3f cameraFront;
    private final Vector3f cameraUp;
    private final Vector3f cameraRight;

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
        cameraRight = new Vector3f(1.0f, 0.0f, 0.0f);
    }

    private void setProjection() {
        projectionMatrix.identity();
        projectionMatrix.perspective((float) Math.toRadians(45), Window.getWidth() / Window.getHeight(), 0.1f, 10000.0f);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraDirection = new Vector3f();
        cameraRight.cross(cameraFront, cameraUp);
        cameraPosition.add(cameraFront, cameraDirection);

        viewMatrix.identity();
        viewMatrix.lookAt(cameraPosition, cameraDirection, cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public void processPlayerInput(float dt) {
        float cameraRotateSpeed = 10.0f * dt;
        float cameraMoveSpeed = 2000.0f * dt;

        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            cameraPosition.add(cameraFront.mul(cameraMoveSpeed, new Vector3f()));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            cameraPosition.sub(cameraFront.mul(cameraMoveSpeed, new Vector3f()));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            cameraPosition.add(cameraRight.mul(cameraMoveSpeed, new Vector3f()));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            cameraPosition.sub(cameraRight.mul(cameraMoveSpeed, new Vector3f()));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraPosition.sub(cameraUp.mul(cameraMoveSpeed, new Vector3f()));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraPosition.add(cameraUp.mul(cameraMoveSpeed, new Vector3f()));
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {

        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {

        }
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {

        }
        if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {

        }
    }
}
