package com.cocwar.assistant

import com.cocwar.assistant.data.GameData
import org.junit.Assert.*
import org.junit.Test

class GameDataTest {
    @Test fun townHallRange() {
        assertTrue(GameData.isValidTownHall(1))
        assertTrue(GameData.isValidTownHall(GameData.MAX_TOWN_HALL))
        assertFalse(GameData.isValidTownHall(0))
        assertFalse(GameData.isValidTownHall(GameData.MAX_TOWN_HALL + 1))
    }
}
