package com.duckcraftian.gildedlibrary.api.system.gfx;

import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.api.system.Pointer;
import com.duckcraftian.gildedlibrary.api.system.engine.IEngine;

public abstract class WindowBackend implements IDisposable {

    protected Pointer windowPointer;

    protected String title;
    protected int width, height;

    public abstract void onInitialize();

    public abstract void handleEvents(IEngine engine);

    public abstract void onResize(int width, int height);

    public abstract void setTitle(String title);

    public Pointer getWindowPointer() {
        return windowPointer;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

