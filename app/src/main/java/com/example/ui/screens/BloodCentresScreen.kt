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
import com.example.data.model.BloodCentre
import com.example.data.model.BloodGroup
import com.example.data.repository.BloodDatabase
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun BloodCentresScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language
    var filterDistrict by remember { mutableStateOf("All Districts") }

    val allCentres = BloodDatabase.syntheticBloodCentres
    val filteredCentres = if (filterDistrict == "All Districts") {
        allCentres
    } else {
        allCentres.filter { it.district.equals(filterDistrict, true) }
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
                        text = "🏥 " + if (language == AppLanguage.MARATHI) "अधिकृत रक्तपेढ्यांची निर्देशिका"
                        else if (language == AppLanguage.HINDI) "अधिकृत रक्त केंद्रों की निर्देशिका"
                        else "Authorized Blood Centres Directory",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "e-RaktKosh Integrated Licensed Blood Banks (${filteredCentres.size} Units)",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            MedicalDisclaimerBanner(language = language)
        }

        // Quick District Filter Chips
        item {
            val chips = listOf("All Districts", "Kolhapur", "Sangli", "Satara", "Pune", "Delhi")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                chips.forEach { dist ->
                    val isSelected = filterDistrict == dist
                    FilterChip(
                        selected = isSelected,
                        onClick = { filterDistrict = dist },
                        label = { Text(dist, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GovNavyPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        items(filteredCentres) { centre ->
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
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = centre.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = GovNavyPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "📍 ${centre.address}",
                                fontSize = 11.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "📞 अधिकृत हेल्पलाइन: ${centre.officialHelpline}",
                                fontSize = 11.sp,
                                color = NationalGreenDark,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Lic: ${centre.licenseNumber} (Govt Authorized)",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text("e-RaktKosh Active", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NationalGreenDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "उपलब्ध रक्त साठा (Blood Group Stock):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Blood Group units mini-chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val keyGroups = listOf(BloodGroup.O_POS, BloodGroup.O_NEG, BloodGroup.A_POS, BloodGroup.B_POS, BloodGroup.AB_POS, BloodGroup.BOMBAY_OH)
                        keyGroups.forEach { bg ->
                            val units = centre.getUnitsFor(bg)
                            val isCritical = units <= 3
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isCritical) Color(0xFFFFEBEE) else Color(0xFFE8F5E9))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${bg.code}: $units",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCritical) EmergencyCrimson else NationalGreenDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Component Breakdown
                    Text(
                        text = "घटक (Components): PRBC: ${centre.componentStockMap[com.example.data.model.BloodComponent.PRBC] ?: 0} u | Platelets: ${centre.componentStockMap[com.example.data.model.BloodComponent.PLATELETS] ?: 0} u | Plasma: ${centre.componentStockMap[com.example.data.model.BloodComponent.PLASMA] ?: 0} u",
                        fontSize = 10.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onNavigate(AppScreen.STOCK_DASHBOARD) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("साठा डॅशबोर्ड", fontSize = 11.sp)
                        }
                        Button(
                            onClick = { onNavigate(AppScreen.FIND_BLOOD) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("रक्त मागणी", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
