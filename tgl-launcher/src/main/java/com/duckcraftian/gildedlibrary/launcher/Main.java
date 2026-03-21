package com.duckcraftian.gildedlibrary.launcher;

import com.duckcraftian.gildedlibrary.core.TGLEngine;

public class Main {

    public static void main(String[] args) {
        TGLLauncher launcher = new TGLLauncher();
        TGLEngine engine = new TGLEngine.EngineBuilder()
                .windowBackend(new JFXBackend(launcher))
                .renderBackend(new JFXBackend.JFXRenderBackend())
                .engineContext(launcher)
                .build();
        engine.run();

        launcher.launch(args);
    }

}
