package com.example.data.model

data class ImbalanceItem(
    val district: String,
    val bloodGroup: BloodGroup,
    val unitsAvailable: Int,
    val status: StockStatus
)

data class ImbalanceAlert(
    val id: String,
    val title: String,
    val state: String,
    val primaryGroup: BloodGroup,
    val surplusDistrict: String,
    val surplusUnits: Int,
    val shortageDistrict: String,
    val shortageUnits: Int,
    val recommendedAction: String,
    val timestamp: String
)

data class AuditLogEntry(
    val id: String,
    val timestamp: String,
    val userRole: String,
    val action: String,
    val details: String,
    val ipHash: String = "SHA256:8f2a...c041"
)
