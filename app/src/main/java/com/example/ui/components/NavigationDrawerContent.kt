package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen

data class DrawerMenuItem(
    val screen: AppScreen,
    val titleMar: String,
    val titleEng: String,
    val icon: ImageVector,
    val isEmergency: Boolean = false
)

@Composable
fun NavigationDrawerContent(
    language: AppLanguage,
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onOpenRoleDialog: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val menuItems = listOf(
        DrawerMenuItem(AppScreen.HOME, "मुख्यपृष्ठ (Home)", "Home", Icons.Default.Home),
        DrawerMenuItem(AppScreen.FIND_BLOOD, "रक्त शोधा (Find Blood 🆘)", "Find Blood 🆘", Icons.Default.Emergency, isEmergency = true),
        DrawerMenuItem(AppScreen.EMERGENCY_REQUEST, "रुग्णालय आपत्कालीन मागणी", "Hospital Request", Icons.Default.LocalHospital, isEmergency = true),
        DrawerMenuItem(AppScreen.REGISTER_DONOR, "रक्तदाता नोंदणी (Register)", "Register as Donor", Icons.Default.VolunteerActivism),
        DrawerMenuItem(AppScreen.DONOR_DASHBOARD, "माझे प्रोफाइल व विनंत्या", "Donor Dashboard", Icons.Default.Person),
        DrawerMenuItem(AppScreen.BLOOD_CENTRES, "रक्तपेढ्यांची यादी", "Blood Centres", Icons.Default.Domain),
        DrawerMenuItem(AppScreen.STOCK_DASHBOARD, "रक्त साठा डॅशबोर्ड", "Blood Availability", Icons.Default.Assessment),
        DrawerMenuItem(AppScreen.RARE_REGISTRY, "दुर्मीळ रक्तगट (Bombay Oh)", "Rare Blood Registry", Icons.Default.Biotech),
        DrawerMenuItem(AppScreen.REGIONAL_IMBALANCE, "प्रादेशिक असंतुलन अलर्ट", "Regional Imbalance", Icons.Default.Warning),
        DrawerMenuItem(AppScreen.MAP_VIEW, "नकाशा दृश्य (India Map)", "Interactive Map", Icons.Default.Map),
        DrawerMenuItem(AppScreen.ADMIN_DASHBOARD, "शासकीय प्रशासक नियंत्रण", "Government Admin", Icons.Default.AdminPanelSettings),
        DrawerMenuItem(AppScreen.AUDIT_LOGS, "सुरक्षा नोंदी (Audit Logs)", "Security Audit", Icons.Default.Security)
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.width(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GovNavyPrimary)
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🇮🇳", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "भारत सरकार | GOVT OF INDIA",
                            color = NationalSaffronLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "राष्ट्रीय रक्तसुरक्षा व आपत्कालीन रक्तदाता शोध प्रणाली",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "National Blood Emergency & Donor Finder",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }

            // Universal Search Input (Requirement 27)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("उदा. O+, Kolhapur, Blood Centre...", fontSize = 11.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("universal_search_input")
                )
            }

            // Filtered or full menu list
            val displayedItems = if (searchQuery.isBlank()) menuItems else {
                menuItems.filter {
                    it.titleMar.contains(searchQuery, true) || it.titleEng.contains(searchQuery, true)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                items(displayedItems) { item ->
                    val isSelected = currentScreen == item.screen
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (item.isEmergency) EmergencyCrimson else if (isSelected) GovNavyPrimary else Color.DarkGray
                            )
                        },
                        label = {
                            Text(
                                text = if (language == AppLanguage.MARATHI) item.titleMar else item.titleEng,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (item.isEmergency) EmergencyCrimson else Color.Black
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            onNavigate(item.screen)
                            onCloseDrawer()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .testTag("nav_item_${item.screen.name}")
                    )
                }
            }

            Divider()

            // Footer: Role Switcher & Dismiss
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCloseDrawer()
                        onOpenRoleDialog()
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SwitchAccount, contentDescription = null, tint = GovernmentGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "भूमिका बदला / Admin Login",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = GovNavyPrimary
                    )
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}
