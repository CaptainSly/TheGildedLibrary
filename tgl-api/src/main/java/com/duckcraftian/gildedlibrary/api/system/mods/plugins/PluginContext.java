package com.duckcraftian.gildedlibrary.api.system.mods.plugins;

import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import org.tinylog.Logger;

public record PluginContext(RegistryManager registryManager, String pluginId) {

}
