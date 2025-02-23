package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;
import com.cgvsu.model.Model;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void mainRender(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final double width,
            final double height) {
        Vector3f[] transformedVertices =
                transformAllVertices(mesh,
                        calculateModelViewProjectionMatrix(camera, mesh.getTranslate(), mesh.getRotate(), mesh.getScale()));


        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygons.get(polygonInd).getVertexIndices().size();

            ArrayList<Vector2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3f vector3f = new Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());
                Vector2f resultPoint = vertexToPoint(calculateModelViewProjectionMatrix(
                        camera, mesh.getTranslate(), mesh.getRotate(), mesh.getScale())
                        .multiply3(new Vector4f(vector3f)), (int) width, (int) height);
                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());
        }
    }

    private static Vector3f[] transformAllVertices(Model model, Matrix4f mvpMatrix) {
        Vector3f[] transformedVertices = new Vector3f[model.getVertices().size()];
        for (int i = 0; i < model.getVertices().size(); i++) {
            transformedVertices[i] = mvpMatrix.multiply3(new Vector4f(model.getVertices().get(i)));
        }
        return transformedVertices;
    }
}