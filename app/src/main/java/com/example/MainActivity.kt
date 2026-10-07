package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppLanguage
import com.example.ui.components.AuthDialog
import com.example.ui.components.GovernmentHeader
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    mainViewModel: MainViewModel = viewModel()
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Display snackbar message if triggered in ViewModel
    LaunchedEffect(uiState.successSnackbarMessage) {
        uiState.successSnackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            mainViewModel.clearSnackbar()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                language = uiState.language,
                currentScreen = uiState.currentScreen,
                onNavigate = { screen -> mainViewModel.navigateTo(screen) },
                onOpenRoleDialog = { mainViewModel.toggleAuthDialog(true) },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                GovernmentHeader(
                    currentLanguage = uiState.language,
                    activeRole = uiState.activeRole,
                    onLanguageSelected = { lang -> mainViewModel.setLanguage(lang) },
                    onRoleClick = { mainViewModel.toggleAuthDialog(true) },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onEmergencySosClick = { mainViewModel.navigateTo(AppScreen.FIND_BLOOD) }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = GovNavyPrimary,
                    contentColor = Color.White,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("main_bottom_nav_bar")
                ) {
                    val navItems = listOf(
                        Triple(AppScreen.HOME, if (uiState.language == AppLanguage.MARATHI) "मुख्य" else "Home", Icons.Default.Home),
                        Triple(AppScreen.FIND_BLOOD, if (uiState.language == AppLanguage.MARATHI) "रक्त शोधा 🆘" else "Find Blood 🆘", Icons.Default.Emergency),
                        Triple(AppScreen.STOCK_DASHBOARD, if (uiState.language == AppLanguage.MARATHI) "साठा" else "Stock", Icons.Default.Assessment),
                        Triple(AppScreen.REGISTER_DONOR, if (uiState.language == AppLanguage.MARATHI) "नोंदणी ❤️" else "Register ❤️", Icons.Default.VolunteerActivism)
                    )

                    navItems.forEach { (screen, label, icon) ->
                        val isSelected = uiState.currentScreen == screen
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { mainViewModel.navigateTo(screen) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.65f)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    color = if (isSelected) GovernmentGold else Color.White.copy(alpha = 0.75f)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = EmergencyCrimson,
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.65f)
                            )
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (uiState.currentScreen) {
                    AppScreen.HOME -> HomeScreen(
                        language = uiState.language,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.FIND_BLOOD -> FindBloodScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.EMERGENCY_REQUEST -> EmergencyRequestScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.REGISTER_DONOR -> RegisterDonorScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.DONOR_DASHBOARD -> DonorDashboardScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.BLOOD_CENTRES -> BloodCentresScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.STOCK_DASHBOARD -> StockDashboardScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.RARE_REGISTRY -> RareBloodRegistryScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.REGIONAL_IMBALANCE -> RegionalImbalanceScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.MAP_VIEW -> MapDrilldownScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.ADMIN_DASHBOARD -> AdminDashboardScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                    AppScreen.AUDIT_LOGS -> AuditLogScreen(
                        viewModel = mainViewModel,
                        onNavigate = { scr -> mainViewModel.navigateTo(scr) }
                    )
                }
            }
        }
    }

    // Role switcher dialog
    if (uiState.isAuthDialogOpen) {
        AuthDialog(
            language = uiState.language,
            currentRole = uiState.activeRole,
            onRoleSelected = { role -> mainViewModel.setActiveRole(role) },
            onDismiss = { mainViewModel.toggleAuthDialog(false) }
        )
    }
}
