package com.duckcraftian.gildedlibrary.launcher;

import com.duckcraftian.gildedlibrary.api.system.engine.IEngine;
import com.duckcraftian.gildedlibrary.api.system.gfx.RenderBackend;
import com.duckcraftian.gildedlibrary.api.system.gfx.WindowBackend;

public class JFXBackend extends WindowBackend {

    private TGLLauncher launcher;

    public JFXBackend(TGLLauncher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void onInitialize() {

    }

    @Override
    public void handleEvents(IEngine engine) {

    }

    @Override
    public void onResize(int width, int height) {
    }

    @Override
    public void setTitle(String title) {
        launcher.getPrimaryStage().setTitle(title);
    }

    @Override
    public void onDispose() {

    }

    public static class JFXRenderBackend extends RenderBackend {

        @Override
        public void onInitialize() {

        }

        @Override
        public void swapBuffers() {

        }

        @Override
        public void clearScreen() {

        }

        @Override
        public void onDispose() {

        }
    }
}
