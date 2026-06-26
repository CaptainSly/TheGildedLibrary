package com.duckcraftian.gildedlibrary.api.system.geometry;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {

    private Vector3f position;
    private Vector3f scale;
    private Quaternionf rotation;

    public Transform(float x, float y, float z, float sx, float sy, float sz) {
        position = new Vector3f(x, y, z);
        scale = new Vector3f(sx, sy, sz);
        rotation = new Quaternionf();
    }

    public Transform() {
        this(0, 0, 0, 1, 1, 1);
    }

    public Transform(float x, float y, float z) {
        this(x, y, z, 1, 1, 1);
    }

    public Transform(Vector3f position) {
        this(position.x, position.y, position.z);
    }

    public Transform(Vector3f position, Vector3f scale) {
        this(position.x, position.y, position.z, scale.x, scale.y, scale.z);
    }

    public Transform setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Transform setScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }

    public Transform setRotation(Quaternionf rotation) {
        this.rotation = rotation;
        return this;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Quaternionf getRotation() {
        return rotation;
    }
}