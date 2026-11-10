package com.cocwar.assistant

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.cocwar.assistant.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainVm(app: Application) : AndroidViewModel(app) {
    private val dao = AppDb.get(app).profiles()
    val profiles = dao.all().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun add(name: String, tag: String, th: Int) { viewModelScope.launch { dao.insert(ProfileEntity(name = name, tag = tag, townHall = th)) } }
    fun remove(p: ProfileEntity) { viewModelScope.launch { dao.delete(p) } }
}

enum class Section(val title: String, val status: String) {
    Dashboard("Dashboard", "Partial (Phase 1)"),
    Accounts("Account Manager", "Partial: profiles only; troops/spells/heroes in Phase 2"),
    WarAnalyzer("War Analyzer", "Not implemented (Phase 3, needs trained model + dataset)"),
    Planner("Attack Planner", "Not implemented (Phase 4)"),
    MyArmy("My Army", "Not implemented (Phase 5)"),
    AiArmy("AI Recommended Army", "Not implemented (Phase 5)"),
    Hidden("Hidden Defense Analysis", "Not implemented (Phase 4)"),
    History("Attack History", "Not implemented (Phase 6)"),
    Data("Game Data", "Partial: version metadata only (Phase 2)"),
    Settings("Settings", "Not implemented (overlay in Phase 7)")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Color(0xFFFF5722), secondary = Color(0xFFFFC107),
                background = Color(0xFF0E0E10), surface = Color(0xFF1A1A1D))) {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { App() }
            }
        }
    }
}

@Composable
fun App(vm: MainVm = viewModel()) {
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var current by remember { mutableStateOf(Section.Dashboard) }
    val profiles by vm.profiles.collectAsStateWithLifecycle()
    ModalNavigationDrawer(drawerState = drawer, drawerContent = {
        ModalDrawerSheet {
            Text("COMMAND CENTER", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.primary)
            Section.values().forEach { s ->
                NavigationDrawerItem(label = { Text(s.title) }, selected = s == current,
                    onClick = { current = s; scope.launch { drawer.close() } })
            }
        }
    }) {
        Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
            Row { TextButton(onClick = { scope.launch { drawer.open() } }) { Text("☰ Menu") }
                Text(current.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 8.dp)) }
            Spacer(Modifier.height(12.dp))
            when (current) {
                Section.Dashboard -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) {
                    Text("Profiles: ${profiles.size}")
                    Text("Game data: ${GameData.DATA_VERSION}")
                    Text("Supported TH range (configured, unvalidated): 1–${GameData.MAX_TOWN_HALL}") } }
                Section.Accounts -> Accounts(profiles, vm)
                else -> Card(Modifier.fillMaxWidth()) { Text("Status: ${current.status}", Modifier.padding(16.dp)) }
            }
        }
    }
}

@Composable
fun Accounts(profiles: List<ProfileEntity>, vm: MainVm) {
    var name by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }
    var th by remember { mutableStateOf("") }
    val lvl = th.toIntOrNull()
    val ok = lvl != null && GameData.isValidTownHall(lvl)
    Column {
        OutlinedTextField(name, { name = it }, label = { Text("Player name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(tag, { tag = it }, label = { Text("Tag (optional)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(th, { th = it.filter(Char::isDigit) }, label = { Text("Town Hall level") },
            isError = th.isNotEmpty() && !ok, modifier = Modifier.fillMaxWidth())
        Button(enabled = name.isNotBlank() && ok,
            onClick = { vm.add(name.trim(), tag.trim(), lvl!!); name = ""; tag = ""; th = "" }) { Text("Add profile") }
        LazyColumn { items(profiles) { p ->
            Card(Modifier.fillMaxWidth().padding(top = 8.dp)) { Row(Modifier.padding(12.dp)) {
                Text("${p.name} — TH${p.townHall}", Modifier.weight(1f))
                TextButton(onClick = { vm.remove(p) }) { Text("Delete") } } } } }
    }
}
