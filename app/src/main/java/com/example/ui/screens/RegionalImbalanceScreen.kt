package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.repository.BloodDatabase
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun RegionalImbalanceScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language
    val alerts = BloodDatabase.regionalImbalanceAlerts

    var showActionDialog by remember { mutableStateOf(false) }

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
                        text = "⚠️ " + if (language == AppLanguage.MARATHI) "प्रादेशिक रक्त असंतुलन व AI तुटवडा अंदाज"
                        else if (language == AppLanguage.HINDI) "क्षेत्रीय रक्त असंतुलन एवं पूर्वानुमान"
                        else "Regional Imbalance & AI Shortage Alert",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "Intelligent Surplus → Shortage Transfer & Mobilisation Advisory",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            MedicalDisclaimerBanner(language = language)
        }

        // Section: AI Demand Forecast Model Summary (Requirement 33)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = GovNavyPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🤖", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Blood Demand Forecast & Mobilisation Engine",
                            color = NationalSaffronLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "ही प्रणाली मागील ३ वर्षांचा वापर, सणासुदीचे दिवस, आपत्कालीन ट्रॉमा आकडेवारी व हवामान स्थिती यांवर आधारित ७ दिवसांतील संभाव्य तुटवड्याचा अंदाज घेते. 'Do not automatically transfer blood. Final decision must remain with authorized blood services.'",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Section: Active Regional Imbalance Alerts (Requirement 23)
        item {
            Text(
                text = "सक्रिय प्रादेशिक असंतुलन सूचना (ACTIVE ALERTS):",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = EmergencyCrimson
            )
        }

        items(alerts) { alert ->
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
                        Text(
                            text = alert.title,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = EmergencyCrimson,
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFEBEE))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("ACTION NEEDED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmergencyCrimson)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Regional cluster comparison
                    Surface(
                        color = Color(0xFFF5F7FA),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("कोल्हापूर (Kolhapur):", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("O+ = Adequate (${alert.surplusUnits} u) 🟢", fontWeight = FontWeight.Bold, color = NationalGreenDark, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("सांगली (Sangli):", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("O+ = Critical (${alert.shortageUnits} u) 🔴", fontWeight = FontWeight.Bold, color = EmergencyCrimson, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("सातारा (Satara):", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("O+ = Low (14 u) 🟡", fontWeight = FontWeight.Bold, color = Color(0xFFF57F17), fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "शिफारस कृती (Recommended Action):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = alert.recommendedAction,
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { showActionDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("initiate_inter_district_transfer_btn")
                        ) {
                            Text("अधिकृत हस्तांतरण समन्वय", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { onNavigate(AppScreen.MAP_VIEW) },
                            modifier = Modifier.weight(0.8f)
                        ) {
                            Text("नकाशा पहा", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Section: Donor Mobilisation Cascade (Requirement 33)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🔄 आपत्कालीन रक्तदाता संघटन पायऱ्या (Mobilisation Hierarchy)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val steps = listOf(
                        "1. Required Blood Group & Component Identified (उदा. O+ Whole Blood)",
                        "2. Nearby eligible consenting donors in local Taluka alerted via SMS/App",
                        "3. District-level verified donors mobilised for voluntary camp",
                        "4. Nearby district donors (Kolhapur ↔ Sangli border) alerted",
                        "5. State-level Blood Transfusion Council emergency mobilization"
                    )

                    steps.forEach { step ->
                        Text(step, fontSize = 11.sp, color = Color.DarkGray, lineHeight = 16.sp, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }
        }
    }

    if (showActionDialog) {
        AlertDialog(
            onDismissRequest = { showActionDialog = false },
            icon = { Icon(Icons.Default.ShareLocation, contentDescription = null, tint = GovNavyPrimary, modifier = Modifier.size(32.dp)) },
            title = {
                Text(
                    text = "आंतर-जिल्हा रक्त समन्वय प्रस्ताव (Inter-District Transfer Proposal)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "CPR कोल्हापूर रक्त केंद्रातून मिरज GMC रक्त केंद्राकडे २० युनिट O+ रक्त पाठवण्याचा समन्वय प्रस्ताव राज्य रक्त संक्रमण परिषदेकडे (SBTC) पाठवला गेला आहे.",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "नोंद: अंतिम वैद्यकीय मंजुरी व प्रत्यक्ष वाहतूक अधिकृत शासकीय प्रोटोकॉलनुसार होईल.",
                        fontSize = 11.sp,
                        color = EmergencyCrimson,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showActionDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary)
                ) {
                    Text("स्वीकारा (Acknowledge)")
                }
            }
        )
    }
}
