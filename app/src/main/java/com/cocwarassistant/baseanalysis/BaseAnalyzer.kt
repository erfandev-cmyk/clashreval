package com.cocwarassistant.baseanalysis

import com.cocwarassistant.core.*
import kotlin.math.hypot

data class BaseAnalysis(val center: Point, val townHall: Point?, val defensiveCoverage: Float, val entryCandidates: List<EntryCandidate>, val rationale: List<String>)
data class EntryCandidate(val point: Point, val score: Int, val reasons: List<String>)

class BaseAnalyzer {
    fun analyze(width: Int, height: Int, buildings: List<DetectedBuilding>): BaseAnalysis {
        val center=Point(width/2f,height/2f); val th=buildings.firstOrNull{it.category==BuildingCategory.TOWN_HALL}?.box?.center
        val defenses=buildings.filter{it.category==BuildingCategory.DEFENSE}; val radius=minOf(width,height)*0.32f
        val candidates=listOf(Point(width/2f, height*0.08f),Point(width*0.92f,height/2f),Point(width/2f,height*0.92f),Point(width*0.08f,height/2f))
        val entries=candidates.map{p->
            val nearby=defenses.count{hypot((it.box.center.x-p.x).toDouble(),(it.box.center.y-p.y).toDouble())<radius}
            val core=th?.let{hypot((it.x-p.x).toDouble(),(it.y-p.y).toDouble())<width*0.35f}==true
            val score=(100-nearby*8-(if(core) 0 else 12)).coerceIn(0,100)
            EntryCandidate(p,score,listOf("$nearby detected defenses are near the entry corridor",if(core) "Town Hall lies on a direct corridor" else "Town Hall is not on the first corridor"))
        }.sortedByDescending{it.score}
        return BaseAnalysis(center,th,if(defenses.isEmpty())0f else (defenses.size/(defenses.size+10f)).coerceIn(0f,1f),entries,listOf("Entry scoring uses detected building coordinates; hidden defenses are not treated as confirmed."))
    }
}
