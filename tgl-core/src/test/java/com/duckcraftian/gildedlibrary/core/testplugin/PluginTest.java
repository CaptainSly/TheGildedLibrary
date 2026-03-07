package com.duckcraftian.gildedlibrary.core.testplugin;

import com.duckcraftian.gildedlibrary.api.system.registries.Registry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.core.system.mods.PluginLoader;
import org.junit.jupiter.api.Assertions;
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
        assertTrue(registryManager.containsRegistry("holotapes"));

        var holotapeRegistry = registryManager.getRegistry("holotapes");
        if (holotapeRegistry.isPresent()) {
            Registry<?> reg = holotapeRegistry.get();
            var holotape = reg.resolve("test_tape");
            holotape.ifPresent(abstractRecord -> assertEquals("test_plugin:holotapes:test_tape", abstractRecord.getId()));
        }
    }

}
