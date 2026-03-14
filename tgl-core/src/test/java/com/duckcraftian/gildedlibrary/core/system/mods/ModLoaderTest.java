package com.duckcraftian.gildedlibrary.core.system.mods;

import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetWriter;
import com.duckcraftian.gildedlibrary.api.system.archive.base.GLABaseWriter;
import com.duckcraftian.gildedlibrary.api.system.archive.mod.GLAModWriter;
import com.duckcraftian.gildedlibrary.api.system.mods.*;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.core.TGL;
import com.duckcraftian.gildedlibrary.core.system.records.ItemRecord;
import com.duckcraftian.gildedlibrary.core.system.vfs.VirtualFileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModLoaderTest {

    @TempDir
    Path modsDir;

    private RegistryManager registryManager;
    private VirtualFileSystem vfs;
    private ModLoader modLoader;

    @BeforeEach
    void setup() {
        registryManager = new RegistryManager();
        registryManager.addRecordRegistry("items", new RecordRegistry<>("items"));
        registryManager.addSerializerRegistry("items", new ItemRecord.ItemSerializer());
        vfs = new VirtualFileSystem();
        modLoader = new ModLoader(modsDir, registryManager, vfs);
    }

    // --- Helpers ---

    private ModMetadata meta(String id, String[] deps, String[] conflicts) {
        return new ModMetadata.ModMetadataBuilder()
                .id(id).name(id).version("1.0.0").description("Test mod")
                .author("test").website("").engineVersion(TGL.getVersion())
                .type(ModMetadata.ModType.MOD).priority(0)
                .dependencies(deps).optionalDependencies().conflicts(conflicts)
                .creationDate(System.currentTimeMillis())
                .lastUpdated(System.currentTimeMillis())
                .build();
    }

    private ItemRecord item(String modId, String itemId) {
        return new ItemRecord.ItemBuilder()
                .itemId(itemId).editorId(itemId).modId(modId)
                .name(itemId).description("test item")
                .image(itemId + ".png").model("default")
                .rarity("common").weight(1.0f).cost(10)
                .build();
    }

    private RegistryManager registryWith(ItemRecord... items) {
        RegistryManager registrymanager = new RegistryManager();
        RecordRegistry<ItemRecord> reg = new RecordRegistry<>("items");
        for (ItemRecord item : items) reg.addRecord(item);
        registrymanager.addRecordRegistry("items", reg);
        registrymanager.addSerializerRegistry("items", new ItemRecord.ItemSerializer());
        return registrymanager;
    }

    // --- Tests ---

    @Test
    void testBasicGLMLoad() throws IOException {
        RegistryManager registryManager = registryWith(
                item("test_mod", "iron_sword"),
                item("test_mod", "gold_coin"),
                item("test_mod", "health_potion")
        );

        new GLAModWriter(registryManager).write(
                meta("test_mod", new String[]{}, new String[]{}),
                List.copyOf(registryManager.getRecordRegistries().values()),
                modsDir.resolve("test_mod.glm")
        );

        modLoader.loadMods();

        // NOTE: requires ModLoader.getLoadOrder() getter
        assertTrue(modLoader.getLoadOrder().contains("test_mod"));

        var reg = this.registryManager.getRecordRegistry("items");
        assertTrue(reg.isPresent());
        assertTrue(reg.get().resolve("test_mod:items:iron_sword").isPresent());
        assertTrue(reg.get().resolve("test_mod:items:gold_coin").isPresent());
        assertTrue(reg.get().resolve("test_mod:items:health_potion").isPresent());
    }

    @Test
    void testDependencyOrder() throws IOException {
        RegistryManager registryManagerA = registryWith(item("mod_a", "item_a"));
        RegistryManager registryManagerB = registryWith(item("mod_b", "item_b"));

        // mod_a depends on mod_b
        new GLAModWriter(registryManagerB).write(
                meta("mod_b", new String[]{}, new String[]{}),
                List.copyOf(registryManagerB.getRecordRegistries().values()),
                modsDir.resolve("mod_b.glm")
        );
        new GLAModWriter(registryManagerA).write(
                meta("mod_a", new String[]{"mod_b"}, new String[]{}),
                List.copyOf(registryManagerA.getRecordRegistries().values()),
                modsDir.resolve("mod_a.glm")
        );

        modLoader.loadMods();

        List<String> order = modLoader.getLoadOrder();
        assertTrue(order.indexOf("mod_b") < order.indexOf("mod_a"),
                "mod_b should load before mod_a");
    }

    @Test
    void testMissingDependency() throws IOException {
        RegistryManager registryManager = registryWith(item("orphan_mod", "some_item"));

        new GLAModWriter(registryManager).write(
                meta("orphan_mod", new String[]{"nonexistent_mod"}, new String[]{}),
                List.copyOf(registryManager.getRecordRegistries().values()),
                modsDir.resolve("orphan_mod.glm")
        );

        assertThrows(MissingDependencyException.class, () -> modLoader.loadMods());
    }

    @Test
    void testConflictDetection() throws IOException {
        RegistryManager modRegistryA = registryWith(item("mod_a", "item_a"));
        RegistryManager modRegistryB = registryWith(item("mod_b", "item_b"));

        // mod_a declares conflict with mod_b
        new GLAModWriter(modRegistryA).write(
                meta("mod_a", new String[]{}, new String[]{"mod_b"}),
                List.copyOf(modRegistryA.getRecordRegistries().values()),
                modsDir.resolve("mod_a.glm")
        );
        new GLAModWriter(modRegistryB).write(
                meta("mod_b", new String[]{}, new String[]{}),
                List.copyOf(modRegistryB.getRecordRegistries().values()),
                modsDir.resolve("mod_b.glm")
        );

        assertThrows(ModConflictException.class, () -> modLoader.loadMods());
    }

    @Test
    void testGLADiscovery() throws IOException {
        // Write a GLA asset pack
        Path assetDir = Files.createDirectory(modsDir.resolve("assets_src"));
        Files.writeString(assetDir.resolve("icon.png"), "fake_png");
        new GLAAssetWriter().write(
                List.of(assetDir.resolve("icon.png")),
                assetDir,
                modsDir.resolve("test_assets.gla")
        );

        // Write a GLB that declares test_assets as a dependency
        Path glbAssetDir = Files.createDirectory(modsDir.resolve("glb_src"));
        Files.writeString(glbAssetDir.resolve("sword.png"), "fake_png");
        RegistryManager registryManager = registryWith(item("base_mod", "staff"));

        new GLABaseWriter(registryManager).write(
                meta("base_mod", new String[]{"test_assets"}, new String[]{}),
                List.of(glbAssetDir.resolve("sword.png")),
                List.copyOf(registryManager.getRecordRegistries().values()),
                glbAssetDir,
                modsDir.resolve("base_mod.glb")
        );

        modLoader.loadMods();

        // GLA should be discovered and mounted
        boolean glaIsMounted = vfs.getArchiveMounts().stream()
                .anyMatch(m -> m.getMountId().equals("test_assets"));
        assertTrue(glaIsMounted, "GLA asset pack should be mounted in VFS");
    }

    @Test
    void testVFSMounting() throws IOException {
        Path assetDir = Files.createDirectory(modsDir.resolve("vfs_src"));
        Files.writeString(assetDir.resolve("potion.png"), "fake_texture");
        RegistryManager registryManager = registryWith(item("vfs_mod", "potion"));

        new GLABaseWriter(registryManager).write(
                meta("vfs_mod", new String[]{}, new String[]{}),
                List.of(assetDir.resolve("potion.png")),
                List.copyOf(registryManager.getRecordRegistries().values()),
                assetDir,
                modsDir.resolve("vfs_mod.glb")
        );

        modLoader.loadMods();

        assertTrue(vfs.exists("potion.png"), "Asset from GLB should be resolvable in VFS");
    }
}
