package com.kamilereon.npccontroller.memory;

public enum MemoryType {
    LONG_TERM, // npc 의 장기기억
    SHORT_TERM, // npc 의 행동 페이즈가 바뀌면 삭제되는 기억
    WORKING, // npc 가 현재 하고 있는 행동에 대한 기억
    OTHER, // 분류를 굳이 할 필요가 없을 때
    ;
}
