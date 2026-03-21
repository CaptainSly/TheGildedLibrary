package com.duckcraftian.gildedlibrary.core.system.render;

import com.duckcraftian.gildedlibrary.api.system.gfx.RenderBackend;
import com.duckcraftian.gildedlibrary.api.system.Pointer;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.sdl.SDLVideo;

public class GLBackend extends RenderBackend {

    private long openGLContext;

    public GLBackend() {
    }

    @Override
    public void onInitialize() {
        SDLVideo.SDL_GL_SetAttribute(SDLVideo.SDL_GL_CONTEXT_MAJOR_VERSION, 3);
        SDLVideo.SDL_GL_SetAttribute(SDLVideo.SDL_GL_CONTEXT_MINOR_VERSION, 3);
        SDLVideo.SDL_GL_SetAttribute(SDLVideo.SDL_GL_CONTEXT_PROFILE_CORE, 1);

        openGLContext = SDLVideo.SDL_GL_CreateContext(windowPointer.address());
        GL.createCapabilities();
    }

    public void swapBuffers() {
        SDLVideo.SDL_GL_SwapWindow(windowPointer.address());
    }

    public void clearScreen() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.32f, 0.32f, 0.38f, 1);
    }

    public long getOpenGLContext() {
        return openGLContext;
    }

    @Override
    public void onDispose() {
        SDLVideo.SDL_GL_DestroyContext(openGLContext);
    }
}
