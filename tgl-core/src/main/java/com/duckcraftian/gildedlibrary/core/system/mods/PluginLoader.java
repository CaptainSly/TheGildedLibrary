package com.duckcraftian.gildedlibrary.core.system.mods;

import com.duckcraftian.gildedlibrary.api.system.mods.plugins.IPlugin;
import com.duckcraftian.gildedlibrary.api.system.mods.plugins.TGLPlugin;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.api.system.mods.plugins.PluginContext;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class PluginLoader {

    private final Path pluginDirectory;
    private final RegistryManager registryManager;
    private final Map<String, IPlugin> loadedPlugins;
    private final Map<String, TGLPlugin> pluginMetadata;
    private final Map<String, URLClassLoader> pluginClassLoaders;
    private final List<String> loadOrder;
    private final Logger logger;

    public PluginLoader(Path pluginDirectory, RegistryManager registryManager) {
        this.pluginDirectory = pluginDirectory;
        this.registryManager = registryManager;
        this.loadedPlugins = new HashMap<>();
        this.pluginMetadata = new HashMap<>();
        this.pluginClassLoaders = new HashMap<>();
        this.logger = Logger.getLogger(PluginLoader.class.getName());
        this.loadOrder = new ArrayList<>();
    }

    public void loadPlugins() {
        List<Path> jarFiles;

        try (Stream<Path> stream = Files.list(pluginDirectory)) {
            jarFiles = new ArrayList<>(stream.filter(p -> p.toString().endsWith(".jar")).toList());
        } catch (IOException e) {
            logger.severe("Failed to read plugin directory: " + e.getMessage());
            return;
        }

        if (jarFiles.isEmpty()) {
            logger.info("No plugins found in: " + pluginDirectory);
            return;
        }

        jarFiles.forEach(this::loadJar);

        resolveDependencyOrder();
        for (String pluginId : loadOrder) {
            IPlugin plugin = loadedPlugins.get(pluginId);
            TGLPlugin meta = pluginMetadata.get(pluginId);

            PluginContext context = new PluginContext(registryManager, logger, pluginId);

            plugin.onInitialize(context);
            plugin.onEnable();

            logger.info("Loaded plugin: " + meta.name() + " v" + meta.version());
        }

    }

    public void postInitializePlugins() {
        for (String pluginId : loadOrder) {
            IPlugin plugin = loadedPlugins.get(pluginId);
            plugin.onPostInitialize();
            logger.info("Post Initialized Plugin: " + pluginId);
        }
    }

    private void loadJar(Path jarPath) {
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarPath.toUri().toURL()}, getClass().getClassLoader());

            try (JarFile jar = new JarFile(jarPath.toFile())) {
                jar.entries().asIterator().forEachRemaining(entry -> {
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName().replace("/", ".").replace(".class", "");
                        tryLoadPluginClass(classLoader, className);
                    }
                });
            }

        } catch (IOException e) {
            logger.severe("Failed to load JAR: " + jarPath + " - " + e.getMessage());
        }

    }

    public void tryLoadPluginClass(URLClassLoader classLoader, String className) {
        try {
            Class<?> clazz = classLoader.loadClass(className);

            TGLPlugin annotation = clazz.getAnnotation(TGLPlugin.class);
            if (annotation == null) return;

            if (!IPlugin.class.isAssignableFrom(clazz)) {
                logger.warning(className + " has @TGLPlugin but does not implement IPlugin");
                return;
            }

            IPlugin plugin = (IPlugin) clazz.getDeclaredConstructor().newInstance();

            pluginMetadata.put(annotation.id(), annotation);
            loadedPlugins.put(annotation.id(), plugin);
            pluginClassLoaders.put(annotation.id(), classLoader);

            logger.info("Discovered Plugin: " + annotation.name() + " v" + annotation.version());

        } catch (Exception e) {
            logger.warning("Failed to load class: " + className + " - " + e.getMessage());
        }
    }

    private void resolveDependencyOrder() {
        List<String> sorted = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> inProgress = new HashSet<>();

        for (String pluginId : pluginMetadata.keySet()) {
            if (!visited.contains(pluginId)) {
                visit(pluginId, sorted, visited, inProgress);
            }
        }

        loadOrder.addAll(sorted);
    }

    private void visit(String pluginId, List<String> sorted, Set<String> visited, Set<String> inProgress) {
        if (inProgress.contains(pluginId)) {
            throw new RuntimeException("Circular dependency detected involving plugin: " + pluginId);
        }

        if (visited.contains(pluginId)) return;

        inProgress.add(pluginId);

        TGLPlugin meta = pluginMetadata.get(pluginId);
        if (meta != null) {
            for (String dep : meta.dependencies()) {
                if (!pluginMetadata.containsKey(dep))
                    throw new RuntimeException("Plugin " + pluginId + " requires missing dependency: " + dep);

                visit(dep, sorted, visited, inProgress);
            }
        }

        inProgress.remove(pluginId);
        visited.add(pluginId);
        sorted.add(pluginId);
    }


    public Path getPluginDirectory() {
        return pluginDirectory;
    }

    public RegistryManager getRegistryManager() {
        return registryManager;
    }

    public Map<String, IPlugin> getLoadedPlugins() {
        return loadedPlugins;
    }

    public Map<String, TGLPlugin> getPluginMetadata() {
        return pluginMetadata;
    }

    public Map<String, URLClassLoader> getPluginClassLoaders() {
        return pluginClassLoaders;
    }

    public Logger getLogger() {
        return logger;
    }
}
