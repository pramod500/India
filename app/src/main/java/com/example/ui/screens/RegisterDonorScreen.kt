package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterDonorScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val language = state.language

    // Form fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("26") }
    var gender by remember { mutableStateOf("Male") }
    var mobile by remember { mutableStateOf("9823012345") }
    var email by remember { mutableStateOf("") }

    var selectedBloodGroup by remember { mutableStateOf(BloodGroup.O_POS) }
    var groupDropdownExpanded by remember { mutableStateOf(false) }
    var isVerified by remember { mutableStateOf(true) }
    var verificationSource by remember { mutableStateOf("CPR Civil Hospital Blood Lab") }

    var selectedState by remember { mutableStateOf("Maharashtra") }
    var selectedDistrict by remember { mutableStateOf("Kolhapur") }
    var selectedTaluka by remember { mutableStateOf("Kolhapur City") }
    var locality by remember { mutableStateOf("Shahupuri, Near Station") }
    var pincode by remember { mutableStateOf("416001") }

    var stateExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }
    var talukaExpanded by remember { mutableStateOf(false) }

    var hasConsentChecked by remember { mutableStateOf(true) }
    var emergencyAvailable by remember { mutableStateOf(true) }
    var notificationPref by remember { mutableStateOf("App + SMS") }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var generatedDonorId by remember { mutableStateOf("") }

    val statesList = LocationHierarchy.states.map { it.name }
    val districtsList = LocationHierarchy.getDistrictsForState(selectedState)
    val talukasList = LocationHierarchy.getTalukasForDistrict(selectedDistrict)

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
                        text = "❤️ " + if (language == AppLanguage.MARATHI) "रक्तदाता नोंदणी (Donor Registration)"
                        else if (language == AppLanguage.HINDI) "रक्तदाता पंजीकरण (Donor Registration)"
                        else "Citizen Blood Donor Registration",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "National Emergency Voluntary Donor Network",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Medical Notice Banner
        item {
            MedicalDisclaimerBanner(language = language)
        }

        // Section 1: Personal Information
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "1. वैयक्तिक माहिती (Personal Information)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("First Name *") },
                            placeholder = { Text("उदा. सागर") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("donor_reg_first_name")
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Last Name *") },
                            placeholder = { Text("उदा. पाटील") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("donor_reg_last_name")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("Age (18-65) *") },
                            modifier = Modifier.weight(0.8f)
                        )
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = { Text("Gender") },
                            modifier = Modifier.weight(1.2f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = { mobile = it },
                        label = { Text("Mobile Number (Verified via OTP) *") },
                        leadingIcon = { Text("+91 ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email (Optional)") },
                        placeholder = { Text("name@example.com") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Section 2: Blood Information
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "2. रक्तगट माहिती (Blood Group Information)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Blood Group Selection Dropdown
                    ExposedDropdownMenuBox(
                        expanded = groupDropdownExpanded,
                        onExpandedChange = { groupDropdownExpanded = !groupDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedBloodGroup.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Blood Group *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("donor_reg_blood_group")
                        )
                        ExposedDropdownMenu(
                            expanded = groupDropdownExpanded,
                            onDismissRequest = { groupDropdownExpanded = false }
                        ) {
                            BloodGroup.entries.forEach { bg ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = if (bg.isRare) "⭐ ${bg.displayName} (Rare)" else bg.displayName,
                                            fontWeight = if (bg.isRare) FontWeight.Bold else FontWeight.Normal,
                                            color = if (bg.isRare) EmergencyCrimson else Color.Unspecified
                                        )
                                    },
                                    onClick = {
                                        selectedBloodGroup = bg
                                        groupDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Verification Checkbox & Source
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isVerified,
                            onCheckedChange = { isVerified = it },
                            colors = CheckboxDefaults.colors(checkedColor = NationalGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Blood Group Verified by Lab / Blood Centre? (YES/NO)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (isVerified) {
                        OutlinedTextField(
                            value = verificationSource,
                            onValueChange = { verificationSource = it },
                            label = { Text("Verification Source / Blood Centre Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Section 3: Location Hierarchy
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "3. निवासस्थान श्रेणी (Location Hierarchy)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "NEVER show exact home address to other users.",
                        fontSize = 10.sp,
                        color = EmergencyCrimson,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // State Dropdown
                    ExposedDropdownMenuBox(
                        expanded = stateExpanded,
                        onExpandedChange = { stateExpanded = !stateExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedState,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("India → State / UT *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = stateExpanded,
                            onDismissRequest = { stateExpanded = false }
                        ) {
                            statesList.forEach { st ->
                                DropdownMenuItem(
                                    text = { Text(st) },
                                    onClick = {
                                        selectedState = st
                                        selectedDistrict = LocationHierarchy.getDistrictsForState(st).firstOrNull() ?: "Central"
                                        selectedTaluka = LocationHierarchy.getTalukasForDistrict(selectedDistrict).firstOrNull() ?: "$selectedDistrict City"
                                        stateExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // District Dropdown
                    ExposedDropdownMenuBox(
                        expanded = districtExpanded,
                        onExpandedChange = { districtExpanded = !districtExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedDistrict,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("District *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = districtExpanded,
                            onDismissRequest = { districtExpanded = false }
                        ) {
                            districtsList.forEach { dist ->
                                DropdownMenuItem(
                                    text = { Text(dist) },
                                    onClick = {
                                        selectedDistrict = dist
                                        selectedTaluka = LocationHierarchy.getTalukasForDistrict(dist).firstOrNull() ?: "$dist City"
                                        districtExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Taluka Dropdown
                    ExposedDropdownMenuBox(
                        expanded = talukaExpanded,
                        onExpandedChange = { talukaExpanded = !talukaExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedTaluka,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Taluka / City *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = talukaExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = talukaExpanded,
                            onDismissRequest = { talukaExpanded = false }
                        ) {
                            talukasList.forEach { tal ->
                                DropdownMenuItem(
                                    text = { Text(tal) },
                                    onClick = {
                                        selectedTaluka = tal
                                        talukaExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = locality,
                            onValueChange = { locality = it },
                            label = { Text("Locality / Village Area") },
                            modifier = Modifier.weight(1.3f)
                        )
                        OutlinedTextField(
                            value = pincode,
                            onValueChange = { pincode = it },
                            label = { Text("PIN Code") },
                            modifier = Modifier.weight(0.7f)
                        )
                    }
                }
            }
        }

        // Section 4: Donor Consent (Mandatory Requirements)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "4. रक्तदाता संमती (Donor Consent)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mandatory Checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Checkbox(
                            checked = hasConsentChecked,
                            onCheckedChange = { hasConsentChecked = it },
                            colors = CheckboxDefaults.colors(checkedColor = EmergencyCrimson)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "“I voluntarily agree to be contacted through this authorized platform when an emergency blood donation request matching my blood group and location is identified.”",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Emergency Availability Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Emergency Donation Available?", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (emergencyAvailable) "🟢 Available for request" else "🟡 Temporarily unavailable",
                                fontSize = 11.sp,
                                color = if (emergencyAvailable) NationalGreenDark else Color.Gray
                            )
                        }
                        Switch(
                            checked = emergencyAvailable,
                            onCheckedChange = { emergencyAvailable = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Notification Channel: SMS / App Notification",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        // Section 5: Donor Eligibility Disclaimer
        item {
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "पात्रता स्थिती (Eligibility Status):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = NationalGreenDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "“Final eligibility for blood donation will be determined by the authorized blood centre according to applicable medical guidelines.”",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        // Register Button
        item {
            Button(
                onClick = {
                    if (firstName.isBlank()) firstName = "नागरिक"
                    if (lastName.isBlank()) lastName = "रक्तदाता"

                    generatedDonorId = viewModel.registerNewDonor(
                        firstName = firstName,
                        lastName = lastName,
                        bloodGroup = selectedBloodGroup,
                        isVerified = isVerified,
                        verificationSource = verificationSource,
                        state = selectedState,
                        district = selectedDistrict,
                        taluka = selectedTaluka,
                        hasConsent = hasConsentChecked,
                        emergencyAvailable = emergencyAvailable
                    )
                    showSuccessDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_donor_registration_button")
            ) {
                Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "नोंदणी पूर्ण करा (REGISTER AS DONOR)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = { Icon(Icons.Default.Verified, contentDescription = null, tint = NationalGreen, modifier = Modifier.size(36.dp)) },
            title = {
                Text(
                    text = "रक्तदाता नोंदणी यशस्वी! (Registration Complete)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "आपला प्रात्यक्षिक रक्तदाता आयडी तयार झाला आहे:",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = generatedDonorId,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = EmergencyCrimson
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "रक्तगट: ${selectedBloodGroup.code} | स्थान: $selectedTaluka, $selectedDistrict",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "आपत्कालीन वेळी आवश्यकतेनुसार अधिकृत प्रणालीद्वारे आपल्याला संपर्क केला जाईल. आपल्या रक्तदानाने जीव वाचू शकतो!",
                        fontSize = 11.sp,
                        color = GovNavyPrimary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onNavigate(AppScreen.DONOR_DASHBOARD)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary)
                ) {
                    Text("माझे प्रोफाइल पहा (View Profile)")
                }
            }
        )
    }
}
