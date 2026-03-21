package com.duckcraftian.gildedlibrary.api.system.engine;

import com.duckcraftian.gildedlibrary.api.system.Builder;

public class EngineConfig {

    private final String windowTitle;
    private final int width, height;
    private final boolean fullscreen;

    public EngineConfig(EngineConfigBuilder builder) {
        this.windowTitle = builder.windowTitle;
        this.width = builder.width;
        this.height = builder.height;
        this.fullscreen = builder.fullscreen;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public static class EngineConfigBuilder extends Builder<EngineConfigBuilder, EngineConfig> {

        private String windowTitle;
        private int width;
        private int height;
        private boolean fullscreen;

        public EngineConfigBuilder windowTitle(String windowTitle) {
            this.windowTitle = windowTitle;
            return self();
        }

        public EngineConfigBuilder setWidth(int width) {
            this.width = width;
            return this;
        }

        public EngineConfigBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public EngineConfigBuilder setFullscreen(boolean fullscreen) {
            this.fullscreen = fullscreen;
            return this;
        }

        @Override
        public EngineConfigBuilder self() {
            return this;
        }

        @Override
        public EngineConfig build() {
            return new EngineConfig(this);
        }
    }


}
