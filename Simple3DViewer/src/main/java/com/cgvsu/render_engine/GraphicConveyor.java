package com.cgvsu.render_engine;


import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import javafx.beans.NamedArg;

import static java.lang.Math.tan;

public class GraphicConveyor {

    /**
     * Аффинные преобразования.
     * @return {@code Matrix4f} - матрица из локальной системы координат в мировую
     */
    public static Matrix4f translateRotateScale(@NamedArg("translate") Vector3f translate, @NamedArg("rotate") Vector3f rotate, @NamedArg("scale") Vector3f scale) {
        Matrix4f t = AffineTransformations.translate(new Vector3f(translate.getX(), translate.getY(), translate.getZ()));
        Matrix4f r = AffineTransformations.rotate(rotate.getX(), rotate.getY(), rotate.getZ());
        Matrix4f s = AffineTransformations.scale(scale.getX(), scale.getY(), scale.getZ());
        return t.multiplyNew(r.multiplyNew(s));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        Matrix4f lookat = lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
        lookat.transposition();
        return lookat;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultX;
        Vector3f resultY;
        Vector3f resultZ = new Vector3f();

        resultZ.sub(target, eye);
        resultX = up.cross(resultZ);
        resultY = resultZ.cross(resultX);

        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();

        float[] matrix = new float[]{
                resultX.getX(), resultY.getX(), resultZ.getX(), 0,
                resultX.getY(), resultY.getY(), resultZ.getY(), 0,
                resultX.getZ(), resultY.getZ(), resultZ.getZ(), 0,
                -resultX.dot(eye), -resultY.dot(eye), -resultZ.dot(eye), 1};

        return new Matrix4f(matrix);
    }

    /**
     * Матрица перспективы. я хз что с ней делать, вроде как, ее косенко писал, писал и дописал нормально... но она не такая же, как в методичке, но всем работает... делаем вывод: или я обосрался, или чет да
     *
     * @param fov         угол обзора
     * @param aspectRatio соотношение сторон экрана
     * @param nearPlane   ближний план
     * @param farPlane    дальний план
     * @return {@code Matrix4f} - матрица перспективы.
     */
    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        float tangentMinusOnDegree = (float) (1.0F / (tan(fov * 0.5F))); // fov задается как половина от всего угла
        float[] res = {
                tangentMinusOnDegree / aspectRatio, 0, 0, 0,
                0, tangentMinusOnDegree, 0, 0,
                0, 0, (farPlane + nearPlane) / (farPlane - nearPlane), 1.0F,
                0, 0, 2 * (nearPlane * farPlane) / (nearPlane - farPlane), 0
        };
        Matrix4f matrix4f = new Matrix4f(res);
        matrix4f.transposition();
        return matrix4f;
    }

    public static Matrix4f calculateModelViewProjectionMatrix(Camera camera, Vector3f translate, Vector3f rotate, Vector3f scale) {
        Matrix4f modelMatrix = GraphicConveyor.translateRotateScale(translate, rotate, scale);
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();


        Matrix4f mvpMatrix = new Matrix4f(projectionMatrix);
        mvpMatrix.multiply(viewMatrix);
        mvpMatrix.multiply(modelMatrix);
        return mvpMatrix;
    }

    /**
     * Преобразует вершину в экранные координаты.
     *
     * @param vertex Вершина в пространстве камеры.
     * @param width  Ширина экрана.
     * @param height Высота экрана.
     * @return Точка на экране.
     */
    public static Vector2f vertexToPoint(Vector3f vertex, int width, int height) {
        float x = (vertex.getX() + 1) * 0.5f * width;
        float y = (1 - (vertex.getY() + 1) * 0.5f) * height;
        return new Vector2f(x, y);
    }
}
