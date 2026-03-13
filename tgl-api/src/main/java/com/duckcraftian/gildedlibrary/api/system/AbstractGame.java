package com.duckcraftian.gildedlibrary.api.system;

public abstract class AbstractGame implements IDisposable {

    public abstract void onInitialize();

    public abstract void update(float delta);

    public abstract void render();

}
