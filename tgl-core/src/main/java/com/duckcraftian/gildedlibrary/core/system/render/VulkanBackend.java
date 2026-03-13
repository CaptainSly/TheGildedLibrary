package com.duckcraftian.gildedlibrary.core.system.render;

import com.duckcraftian.gildedlibrary.api.assets.RenderBackend;
import com.duckcraftian.gildedlibrary.core.TGL;
import org.lwjgl.PointerBuffer;
import org.lwjgl.sdl.SDLVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.util.Set;

public class VulkanBackend extends RenderBackend {

    private VkInstance instance;
    private VkDebugUtilsMessengerCallbackEXT debugMessenger;
    private VkPhysicalDevice chosenGPU;
    private VkDevice device;

    @Override
    public void onInitialize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            VkApplicationInfo appInfo = VkApplicationInfo.calloc(stack);

            // Create the Vulkan Application
            appInfo.sType(VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO);
            appInfo.pApplicationName(stack.UTF8Safe("TheGildedLibrary"));
            appInfo.applicationVersion(VK10.VK_MAKE_VERSION(Integer.parseInt(TGL.MAJOR), Integer.parseInt(TGL.MINOR), Integer.parseInt(TGL.BUGFIX)));
            appInfo.pEngineName(stack.UTF8Safe("TheGildedLibrary"));
            appInfo.engineVersion(VK10.VK_MAKE_VERSION(Integer.parseInt(TGL.MAJOR), Integer.parseInt(TGL.MINOR), Integer.parseInt(TGL.BUGFIX)));
            appInfo.apiVersion(VK10.VK_API_VERSION_1_0);

            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.calloc(stack);

            createInfo.sType(VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
            createInfo.pApplicationInfo(appInfo);
            createInfo.ppEnabledExtensionNames(SDLVulkan.SDL_Vulkan_GetInstanceExtensions());
            createInfo.ppEnabledLayerNames(null);

            PointerBuffer instancePtr = stack.mallocPointer(1);

            if (VK10.vkCreateInstance(createInfo, null, instancePtr) != VK10.VK_SUCCESS)
                throw new RuntimeException("Failed to create instance");

            instance = new VkInstance(instancePtr.get(0), createInfo);
        }

    }

    @Override
    public void swapBuffers() {

    }

    @Override
    public void clearScreen() {

    }

    @Override
    public void onDispose() {
        VK10.vkDestroyInstance(instance, null);
    }

}
