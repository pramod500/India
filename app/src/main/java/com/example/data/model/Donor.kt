package com.example.data.model

data class DonationRecord(
    val id: String,
    val date: String,
    val centreName: String,
    val component: BloodComponent,
    val units: Int = 1,
    val certificateId: String
)

enum class DonorAvailabilityStatus(val label: String, val marathiLabel: String, val iconEmoji: String) {
    AVAILABLE("Available for request", "उपलब्ध", "🟢"),
    TEMPORARILY_UNAVAILABLE("Temporarily unavailable", "तात्पुरते अनुपलब्ध", "🟡"),
    NOT_AVAILABLE("Not available", "अनुपलब्ध", "🔴"),
    VERIFICATION_PENDING("Verification pending", "पडताळणी प्रलंबित", "⚪")
}

data class Donor(
    val id: String,                         // e.g. "DEMO-MH-KOL-0001"
    val state: String,
    val district: String,
    val cityOrTaluka: String,
    val bloodGroup: BloodGroup,
    val isVerified: Boolean = true,
    val verificationSource: String = "Govt Blood Centre / e-RaktKosh Lab",
    val verificationDate: String = "15 Jan 2026",
    val hasConsent: Boolean = true,
    val emergencyAvailable: Boolean = true,
    val status: DonorAvailabilityStatus = DonorAvailabilityStatus.AVAILABLE,
    val approximateDistanceKm: Double = 3.2,
    val lastDonationDate: String = "12 Nov 2025",
    val donationHistory: List<DonationRecord> = emptyList(),
    // Private attributes only visible in Donor's own private logged-in profile (never in public/search listings)
    val privateFirstName: String = "Citizen",
    val privateLastName: String = "Donor",
    val privateMobileMasked: String = "XXXXXX1234",
    val notificationChannel: String = "App + SMS"
) {
    // Public anonymized label to strictly protect privacy
    val publicDisplayTag: String
        get() = "${id.take(4)}-${district.take(3).uppercase()}-${id.takeLast(4)}"
}
