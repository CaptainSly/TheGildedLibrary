package com.duckcraftian.gildedlibrary.api.object;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.records.RecordReference;
import com.duckcraftian.gildedlibrary.api.system.records.RecordReference;

public class AbstractObject<T extends AbstractRecord> {

    public final RecordReference<T> objectRecord;

    public AbstractObject(String recordId) {
        this.objectRecord = new RecordReference<T>(recordId);
    }

    public RecordReference<T> getObjectRecord() {
        return objectRecord;
    }
}