package com.duckcraftian.gildedlibrary.api.system.vfs;

public abstract class AbstractVFSMount implements IVFSMount {

    private final String modId;
    private final String mountId;
    private final int priority;

    protected AbstractVFSMount(String modId, String mountId, int priority) {
        this.modId = modId;
        this.mountId = mountId;
        this.priority = priority;
    }

    @Override
    public String getModId() {
        return modId;
    }

    public String getMountId() {
        return mountId;
    }

    public int getPriority() {
        return priority;
    }
}
