package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.*
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindBloodScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val state by viewModel.uiState.collectAsState()
    val criteria = state.searchCriteria
    val language = state.language

    // State, District, Taluka Dropdowns
    val statesList = LocationHierarchy.states.map { it.name }
    val districtsList = LocationHierarchy.getDistrictsForState(criteria.state)
    val talukasList = LocationHierarchy.getTalukasForDistrict(criteria.district)

    var stateExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }
    var talukaExpanded by remember { mutableStateOf(false) }
    var groupExpanded by remember { mutableStateOf(false) }
    var componentExpanded by remember { mutableStateOf(false) }
    var urgencyExpanded by remember { mutableStateOf(false) }

    // Dialog for requesting donor confirmation
    var selectedDonorForRequest by remember { mutableStateOf<Donor?>(null) }
    var showRequestSentDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Header
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
                        text = "🆘 " + if (language == AppLanguage.MARATHI) "रक्त शोधा (FIND BLOOD)"
                        else if (language == AppLanguage.HINDI) "रक्त खोजें (FIND BLOOD)"
                        else "Emergency Blood Search",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                    Text(
                        text = "Authorized Blood Bank Stock & Consenting Donor Matching",
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

        // Search Filters Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🔎 शोध निकष (Search Criteria)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GovNavyPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row 1: Blood Group & Component
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Blood Group Dropdown
                        ExposedDropdownMenuBox(
                            expanded = groupExpanded,
                            onExpandedChange = { groupExpanded = !groupExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = criteria.bloodGroup.displayName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Blood Group", fontSize = 11.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .testTag("search_bg_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = groupExpanded,
                                onDismissRequest = { groupExpanded = false }
                            ) {
                                BloodGroup.entries.forEach { bg ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = if (bg.isRare) "⭐ ${bg.displayName}" else bg.displayName,
                                                fontWeight = if (bg.isRare) FontWeight.Bold else FontWeight.Normal,
                                                color = if (bg.isRare) EmergencyCrimson else Color.Unspecified
                                            )
                                        },
                                        onClick = {
                                            viewModel.updateSearchCriteria(bloodGroup = bg)
                                            groupExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Component Dropdown
                        ExposedDropdownMenuBox(
                            expanded = componentExpanded,
                            onExpandedChange = { componentExpanded = !componentExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = criteria.component.displayName.take(12) + "...",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Component", fontSize = 11.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = componentExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = componentExpanded,
                                onDismissRequest = { componentExpanded = false }
                            ) {
                                BloodComponent.entries.forEach { comp ->
                                    DropdownMenuItem(
                                        text = { Text(comp.displayName) },
                                        onClick = {
                                            viewModel.updateSearchCriteria(component = comp)
                                            componentExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row 2: Location Hierarchy (State, District, Taluka)
                    // State
                    ExposedDropdownMenuBox(
                        expanded = stateExpanded,
                        onExpandedChange = { stateExpanded = !stateExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = criteria.state,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("State / Union Territory", fontSize = 11.sp) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = stateExpanded,
                            onDismissRequest = { stateExpanded = false }
                        ) {
                            statesList.forEach { st ->
                                DropdownMenuItem(
                                    text = { Text(st) },
                                    onClick = {
                                        viewModel.updateSearchCriteria(state = st)
                                        stateExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // District
                        ExposedDropdownMenuBox(
                            expanded = districtExpanded,
                            onExpandedChange = { districtExpanded = !districtExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = criteria.district,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("District", fontSize = 11.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = districtExpanded,
                                onDismissRequest = { districtExpanded = false }
                            ) {
                                districtsList.forEach { dist ->
                                    DropdownMenuItem(
                                        text = { Text(dist) },
                                        onClick = {
                                            viewModel.updateSearchCriteria(district = dist)
                                            districtExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Taluka / City
                        ExposedDropdownMenuBox(
                            expanded = talukaExpanded,
                            onExpandedChange = { talukaExpanded = !talukaExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = criteria.taluka,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Taluka / City", fontSize = 11.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = talukaExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = talukaExpanded,
                                onDismissRequest = { talukaExpanded = false }
                            ) {
                                talukasList.forEach { tal ->
                                    DropdownMenuItem(
                                        text = { Text(tal) },
                                        onClick = {
                                            viewModel.updateSearchCriteria(taluka = tal)
                                            talukaExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row 3: Urgency & Units
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = urgencyExpanded,
                            onExpandedChange = { urgencyExpanded = !urgencyExpanded },
                            modifier = Modifier.weight(1.5f)
                        ) {
                            OutlinedTextField(
                                value = criteria.urgency.label,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Urgency Level", fontSize = 11.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = urgencyExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = urgencyExpanded,
                                onDismissRequest = { urgencyExpanded = false }
                            ) {
                                UrgencyLevel.entries.forEach { urg ->
                                    DropdownMenuItem(
                                        text = { Text(urg.label) },
                                        onClick = {
                                            viewModel.updateSearchCriteria(urgency = urg)
                                            urgencyExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = criteria.unitsRequired.toString(),
                            onValueChange = { str ->
                                val u = str.toIntOrNull() ?: 1
                                viewModel.updateSearchCriteria(units = u)
                            },
                            label = { Text("Units", fontSize = 11.sp) },
                            modifier = Modifier.weight(0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search Button: 🔎 SEARCH BLOOD
                    Button(
                        onClick = { viewModel.performBloodSearch() },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("execute_search_blood_button")
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "🔎 रक्त शोधा (SEARCH BLOOD)"
                            else if (language == AppLanguage.HINDI) "🔎 रक्त खोजें (SEARCH BLOOD)"
                            else "🔎 SEARCH BLOOD",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // ================= SECTION A: BLOOD CENTRES =================
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SECTION A – BLOOD CENTRES 🏥",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GovNavyPrimary
                )
                Text(
                    text = "${state.searchedCentres.size} Centres Found",
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
            }
        }

        if (state.searchedCentres.isEmpty()) {
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "या भागात थेट रक्तपेढी उपलब्ध नाही. कृपया जवळचा जिल्हा किंवा खालील रक्तदाता नेटवर्क तपासा.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(state.searchedCentres) { centre ->
                val availableUnits = centre.getUnitsFor(criteria.bloodGroup)
                val status = centre.getStatusFor(criteria.bloodGroup)
                val statusColor = if (availableUnits > 0) StatusAdequateGreen else StatusCriticalRed

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = centre.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = GovNavyPrimary
                                )
                                Text(
                                    text = "${centre.city}, ${centre.district} • ${centre.distanceKm} km",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Lic: ${centre.licenseNumber} (Govt Authorized)",
                                    fontSize = 10.sp,
                                    color = NationalGreenDark
                                )
                            }

                            // Status badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (availableUnits > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (availableUnits > 0) "AVAILABLE 🟢" else "CRITICAL 🔴",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Blood Availability Highlight
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF0F4F8))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Blood Group: ${criteria.bloodGroup.code}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = GovNavyPrimary
                            )
                            Text(
                                text = "Available: $availableUnits Units",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = if (availableUnits >= criteria.unitsRequired) StatusAdequateGreen else StatusCriticalRed
                            )
                            Text(
                                text = "Updated: ${centre.lastUpdated}",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Action Buttons: [VIEW] [DIRECTIONS] [HELPLINE]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onNavigate(AppScreen.BLOOD_CENTRES) },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("VIEW STOCK", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { onNavigate(AppScreen.MAP_VIEW) },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("DIRECTIONS", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // ================= SECTION B: DONOR NETWORK =================
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "SECTION B – DONOR NETWORK ❤️",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = EmergencyCrimson
                    )
                    Text(
                        text = "Potential Matching Donors (Anonymized & Consent-Verified)",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "${state.matchedDonors.size} Matched",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NationalGreenDark
                )
            }
        }

        // Privacy Warning Banner
        item {
            Surface(
                color = GovNavyContainer,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = GovNavyPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "नागरिकांची गोपनीयता: रक्तदात्यांची नावे, मोबाईल किंवा अचूक पत्ते सार्वजनिक केले जात नाहीत.",
                        fontSize = 10.sp,
                        color = GovNavyPrimary
                    )
                }
            }
        }

        if (state.matchedDonors.isEmpty()) {
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "या भागात थेट नोंदणीकृत रक्तदाते आढळले नाहीत. कृपया राज्यस्तरीय आपत्कालीन संघटन विनंती पाठवा.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(state.matchedDonors.take(8)) { (donor, priority) ->
                val priorityLabel = when (priority) {
                    1 -> "Priority 1: Nearest Verified Donor 🟢"
                    2 -> "Priority 2: Nearby Verified Donor 🟢"
                    3 -> "Priority 3: District-Level Donor 🟡"
                    4 -> "Priority 4: Neighbor District Donor 🟠"
                    else -> "Priority 5: State Mobilisation 🔴"
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Blood Drop Circle
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(if (donor.bloodGroup.isRare) NationalSaffron else EmergencyCrimson),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = donor.bloodGroup.code,
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column {
                                    Text(
                                        text = "${donor.publicDisplayTag} (${donor.district})",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = GovNavyPrimary
                                    )
                                    Text(
                                        text = "Approx. ${donor.approximateDistanceKm} km • Eligible: Pending final centre screening",
                                        fontSize = 10.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }

                            // Request Button
                            Button(
                                onClick = {
                                    selectedDonorForRequest = donor
                                    showRequestSentDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmergencyCrimson),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.testTag("request_donor_btn_${donor.id}")
                            ) {
                                Text(
                                    text = "REQUEST DONOR",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Priority Tag
                        Text(
                            text = priorityLabel,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (priority <= 2) StatusAdequateGreen else StatusShortageOrange
                        )
                    }
                }
            }
        }
    }

    // Confirmation Alert Dialog when "REQUEST DONOR" is clicked
    if (showRequestSentDialog && selectedDonorForRequest != null) {
        val donor = selectedDonorForRequest!!
        AlertDialog(
            onDismissRequest = { showRequestSentDialog = false },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NationalGreen, modifier = Modifier.size(32.dp)) },
            title = {
                Text(
                    text = "आपत्कालीन विनंती पाठवली (Emergency Request Dispatched)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = GovNavyPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "सुरक्षित संदेश प्रणालीद्वारे रक्तदाता ${donor.publicDisplayTag} यांना ${criteria.bloodGroup.code} रक्तदानाची आपत्कालीन विनंती पाठवण्यात आली आहे.",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "गोपनीयता सूचना: रक्तदात्याचा वैयक्तिक संपर्क संरक्षित आहे. रक्तदात्याने संमती दिल्यास अधिकृत रक्त केंद्रात समन्वय साधला जाईल.",
                        fontSize = 11.sp,
                        color = GovNavyPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRequestSentDialog = false
                        viewModel.acceptEmergencyRequest("REQ-EMG-2026-0891", donor.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary)
                ) {
                    Text("ठीक आहे (OK)")
                }
            }
        )
    }
}
