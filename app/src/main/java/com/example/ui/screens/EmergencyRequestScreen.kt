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
import com.example.data.model.*
import com.example.data.repository.BloodDatabase
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyRequestScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language

    // Form inputs
    var hospitalName by remember { mutableStateOf("Chhatrapati Pramila Raje (CPR) Hospital, Kolhapur") }
    var doctorName by remember { mutableStateOf("Dr. S. K. Deshmukh (Casualty Incharge)") }
    var selectedBloodGroup by remember { mutableStateOf(BloodGroup.O_NEG) }
    var selectedComponent by remember { mutableStateOf(BloodComponent.PRBC) }
    var unitsRequired by remember { mutableStateOf("2") }
    var patientCaseRef by remember { mutableStateOf("IPD-EMG-8812") }
    var selectedState by remember { mutableStateOf("Maharashtra") }
    var selectedDistrict by remember { mutableStateOf("Kolhapur") }
    var selectedCity by remember { mutableStateOf("Kolhapur City") }
    var requiredWithinHours by remember { mutableStateOf("2") }
    var urgency by remember { mutableStateOf(UrgencyLevel.CRITICAL_EMERGENCY) }
    var clinicalNotes by remember { mutableStateOf("Emergency trauma requirement - immediate cross-match") }

    var groupExpanded by remember { mutableStateOf(false) }
    var compExpanded by remember { mutableStateOf(false) }
    var urgencyExpanded by remember { mutableStateOf(false) }

    var isCreatedDialogOpen by remember { mutableStateOf(false) }
    var createdRequestId by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
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
                        text = "🚨 " + if (language == AppLanguage.MARATHI) "रुग्णालय आपत्कालीन रक्त मागणी"
                        else if (language == AppLanguage.HINDI) "अस्पताल आपातकालीन रक्त मांग"
                        else "Hospital Emergency Blood Request",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmergencyCrimson
                    )
                    Text(
                        text = "Authorized Medical Professional Dispatch & Donor Alerting",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Fraud Prevention & Medical Banner
        item {
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = NationalGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "सुरक्षा पडताळणी: ही प्रणाली बनावट विनंत्या रोखण्यासाठी डिजिटल ऑडिट लॉग व अधिकृत रुग्णालय कोडद्वारे संरक्षित आहे.",
                        fontSize = 11.sp,
                        color = NationalGreenDark,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Workflow visual bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = GovNavyPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "कार्यपद्धती प्रवाह (WORKFLOW):",
                        color = NationalSaffronLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "रुग्णालय मागणी → रक्तपेढी साठा तपासणी → जवळचे रक्तदाते शोध → सुरक्षित अलर्ट → संमती → रक्तसंक्रमण",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Form Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "अधिकृत आपत्कालीन मागणी फॉर्म (Official Request Form)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = hospitalName,
                        onValueChange = { hospitalName = it },
                        label = { Text("Hospital Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = doctorName,
                        onValueChange = { doctorName = it },
                        label = { Text("Doctor / Authorized Medical Officer *") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Blood Group Dropdown
                        ExposedDropdownMenuBox(
                            expanded = groupExpanded,
                            onExpandedChange = { groupExpanded = !groupExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedBloodGroup.displayName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Blood Group *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = groupExpanded,
                                onDismissRequest = { groupExpanded = false }
                            ) {
                                BloodGroup.entries.forEach { bg ->
                                    DropdownMenuItem(
                                        text = { Text(bg.displayName) },
                                        onClick = {
                                            selectedBloodGroup = bg
                                            groupExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Component Dropdown
                        ExposedDropdownMenuBox(
                            expanded = compExpanded,
                            onExpandedChange = { compExpanded = !compExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedComponent.displayName.take(10) + "...",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Component *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = compExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = compExpanded,
                                onDismissRequest = { compExpanded = false }
                            ) {
                                BloodComponent.entries.forEach { comp ->
                                    DropdownMenuItem(
                                        text = { Text(comp.displayName) },
                                        onClick = {
                                            selectedComponent = comp
                                            compExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = unitsRequired,
                            onValueChange = { unitsRequired = it },
                            label = { Text("Units Required *") },
                            modifier = Modifier.weight(0.8f)
                        )
                        OutlinedTextField(
                            value = patientCaseRef,
                            onValueChange = { patientCaseRef = it },
                            label = { Text("Case Ref (IPD/OPD) *") },
                            placeholder = { Text("उदा. IPD-8912") },
                            modifier = Modifier.weight(1.2f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Urgency Dropdown
                        ExposedDropdownMenuBox(
                            expanded = urgencyExpanded,
                            onExpandedChange = { urgencyExpanded = !urgencyExpanded },
                            modifier = Modifier.weight(1.3f)
                        ) {
                            OutlinedTextField(
                                value = urgency.label,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Urgency *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = urgencyExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = urgencyExpanded,
                                onDismissRequest = { urgencyExpanded = false }
                            ) {
                                UrgencyLevel.entries.forEach { urg ->
                                    DropdownMenuItem(
                                        text = { Text(urg.label) },
                                        onClick = {
                                            urgency = urg
                                            urgencyExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = requiredWithinHours,
                            onValueChange = { requiredWithinHours = it },
                            label = { Text("Within (Hours)") },
                            modifier = Modifier.weight(0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = clinicalNotes,
                        onValueChange = { clinicalNotes = it },
                        label = { Text("Clinical Notes / Instructions") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val units = unitsRequired.toIntOrNull() ?: 1
                            val hours = requiredWithinHours.toIntOrNull() ?: 2
                            createdRequestId = viewModel.submitEmergencyRequest(
                                hospitalName = hospitalName,
                                doctorName = doctorName,
                                bloodGroup = selectedBloodGroup,
                                component = selectedComponent,
                                units = units,
                                patientCaseRef = patientCaseRef,
                                state = selectedState,
                                district = selectedDistrict,
                                city = selectedCity,
                                urgency = urgency,
                                hours = hours,
                                notes = clinicalNotes
                            )
                            isCreatedDialogOpen = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_emergency_blood_request_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("आपत्कालीन विनंती जारी करा (DISPATCH REQUEST)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Section: Active Hospital Requests Tracking
        item {
            Text(
                text = "📊 सक्रिय रुग्णालयीन मागण्या (Active Requests Tracking)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = GovNavyPrimary
            )
        }

        items(BloodDatabase.activeRequests) { req ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(req.id, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GovNavyPrimary)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (req.status == RequestStatus.DONOR_ACCEPTED) Color(0xFFE8F5E9) else Color(0xFFFFEBEE))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(req.status.label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (req.status == RequestStatus.DONOR_ACCEPTED) NationalGreenDark else EmergencyCrimson)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${req.bloodGroup.code} (${req.component.displayName}) • ${req.unitsRequired} Units • Ref: ${req.patientCaseRef}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${req.hospitalName} (${req.doctorOrOfficerName})",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Matched Donors: ${req.matchedDonors.size} notified via secure network",
                        fontSize = 10.sp,
                        color = NationalGreenDark
                    )
                }
            }
        }
    }

    if (isCreatedDialogOpen) {
        AlertDialog(
            onDismissRequest = { isCreatedDialogOpen = false },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NationalGreen, modifier = Modifier.size(36.dp)) },
            title = {
                Text(
                    text = "आपत्कालीन विनंती प्रसारित केली!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Text("विनंती क्रमांक (Request ID):", fontSize = 11.sp, color = Color.Gray)
                    Text(createdRequestId, fontWeight = FontWeight.Black, fontSize = 15.sp, color = EmergencyCrimson)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "प्रणालीने जवळच्या पात्र व संमती असलेल्या रक्तदात्यांना सुरक्षित SMS/App अलर्ट पाठवला आहे. रक्तदाता प्रतिसाद मिळताच आपल्याला सूचित केले जाईल.",
                        fontSize = 11.sp,
                        color = GovNavyPrimary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isCreatedDialogOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary)
                ) {
                    Text("ठीक आहे (OK)")
                }
            }
        )
    }
}
