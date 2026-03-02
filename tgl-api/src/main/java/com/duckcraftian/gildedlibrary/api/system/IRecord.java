package com.duckcraftian.gildedlibrary.api.system;

import java.util.Optional;

public interface IRecord {

    String getId();

    String getEditorId();

    String getModId();

    String getRecordType();

    Optional<String> getParentId();

}
