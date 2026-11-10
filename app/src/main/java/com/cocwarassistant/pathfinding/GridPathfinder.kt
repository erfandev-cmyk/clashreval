package com.cocwarassistant.pathfinding
import com.cocwarassistant.core.Point
import java.util.PriorityQueue
import kotlin.math.abs

data class GridPoint(val x:Int,val y:Int)
class GridPathfinder {
    fun route(start:Point,end:Point,blocked:Set<GridPoint>,gridSize:Int=20):List<Point>{
        fun toGrid(p:Point)=GridPoint((p.x/gridSize).toInt().coerceAtLeast(0),(p.y/gridSize).toInt().coerceAtLeast(0))
        val s=toGrid(start); val e=toGrid(end); val came=mutableMapOf<GridPoint,GridPoint>(); val gScore=mutableMapOf<GridPoint,Int>(s to 0); val open=PriorityQueue(compareBy<GridPoint>{gScore[it]?:Int.MAX_VALUE}); open.add(s); val dirs=listOf(GridPoint(1,0),GridPoint(-1,0),GridPoint(0,1),GridPoint(0,-1))
        var found=false
        while(open.isNotEmpty()){val cur=open.poll(); if(cur==e){found=true;break}; for(d in dirs){val n=GridPoint(cur.x+d.x,cur.y+d.y); if(n in blocked || n.x !in 0..200 || n.y !in 0..200) continue; val ng=(gScore[cur]?:Int.MAX_VALUE)+1; if(ng<(gScore[n]?:Int.MAX_VALUE)){came[n]=cur;gScore[n]=ng;open.add(n)}}}
        if(!found)return listOf(start,end)
        val path=mutableListOf<GridPoint>(); var cur=e; path.add(cur); while(cur!=s){cur=came[cur]?:break;path.add(cur)}; return path.asReversed().map{Point(it.x*gridSize+gridSize/2f,it.y*gridSize+gridSize/2f)}
    }
}
