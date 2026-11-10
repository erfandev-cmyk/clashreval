package com.cocwarassistant.armybuilder
import com.cocwarassistant.core.*
import com.cocwarassistant.gamedata.GameDataCatalog
class ArmyBuilder {
    fun validate(plan:ArmyPlan, capacity:Int):List<String>{val errors=mutableListOf<String>(); if(plan.totalHousing>capacity)errors+="Army exceeds capacity by ${plan.totalHousing-capacity}."; plan.troops.filter{!it.unlocked}.forEach{errors+="${it.name} is locked in this profile."}; return errors}
    fun recommended(townHall:Int,capacity:Int,air: Boolean):ArmyPlan {
        val troop=if(air) ArmyUnit("Dragon",(capacity/20).coerceAtLeast(1),1,20) else ArmyUnit("Root Rider",(capacity/6).coerceAtLeast(1),1,6)
        val housing=(troop.count*troop.housingSpace).coerceAtMost(capacity)
        return ArmyPlan(listOf(troop.copy(count=housing/troop.housingSpace)),emptyList(),listOfNotNull(if(townHall>=15)"Dragon Duke" else null),null,housing,listOf("Heuristic composition; verify current in-game unlocks and levels before battle."))
    }
}
