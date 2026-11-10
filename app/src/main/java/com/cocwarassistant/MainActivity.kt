package com.cocwarassistant

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cocwarassistant.ui.*

class MainActivity: ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{WarAssistantApp()}}
}

private val TacticalBg=Color(0xFF090A0C); private val Panel=Color(0xFF15171C); private val Accent=Color(0xFFE23B3B); private val Gold=Color(0xFFD6B45A)

@Composable fun WarAssistantApp(vm:WarViewModel=viewModel()){
    val selected by vm.selectedScreen.collectAsState(); val screens=Screen.entries
    MaterialTheme(colorScheme=darkColorScheme(background=TacticalBg,surface=Panel,primary=Accent,secondary=Gold)){
        Row(Modifier.fillMaxSize().background(TacticalBg)){
            NavigationRail(containerColor=Color(0xFF0D0F12),contentColor=Color.White,modifier=Modifier.width(76.dp)){
                Icon(Icons.Default.Shield,null,Modifier.padding(16.dp).size(34.dp),tint=Gold)
                screens.take(6).forEach{screen->NavigationRailItem(selected=selected==screen,onClick={vm.select(screen)},icon={Icon(screen.icon,null)},label={Text(screen.short, fontSize=9.sp)},alwaysShowLabel=false)}
                Spacer(Modifier.weight(1f)); NavigationRailItem(selected=selected==Screen.SETTINGS,onClick={vm.select(Screen.SETTINGS)},icon={Icon(Icons.Default.Settings,null)},alwaysShowLabel=false)
            }
            Column(Modifier.fillMaxSize().padding(16.dp)){ when(selected){
                Screen.DASHBOARD->Dashboard(vm); Screen.ACCOUNT->AccountScreen(vm); Screen.WAR_ANALYZER->WarAnalyzer(vm); Screen.ATTACK_PLANNER->PlannerScreen(vm); Screen.ARMY->ArmyScreen(vm); Screen.RECOMMENDED->RecommendedScreen(vm); Screen.HIDDEN->HiddenScreen(vm); Screen.HISTORY->HistoryScreen(vm); Screen.GAME_DATA->GameDataScreen(); Screen.SETTINGS->SettingsScreen(vm)
            }}
        }
    }
}

enum class Screen(val short:String,val icon:androidx.compose.ui.graphics.vector.ImageVector){DASHBOARD("Home",Icons.Default.Home),ACCOUNT("Account",Icons.Default.Person),WAR_ANALYZER("Analyze",Icons.Default.Search),ATTACK_PLANNER("Plan",Icons.Default.Map),ARMY("Army",Icons.Default.Group),RECOMMENDED("AI Army",Icons.Default.AutoAwesome),HIDDEN("Traps",Icons.Default.Visibility),HISTORY("History",Icons.Default.History),GAME_DATA("Data",Icons.Default.Storage),SETTINGS("Settings",Icons.Default.Settings)}

@Composable fun Header(title:String,subtitle:String?=null){Column{Text(title,style=MaterialTheme.typography.headlineMedium,fontSize=26.sp);subtitle?.let{Text(it,color=Color.LightGray,fontSize=12.sp)};Spacer(Modifier.height(12.dp))}}
@Composable fun PanelCard(modifier:Modifier=Modifier,content:@Composable ColumnScope.()->Unit){Card(modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=Panel),shape=MaterialTheme.shapes.medium){Column(Modifier.padding(16.dp),content=content)}}

@Composable fun Dashboard(vm:WarViewModel){val profiles by vm.profiles.collectAsState(initial=emptyList()); val analyses by vm.analyses.collectAsState(initial=emptyList()); Header("WAR COMMAND","Clash of Clans tactical assistant • offline-first"); Row(Modifier.horizontalScroll(rememberScrollState())){Metric("PROFILES",profiles.size.toString());Metric("ANALYSES",analyses.size.toString());Metric("GAME DATA",com.cocwarassistant.gamedata.GameDataCatalog.CURRENT_VERSION)}; Spacer(Modifier.height(12.dp)); PanelCard{Text(if(profiles.isEmpty())"No player profile configured" else "Active player: ${profiles.first().name}",color=Color.White);Text("Build a local profile, import a war screenshot, then correct detections before generating a plan.",color=Color.Gray);Spacer(Modifier.height(10.dp));Button(onClick={vm.select(Screen.ACCOUNT)}){Text("Manage account")}}; Spacer(Modifier.height(12.dp)); PanelCard{Text("Quick analysis",style=MaterialTheme.typography.titleMedium);Text("Recognition is model-driven. This build intentionally does not fabricate detections when no trained model is installed.",color=Color.Gray);Button(onClick={vm.select(Screen.WAR_ANALYZER)},modifier=Modifier.padding(top=8.dp)){Text("Import war screenshot")}}}
@Composable fun Metric(label:String,value:String){Card(Modifier.padding(end=8.dp).widthIn(min=105.dp),colors=CardDefaults.cardColors(containerColor=Color(0xFF1B1E24))){Column(Modifier.padding(12.dp)){Text(label,fontSize=10.sp,color=Gold);Text(value,fontSize=16.sp)}}}

@Composable fun AccountScreen(vm:WarViewModel){val profiles by vm.profiles.collectAsState(initial=emptyList()); Header("ACCOUNT MANAGER","Multiple local profiles • no API key required"); PanelCard{var name by remember{mutableStateOf("")};var tag by remember{mutableStateOf("")};var th by remember{mutableStateOf("18")}; OutlinedTextField(name,{name=it},label={Text("Player name")},modifier=Modifier.fillMaxWidth());OutlinedTextField(tag,{tag=it},label={Text("Player tag (optional)")},modifier=Modifier.fillMaxWidth().padding(top=8.dp));OutlinedTextField(th,{th=it.filter(Char::isDigit)},label={Text("Town Hall")},modifier=Modifier.fillMaxWidth().padding(top=8.dp));Button(onClick={vm.addProfile(name.ifBlank{"Chief"},tag,th.toIntOrNull()?.coerceIn(1,18)?:18);},modifier=Modifier.padding(top=10.dp)){Text("Create profile")}}; Spacer(Modifier.height(12.dp));profiles.forEach{p->PanelCard{Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(p.name);Text("${p.tag.ifBlank{"No tag"}} • TH${p.townHall}",color=Color.Gray);Text(if(p.armyCapacity>0)"Army capacity ${p.armyCapacity}" else "Army capacity not verified",color=Color.Gray,fontSize=12.sp)};IconButton({vm.deleteProfile(p)}){Icon(Icons.Default.Delete,null)}}}}}

@Composable fun WarAnalyzer(vm:WarViewModel){val launcher=rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){uri:Uri?->uri?.let(vm::importImage)}; Header("WAR ANALYZER","Screenshot import → preprocessing → local recognition → manual correction"); PanelCard{Button(onClick={launcher.launch("image/*")}){Icon(Icons.Default.AddPhotoAlternate,null);Spacer(Modifier.width(8.dp));Text("Select screenshot")};vm.imageBitmap?.let{Spacer(Modifier.height(12.dp));Image(it.asImageBitmap(),null,Modifier.fillMaxWidth().heightIn(max=420.dp));Text("${it.width} × ${it.height}",color=Color.Gray,fontSize=11.sp);Button(onClick=vm::runRecognition,modifier=Modifier.padding(top=8.dp)){Text("Run local recognition")}}}; if(vm.recognitionWarnings.isNotEmpty())PanelCard{Text("Recognition status",color=Gold);vm.recognitionWarnings.forEach{Text("• $it",color=Color.LightGray)}}; if(vm.detections.isNotEmpty())PanelCard{Text("Detected structures",style=MaterialTheme.typography.titleMedium);vm.detections.forEach{d->Text("${d.type} • ${(d.confidence*100).toInt()}% • ${d.source}")};Button(onClick=vm::generatePlan,modifier=Modifier.padding(top=8.dp)){Text("Analyze base & build plan")}}}

@Composable fun PlannerScreen(vm:WarViewModel){Header("ATTACK PLANNER","Generated from detected geometry and the selected army mode");val plan=vm.plan;if(plan==null){PanelCard{Text("No plan yet");Button(onClick=vm::generatePlan){Text("Generate tactical plan")}}}else{PanelCard{Text(plan.name,style=MaterialTheme.typography.titleLarge);Text("Score ${plan.score.score}/100",color=Gold);plan.score.reasons.forEach{Text("✓ $it",color=Color.LightGray)};plan.score.risks.forEach{Text("! $it",color=Color(0xFFFFA6A6))}};Spacer(Modifier.height(10.dp));vm.imageBitmap?.let{bmp->PanelCard{Text("Tactical map",style=MaterialTheme.typography.titleMedium);Box(Modifier.fillMaxWidth().heightIn(max=440.dp)){Image(bmp.asImageBitmap(),null,Modifier.fillMaxSize());Canvas(Modifier.fillMaxSize()){val sx=size.width/bmp.width;val sy=size.height/bmp.height;fun px(p:com.cocwarassistant.core.Point)=androidx.compose.ui.geometry.Offset(p.x*sx,p.y*sy);plan.route.zipWithNext().forEach{(a,b)->drawLine(Color(0xFFE23B3B),px(a),px(b),strokeWidth=6f)};plan.steps.forEach{step->drawCircle(if(step.colorKey=="cleanup") Color(0xFFD6B45A) else Color(0xFFE23B3B),10f,px(step.point))};vm.traps.forEach{t->drawCircle(if(t.probability==com.cocwarassistant.core.Probability.HIGH) Color(0xFFFFA6A6) else Color(0xFFFFC857),7f,px(t.point))}}}}};Spacer(Modifier.height(10.dp));PanelCard{Text("Deployment sequence",style=MaterialTheme.typography.titleMedium);plan.steps.forEach{Text("${it.order}. ${it.label}: ${it.description}",modifier=Modifier.padding(vertical=5.dp))}}}}

@Composable fun ArmyScreen(vm:WarViewModel){Header("MY ARMY","Current profile capabilities");PanelCard{Text("Profile-aware validation");Text("Edit troop levels and unlocks through the account data model. Plans never add units marked locked.",color=Color.Gray);Button(onClick=vm::generateMyArmyPlan){Text("Generate with my army")}}}
@Composable fun RecommendedScreen(vm:WarViewModel){Header("AI RECOMMENDED ARMY","Local heuristic optimizer — no paid AI API");PanelCard{Text("Recommendation engine");Text("The current engine evaluates air-vs-ground pressure using detected defenses and builds a capacity-safe candidate. Exact level metadata must be supplied by the profile.",color=Color.Gray);Button(onClick=vm::generateRecommendedPlan){Text("Generate recommendation")}}}
@Composable fun HiddenScreen(vm:WarViewModel){Header("HIDDEN DEFENSE ANALYSIS","Probabilities only — never confirmed detections");PanelCard{if(vm.traps.isEmpty())Text("Generate a plan to estimate likely trap positions.") else vm.traps.forEach{Text("${it.probability} • ${it.type} @ ${it.point.x.toInt()},${it.point.y.toInt()} — ${it.rationale}",modifier=Modifier.padding(vertical=5.dp))}}}
@Composable fun HistoryScreen(vm:WarViewModel){val analyses by vm.analyses.collectAsState(initial=emptyList());Header("ATTACK HISTORY","Local Room database");if(analyses.isEmpty())PanelCard{Text("No saved analyses yet.")}else analyses.forEach{a->PanelCard{Text(a.strategySummary);Text("${a.buildingCount} structures • ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(a.createdAt))}",color=Color.Gray);Button(onClick={vm.deleteAnalysis(a.id)}){Text("Delete")}}}}
@Composable fun GameDataScreen(){Header("GAME DATA","Versioned local catalog");PanelCard{Text("${com.cocwarassistant.gamedata.GameDataCatalog.CURRENT_VERSION}",style=MaterialTheme.typography.titleLarge);Text("Supported baseline: TH1–TH18. TH19 is not yet included because Supercell announced it for mid-November 2026; the schema is extensible and avoids inventing unverified statistics.",color=Color.Gray);Text("Defense classes: ${com.cocwarassistant.gamedata.GameDataCatalog.packageData.defenses.size}",modifier=Modifier.padding(top=8.dp))}}
@Composable fun SettingsScreen(vm:WarViewModel){Header("SETTINGS","Privacy, overlay and model management");PanelCard{Text("Floating tactical companion");Row{Button(onClick=vm::requestOverlay){Text("Overlay permission")};Spacer(Modifier.width(8.dp));Button(onClick=vm::startOverlay){Text("Start companion")}};Text("The overlay never injects into or automates Clash of Clans. Android user consent is required.",color=Color.Gray,fontSize=12.sp)};PanelCard{Text("Local model");Text("Install a compatible trained detector into the app's managed model directory. No bundled fake detections are used.",color=Color.Gray)}}
