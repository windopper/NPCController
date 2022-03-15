package com.kamilereon.npccontroller.utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Logger {

    private boolean enabled = true;

    private final String prefix;

    public Logger(String prefix) {
        this.prefix = prefix + " ";
    }

    public void disable() {
        this.enabled = false;
    }

    public void info(String info) {
        log(Level.INFO, info);
    }

    public void warning(String warning) {
        log(Level.WARNING, warning);
    }

    public void severe(String severe) {
        log(Level.SEVERE, severe);
    }

    public void log(Level level, String message) {
        if(enabled) {
            Bukkit.getLogger().log(level, prefix + message);
        }
    }
}
