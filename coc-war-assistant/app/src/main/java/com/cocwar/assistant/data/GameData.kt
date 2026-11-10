package com.cocwar.assistant.data

/** Versioned game-data entry point. Values mirror assets/gamedata/meta.json (Phase 2 expands this). */
object GameData {
    const val DATA_VERSION = "0.0.1-unvalidated"
    /** Placeholder until verified against the current game. */
    const val MAX_TOWN_HALL = 17
    fun isValidTownHall(level: Int) = level in 1..MAX_TOWN_HALL
}
