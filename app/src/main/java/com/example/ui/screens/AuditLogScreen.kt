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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun AuditLogScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language
    val logs = BloodDatabase.auditLogs

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
                        text = "🔒 " + if (language == AppLanguage.MARATHI) "सुरक्षा व पडताळणी नोंदी (Audit Logs)"
                        else if (language == AppLanguage.HINDI) "सुरक्षा एवं ऑडिट लॉग्स"
                        else "Tamper-Evident Security Audit Logs",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "Governance, Consent Verification & Request Tracking Logs",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            MedicalDisclaimerBanner(language = language)
        }

        item {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Data Minimization & Zero-Trust Governance Active",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = NationalGreenDark
                    )
                    Text(
                        text = "सिटिझन मोबाईल व अचूक पत्ते कधीही उघडे केले जात नाहीत. सर्व आपत्कालीन विनंत्या क्रिप्टोग्राफिक हॅशसह नोंदवल्या जातात.",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        items(logs) { log ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(log.action, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GovNavyPrimary)
                        Text(log.timestamp, fontSize = 10.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(log.details, fontSize = 11.sp, color = Color.Black)

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Role: ${log.userRole}", fontSize = 10.sp, color = NationalGreenDark, fontWeight = FontWeight.Medium)
                        Text("Hash: ${log.ipHash}", fontSize = 9.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
