package com.duckcraftian.gildedlibrary.api.system.gfx;

import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.api.system.Pointer;

public abstract class RenderBackend implements IDisposable {

    protected Pointer windowPointer;

    public void setWindow(Pointer windowPointer) {
        this.windowPointer = windowPointer;
    }

    public abstract void onInitialize();

    public abstract void swapBuffers();

    public abstract void clearScreen();

}
