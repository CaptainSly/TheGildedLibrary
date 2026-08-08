package com.duckcraftian.gildedlibrary.api.object;

import com.duckcraftian.gildedlibrary.api.system.geometry.Transform;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.records.RecordReference;

public abstract class AbstractWorldObject<T extends AbstractRecord> {

    private final String instanceId;
    private final RecordReference<T> objectRecord;
    private final Transform transform;

    public AbstractWorldObject(String instanceId, String recordId) {
        this.instanceId = instanceId;
        this.objectRecord = new RecordReference<T>(recordId);
        transform = new Transform();
    }

    public RecordReference<T> getObjectRecord() {
        return objectRecord;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public Transform getTransform() {
        return transform;
    }
}