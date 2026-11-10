package com.cocwarassistant
import com.cocwarassistant.armybuilder.ArmyBuilder
import com.cocwarassistant.core.*
import org.junit.Assert.*
import org.junit.Test
class StrategyEngineTest {
 @Test fun armyCapacityValidation(){val a=ArmyBuilder();val plan=ArmyPlan(listOf(ArmyUnit("Dragon",20,1,20)),emptyList(),emptyList(),null,400,emptyList());assertTrue(a.validate(plan,300).isNotEmpty())}
 @Test fun emptyArmyFits(){val a=ArmyBuilder();val plan=ArmyPlan(emptyList(),emptyList(),emptyList(),null,0,emptyList());assertTrue(a.validate(plan,300).isEmpty())}
}
