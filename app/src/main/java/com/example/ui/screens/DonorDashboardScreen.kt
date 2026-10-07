package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import com.example.data.model.BloodComponent
import com.example.data.model.DonationRecord
import com.example.data.repository.BloodDatabase
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun DonorDashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language

    // Find active demo donor or fallback
    val currentDonor = BloodDatabase.syntheticDonors.firstOrNull { it.id == state.currentDonorId }
        ?: BloodDatabase.syntheticDonors.first()

    var emergencyAvailability by remember { mutableStateOf(currentDonor.emergencyAvailable) }

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
                        text = if (language == AppLanguage.MARATHI) "माझे प्रोफाइल व विनंत्या (Donor Dashboard)"
                        else if (language == AppLanguage.HINDI) "मेरा प्रोफ़ाइल एवं अनुरोध (Donor Dashboard)"
                        else "Citizen Donor Dashboard",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "ID: ${currentDonor.id} • ${currentDonor.district}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Profile Overview Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(EmergencyCrimson),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentDonor.bloodGroup.code,
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "रक्तगट: ${currentDonor.bloodGroup.displayName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = GovNavyPrimary
                                )
                                Text(
                                    text = "पडताळणी: Verified (e-RaktKosh Lab)",
                                    fontSize = 11.sp,
                                    color = NationalGreenDark,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (emergencyAvailability) Color(0xFFE8F5E9) else Color(0xFFFFFDE7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (emergencyAvailability) "🟢 Available" else "🟡 Unavailable",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (emergencyAvailability) NationalGreenDark else Color(0xFFF57F17)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("स्थान / Location", fontSize = 10.sp, color = Color.Gray)
                            Text("${currentDonor.cityOrTaluka}, ${currentDonor.district}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text("पुढील संभाव्य पात्रता / Next Eligibility", fontSize = 10.sp, color = Color.Gray)
                            Text("As determined by blood centre", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = GovNavyPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Emergency Availability Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF5F7FA))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Emergency Availability", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("आपत्कालीन संदेश प्राप्त करण्याची स्थिती", fontSize = 10.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = emergencyAvailability,
                            onCheckedChange = { emergencyAvailability = it }
                        )
                    }
                }
            }
        }

        // Section: Active Emergency Requests Alert (Requirement 13)
        item {
            Text(
                text = "🆘 आपत्कालीन विनंती सूचना (EMERGENCY ALERTS)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = EmergencyCrimson
            )
        }

        val matchingActiveRequests = BloodDatabase.activeRequests.filter {
            it.bloodGroup == currentDonor.bloodGroup || currentDonor.bloodGroup.code == "O+" || currentDonor.bloodGroup.code == "O-"
        }

        if (matchingActiveRequests.isEmpty()) {
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "सध्या आपल्या रक्तगटासाठी कोणतीही सक्रिय आपत्कालीन विनंती प्रलंबित नाही.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(matchingActiveRequests) { req ->
                var isAcceptedLocally by remember { mutableStateOf(false) }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(EmergencyCrimson, EmergencyCrimsonDark))),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🆘 EMERGENCY BLOOD REQUEST",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = EmergencyCrimson
                            )
                            Text(
                                text = req.createdAt,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Blood Group: ${req.bloodGroup.code} | Required: ${req.unitsRequired} Units (${req.component.displayName})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Hospital: ${req.hospitalName}",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Location: ${req.district} • Approx Distance: 4 km",
                            fontSize = 11.sp,
                            color = GovNavyPrimary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "गोपनीयता सूचना: रुग्णाची खाजगी माहिती सुरक्षित आहे.",
                            fontSize = 9.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (isAcceptedLocally) {
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NationalGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "विनंती स्वीकारली! अधिकृत रक्त केंद्र आपल्याशी समन्वयासाठी संपर्क करेल.",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NationalGreenDark
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        isAcceptedLocally = true
                                        viewModel.acceptEmergencyRequest(req.id, currentDonor.id)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .testTag("accept_emergency_request_button")
                                ) {
                                    Text("ACCEPT REQUEST", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {},
                                    modifier = Modifier.weight(0.8f)
                                ) {
                                    Text("NOT AVAILABLE", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Donation History (Item 25)
        item {
            Text(
                text = "📋 रक्तदान इतिहास (Donation History)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = GovNavyPrimary
            )
        }

        val history = listOf(
            DonationRecord("DON-2026-01", "15 Jan 2026", "CPR Civil Hospital Blood Centre, Kolhapur", BloodComponent.WHOLE_BLOOD, 1, "CERT-MH-88190"),
            DonationRecord("DON-2025-10", "12 Oct 2025", "Sanjeevani Blood Centre, Kolhapur", BloodComponent.PRBC, 1, "CERT-MH-77401")
        )

        items(history) { record ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(record.centreName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GovNavyPrimary)
                        Text("${record.date} • ${record.component.displayName}", fontSize = 10.sp, color = Color.Gray)
                        Text("Govt Certificate: ${record.certificateId}", fontSize = 10.sp, color = NationalGreenDark)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("VERIFIED 🟢", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NationalGreenDark)
                    }
                }
            }
        }
    }
}
