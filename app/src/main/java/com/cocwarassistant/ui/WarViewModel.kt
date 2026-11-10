package com.cocwarassistant.ui

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.cocwarassistant.account.AccountRepository
import com.cocwarassistant.attackplanner.StrategyEngine
import com.cocwarassistant.baseanalysis.BaseAnalyzer
import com.cocwarassistant.core.*
import com.cocwarassistant.database.WarDatabase
import com.cocwarassistant.recognition.ModelAssetDetector
import com.cocwarassistant.strategy.TrapEstimator
import com.cocwarassistant.vision.ImagePipeline
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.InputStream

class WarViewModel(app:Application):AndroidViewModel(app){
    private val repo=AccountRepository(app); private val db=WarDatabase.get(app); private val detector=ModelAssetDetector(app); private val baseAnalyzer=BaseAnalyzer(); private val strategy=StrategyEngine(); private val trapEstimator=TrapEstimator()
    private val _selected=MutableStateFlow(Screen.DASHBOARD);val selectedScreen=_selected.asStateFlow();fun select(s:Screen){_selected.value=s}
    val profiles=repo.observeProfiles();val analyses=db.analyses().observeAll();
    var imageBitmap:Bitmap? by mutableStateOf(null);private set
    var detections by mutableStateOf<List<DetectedBuilding>>(emptyList());private set
    var recognitionWarnings by mutableStateOf<List<String>>(emptyList());private set
    var plan by mutableStateOf<AttackPlan?>(null);private set
    var traps by mutableStateOf<List<HiddenDefensePrediction>>(emptyList());private set
    private var imageUri:String?=null
    init{viewModelScope.launch{}}
    fun addProfile(name:String,tag:String,th:Int){viewModelScope.launch{repo.createDefault(name,tag,th)}}
    fun deleteProfile(p:PlayerProfileEntity){viewModelScope.launch{repo.delete(p)}}
    fun importImage(uri:Uri){viewModelScope.launch{getApplication<Application>().contentResolver.openInputStream(uri)?.use{stream->BitmapFactory.decodeStream(stream)?.let{imageBitmap=ImagePipeline.normalize(it).bitmap;imageUri=uri.toString();detections=emptyList();plan=null;traps=emptyList();recognitionWarnings=emptyList()}}}}
    fun runRecognition(){val bmp=imageBitmap?:return;viewModelScope.launch{val result=detector.detect(bmp);detections=result.detections;recognitionWarnings=result.warnings}}
    fun generatePlan(){val bmp=imageBitmap?:return;viewModelScope.launch{val analysis=baseAnalyzer.analyze(bmp.width,bmp.height,detections);val p=strategy.generate(bmp.width,bmp.height,analysis,detections,AttackMode.RECOMMENDED,300,18);plan=p;traps=trapEstimator.estimate(bmp.width,bmp.height,detections);db.analyses().upsert(AnalysisEntity(java.util.UUID.randomUUID().toString(),null,imageUri.orEmpty(),18,detections.size,"${p.name} • score ${p.score.score}/100",System.currentTimeMillis()));select(Screen.ATTACK_PLANNER)}}
    fun generateMyArmyPlan(){generatePlan()};fun generateRecommendedPlan(){generatePlan()}
    fun deleteAnalysis(id:String){viewModelScope.launch{db.analyses().delete(id)}}
    fun requestOverlay(){val ctx=getApplication<Application>();if(!Settings.canDrawOverlays(ctx)){ctx.startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:${ctx.packageName}")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))}}
    fun startOverlay(){val ctx=getApplication<Application>();if(Settings.canDrawOverlays(ctx)){androidx.core.content.ContextCompat.startForegroundService(ctx,Intent(ctx,com.cocwarassistant.overlay.TacticalOverlayService::class.java))}else requestOverlay()}
}
