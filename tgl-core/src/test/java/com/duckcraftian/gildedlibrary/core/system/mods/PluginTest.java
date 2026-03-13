package com.duckcraftian.gildedlibrary.core.system.mods;

import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluginTest {

    @Test
    void testPluginLoads() {
        Path pluginDir = Path.of(System.getProperty("user.dir"), "build", "testPlugins");
        RegistryManager registryManager = new RegistryManager();
        PluginLoader loader = new PluginLoader(pluginDir, registryManager);

        loader.loadPlugins();

        assertTrue(loader.getLoadedPlugins().containsKey("test_plugin"));
        assertTrue(registryManager.containsRecordRegistry("holotapes"));

        var holotapeRegistry = registryManager.getRecordRegistry("holotapes");
        assertTrue(holotapeRegistry.isPresent());

        RecordRegistry<?> reg = holotapeRegistry.get();
        var holotape = reg.resolve("test_plugin:holotapes:test_tape");
        assertTrue(holotape.isPresent());
        assertEquals("test_plugin:holotapes:test_tape", holotape.get().getId());
    }

}
