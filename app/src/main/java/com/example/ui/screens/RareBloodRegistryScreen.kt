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
import com.example.data.model.BloodGroup
import com.example.data.repository.BloodDatabase
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun RareBloodRegistryScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language

    val rareDonors = BloodDatabase.syntheticDonors.filter { it.bloodGroup == BloodGroup.BOMBAY_OH }

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
                        text = "🩸 " + if (language == AppLanguage.MARATHI) "दुर्मीळ रक्तगट नोंदणी (Rare Blood Registry)"
                        else if (language == AppLanguage.HINDI) "दुर्लभ रक्त समूह रजिस्ट्री"
                        else "National Rare Blood Registry",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmergencyCrimson
                    )
                    Text(
                        text = "Bombay / Oh Phenotype Specialized Mobilisation Unit",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            MedicalDisclaimerBanner(language = language)
        }

        // Educational / Explanatory Banner on Bombay Blood Group
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = GovNavyPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🧬", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "बॉम्बे रक्तगट (Bombay Phenotype / Oh)",
                            color = NationalSaffronLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "बॉम्बे रक्तगट (Oh) हा भारतात दर १०,००० व्यक्तींमागे एका व्यक्तीमध्ये आढळणारा अत्यंत दुर्मीळ रक्तगट आहे. या व्यक्तींना केवळ बॉम्बे रक्तगटाचेच रक्त दिले जाऊ शकते, परंतु ते इतर कोणत्याही सामान्य रक्तगटाला (A, B, AB, O) रक्त देऊ शकतात.",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "National Rare Blood Cell Network • 24x7 Dedicated Mobilisation Protocol",
                        color = GovernmentGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Rare Blood Stats Banner
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    tonalElevation = 1.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("नोंदणीकृत Oh रक्तदाते", fontSize = 11.sp, color = Color.Gray)
                        Text("${rareDonors.size} Donors", fontSize = 18.sp, fontWeight = FontWeight.Black, color = EmergencyCrimson)
                        Text("In Maharashtra (Demo)", fontSize = 10.sp, color = Color.DarkGray)
                    }
                }

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    tonalElevation = 1.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("रक्तपेढी साठा (Units)", fontSize = 11.sp, color = Color.Gray)
                        Text("6 Units", fontSize = 18.sp, fontWeight = FontWeight.Black, color = NationalGreen)
                        Text("CPR, Sassoon, AIIMS", fontSize = 10.sp, color = Color.DarkGray)
                    }
                }
            }
        }

        // Section: Registered Rare Donors (Privacy strictly protected)
        item {
            Text(
                text = "सुरक्षित दुर्मीळ रक्तदाता यादी (Verified Consenting Donors)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = GovNavyPrimary
            )
        }

        items(rareDonors) { donor ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(NationalSaffron),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Oh", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "${donor.publicDisplayTag} (${donor.district})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = GovNavyPrimary
                            )
                            Text(
                                text = "स्थान: ${donor.cityOrTaluka}, ${donor.state}",
                                fontSize = 11.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "पडताळणी: ${donor.verificationSource}",
                                fontSize = 10.sp,
                                color = NationalGreenDark
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text("🟢 संमती उपलब्ध", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NationalGreenDark)
                    }
                }
            }
        }

        // Emergency Rare Blood Request Trigger Button
        item {
            Button(
                onClick = {
                    viewModel.updateSearchCriteria(bloodGroup = BloodGroup.BOMBAY_OH)
                    onNavigate(AppScreen.FIND_BLOOD)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("rare_blood_search_trigger_btn")
            ) {
                Text("तातडीने बॉम्बे रक्तगट शोधा / विनंती करा", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
