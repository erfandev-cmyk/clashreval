package com.cocwarassistant.strategy
import com.cocwarassistant.core.*
import kotlin.math.hypot
class TrapEstimator {
    fun estimate(width:Int,height:Int,buildings:List<DetectedBuilding>):List<HiddenDefensePrediction>{
        val points=buildings.filter{it.category==BuildingCategory.DEFENSE}.flatMap{b->listOf(Point(b.box.left-40,b.box.center.y),Point(b.box.right+40,b.box.center.y))}.filter{it.x in 0f..width.toFloat() && it.y in 0f..height.toFloat()}.distinctBy{"${it.x.toInt()}:${it.y.toInt()}"}
        return points.take(12).mapIndexed{idx,p->val prob=when{idx<2->Probability.HIGH;idx<6->Probability.MEDIUM;else->Probability.LOW};HiddenDefensePrediction(if(idx%2==0)"Giant Bomb" else "Seeking Air Mine",p,prob,"Open corridor near a detected defensive cluster; this is a heuristic, not a confirmed trap." )}
    }
}
