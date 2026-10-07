package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.model.*
import com.example.data.repository.BloodDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppScreen {
    HOME,
    FIND_BLOOD,
    EMERGENCY_REQUEST,
    REGISTER_DONOR,
    DONOR_DASHBOARD,
    BLOOD_CENTRES,
    STOCK_DASHBOARD,
    RARE_REGISTRY,
    REGIONAL_IMBALANCE,
    MAP_VIEW,
    ADMIN_DASHBOARD,
    AUDIT_LOGS
}

data class SearchCriteria(
    val bloodGroup: BloodGroup = BloodGroup.O_POS,
    val component: BloodComponent = BloodComponent.WHOLE_BLOOD,
    val urgency: UrgencyLevel = UrgencyLevel.CRITICAL_EMERGENCY,
    val unitsRequired: Int = 2,
    val state: String = "Maharashtra",
    val district: String = "Kolhapur",
    val taluka: String = "Kolhapur City"
)

data class MainUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val screenHistory: List<AppScreen> = listOf(AppScreen.HOME),
    val language: AppLanguage = AppLanguage.MARATHI,
    val activeRole: UserRole = UserRole.CITIZEN_DONOR,
    val currentDonorId: String = "DEMO-MH-KOL-0001", // Active logged-in demo donor
    val searchCriteria: SearchCriteria = SearchCriteria(),
    val searchedCentres: List<BloodCentre> = emptyList(),
    val matchedDonors: List<Pair<Donor, Int>> = emptyList(),
    val isSearchPerformed: Boolean = false,
    val isAuthDialogOpen: Boolean = false,
    val successSnackbarMessage: String? = null,
    val universalSearchQuery: String = ""
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        // Run initial search with default criteria for instant demonstration
        performBloodSearch()
    }

    fun setLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(language = lang) }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { current ->
            if (current.currentScreen == screen) current
            else current.copy(
                currentScreen = screen,
                screenHistory = current.screenHistory + screen
            )
        }
    }

    fun navigateBack(): Boolean {
        val history = _uiState.value.screenHistory
        return if (history.size > 1) {
            val newHistory = history.dropLast(1)
            _uiState.update { it.copy(currentScreen = newHistory.last(), screenHistory = newHistory) }
            true
        } else {
            false
        }
    }

    fun setActiveRole(role: UserRole) {
        _uiState.update { it.copy(activeRole = role, isAuthDialogOpen = false) }
    }

    fun toggleAuthDialog(show: Boolean) {
        _uiState.update { it.copy(isAuthDialogOpen = show) }
    }

    fun updateSearchCriteria(
        bloodGroup: BloodGroup? = null,
        component: BloodComponent? = null,
        urgency: UrgencyLevel? = null,
        units: Int? = null,
        state: String? = null,
        district: String? = null,
        taluka: String? = null
    ) {
        _uiState.update { current ->
            val prev = current.searchCriteria
            val newCriteria = prev.copy(
                bloodGroup = bloodGroup ?: prev.bloodGroup,
                component = component ?: prev.component,
                urgency = urgency ?: prev.urgency,
                unitsRequired = units ?: prev.unitsRequired,
                state = state ?: prev.state,
                district = district ?: if (state != null) LocationHierarchy.getDistrictsForState(state).firstOrNull() ?: "Central" else prev.district,
                taluka = taluka ?: if (district != null) LocationHierarchy.getTalukasForDistrict(district).firstOrNull() ?: "$district City" else prev.taluka
            )
            current.copy(searchCriteria = newCriteria)
        }
    }

    fun performBloodSearch() {
        val criteria = _uiState.value.searchCriteria
        val centres = BloodDatabase.syntheticBloodCentres.filter {
            it.state.equals(criteria.state, true) &&
                    it.district.equals(criteria.district, true)
        }.sortedBy { it.distanceKm }

        val donors = BloodDatabase.matchDonors(
            requiredBloodGroup = criteria.bloodGroup,
            state = criteria.state,
            district = criteria.district,
            cityOrTaluka = criteria.taluka
        )

        _uiState.update {
            it.copy(
                searchedCentres = centres,
                matchedDonors = donors,
                isSearchPerformed = true
            )
        }
    }

    fun registerNewDonor(
        firstName: String,
        lastName: String,
        bloodGroup: BloodGroup,
        isVerified: Boolean,
        verificationSource: String,
        state: String,
        district: String,
        taluka: String,
        hasConsent: Boolean,
        emergencyAvailable: Boolean
    ): String {
        val distCode = district.take(3).uppercase()
        val newId = "DEMO-${state.take(2).uppercase()}-$distCode-${String.format("%04d", (100..999).random())}"

        val donor = Donor(
            id = newId,
            state = state,
            district = district,
            cityOrTaluka = taluka,
            bloodGroup = bloodGroup,
            isVerified = isVerified,
            verificationSource = if (isVerified) verificationSource else "Verification Pending",
            verificationDate = "Today",
            hasConsent = hasConsent,
            emergencyAvailable = emergencyAvailable,
            status = if (emergencyAvailable) DonorAvailabilityStatus.AVAILABLE else DonorAvailabilityStatus.NOT_AVAILABLE,
            approximateDistanceKm = 2.5,
            lastDonationDate = "None recorded",
            donationHistory = emptyList(),
            privateFirstName = firstName,
            privateLastName = lastName
        )

        BloodDatabase.registerDonor(donor)
        _uiState.update {
            it.copy(
                currentDonorId = newId,
                successSnackbarMessage = "नोंदणी यशस्वी! रक्तदाता आयडी: $newId (Registration Successful!)"
            )
        }
        return newId
    }

    fun submitEmergencyRequest(
        hospitalName: String,
        doctorName: String,
        bloodGroup: BloodGroup,
        component: BloodComponent,
        units: Int,
        patientCaseRef: String,
        state: String,
        district: String,
        city: String,
        urgency: UrgencyLevel,
        hours: Int,
        notes: String
    ): String {
        val reqId = "REQ-EMG-2026-${String.format("%04d", (1000..9999).random())}"

        // Match donors to alert immediately
        val matches = BloodDatabase.matchDonors(bloodGroup, state, district, city).take(4)
        val matchRecords = matches.map {
            DonorMatchRecord(
                donorId = it.first.id,
                approximateDistanceKm = it.first.approximateDistanceKm,
                matchPriorityLevel = it.second,
                notificationTime = "Just now",
                responseStatus = "ALERTED"
            )
        }

        val request = EmergencyRequest(
            id = reqId,
            hospitalName = hospitalName,
            doctorOrOfficerName = doctorName,
            bloodGroup = bloodGroup,
            component = component,
            unitsRequired = units,
            patientCaseRef = patientCaseRef,
            state = state,
            district = district,
            city = city,
            requiredWithinHours = hours,
            urgency = urgency,
            status = RequestStatus.DONORS_NOTIFIED,
            createdAt = "Just now",
            matchedDonors = matchRecords,
            notes = notes
        )

        BloodDatabase.createEmergencyRequest(request)
        _uiState.update {
            it.copy(successSnackbarMessage = "आपत्कालीन विनंती निर्माण केली! ID: $reqId")
        }
        return reqId
    }

    fun acceptEmergencyRequest(requestId: String, donorId: String) {
        BloodDatabase.acceptRequestByDonor(requestId, donorId)
        _uiState.update {
            it.copy(successSnackbarMessage = "आपत्कालीन विनंती स्वीकारली! रक्त केंद्राशी समन्वय साधला जाईल.")
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(successSnackbarMessage = null) }
    }

    fun setUniversalSearchQuery(query: String) {
        _uiState.update { it.copy(universalSearchQuery = query) }
    }
}
