package com.example.data.model

enum class RequestStatus(val label: String, val marathiLabel: String) {
    PENDING_VERIFICATION("Pending Centre Verification", "पडताळणी प्रलंबित"),
    DONORS_NOTIFIED("Donors Alerted", "रक्तदात्यांना संदेश पाठवला"),
    DONOR_ACCEPTED("Donor Accepted Assistance", "रक्तदात्याने संमती दर्शवली"),
    DISPATCHED("Units Allocated / In Transit", "रक्त युनिट्स रवाना"),
    COMPLETED("Completed & Administered", "यशस्वीरित्या पूर्ण")
}

data class DonorMatchRecord(
    val donorId: String,
    val approximateDistanceKm: Double,
    val matchPriorityLevel: Int, // 1 to 5
    val notificationTime: String,
    val responseStatus: String // "ALERTED", "ACCEPTED", "DECLINED", "NO_RESPONSE"
)

data class EmergencyRequest(
    val id: String,
    val hospitalName: String,
    val doctorOrOfficerName: String,
    val bloodGroup: BloodGroup,
    val component: BloodComponent,
    val unitsRequired: Int,
    val patientCaseRef: String, // Anonymized reference, never patient private name/aadhaar
    val state: String,
    val district: String,
    val city: String,
    val requiredWithinHours: Int = 2,
    val urgency: UrgencyLevel,
    val status: RequestStatus = RequestStatus.DONORS_NOTIFIED,
    val createdAt: String,
    val matchedDonors: List<DonorMatchRecord> = emptyList(),
    val notes: String = "Authorized hospital emergency requirement"
)
