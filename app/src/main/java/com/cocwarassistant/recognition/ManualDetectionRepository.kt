package com.cocwarassistant.recognition
import com.cocwarassistant.core.*
class ManualDetectionRepository { fun add(list: List<DetectedBuilding>, building: DetectedBuilding)=list+building; fun remove(list: List<DetectedBuilding>, id:String)=list.filterNot{it.id==id}; fun replace(list:List<DetectedBuilding>, building:DetectedBuilding)=list.map{if(it.id==building.id) building else it} }
