package com.duckcraftian.gildedlibrary.api.system.engine;

import java.util.HashSet;
import java.util.Set;

public enum EngineMode {

    GAME(true,
            SubsystemType.RENDERING,
            SubsystemType.ASSET_MANAGER,
            SubsystemType.SCRIPTING,
            SubsystemType.AUDIO,
            SubsystemType.INPUT,
            SubsystemType.PHYSICS,
            SubsystemType.NETWORKING),

    LAUNCHER(false,
            SubsystemType.RENDERING,
            SubsystemType.ASSET_MANAGER),

    EDITOR(false,
            SubsystemType.ASSET_MANAGER,
            SubsystemType.SCRIPTING,
            SubsystemType.INPUT,
            SubsystemType.PHYSICS,
            SubsystemType.AUDIO,
            SubsystemType.RENDERING),

    SERVER(true,
            SubsystemType.PHYSICS,
            SubsystemType.ASSET_MANAGER,
            SubsystemType.SCRIPTING,
            SubsystemType.NETWORKING);

    boolean hasGameLoop;
    Set<SubsystemType> allowedSystems;

    EngineMode(boolean hasGameLoop, SubsystemType... types) {
        this.hasGameLoop = hasGameLoop;
        allowedSystems = new HashSet<>();

        for (SubsystemType type : types)
            allowedSystems.add(type);
    }

    public boolean requires(SubsystemType type) {
        return this.allowedSystems.contains(type);
    }

    public boolean hasGameLoop() {
        return hasGameLoop;
    }

}
