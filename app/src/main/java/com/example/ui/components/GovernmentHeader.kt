package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun GovernmentHeader(
    currentLanguage: AppLanguage,
    activeRole: UserRole,
    onLanguageSelected: (AppLanguage) -> Unit,
    onRoleClick: () -> Unit,
    onMenuClick: () -> Unit,
    onEmergencySosClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GovNavyPrimary)
    ) {
        // Indian Tricolor Top Ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        ) {
            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(NationalSaffron))
            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color.White))
            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(NationalGreen))
        }

        // Demo Data Notice Banner (Mandatory Requirement)
        Surface(
            color = EmergencyCrimsonDark,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Demo Warning",
                    tint = GovernmentGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = AppStrings.getDemoWarning(currentLanguage),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp
                )
            }
        }

        // Main Header App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Menu Icon + Emblem / Identity
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.testTag("header_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Navigation Menu",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Emblem & Title
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(EmergencyCrimson),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🩸",
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🇮🇳",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "भारत सरकार | Govt of India",
                            color = NationalSaffronLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = if (currentLanguage == AppLanguage.MARATHI) "रक्तसुरक्षा व रक्तदाता शोध"
                        else if (currentLanguage == AppLanguage.HINDI) "रक्त सुरक्षा एवं रक्तदाता खोज"
                        else "National Blood Emergency",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Right: Language Switcher & SOS button
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Emergency SOS Button
                FilledTonalButton(
                    onClick = onEmergencySosClick,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = EmergencyCrimson,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("header_sos_button")
                ) {
                    Text(
                        text = "🆘 SOS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Language Selector Dropdown Chip
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GovNavySurface)
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppLanguage.entries.forEach { lang ->
                        val isSelected = currentLanguage == lang
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NationalSaffron else Color.Transparent)
                                .clickable { onLanguageSelected(lang) }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                                .testTag("lang_btn_${lang.code}")
                        ) {
                            Text(
                                text = lang.label,
                                color = if (isSelected) Color.Black else Color.White.copy(alpha = 0.85f),
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Secondary Active Role Indicator Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GovNavySurface)
                .clickable { onRoleClick() }
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Role Indicator",
                    tint = GovernmentGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${if (currentLanguage == AppLanguage.MARATHI) "सक्रिय भूमिका:" else if (currentLanguage == AppLanguage.HINDI) "सक्रिय भूमिका:" else "Active Role:"} ${activeRole.displayName}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (currentLanguage == AppLanguage.MARATHI) "बदला" else if (currentLanguage == AppLanguage.HINDI) "बदलें" else "Switch",
                    color = GovernmentGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Switch Role",
                    tint = GovernmentGold,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
