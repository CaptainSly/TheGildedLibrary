package com.duckcraftian.gildedlibrary.api.system.character;

import com.duckcraftian.gildedlibrary.api.system.records.RecordReference;

public class Modifier {

    private float value;

    private RecordReference<?> source;

    private float duration;

    public Modifier(float value, RecordReference<?> source, float duration) {
        this.value = value;
        this.source = source;
        this.duration = duration;
    }

    public float getValue() {
        return value;
    }

    public RecordReference<?> getSource() {
        return source;
    }

    public float getDuration() {
        return duration;
    }
}