package com.duckcraftian.gildedlibrary.api.system.mods.plugins;

import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;

import java.util.logging.Logger;

public record PluginContext(RegistryManager registryManager, Logger logger, String pluginId) {

}
