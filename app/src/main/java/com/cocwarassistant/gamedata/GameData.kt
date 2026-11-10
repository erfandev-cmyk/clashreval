package com.cocwarassistant.gamedata

import com.cocwarassistant.core.DefenseType

data class GameDataPackage(val version: String, val maxTownHall: Int, val townHalls: List<TownHallData>, val units: List<UnitData>, val defenses: List<DefenseData>)
data class TownHallData(val level: Int, val armyCapacity: Int?, val unlocks: List<String>, val verified: Boolean = false)
data class UnitData(val name: String, val kind: String, val unlockTownHall: Int, val housingSpace: Int, val maxLevelByTownHall: Map<Int, Int>)
data class DefenseData(val name: String, val type: DefenseType, val unlockTownHall: Int)

object GameDataCatalog {
    const val CURRENT_VERSION = "2026.11-th18-baseline"
    const val CURRENT_MAX_TH = 18
    val packageData = GameDataPackage(
        CURRENT_VERSION, CURRENT_MAX_TH,
        (1..18).map { TownHallData(it, null, emptyList(), verified = false) },
        listOf(
            UnitData("Barbarian", "TROOP", 1, 1, emptyMap()),
            UnitData("Archer", "TROOP", 1, 1, emptyMap()),
            UnitData("Giant", "TROOP", 1, 5, emptyMap()),
            UnitData("Wizard", "TROOP", 5, 4, emptyMap()),
            UnitData("Dragon", "TROOP", 7, 20, emptyMap()),
            UnitData("Electro Dragon", "TROOP", 11, 30, emptyMap()),
            UnitData("Hog Rider", "TROOP", 7, 5, emptyMap()),
            UnitData("Lava Hound", "TROOP", 9, 30, emptyMap()),
            UnitData("Miner", "TROOP", 10, 6, emptyMap()),
            UnitData("Yeti", "TROOP", 12, 18, emptyMap()),
            UnitData("Root Rider", "TROOP", 15, 6, emptyMap()),
            UnitData("Meteor Golem", "TROOP", 17, 40, emptyMap()),
            UnitData("Dragon Duke", "HERO", 15, 0, emptyMap()),
            UnitData("Totem Spell", "SPELL", 16, 0, emptyMap()),
            UnitData("Revive Spell", "SPELL", 18, 0, emptyMap())
        ),
        listOf(
            DefenseData("Town Hall", DefenseType.UNKNOWN, 1), DefenseData("Cannon", DefenseType.CANNON, 1), DefenseData("Archer Tower", DefenseType.ARCHER_TOWER, 2), DefenseData("Mortar", DefenseType.MORTAR, 3), DefenseData("Wizard Tower", DefenseType.WIZARD_TOWER, 5), DefenseData("Air Defense", DefenseType.AIR_DEFENSE, 4), DefenseData("X-Bow", DefenseType.X_BOW, 9), DefenseData("Inferno Tower", DefenseType.INFERNO, 10), DefenseData("Eagle Artillery", DefenseType.EAGLE, 11), DefenseData("Bomb Tower", DefenseType.BOMB_TOWER, 8), DefenseData("Hidden Tesla", DefenseType.TESLA, 7), DefenseData("Scattershot", DefenseType.SCATTERSHOT, 13), DefenseData("Spell Tower", DefenseType.SPELL_TOWER, 15), DefenseData("Monolith", DefenseType.MONOLITH, 15), DefenseData("Revenge Tower", DefenseType.REVENGE_TOWER, 18), DefenseData("Super Wizard Tower", DefenseType.SUPER_WIZARD_TOWER, 18), DefenseData("Guardian", DefenseType.GUARDIAN, 18)
        )
    )
}
