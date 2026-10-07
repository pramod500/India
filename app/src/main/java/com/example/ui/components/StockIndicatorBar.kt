package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BloodGroup
import com.example.data.model.StockStatus
import com.example.ui.theme.*

@Composable
fun StockStatusBar(
    bloodGroup: BloodGroup,
    units: Int,
    maxReferenceUnits: Int = 150,
    modifier: Modifier = Modifier
) {
    val status = when {
        units >= 20 -> StockStatus.ADEQUATE
        units in 10..19 -> StockStatus.LOW
        units in 4..9 -> StockStatus.SHORTAGE
        units in 1..3 -> StockStatus.CRITICAL
        else -> StockStatus.UNAVAILABLE
    }

    val (barColor, statusBg, statusText) = when (status) {
        StockStatus.ADEQUATE -> Triple(StatusAdequateGreen, Color(0xFFE8F5E9), "Adequate 🟢")
        StockStatus.LOW -> Triple(StatusLowYellow, Color(0xFFFFFDE7), "Low 🟡")
        StockStatus.SHORTAGE -> Triple(StatusShortageOrange, Color(0xFFFFF3E0), "Shortage 🟠")
        StockStatus.CRITICAL -> Triple(StatusCriticalRed, Color(0xFFFFEBEE), "Critical 🔴")
        StockStatus.UNAVAILABLE -> Triple(StatusUnavailableGrey, Color(0xFFEEEEEE), "Nil ⚫")
    }

    val fraction = (units.toFloat() / maxReferenceUnits.toFloat()).coerceIn(0.04f, 1f)
    val animatedFraction by animateFloatAsState(targetValue = fraction, label = "stock_bar")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Group Code
        Text(
            text = bloodGroup.code,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = GovNavyPrimary,
            modifier = Modifier.width(52.dp)
        )

        // Visual Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedFraction)
                    .clip(RoundedCornerShape(7.dp))
                    .background(barColor)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Units count
        Text(
            text = "$units u",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.width(38.dp)
        )

        // Status Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(statusBg)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = statusText,
                color = barColor,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        }
    }
}
