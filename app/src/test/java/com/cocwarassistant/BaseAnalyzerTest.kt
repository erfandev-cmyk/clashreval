package com.cocwarassistant
import com.cocwarassistant.baseanalysis.BaseAnalyzer
import com.cocwarassistant.core.*
import org.junit.Assert.*
import org.junit.Test
class BaseAnalyzerTest{@Test fun producesFourDirectionalEntries(){val r=BaseAnalyzer().analyze(1000,1000, listOf(DetectedBuilding(type=DefenseType.INFERNO,category=BuildingCategory.DEFENSE,box=Rect(480f,480f,520f,520f),confidence=1f)));assertEquals(4,r.entryCandidates.size)}}
