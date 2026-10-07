package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.repository.BloodDatabase
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen

@Composable
fun HomeScreen(
    language: AppLanguage,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        // Hero Section with Official Tricolor & Red Emergency Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GovNavyPrimary, GovNavySurface)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Official Emblem Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("🇮🇳", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "National Blood Emergency Network",
                        color = NationalSaffronLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Hero Title: रक्ताची गरज आहे?
                Text(
                    text = "🩸 " + AppStrings.getHeroAction(language),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = AppStrings.getTagline(language),
                    color = GovernmentGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = AppStrings.getHeroSub(language),
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Large Emergency Button: 🆘 FIND BLOOD NOW
                Button(
                    onClick = { onNavigate(AppScreen.FIND_BLOOD) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmergencyCrimson,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("hero_find_blood_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = "SOS",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.MARATHI) "🆘 रक्त शोधा (FIND BLOOD NOW)"
                        else if (language == AppLanguage.HINDI) "🆘 तुरंत रक्त खोजें (FIND BLOOD NOW)"
                        else "🆘 FIND BLOOD NOW",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Secondary Action Buttons Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Register as Donor
                    OutlinedButton(
                        onClick = { onNavigate(AppScreen.REGISTER_DONOR) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(NationalSaffron, GovernmentGold))),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("hero_register_donor_button"),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "❤️ रक्तदाता नोंदणी"
                            else if (language == AppLanguage.HINDI) "❤️ रक्तदाता पंजीकरण"
                            else "❤️ Register Donor",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Find Blood Centre
                    OutlinedButton(
                        onClick = { onNavigate(AppScreen.BLOOD_CENTRES) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(Color.White, Color.LightGray))),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("hero_blood_centres_button"),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "🏥 रक्तपेढ्या शोधा"
                            else if (language == AppLanguage.HINDI) "🏥 रक्त केंद्र खोजें"
                            else "🏥 Blood Centres",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    // View Blood Availability
                    OutlinedButton(
                        onClick = { onNavigate(AppScreen.STOCK_DASHBOARD) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(NationalGreen, Color.White))),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("hero_stock_dashboard_button"),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "📊 रक्त साठा"
                            else if (language == AppLanguage.HINDI) "📊 रक्त उपलब्धता"
                            else "📊 Availability",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Body Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            // Medical Disclaimer
            MedicalDisclaimerBanner(language = language)

            Spacer(modifier = Modifier.height(14.dp))

            // Live Prototype Key Indicators
            Text(
                text = if (language == AppLanguage.MARATHI) "🇮🇳 राष्ट्रीय रक्त नेटवर्क आकडेवारी (Live Stats)"
                else if (language == AppLanguage.HINDI) "🇮🇳 राष्ट्रीय रक्त नेटवर्क आंकड़े (Live Stats)"
                else "🇮🇳 National Network Metrics (Live Prototype)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = GovNavyPrimary
            )
            Text(
                text = "Prototype synthetic indicators – not official government population statistics",
                fontSize = 10.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 4 Grid KPI Cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricCard(
                    title = if (language == AppLanguage.MARATHI) "नोंदणीकृत रक्तदाते" else "Donors Registered",
                    value = "${BloodDatabase.syntheticDonors.size}+",
                    sub = "Verified Donors",
                    color = EmergencyCrimson,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = if (language == AppLanguage.MARATHI) "अधिकृत रक्तपेढ्या" else "Blood Centres",
                    value = "${BloodDatabase.syntheticBloodCentres.size}",
                    sub = "e-RaktKosh Units",
                    color = GovNavyPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricCard(
                    title = if (language == AppLanguage.MARATHI) "सक्रिय आपत्कालीन" else "Active SOS",
                    value = "${BloodDatabase.activeRequests.size}",
                    sub = "Hospital Requests",
                    color = StatusShortageOrange,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = if (language == AppLanguage.MARATHI) "बॉम्बे Oh दुर्मीळ नोंद" else "Rare Bombay Oh",
                    value = "4 Donors",
                    sub = "Special Registry",
                    color = NationalGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Active SOS Requests Alert Card
            val activeRequests = BloodDatabase.activeRequests
            if (activeRequests.isNotEmpty()) {
                val latest = activeRequests.first()
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(EmergencyCrimson, EmergencyCrimsonDark))),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(AppScreen.DONOR_DASHBOARD) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(EmergencyCrimson)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "तात्काळ आपत्कालीन गरज (ACTIVE EMERGENCY)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmergencyCrimson
                                )
                            }
                            Text(
                                text = latest.urgency.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmergencyCrimson
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "रक्तगट: ${latest.bloodGroup.code} | युनिट्स: ${latest.unitsRequired} (${latest.component.displayName})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "रुग्णालय: ${latest.hospitalName} (${latest.district})",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "तपशील व प्रतिसाद द्या →",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmergencyCrimson
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Regional Blood Imbalance Advisory Preview
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.REGIONAL_IMBALANCE) }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚠️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "प्रादेशिक तुटवडा विश्लेषण (Regional Imbalance)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary
                            )
                        }
                        Text("तपासा →", fontSize = 11.sp, color = NationalSaffronDark, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "कोल्हापूर: O+ पुरेसा (140 u) 🟢  |  सांगली: O+ गंभीर तुटवडा (6 u) 🔴  |  सातारा: O+ कमी (14 u) 🟡",
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "कृती: CPR कोल्हापूर ते मिरज GMC रक्त केंद्र अधिकृत समन्वय व रक्तदाता संघटन.",
                        fontSize = 10.sp,
                        color = GovNavySurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Navigation Quick Shortcuts
            Text(
                text = if (language == AppLanguage.MARATHI) "महत्त्वाची वैशिष्ट्ये (Key Features)"
                else if (language == AppLanguage.HINDI) "प्रमुख विशेषताएं (Key Features)"
                else "Key Platform Portals",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = GovNavyPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FeatureShortcutRow(
                    icon = "🗺️",
                    title = "India & State Map Drilldown",
                    subtitle = "महाराष्ट्र, कोल्हापूर, सांगली, सातारा जिल्हा साठा नकाशा",
                    onClick = { onNavigate(AppScreen.MAP_VIEW) }
                )
                FeatureShortcutRow(
                    icon = "🧬",
                    title = "Rare Blood Registry (Bombay / Oh)",
                    subtitle = "अत्यंत दुर्मीळ रक्तगट शोध व विशेष नोंदणी कक्ष",
                    onClick = { onNavigate(AppScreen.RARE_REGISTRY) }
                )
                FeatureShortcutRow(
                    icon = "🏥",
                    title = "Hospital Emergency Portal",
                    subtitle = "डॉक्टर व अधिकृत रुग्णालय आपत्कालीन विनंती निर्मिती",
                    onClick = { onNavigate(AppScreen.EMERGENCY_REQUEST) }
                )
                FeatureShortcutRow(
                    icon = "🏛️",
                    title = "Government Administrative Dashboard",
                    subtitle = "राष्ट्रीय (MoHFW) / राज्य / जिल्हा आरोग्य अधिकारी नियंत्रण",
                    onClick = { onNavigate(AppScreen.ADMIN_DASHBOARD) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Final Home Page Message (Requested in Item 41)
            Card(
                colors = CardDefaults.cardColors(containerColor = GovNavyPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❤️ " + AppStrings.getFinalMsg(language),
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.getFinalSub(language),
                        color = GovernmentGold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { onNavigate(AppScreen.REGISTER_DONOR) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🩸 नोंदणी करा", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onNavigate(AppScreen.FIND_BLOOD) },
                            colors = ButtonDefaults.buttonColors(containerColor = NationalGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🆘 रक्त शोधा", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = sub,
                fontSize = 10.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun FeatureShortcutRow(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GovNavyContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = GovNavyPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = Color.Gray
            )
        }
    }
}
