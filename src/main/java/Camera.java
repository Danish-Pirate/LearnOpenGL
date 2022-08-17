import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Vector;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f modelMatrix;
    private Matrix4f viewMatrix;
    private Vector3f cameraFront;
    private Vector3f cameraUp;
    private Vector3f cameraPosition;
    private Vector3f cameraRotation;

    public Camera() {
        projectionMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();

        setProjection();

        modelMatrix.translate(0.0f, 0.0f, -500.0f);
        modelMatrix.scale(500.0f);

        cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        cameraRotation = new Vector3f(0, 0,0);

    }

    private void setProjection() {
        projectionMatrix.identity();
        projectionMatrix.perspective((float) Math.toRadians(45), Window.getWidth() / Window.getHeight(), 0.1f, 10000.0f);
    }

    public Matrix4f getViewMatrix() {


        viewMatrix.identity();
        viewMatrix.rotate((float)Math.toRadians(cameraRotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(cameraRotation.y), new Vector3f(0, 1, 0));
        viewMatrix.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        //viewMatrix.lookAt(cameraPosition, cameraPosition.add(cameraFront, new Vector3f()), cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
    public void rotateView(Vector3f rotationOffset) {
        cameraRotation.add(rotationOffset.x, rotationOffset.y, rotationOffset.z);
    }

    public void moveView(Vector3f positionOffset) {
        cameraPosition.add(positionOffset.x, positionOffset.y, positionOffset.z);
    }

    public void processPlayerInput(float dt) {
        float cameraRotateSpeed = 10.0f * dt;
        float cameraMoveSpeed = 200.0f * dt;

        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            moveView(new Vector3f(cameraMoveSpeed, 0.0f, 0.0f));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            moveView(new Vector3f(-cameraMoveSpeed, 0.0f, 0.0f));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            moveView(new Vector3f(0.0f, 0.0f, cameraMoveSpeed));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            moveView(new Vector3f(0.0f, 0.0f, -cameraMoveSpeed));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            moveView(new Vector3f(0.0f, -cameraMoveSpeed, 0.0f));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            moveView(new Vector3f(0.0f, cameraMoveSpeed, 0.0f));
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            rotateView(new Vector3f(cameraRotateSpeed, 0.0f, 0.0f));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            rotateView(new Vector3f(-cameraRotateSpeed, 0.0f, 0.0f));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            rotateView(new Vector3f(0.0f, cameraRotateSpeed, 0.0f));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            rotateView(new Vector3f(0.0f, -cameraRotateSpeed, 0.0f));
        }
    }
}
