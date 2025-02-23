package com.cgvsu.model;
import com.cgvsu.math.*;

import java.util.*;

public class Model {
    private final Vector3f rotate = new Vector3f(0.0f, 0.0f, 0.0f);
    private final Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    private final Vector3f translate = new Vector3f(0.0f, 0.0f, 0.0f);
    public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    public ArrayList<Vector3f> originalVertices = new ArrayList<Vector3f>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<Vector2f>();

    public ArrayList<Vector3f> getNormals() {
        return normals;
    }

    public void setOriginalVertices(ArrayList<Vector3f> originalVertices) {
        this.originalVertices = originalVertices;
    }

    public ArrayList<Vector3f> getOriginalVertices() {
        return originalVertices;
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(ArrayList<Polygon> polygons) {
        this.polygons = polygons;
    }

    public ArrayList<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public void setTextureVertices(ArrayList<Vector2f> textureVertices) {
        this.textureVertices = textureVertices;
    }

    public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();

    public Vector3f getRotate() {
        return rotate;
    }


    public Vector3f getScale() {
        return scale;
    }


    public Vector3f getTranslate() {
        return translate;
    }


    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vector3f> vertices) {
        this.vertices = vertices;
    }
    public void setNormals(ArrayList<Vector3f> normals) {
        this.normals.clear();
        this.normals.addAll(normals);
    }
}
