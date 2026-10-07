package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.UserRole
import com.example.data.repository.BloodDatabase
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language
    val role = state.activeRole

    var exportNoticeDialog by remember { mutableStateOf<String?>(null) }

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
                        text = "🏛️ " + if (language == AppLanguage.MARATHI) "शासकीय प्रशासकीय डॅशबोर्ड"
                        else if (language == AppLanguage.HINDI) "सरकारी प्रशासनिक डैशबोर्ड"
                        else "Government Governance & Analytics",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "National MoHFW / State SBTC / District Level Governance",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            MedicalDisclaimerBanner(language = language)
        }

        // Active Admin Credentials & Security Notice (Item 40)
        item {
            Surface(
                color = GovNavyContainer,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Admin Session: ${role.displayName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GovNavyPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(EmergencyCrimson)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("DEMO ONLY", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Authorized credentials verified under e-RaktKosh National Health Protocol. All operations are logged to tamper-evident audit trails.",
                        fontSize = 10.sp,
                        color = GovNavyPrimary
                    )
                }
            }
        }

        // Section: National / State KPI Cards (Requirement 20 & 21)
        item {
            Text(
                text = "📊 राष्ट्रीय व राज्यस्तरीय मुख्य निर्देशांक (National KPIs)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = GovNavyPrimary
            )
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminMetricCard("Registered Donors", "${BloodDatabase.syntheticDonors.size}+", "Verified Donors", EmergencyCrimson, Modifier.weight(1f))
                AdminMetricCard("Blood Centres", "${BloodDatabase.syntheticBloodCentres.size}", "e-RaktKosh Connected", GovNavyPrimary, Modifier.weight(1f))
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminMetricCard("Emergency Requests", "${BloodDatabase.activeRequests.size}", "Hospital Dispatches", StatusShortageOrange, Modifier.weight(1f))
                AdminMetricCard("Critical Shortages", "2 Districts", "Sangli, Solapur Cluster", StatusCriticalRed, Modifier.weight(1f))
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminMetricCard("Rare Blood Requests", "1 Active", "Bombay Oh (CPR Lab)", NationalSaffronDark, Modifier.weight(1f))
                AdminMetricCard("Donor Response Rate", "89.4%", "Within 15 mins", NationalGreenDark, Modifier.weight(1f))
            }
        }

        // Section: Maharashtra State Summary (Requirement 21)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "महाराष्ट्र राज्य साठा सारांश (Maharashtra State Stock Status)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val mhStock = listOf(
                        "O+ Stock: 420 Units 🟢" to "O- Stock: 35 Units 🔴",
                        "A+ Stock: 380 Units 🟢" to "A- Stock: 42 Units 🟡",
                        "B+ Stock: 360 Units 🟢" to "B- Stock: 28 Units 🔴",
                        "AB+ Stock: 110 Units 🟢" to "AB- Stock: 18 Units 🔴"
                    )

                    mhStock.forEach { (col1, col2) ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(col1, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            Text(col2, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        // Section: Reports Generation & CSV/PDF Export (Requirement 31)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📑 अधिकृत शासकीय अहवाल (Official Reports & CSV Export)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val reports = listOf(
                        "Daily Blood Stock Report (दैनिक साठा अहवाल)",
                        "Weekly Donor Mobilisation Report (साप्ताहिक संघटन अहवाल)",
                        "District-wise Shortage & Wastage Report (जिल्हानिहाय तुटवडा अहवाल)",
                        "Rare Blood Registry Audit (दुर्मीळ रक्तगट अहवाल)"
                    )

                    reports.forEach { rep ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(rep, fontSize = 11.sp, modifier = Modifier.weight(1f))
                            OutlinedButton(
                                onClick = { exportNoticeDialog = rep },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("CSV", fontSize = 10.sp)
                            }
                        }
                        Divider(color = Color(0xFFF5F5F5))
                    }
                }
            }
        }

        // Section: Government Integration Architecture (Requirement 32)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = GovNavyPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🇮🇳", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "e-RaktKosh Government API Integration Architecture",
                            color = NationalSaffronLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "• Integration Ready: e-RaktKosh REST Endpoints (NIC Health Stack)\n• State Blood Transfusion Council (SBTC) Real-time Sync\n• National Health Mission (NHM) Data Minimization Protocol\n• No scraping: Official authenticated mutual TLS data exchange",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Action: View Audit Logs
        item {
            Button(
                onClick = { onNavigate(AppScreen.AUDIT_LOGS) },
                colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_open_audit_logs_btn")
            ) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("सुरक्षा व पडताळणी नोंदी पहा (VIEW AUDIT LOGS)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }

    if (exportNoticeDialog != null) {
        AlertDialog(
            onDismissRequest = { exportNoticeDialog = null },
            icon = { Icon(Icons.Default.FileDownloadDone, contentDescription = null, tint = NationalGreen, modifier = Modifier.size(32.dp)) },
            title = { Text("अहवाल निर्यात यशस्वी (Report Generated)") },
            text = {
                Text(
                    text = "$exportNoticeDialog ची CSV फाइल शासकीय प्रमाणीकरण डिजिटल स्वाक्षरीसह तयार करण्यात आली आहे (Demo Export Simulated).",
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { exportNoticeDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun AdminMetricCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = accentColor)
            Text(subtitle, fontSize = 9.sp, color = Color.DarkGray)
        }
    }
}
