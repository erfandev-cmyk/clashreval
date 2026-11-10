package com.cocwarassistant.attackplanner
import com.cocwarassistant.armybuilder.ArmyBuilder
import com.cocwarassistant.baseanalysis.BaseAnalysis
import com.cocwarassistant.core.*
import com.cocwarassistant.pathfinding.GridPathfinder
import com.cocwarassistant.strategy.TrapEstimator
class StrategyEngine(private val armyBuilder:ArmyBuilder=ArmyBuilder(),private val pathfinder:GridPathfinder=GridPathfinder(),private val trapEstimator:TrapEstimator=TrapEstimator()) {
    fun generate(width:Int,height:Int,base:BaseAnalysis,buildings:List<DetectedBuilding>,mode:AttackMode,capacity:Int,townHall:Int):AttackPlan {
        val entry=base.entryCandidates.firstOrNull() ?: EntryCandidate(Point(width/2f,height*0.08f),50,listOf("Fallback entry")); val target=base.townHall?:Point(width/2f,height/2f); val route=pathfinder.route(entry.point,target,emptySet()); val army=armyBuilder.recommended(townHall,capacity,air=buildings.count{it.type==DefenseType.AIR_DEFENSE}>2); val traps=trapEstimator.estimate(width,height,buildings); val risk=traps.count{it.probability==Probability.HIGH}; val score=(entry.score-risk*8).coerceIn(0,100); val reasons=listOf("Entry score ${entry.score}/100", "Route uses detected geometry", "${traps.size} hidden-defense hypotheses are flagged")
        val steps=listOf(DeploymentStep(1,"Funnel",entry.point,"Create the outer funnel before committing the main force.","funnel"),DeploymentStep(2,"Main push",target,"Advance along the generated corridor toward the Town Hall/core.","main"),DeploymentStep(3,"Cleanup",Point(width*0.75f,height*0.75f),"Reserve cleanup on the opposite side after the core is secured.","cleanup"))
        return AttackPlan(name=if(mode==AttackMode.MY_ARMY)"My Army Tactical Plan" else "Recommended Army Tactical Plan",mode,entry.point,route,steps,army,StrategyScore(score,reasons, listOf("Hidden defenses are probabilistic only","Army levels are not verified by this planner")))
    }
}
