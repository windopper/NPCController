package com.kamilereon.npccontroller.states;

public enum States {
    FIRE((byte) 0x01),
    CROUCHING((byte) 0x02),
    SPRINTING((byte) 0x08),
    SWIMMING((byte) 0x10),
    INVISIBLE((byte) 0x20),
    GLOWING((byte) 0x40),
    FLYING((byte) 0x80),
    ;

    private byte bit;

    States(byte bit) {
        this.bit = bit;
    }

    public byte getBitMask() { return bit; }
}
