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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.BloodDatabase
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.components.StockStatusBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun StockDashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language

    var selectedDistrict by remember { mutableStateOf("Kolhapur") }

    // Aggregated district statistics (Demo values based on Prompt Spec Item 22)
    val districtStockData = when (selectedDistrict) {
        "Kolhapur" -> listOf(
            Triple(BloodGroup.A_POS, 120, 80),
            Triple(BloodGroup.A_NEG, 15, 22),
            Triple(BloodGroup.B_POS, 105, 72),
            Triple(BloodGroup.B_NEG, 10, 18),
            Triple(BloodGroup.AB_POS, 35, 30),
            Triple(BloodGroup.AB_NEG, 5, 8),
            Triple(BloodGroup.O_POS, 140, 100),
            Triple(BloodGroup.O_NEG, 12, 25),
            Triple(BloodGroup.BOMBAY_OH, 1, 1)
        )
        "Sangli" -> listOf(
            Triple(BloodGroup.A_POS, 57, 45),
            Triple(BloodGroup.A_NEG, 6, 12),
            Triple(BloodGroup.B_POS, 65, 50),
            Triple(BloodGroup.B_NEG, 3, 10),
            Triple(BloodGroup.AB_POS, 20, 15),
            Triple(BloodGroup.AB_NEG, 1, 6),
            Triple(BloodGroup.O_POS, 11, 65), // CRITICAL
            Triple(BloodGroup.O_NEG, 1, 15),  // CRITICAL
            Triple(BloodGroup.BOMBAY_OH, 0, 1)
        )
        else -> listOf(
            Triple(BloodGroup.A_POS, 40, 30),
            Triple(BloodGroup.A_NEG, 5, 10),
            Triple(BloodGroup.B_POS, 38, 30),
            Triple(BloodGroup.B_NEG, 4, 8),
            Triple(BloodGroup.AB_POS, 14, 12),
            Triple(BloodGroup.AB_NEG, 2, 5),
            Triple(BloodGroup.O_POS, 14, 40),
            Triple(BloodGroup.O_NEG, 2, 10),
            Triple(BloodGroup.BOMBAY_OH, 0, 0)
        )
    }

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
                        text = "📊 " + if (language == AppLanguage.MARATHI) "रक्त साठा व तुटवडा डॅशबोर्ड"
                        else if (language == AppLanguage.HINDI) "रक्त भंडार एवं कमी डैशबोर्ड"
                        else "Blood Stock & Shortage Dashboard",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "District-wise Inventory, Shortage / Surplus Live Analytics",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            MedicalDisclaimerBanner(language = language)
        }

        // District Filter Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Kolhapur", "Sangli", "Satara").forEach { dist ->
                    val isSelected = selectedDistrict == dist
                    FilledTonalButton(
                        onClick = { selectedDistrict = dist },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isSelected) GovNavyPrimary else Color.White,
                            contentColor = if (isSelected) Color.White else GovNavyPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(dist, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: Shortage / Surplus Visual Live-Style Chart (Requirement 18)
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
                        Text(
                            text = "तुटवडा व अतिरिक्त साठा विश्लेषण ($selectedDistrict)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GovNavyPrimary
                        )
                        Text(
                            text = "Threshold: 20 u",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    districtStockData.forEach { (bg, stock, _) ->
                        StockStatusBar(bloodGroup = bg, units = stock, maxReferenceUnits = 150)
                    }
                }
            }
        }

        // Section: District Detailed Table (Requirement 22)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "जिल्हा साठा तपशील (District Stock vs Demand)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(GovNavyContainer)
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Blood Group", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovNavyPrimary, modifier = Modifier.weight(1.2f))
                        Text("Stock", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovNavyPrimary, modifier = Modifier.weight(0.8f))
                        Text("Demand", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovNavyPrimary, modifier = Modifier.weight(0.8f))
                        Text("Status", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovNavyPrimary, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Table Rows
                    districtStockData.forEach { (bg, stock, demand) ->
                        val isCritical = stock < demand
                        val statusEmoji = when {
                            stock >= demand + 10 -> "🟢 Adequate"
                            stock >= demand -> "🟡 Low"
                            stock in (demand / 2)..demand -> "🟠 Shortage"
                            else -> "🔴 Critical"
                        }
                        val statusColor = when {
                            stock >= demand + 10 -> StatusAdequateGreen
                            stock >= demand -> StatusLowYellow
                            stock in (demand / 2)..demand -> StatusShortageOrange
                            else -> StatusCriticalRed
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(bg.displayName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.2f))
                            Text("$stock u", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.8f))
                            Text("$demand u", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(0.8f))
                            Text(statusEmoji, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor, modifier = Modifier.weight(1f))
                        }
                        Divider(color = Color(0xFFF0F0F0))
                    }
                }
            }
        }

        // Action Buttons
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onNavigate(AppScreen.REGIONAL_IMBALANCE) },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("प्रादेशिक असंतुलन अलर्ट तपासा", fontSize = 11.sp)
                }
                Button(
                    onClick = { onNavigate(AppScreen.FIND_BLOOD) },
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("तातडीने रक्त शोधा", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
