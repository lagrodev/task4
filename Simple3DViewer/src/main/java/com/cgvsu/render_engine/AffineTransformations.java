package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class AffineTransformations {

    public static Matrix4f rotate(float alpha, float beta, float gamma) {
        return rotateIntoMatrix(alpha, beta, gamma);
    }

    /**
     * Поворот, за основу берем матрицы
     *
     * @param alpha угол вокруг x
     * @param beta  угол вокруг y
     * @param gamma угол вокруг z
     * @return {@code Matrix4f} матрица поворота
     */
    private static Matrix4f rotateIntoMatrix(float alpha, float beta, float gamma) {
        // вокруг x
        float[] rotateX = new float[]{
                1, 0, 0, 0,
                0, (float) cos(Math.toRadians(alpha)), (float) sin(Math.toRadians(alpha)), 0,
                0, (float) -sin(Math.toRadians(alpha)), (float) cos(Math.toRadians(alpha)), 0,
                0, 0, 0, 1
        };
        // вокруг y
        float[] rotateY = new float[]{
                (float) cos(Math.toRadians(beta)), 0, (float) sin(Math.toRadians(beta)), 0,
                0, 1, 0, 0,
                (float) -sin(Math.toRadians(beta)), 0, (float) cos(Math.toRadians(beta)), 0,
                0, 0, 0, 1
        };
        // вокруг z
        float[] rotateZ = new float[]{
                (float) cos(Math.toRadians(gamma)), (float) sin(Math.toRadians(gamma)), 0, 0,
                (float) -sin(Math.toRadians(gamma)), (float) cos(Math.toRadians(gamma)), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };

        Matrix4f rotateAboutX = new Matrix4f(rotateX);
        Matrix4f rotateAboutY = new Matrix4f(rotateY);
        Matrix4f rotateAboutZ = new Matrix4f(rotateZ);

        // сначала крутим вокруг x, потом y, потом z (да, в реале умножаем сначала Z, потом ыгрик, потом х (а) - плохо)
        // вот нихрена не фАКТ
        rotateAboutZ.multiply(rotateAboutY);
        rotateAboutZ.multiply(rotateAboutX);

        return rotateAboutZ;
    }


    /**
     * Аффинные преобразования - увеличение/уменьшение объекта
     *
     * @param vector3f вектор переноса
     * @return {@code Matrix4f} - матрица переноса
     */
    public static Matrix4f translate(Vector3f vector3f) {
        return calculateTranslate(new Vector4f(vector3f.getX(), vector3f.getY(), vector3f.getZ(), 1));
    }

    private static Matrix4f calculateTranslate(Vector4f vector) {
        Matrix4f res = new Matrix4f(1);
        for (int i = 0; i < res.getElements().length - 1; i++) {
            res.setElement(i, 3, vector.getNum(i));
        }
        return res;
    }


    /**
     * Аффинные преобразования - увеличение/уменьшение объекта
     *
     * @param scaleX растяжение по x
     * @param scaleY растяжение по y
     * @param scaleZ растяжение по z
     * @return {@code Matrix4f} - матрица растяжения (по факту просто по главной диагонали будут стоять не 1, а растяжение наше)
     */
    public static Matrix4f scale(float scaleX, float scaleY, float scaleZ) {
        float[] scale = new float[]{scaleX, scaleY, scaleZ};
        return calculateScale(scale);
    }


    private static Matrix4f calculateScale(float[] scale) {
        Matrix4f result = new Matrix4f(1);
        for (int i = 0; i < scale.length; i++) {
            result.setElement(i, i, scale[i]);
        }
        return result;
    }
}