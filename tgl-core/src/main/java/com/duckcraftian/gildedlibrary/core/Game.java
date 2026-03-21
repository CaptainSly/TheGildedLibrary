package com.duckcraftian.gildedlibrary.core;

import com.duckcraftian.gildedlibrary.api.system.engine.EngineConfig;
import com.duckcraftian.gildedlibrary.api.system.engine.EngineMode;
import com.duckcraftian.gildedlibrary.api.system.engine.IEngine;
import com.duckcraftian.gildedlibrary.api.system.engine.IEngineContext;
import com.duckcraftian.gildedlibrary.core.system.render.GLBackend;
import com.duckcraftian.gildedlibrary.core.system.render.SDLWindow;

public class Game implements IEngineContext {

    public static void main(String[] args) {
        Game game = new Game();
        TGLEngine engine = new TGLEngine.EngineBuilder()
                .engineContext(game)
                .windowBackend(new SDLWindow(game.getConfig().getWindowTitle(), game.getConfig().getWidth(), game.getConfig().getHeight()))
                .renderBackend(new GLBackend())
                .build();

        engine.run();
    }

    @Override
    public EngineMode getMode() {
        return EngineMode.GAME;
    }

    @Override
    public EngineConfig getConfig() {
        return new EngineConfig.EngineConfigBuilder()
                .windowTitle("TheGildedLibrary")
                .setWidth(1280)
                .setHeight(720)
                .setFullscreen(false).build();
    }

    @Override
    public void onEngineReady(IEngine engine) {

    }

    @Override
    public void onRender() {

    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    @Override
    public void onShutdown() {

    }
}
