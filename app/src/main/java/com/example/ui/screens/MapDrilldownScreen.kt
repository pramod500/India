package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.BloodGroup
import com.example.data.model.LocationHierarchy
import com.example.data.repository.BloodDatabase
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun MapDrilldownScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language

    // Drilldown levels: 0 = India (States), 1 = Maharashtra (Districts), 2 = Kolhapur (Talukas/Centres)
    var drillLevel by remember { mutableStateOf(1) } // Default directly to Maharashtra for instant deep demo
    var selectedState by remember { mutableStateOf("Maharashtra") }
    var selectedDistrict by remember { mutableStateOf("Kolhapur") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "🗺️ " + if (language == AppLanguage.MARATHI) "भारत व राज्यस्तरीय परस्परसंवादी नकाशा"
                        else if (language == AppLanguage.HINDI) "भारत एवं राज्यस्तरीय इंटरेक्टिव मानचित्र"
                        else "India & State Interactive Blood Map",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "Geographic Availability Drilldown (India → State → District → Taluka)",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            MedicalDisclaimerBanner(language = language)
        }

        // Breadcrumb Trail (Requirement 19: Click India → Maharashtra → Kolhapur → Taluka)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🇮🇳 India",
                    fontWeight = if (drillLevel == 0) FontWeight.Bold else FontWeight.Normal,
                    color = if (drillLevel == 0) EmergencyCrimson else GovNavyPrimary,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { drillLevel = 0 }
                )
                Text(" > ", color = Color.Gray)
                Text(
                    text = selectedState,
                    fontWeight = if (drillLevel == 1) FontWeight.Bold else FontWeight.Normal,
                    color = if (drillLevel == 1) EmergencyCrimson else GovNavyPrimary,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { drillLevel = 1 }
                )
                if (drillLevel >= 2) {
                    Text(" > ", color = Color.Gray)
                    Text(
                        text = selectedDistrict,
                        fontWeight = FontWeight.Bold,
                        color = EmergencyCrimson,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { drillLevel = 2 }
                    )
                }
            }
        }

        // Legend: Adequate 🟢, Low 🟡, Shortage 🟠, Critical 🔴
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🟢 Adequate", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusAdequateGreen)
                    Text("🟡 Low", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusLowYellow)
                    Text("🟠 Shortage", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusShortageOrange)
                    Text("🔴 Critical", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusCriticalRed)
                }
            }
        }

        // Visual Map Representation Container
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = GovNavyPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = when (drillLevel) {
                            0 -> "सर्व राज्ये व केंद्रशासित प्रदेश (Click a State to Zoom)"
                            1 -> "महाराष्ट्र राज्य जिल्हा साठा नकाशा (Click a District)"
                            else -> "$selectedDistrict जिल्हा तालुका व रक्तपेढी दृश्य"
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Do not expose individual donor home locations. Aggregated geographic view.",
                        color = NationalSaffronLight,
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (drillLevel == 0) {
                        // All States Grid
                        val majorStates = listOf(
                            Triple("Maharashtra", "🟢 Adequate Overall", StatusAdequateGreen),
                            Triple("Karnataka", "🟢 Adequate Overall", StatusAdequateGreen),
                            Triple("Gujarat", "🟢 Adequate Overall", StatusAdequateGreen),
                            Triple("Uttar Pradesh", "🟡 Low Stock in East", StatusLowYellow),
                            Triple("Delhi", "🟢 Adequate Overall", StatusAdequateGreen),
                            Triple("Tamil Nadu", "🟢 Adequate Overall", StatusAdequateGreen)
                        )
                        majorStates.forEach { (st, desc, col) ->
                            Surface(
                                color = GovNavySurface,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedState = st
                                        drillLevel = 1
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(st, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(desc, color = col, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
                                }
                            }
                        }
                    } else if (drillLevel == 1) {
                        // Maharashtra Districts Interactive Representation
                        val mhDistricts = listOf(
                            Triple("Kolhapur", "Adequate (O+: 140u, A+: 120u) 🟢", StatusAdequateGreen),
                            Triple("Sangli", "Critical Shortage (O+: 11u) 🔴", StatusCriticalRed),
                            Triple("Satara", "Low Stock (O+: 14u) 🟡", StatusLowYellow),
                            Triple("Pune", "Adequate (O+: 160u) 🟢", StatusAdequateGreen),
                            Triple("Mumbai", "Adequate (Large Reserve) 🟢", StatusAdequateGreen),
                            Triple("Solapur", "Shortage (B-: 4u) 🟠", StatusShortageOrange)
                        )

                        mhDistricts.forEach { (dist, statusText, color) ->
                            Surface(
                                color = GovNavySurface,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedDistrict = dist
                                        drillLevel = 2
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(dist, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(statusText, color = color, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    Icon(Icons.Default.ZoomIn, contentDescription = "Drill into district", tint = GovernmentGold)
                                }
                            }
                        }
                    } else {
                        // District Level -> Talukas & Blood Centres
                        val talukas = LocationHierarchy.getTalukasForDistrict(selectedDistrict)
                        talukas.take(6).forEach { taluka ->
                            Surface(
                                color = GovNavySurface,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(taluka, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("Donor Coverage: 18+ Donors • 1 Centre", color = Color.LightGray, fontSize = 10.sp)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(NationalGreen)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("ACTIVE 🟢", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Associated Blood Centres for Selected District
        item {
            Text(
                text = "$selectedDistrict रक्तपेढ्या व साठा उपलब्धता (Blood Centres & Stocks)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = GovNavyPrimary
            )
        }

        val districtCentres = BloodDatabase.syntheticBloodCentres.filter {
            it.district.equals(selectedDistrict, true)
        }

        items(districtCentres) { centre ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(centre.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GovNavyPrimary)
                    Text("स्थान: ${centre.address} • Helpline: ${centre.officialHelpline}", fontSize = 11.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "O+: ${centre.getUnitsFor(BloodGroup.O_POS)} u | O-: ${centre.getUnitsFor(BloodGroup.O_NEG)} u | A+: ${centre.getUnitsFor(BloodGroup.A_POS)} u | B+: ${centre.getUnitsFor(BloodGroup.B_POS)} u",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NationalGreenDark
                    )
                }
            }
        }
    }
}
