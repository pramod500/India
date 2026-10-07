package com.example.data.model

enum class BloodGroup(
    val code: String,
    val displayName: String,
    val isRare: Boolean = false,
    val canDonateTo: List<String>,
    val canReceiveFrom: List<String>
) {
    O_POS("O+", "O+", false, listOf("O+", "A+", "B+", "AB+"), listOf("O+", "O-")),
    O_NEG("O-", "O- (Universal)", false, listOf("All Blood Groups"), listOf("O-")),
    A_POS("A+", "A+", false, listOf("A+", "AB+"), listOf("A+", "A-", "O+", "O-")),
    A_NEG("A-", "A-", false, listOf("A+", "A-", "AB+", "AB-"), listOf("A-", "O-")),
    B_POS("B+", "B+", false, listOf("B+", "AB+"), listOf("B+", "B-", "O+", "O-")),
    B_NEG("B-", "B-", false, listOf("B+", "B-", "AB+", "AB-"), listOf("B-", "O-")),
    AB_POS("AB+", "AB+ (Universal Recipient)", false, listOf("AB+"), listOf("All Blood Groups")),
    AB_NEG("AB-", "AB-", false, listOf("AB+", "AB-"), listOf("AB-", "A-", "B-", "O-")),
    BOMBAY_OH("Bombay / Oh", "Bombay / Oh (Rare)", true, listOf("Bombay / Oh, All ABO"), listOf("Bombay / Oh Only"));

    companion object {
        fun fromString(str: String): BloodGroup {
            return entries.firstOrNull { it.code.equals(str.trim(), ignoreCase = true) || it.name.equals(str.trim(), ignoreCase = true) }
                ?: O_POS
        }
    }
}

enum class BloodComponent(val displayName: String, val marathiName: String, val shelfLifeDays: Int) {
    WHOLE_BLOOD("Whole Blood", "संपूर्ण रक्त", 35),
    PRBC("PRBC (Packed Red Cells)", "पॅक्ड रेड ब्लड सेल्स", 42),
    PLATELETS("Platelets (RDP/SDP)", "प्लेटलेट्स", 5),
    PLASMA("Fresh Frozen Plasma (FFP)", "प्लाझ्मा", 365),
    CRYOPRECIPITATE("Cryoprecipitate", "क्रायोप्रेसिपिटेट", 365)
}

enum class UrgencyLevel(val label: String, val marathiLabel: String, val priorityIndex: Int) {
    CRITICAL_EMERGENCY("Critical Emergency", "तात्काळ अति-आपत्कालीन", 1),
    URGENT("Urgent Requirement", "तातडीची गरज", 2),
    NORMAL("Normal / Regular", "सामान्य", 3),
    PLANNED("Planned / Advance", "नियोजित", 4)
}

enum class StockStatus(val label: String, val marathiLabel: String) {
    ADEQUATE("Adequate", "पुरेसा"),
    LOW("Low", "कमी"),
    SHORTAGE("Shortage", "तुटवडा"),
    CRITICAL("Critical", "गंभीर तुटवडा"),
    UNAVAILABLE("Unavailable", "अनुपलब्ध")
}

enum class UserRole(val displayName: String, val marathiTitle: String, val description: String) {
    CITIZEN_DONOR("Citizen / Donor", "नागरिक / रक्तदाता", "रक्तगट नोंदणी, संमती, आपत्कालीन विनंत्या"),
    PATIENT_FAMILY("Patient / Family", "रुग्ण / नातेवाईक", "तातडीने रक्त शोध व मदत विनंती"),
    HOSPITAL_OFFICER("Hospital / Doctor", "रुग्णालय / वैद्यकीय अधिकारी", "अधिकृत आपत्कालीन विनंती निर्मिती"),
    BLOOD_CENTRE_ADMIN("Blood Centre Officer", "रक्तपेढी व्यवस्थापक", "साठा अद्ययावतीकरण व तपासणी"),
    DISTRICT_OFFICER("District Health Officer", "जिल्हा आरोग्य अधिकारी", "जिल्हास्तरीय साठा व समन्वय"),
    STATE_ADMIN("State Government Admin", "राज्य शासन प्रशासक", "राज्यव्यापी डॅशबोर्ड व तुटवडा विश्लेषण"),
    NATIONAL_ADMIN("National Admin (MoHFW)", "राष्ट्रीय प्रशासक (MoHFW)", "अखिल भारतीय डॅशबोर्ड व धोरण नियंत्रण")
}
