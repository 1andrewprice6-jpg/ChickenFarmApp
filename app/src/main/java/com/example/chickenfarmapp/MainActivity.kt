package com.example.chickenfarmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chickenfarmapp.data.ChickenFarmDatabase
import com.example.chickenfarmapp.ui.theme.ChickenFarmAppTheme
import com.example.chickenfarmapp.viewmodel.ChickenViewModel
import com.example.chickenfarmapp.viewmodel.ChickenViewModelFactory
import com.example.chickenfarmapp.viewmodel.EggLogViewModel
import com.example.chickenfarmapp.viewmodel.EggLogViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChickenFarmAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChickenFarmApp()
                }
            }
        }
    }
}

@Composable
fun ChickenFarmApp() {
    val context = LocalContext.current
    val database = remember { ChickenFarmDatabase.getDatabase(context) }

    val chickenViewModel: ChickenViewModel = viewModel(
        factory = ChickenViewModelFactory(database.chickenDao())
    )
    val eggLogViewModel: EggLogViewModel = viewModel(
        factory = EggLogViewModelFactory(database.eggLogDao())
    )

    var currentScreen by remember { mutableStateOf("main") }

    when (currentScreen) {
        "main" -> MainScreen(
            onManageChickensClicked = { currentScreen = "manage_chickens" },
            onTrackEggsClicked = { currentScreen = "track_eggs" }
        )
        "manage_chickens" -> ManageChickensScreen(onBack = { currentScreen = "main" }, viewModel = chickenViewModel)
        "track_eggs" -> TrackEggsScreen(onBack = { currentScreen = "main" }, viewModel = eggLogViewModel, chickenViewModel = chickenViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackEggsScreen(onBack: () -> Unit, viewModel: EggLogViewModel = viewModel(), chickenViewModel: ChickenViewModel = viewModel()) {
    val eggLogs by viewModel.eggLogs.collectAsState()
    val chickens by chickenViewModel.chickens.collectAsState()
    var showAddEggLogDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Eggs") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddEggLogDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Egg Log"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(eggLogs) { eggLog ->
                val chicken = chickens.find { it.id == eggLog.chickenId }
                EggLogCard(eggLog = eggLog, chickenName = chicken?.name ?: "Unknown")
            }

            if (eggLogs.isEmpty()) {
                item {
                    EmptyStateCard(
                        icon = Icons.Default.Info,
                        message = "No egg logs yet. Tap + to add your first log!"
                    )
                }
            }
        }
    }

    if (showAddEggLogDialog) {
        AddEggLogDialog(
            chickens = chickens,
            onAddEggLog = { chickenId, count ->
                viewModel.addEggLog(chickenId, count)
                showAddEggLogDialog = false
            },
            onDismiss = { showAddEggLogDialog = false }
        )
    }
}

@Composable
fun EggLogCard(eggLog: com.example.chickenfarmapp.data.EggLog, chickenName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chickenName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault()).format(eggLog.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "${eggLog.count}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEggLogDialog(
    chickens: List<com.example.chickenfarmapp.data.Chicken>,
    onAddEggLog: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedChicken by remember { mutableStateOf(chickens.firstOrNull()) }
    var count by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Egg Log") },
        text = {
            Column {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = selectedChicken?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        chickens.forEach { chicken ->
                            DropdownMenuItem(
                                text = { Text(chicken.name) },
                                onClick = {
                                    selectedChicken = chicken
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = count,
                    onValueChange = { count = it },
                    label = { Text("Count") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedChicken?.let {
                        onAddEggLog(it.id, count.toIntOrNull() ?: 0)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MainScreen(
    onManageChickensClicked: () -> Unit,
    onTrackEggsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Chicken Farm Manager",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onManageChickensClicked) {
            Text("Manage Chickens")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onTrackEggsClicked) {
            Text("Track Eggs")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageChickensScreen(onBack: () -> Unit, viewModel: ChickenViewModel = viewModel()) {
    val chickens by viewModel.chickens.collectAsState()
    var showAddChickenDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Manage Chickens",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(chickens) { chicken ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = chicken.name)
                    Text(text = chicken.breed)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showAddChickenDialog = true }) {
            Text("Add Chicken")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }

    if (showAddChickenDialog) {
        AddChickenDialog(
            onAddChicken = { name, breed ->
                viewModel.addChicken(name, breed)
                showAddChickenDialog = false
            },
            onDismiss = { showAddChickenDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChickenDialog(
    onAddChicken: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Chicken") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = { Text("Breed") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAddChicken(name, breed) }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun EmptyStateCard(
    icon: ImageVector,
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChickenFarmAppTheme {
        ChickenFarmApp()
    }
}