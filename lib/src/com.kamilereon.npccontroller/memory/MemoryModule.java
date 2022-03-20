package com.kamilereon.npccontroller.memory;

public class MemoryModule<T>  {

    protected int forgetTime;
    protected MemoryType type;
    protected MemoryImportance importance; // 0 ~ 100
    protected String memoryKey;
    protected T data;

    public MemoryModule(String memoryKey, T data) {
        this.type = MemoryType.OTHER;
        this.importance = MemoryImportance.MEDIUM;
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public MemoryModule(MemoryImportance importance, String memoryKey, T data) {
        this.type = MemoryType.OTHER;
        this.importance = importance;
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public MemoryModule(MemoryType type, MemoryImportance importance, String memoryKey, T data) {
        this.type = type;
        this.importance = importance;
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public MemoryModule(MemoryType type, String memoryKey, T data) {
        this.type = type;
        this.importance = MemoryImportance.MEDIUM;
        this.memoryKey = memoryKey;
        this.data = data;
    }

    public MemoryImportance getImportance() { return this.importance; }

    public void setImportance(MemoryImportance var) { this.importance = var; }

    public String getMemoryKey() { return memoryKey; }

    public T getData() { return data; }

    public MemoryType getMemoryType() { return type; }
}
