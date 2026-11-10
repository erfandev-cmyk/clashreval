package com.cocwarassistant.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

data class Point(val x: Float, val y: Float)
data class Size(val width: Float, val height: Float)
data class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    val center get() = Point((left + right) / 2f, (top + bottom) / 2f)
    val area get() = (right - left).coerceAtLeast(0f) * (bottom - top).coerceAtLeast(0f)
}

enum class BuildingCategory { TOWN_HALL, DEFENSE, WALL, RESOURCE, ARMY, HERO, OTHER }
enum class DefenseType { EAGLE, INFERNO, X_BOW, SCATTERSHOT, MONOLITH, SPELL_TOWER, AIR_DEFENSE, ARCHER_TOWER, WIZARD_TOWER, CANNON, MORTAR, BOMB_TOWER, TESLA, REVENGE_TOWER, SUPER_WIZARD_TOWER, GUARDIAN, UNKNOWN }
enum class DetectionSource { MODEL, MANUAL }
enum class Probability { HIGH, MEDIUM, LOW }
enum class AttackMode { MY_ARMY, RECOMMENDED }

data class DetectedBuilding(
    val id: String = UUID.randomUUID().toString(), val type: DefenseType, val category: BuildingCategory,
    val box: Rect, val confidence: Float, val source: DetectionSource = DetectionSource.MODEL,
    val level: Int? = null
)

data class HiddenDefensePrediction(val type: String, val point: Point, val probability: Probability, val rationale: String)
data class StrategyScore(val score: Int, val reasons: List<String>, val risks: List<String>)
data class ArmyUnit(val name: String, val count: Int, val level: Int, val housingSpace: Int, val unlocked: Boolean = true)
data class SpellChoice(val name: String, val count: Int, val level: Int)
data class ArmyPlan(val troops: List<ArmyUnit>, val spells: List<SpellChoice>, val heroes: List<String>, val siege: String?, val totalHousing: Int, val notes: List<String>)
data class DeploymentStep(val order: Int, val label: String, val point: Point, val description: String, val colorKey: String)
data class AttackPlan(val id: String = UUID.randomUUID().toString(), val name: String, val mode: AttackMode, val entry: Point, val route: List<Point>, val steps: List<DeploymentStep>, val army: ArmyPlan, val score: StrategyScore, val createdAt: Long = System.currentTimeMillis())

@Entity(tableName = "profiles")
data class PlayerProfileEntity(@PrimaryKey val id: String, val name: String, val tag: String, val townHall: Int, val armyCapacity: Int, val updatedAt: Long)

@Entity(tableName = "profile_units", primaryKeys = ["profileId", "name"])
data class ProfileUnitEntity(val profileId: String, val name: String, val level: Int, val unlocked: Boolean, val kind: String)

@Entity(tableName = "analyses")
data class AnalysisEntity(@PrimaryKey val id: String, val profileId: String?, val imageUri: String, val townHall: Int?, val buildingCount: Int, val strategySummary: String, val createdAt: Long, val favorite: Boolean = false)

@Entity(tableName = "plans")
data class PlanEntity(@PrimaryKey val id: String, val analysisId: String?, val name: String, val json: String, val createdAt: Long, val favorite: Boolean = false)
