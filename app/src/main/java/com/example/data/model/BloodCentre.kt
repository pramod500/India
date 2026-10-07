package com.example.data.model

data class BloodGroupStock(
    val bloodGroup: BloodGroup,
    val units: Int,
    val status: StockStatus
)

data class BloodCentre(
    val id: String,
    val name: String,
    val state: String,
    val district: String,
    val city: String,
    val address: String,
    val officialHelpline: String,
    val isGovtAuthorized: Boolean = true,
    val licenseNumber: String,
    val distanceKm: Double,
    val stockMap: Map<BloodGroup, Int>,
    val componentStockMap: Map<BloodComponent, Int>,
    val lastUpdated: String = "Today, 10:30 AM"
) {
    fun getUnitsFor(group: BloodGroup): Int = stockMap[group] ?: 0

    fun getStatusFor(group: BloodGroup): StockStatus {
        val count = getUnitsFor(group)
        return when {
            count >= 20 -> StockStatus.ADEQUATE
            count in 10..19 -> StockStatus.LOW
            count in 4..9 -> StockStatus.SHORTAGE
            count in 1..3 -> StockStatus.CRITICAL
            else -> StockStatus.UNAVAILABLE
        }
    }
}
