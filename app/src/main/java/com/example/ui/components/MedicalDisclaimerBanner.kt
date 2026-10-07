package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.ui.theme.GovNavyPrimary

@Composable
fun MedicalDisclaimerBanner(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFF3E0))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.HealthAndSafety,
                contentDescription = "Medical Safety",
                tint = Color(0xFFE65100),
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = if (language == AppLanguage.MARATHI) "वैद्यकीय सुरक्षितता व समन्वय सूचना"
                    else if (language == AppLanguage.HINDI) "चिकित्सा सुरक्षा एवं समन्वय सूचना"
                    else "Medical Safety & Coordination Notice",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFFE65100)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = AppStrings.getMedicalDisclaimer(language),
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    color = GovNavyPrimary
                )
            }
        }
    }
}
