package com.kamilereon.npccontroller.states;

public enum Poses {
    STANDING,
    FALL_FLYING,
    SLEEPING,
    SWIMMING,
    SPIN_ATTACK,
    SNEAKING,
    LONG_JUMPING,
    DYING;

    public int getVarInt() { return ordinal(); }
}
