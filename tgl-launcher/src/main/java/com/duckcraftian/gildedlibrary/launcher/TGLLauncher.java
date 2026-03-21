package com.duckcraftian.gildedlibrary.launcher;

import com.duckcraftian.gildedlibrary.api.system.engine.EngineConfig;
import com.duckcraftian.gildedlibrary.api.system.engine.EngineMode;
import com.duckcraftian.gildedlibrary.api.system.engine.IEngine;
import com.duckcraftian.gildedlibrary.api.system.engine.IEngineContext;
import javafx.application.Application;
import javafx.stage.Stage;

public class TGLLauncher extends Application implements IEngineContext {

    private JFXBackend windowBackend;
    private Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public EngineMode getMode() {
        return EngineMode.LAUNCHER;
    }

    @Override
    public EngineConfig getConfig() {
        return new EngineConfig.EngineConfigBuilder()
                .windowTitle("TGL-Launcher")
                .setWidth(1280)
                .setHeight(720)
                .build();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
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
