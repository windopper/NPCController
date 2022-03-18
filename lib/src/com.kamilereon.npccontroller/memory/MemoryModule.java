package com.kamilereon.npccontroller.memory;

public class MemoryModule<T>  {

    protected String memoryKey;
    protected T data;

    public MemoryModule(String memoryKey, T data) {
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public String getMemoryKey() { return memoryKey; }

    public T getData() { return data; }
}
