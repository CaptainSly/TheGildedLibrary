package com.duckcraftian.gildedlibrary.api.system.archive;

import java.util.List;
import java.util.Map;

public record GLARecordsIndex(List<GLARecordEntry> entries, Map<String, GLARecordEntry> byId) {

    public GLARecordsIndex {
        entries = List.copyOf(entries);
        byId = Map.copyOf(byId);
    }

}
