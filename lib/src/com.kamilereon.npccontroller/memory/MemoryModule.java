package com.kamilereon.npccontroller.memory;

public class MemoryModule<T>  {

    protected MemoryType type;
    protected int importance; // 0 ~ 100
    protected String memoryKey;
    protected T data;

    public MemoryModule(String memoryKey, T data) {
        this.type = MemoryType.OTHER;
        this.importance = 50;
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public MemoryModule(int importance, String memoryKey, T data) {
        this.type = MemoryType.OTHER;
        this.importance = importance;
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public MemoryModule(MemoryType type, int importance, String memoryKey, T data) {
        this.type = type;
        this.importance = importance;
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public MemoryModule(MemoryType type, String memoryKey, T data) {
        this.type = type;
        this.importance = 50;
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public int getImportance() { return this.importance; }

    public void setImportance(int var) { this.importance = var; }

    public String getMemoryKey() { return memoryKey; }

    public T getData() { return data; }

    public MemoryType getMemoryType() { return type; }
}
