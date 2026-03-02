package com.duckcraftian.gildedlibrary.core;

import com.duckcraftian.gildedlibrary.api.system.mods.plugins.IPlugin;
import com.duckcraftian.gildedlibrary.api.system.mods.plugins.PluginContext;
import com.duckcraftian.gildedlibrary.api.system.mods.plugins.TGLPlugin;
import com.duckcraftian.gildedlibrary.api.system.registries.Registry;

import static com.duckcraftian.gildedlibrary.core.TestPlugin.MOD_ID;

@TGLPlugin(
        id = MOD_ID,
        name = "Test Plugin",
        version = "1.0.0"
)
public class TestPlugin implements IPlugin {

    public static final String MOD_ID = "test_plugin";

    public boolean initialized = false;
    public boolean enabled = false;

    @Override
    public void onInitialize(PluginContext context) {
        context.registryManager().addRegistry("holotapes", new Registry<HoloTapeRecord>());

        HoloTapeRecord testTape = new HoloTapeRecord.HoloTapeBuilder()
                .modId(MOD_ID)
                .itemId("test_tape")
                .editorId("test_tape")
                .build();

        context.logger().info("TAPE: " + testTape.getId());

        Registry<HoloTapeRecord> registry = (Registry<HoloTapeRecord>) context.registryManager().getRegistry("holotapes").get();
        registry.addRecord(testTape);
    }

    public void onPostInitialize() {
        
    }

    @Override
    public void onEnable() {
        enabled = true;
    }

    @Override
    public boolean canDisable() {
        return true;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onShutdown() {

    }

    @Override
    public void onDispose() {

    }
}
