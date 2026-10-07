package com.example.data.repository

import com.example.data.model.*
import kotlin.math.roundToInt

object BloodDatabase {

    // Initial synthetic donors (at least 100 realistic records)
    val syntheticDonors: MutableList<Donor> = run {
        val list = mutableListOf<Donor>()

        // Explicit requested seed records
        list.add(Donor("DEMO-MH-KOL-0001", "Maharashtra", "Kolhapur", "Kolhapur City", BloodGroup.O_POS, true, "CPR Civil Hospital Blood Centre", "10 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 2.4, "05 Dec 2025"))
        list.add(Donor("DEMO-MH-KOL-0002", "Maharashtra", "Kolhapur", "Ichalkaranji", BloodGroup.B_POS, true, "Govt Blood Bank Ichalkaranji", "12 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 7.8, "18 Nov 2025"))
        list.add(Donor("DEMO-MH-KOL-0003", "Maharashtra", "Kolhapur", "Kagal", BloodGroup.A_POS, true, "District Red Cross Centre", "15 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 11.2, "22 Oct 2025"))
        list.add(Donor("DEMO-MH-KOL-0004", "Maharashtra", "Kolhapur", "Panhala", BloodGroup.O_NEG, true, "CPR Civil Hospital Blood Centre", "20 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 14.5, "02 Jan 2026"))
        list.add(Donor("DEMO-MH-SNG-0001", "Maharashtra", "Sangli", "Miraj", BloodGroup.B_POS, true, "GMC Miraj Blood Centre", "08 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 4.1, "14 Dec 2025"))
        list.add(Donor("DEMO-MH-SNG-0002", "Maharashtra", "Sangli", "Sangli City", BloodGroup.AB_POS, true, "Bharati Blood Centre", "19 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 6.3, "28 Nov 2025"))
        list.add(Donor("DEMO-MH-SAT-0001", "Maharashtra", "Satara", "Satara City", BloodGroup.O_POS, true, "Krantidoot Blood Bank", "05 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 8.9, "19 Dec 2025"))
        list.add(Donor("DEMO-KA-BEL-0001", "Karnataka", "Belagavi", "Belagavi City", BloodGroup.O_POS, true, "District Civil Hospital", "11 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 12.0, "01 Nov 2025"))
        list.add(Donor("DEMO-GJ-AHM-0001", "Gujarat", "Ahmedabad", "Ahmedabad City", BloodGroup.A_POS, true, "Red Cross Society Ahmedabad", "02 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 3.5, "10 Dec 2025"))
        list.add(Donor("DEMO-UP-LKO-0001", "Uttar Pradesh", "Lucknow", "Lucknow City", BloodGroup.B_POS, true, "KGMU Blood Bank", "14 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 5.0, "15 Dec 2025"))
        list.add(Donor("DEMO-DL-DEL-0001", "Delhi", "New Delhi", "Connaught Place", BloodGroup.O_NEG, true, "AIIMS Blood Bank", "22 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 4.2, "20 Dec 2025"))
        list.add(Donor("DEMO-TN-CHN-0001", "Tamil Nadu", "Chennai", "Chennai Central", BloodGroup.A_POS, true, "Rajiv Gandhi Govt Hospital", "17 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 6.1, "05 Jan 2026"))

        // Rare Bombay/Oh blood group synthetic donors
        list.add(Donor("DEMO-MH-KOL-RARE1", "Maharashtra", "Kolhapur", "Karvir", BloodGroup.BOMBAY_OH, true, "CPR Lab Verified", "01 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 3.8, "12 Oct 2025"))
        list.add(Donor("DEMO-MH-SNG-RARE1", "Maharashtra", "Sangli", "Miraj", BloodGroup.BOMBAY_OH, true, "Miraj GMC Special Registry", "10 Dec 2025", true, true, DonorAvailabilityStatus.AVAILABLE, 9.4, "15 Sep 2025"))
        list.add(Donor("DEMO-MH-PUN-RARE1", "Maharashtra", "Pune", "Pune City", BloodGroup.BOMBAY_OH, true, "Sassoon Rare Phenotype Registry", "18 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 18.0, "03 Aug 2025"))
        list.add(Donor("DEMO-MH-MUM-RARE1", "Maharashtra", "Mumbai City", "Parel", BloodGroup.BOMBAY_OH, true, "KEM Institute of Immunohematology", "24 Jan 2026", true, true, DonorAvailabilityStatus.AVAILABLE, 22.0, "20 Jul 2025"))

        // Generate additional synthetic donors across districts and blood groups to reach 110+ records
        val districtsConfig = listOf(
            Triple("Maharashtra", "Kolhapur", listOf("Kolhapur City", "Ichalkaranji", "Kagal", "Panhala", "Hatkanangle", "Karvir", "Radhanagari", "Shahuwadi")),
            Triple("Maharashtra", "Sangli", listOf("Sangli City", "Miraj", "Tasgaon", "Walwa", "Shirala", "Palus")),
            Triple("Maharashtra", "Satara", listOf("Satara City", "Karad", "Phaltan", "Wai", "Patan")),
            Triple("Maharashtra", "Pune", listOf("Pune City", "Haveli", "Pimpri-Chinchwad", "Baramati")),
            Triple("Karnataka", "Belagavi", listOf("Belagavi City", "Chikkodi", "Gokak", "Athani")),
            Triple("Gujarat", "Ahmedabad", listOf("Ahmedabad City", "Sanand", "Dholka")),
            Triple("Uttar Pradesh", "Lucknow", listOf("Lucknow City", "Mohanlalganj", "Malihabad")),
            Triple("Delhi", "New Delhi", listOf("Connaught Place", "Chanakyapuri", "Vasant Vihar")),
            Triple("Tamil Nadu", "Chennai", listOf("Chennai Central", "Mylapore", "Guindy"))
        )

        val groups = BloodGroup.entries.filter { !it.isRare }
        var idCounter = 10

        districtsConfig.forEach { (state, district, talukas) ->
            talukas.forEach { taluka ->
                groups.forEach { bg ->
                    idCounter++
                    val distCode = district.take(3).uppercase()
                    val donorId = "DEMO-${state.take(2).uppercase()}-$distCode-${String.format("%04d", idCounter)}"
                    val isAvailable = (idCounter % 5 != 0) // 80% available
                    val status = if (isAvailable) DonorAvailabilityStatus.AVAILABLE else DonorAvailabilityStatus.TEMPORARILY_UNAVAILABLE
                    val distance = (1.5 + (idCounter % 15) * 1.3)

                    list.add(
                        Donor(
                            id = donorId,
                            state = state,
                            district = district,
                            cityOrTaluka = taluka,
                            bloodGroup = bg,
                            isVerified = true,
                            verificationSource = "Authorized e-RaktKosh Unit",
                            verificationDate = "15 Jan 2026",
                            hasConsent = true,
                            emergencyAvailable = isAvailable,
                            status = status,
                            approximateDistanceKm = ((distance * 10).roundToInt() / 10.0),
                            lastDonationDate = "2025-11-10",
                            donationHistory = listOf(
                                DonationRecord("DON-2025-09", "15 Apr 2025", "$district Civil Hospital", BloodComponent.WHOLE_BLOOD, 1, "CERT-MH-8831"),
                                DonationRecord("DON-2025-11", "10 Nov 2025", "$district Red Cross Blood Bank", BloodComponent.PRBC, 1, "CERT-MH-9421")
                            )
                        )
                    )
                }
            }
        }

        list
    }

    // Demo Blood Centres
    val syntheticBloodCentres: MutableList<BloodCentre> = mutableListOf(
        BloodCentre(
            id = "BC-MH-KOL-01",
            name = "Chhatrapati Pramila Raje (CPR) Civil Hospital Blood Centre",
            state = "Maharashtra",
            district = "Kolhapur",
            city = "Kolhapur City",
            address = "Bhausinghji Road, CPR Chowk, Kolhapur",
            officialHelpline = "0231-2641555 / 104",
            isGovtAuthorized = true,
            licenseNumber = "MH-SBTC-LIC-041",
            distanceKm = 2.1,
            stockMap = mapOf(
                BloodGroup.O_POS to 140,
                BloodGroup.O_NEG to 12,
                BloodGroup.A_POS to 120,
                BloodGroup.A_NEG to 15,
                BloodGroup.B_POS to 105,
                BloodGroup.B_NEG to 10,
                BloodGroup.AB_POS to 35,
                BloodGroup.AB_NEG to 5,
                BloodGroup.BOMBAY_OH to 1
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 180,
                BloodComponent.PRBC to 140,
                BloodComponent.PLATELETS to 45,
                BloodComponent.PLASMA to 95,
                BloodComponent.CRYOPRECIPITATE to 22
            )
        ),
        BloodCentre(
            id = "BC-MH-KOL-02",
            name = "Sanjeevani Voluntary Blood Centre",
            state = "Maharashtra",
            district = "Kolhapur",
            city = "Kolhapur City",
            address = "Station Road, New Shahupuri, Kolhapur",
            officialHelpline = "0231-2523411",
            isGovtAuthorized = true,
            licenseNumber = "MH-SBTC-LIC-088",
            distanceKm = 3.6,
            stockMap = mapOf(
                BloodGroup.O_POS to 45,
                BloodGroup.O_NEG to 4,
                BloodGroup.A_POS to 38,
                BloodGroup.A_NEG to 6,
                BloodGroup.B_POS to 42,
                BloodGroup.B_NEG to 3,
                BloodGroup.AB_POS to 16,
                BloodGroup.AB_NEG to 2,
                BloodGroup.BOMBAY_OH to 0
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 60,
                BloodComponent.PRBC to 55,
                BloodComponent.PLATELETS to 20,
                BloodComponent.PLASMA to 34,
                BloodComponent.CRYOPRECIPITATE to 8
            )
        ),
        BloodCentre(
            id = "BC-MH-KOL-03",
            name = "Ichalkaranji Municipal Blood Bank",
            state = "Maharashtra",
            district = "Kolhapur",
            city = "Ichalkaranji",
            address = "Near Municipal Council, Main Road, Ichalkaranji",
            officialHelpline = "0230-2432990",
            isGovtAuthorized = true,
            licenseNumber = "MH-SBTC-LIC-112",
            distanceKm = 8.4,
            stockMap = mapOf(
                BloodGroup.O_POS to 28,
                BloodGroup.O_NEG to 2,
                BloodGroup.A_POS to 24,
                BloodGroup.A_NEG to 3,
                BloodGroup.B_POS to 30,
                BloodGroup.B_NEG to 2,
                BloodGroup.AB_POS to 10,
                BloodGroup.AB_NEG to 1,
                BloodGroup.BOMBAY_OH to 0
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 40,
                BloodComponent.PRBC to 35,
                BloodComponent.PLATELETS to 12,
                BloodComponent.PLASMA to 22,
                BloodComponent.CRYOPRECIPITATE to 4
            )
        ),
        BloodCentre(
            id = "BC-MH-SNG-01",
            name = "Government Medical College & Hospital (GMC) Blood Centre",
            state = "Maharashtra",
            district = "Sangli",
            city = "Miraj",
            address = "Pandharpur Road, Miraj Medical Complex, Sangli",
            officialHelpline = "0233-2221102 / 104",
            isGovtAuthorized = true,
            licenseNumber = "MH-SBTC-LIC-029",
            distanceKm = 7.2,
            stockMap = mapOf(
                BloodGroup.O_POS to 6,   // CRITICAL SHORTAGE
                BloodGroup.O_NEG to 1,   // CRITICAL SHORTAGE
                BloodGroup.A_POS to 35,
                BloodGroup.A_NEG to 4,
                BloodGroup.B_POS to 40,
                BloodGroup.B_NEG to 2,
                BloodGroup.AB_POS to 12,
                BloodGroup.AB_NEG to 1,
                BloodGroup.BOMBAY_OH to 0
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 45,
                BloodComponent.PRBC to 30,
                BloodComponent.PLATELETS to 8,
                BloodComponent.PLASMA to 25,
                BloodComponent.CRYOPRECIPITATE to 3
            )
        ),
        BloodCentre(
            id = "BC-MH-SNG-02",
            name = "Bharati Hospital Blood Bank",
            state = "Maharashtra",
            district = "Sangli",
            city = "Sangli City",
            address = "Sangli-Miraj Road, Wanlesswadi, Sangli",
            officialHelpline = "0233-2601234",
            isGovtAuthorized = true,
            licenseNumber = "MH-SBTC-LIC-076",
            distanceKm = 10.5,
            stockMap = mapOf(
                BloodGroup.O_POS to 5,   // CRITICAL
                BloodGroup.O_NEG to 0,   // UNAVAILABLE
                BloodGroup.A_POS to 22,
                BloodGroup.A_NEG to 2,
                BloodGroup.B_POS to 25,
                BloodGroup.B_NEG to 1,
                BloodGroup.AB_POS to 8,
                BloodGroup.AB_NEG to 0,
                BloodGroup.BOMBAY_OH to 0
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 30,
                BloodComponent.PRBC to 20,
                BloodComponent.PLATELETS to 6,
                BloodComponent.PLASMA to 18,
                BloodComponent.CRYOPRECIPITATE to 2
            )
        ),
        BloodCentre(
            id = "BC-MH-SAT-01",
            name = "Krantidoot Blood Bank & Transfusion Centre",
            state = "Maharashtra",
            district = "Satara",
            city = "Satara City",
            address = "Near Powai Naka, Sadashiv Peth, Satara",
            officialHelpline = "02162-234555",
            isGovtAuthorized = true,
            licenseNumber = "MH-SBTC-LIC-145",
            distanceKm = 14.0,
            stockMap = mapOf(
                BloodGroup.O_POS to 14,  // LOW
                BloodGroup.O_NEG to 2,
                BloodGroup.A_POS to 40,
                BloodGroup.A_NEG to 5,
                BloodGroup.B_POS to 38,
                BloodGroup.B_NEG to 4,
                BloodGroup.AB_POS to 14,
                BloodGroup.AB_NEG to 2,
                BloodGroup.BOMBAY_OH to 0
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 48,
                BloodComponent.PRBC to 39,
                BloodComponent.PLATELETS to 14,
                BloodComponent.PLASMA to 28,
                BloodComponent.CRYOPRECIPITATE to 5
            )
        ),
        BloodCentre(
            id = "BC-MH-PUN-01",
            name = "Sassoon General Hospital & BJMC Blood Centre",
            state = "Maharashtra",
            district = "Pune",
            city = "Pune City",
            address = "Near Pune Railway Station, Sassoon Road, Pune",
            officialHelpline = "020-26128000 / 104",
            isGovtAuthorized = true,
            licenseNumber = "MH-SBTC-LIC-002",
            distanceKm = 24.0,
            stockMap = mapOf(
                BloodGroup.O_POS to 160,
                BloodGroup.O_NEG to 18,
                BloodGroup.A_POS to 140,
                BloodGroup.A_NEG to 16,
                BloodGroup.B_POS to 135,
                BloodGroup.B_NEG to 12,
                BloodGroup.AB_POS to 45,
                BloodGroup.AB_NEG to 8,
                BloodGroup.BOMBAY_OH to 2
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 210,
                BloodComponent.PRBC to 180,
                BloodComponent.PLATELETS to 65,
                BloodComponent.PLASMA to 120,
                BloodComponent.CRYOPRECIPITATE to 35
            )
        ),
        BloodCentre(
            id = "BC-KA-BEL-01",
            name = "Belagavi Institute of Medical Sciences (BIMS) Blood Bank",
            state = "Karnataka",
            district = "Belagavi",
            city = "Belagavi City",
            address = "Civil Hospital Road, Sadashiv Nagar, Belagavi",
            officialHelpline = "0831-2403123",
            isGovtAuthorized = true,
            licenseNumber = "KA-SBTC-LIC-064",
            distanceKm = 16.5,
            stockMap = mapOf(
                BloodGroup.O_POS to 52,
                BloodGroup.O_NEG to 7,
                BloodGroup.A_POS to 45,
                BloodGroup.A_NEG to 6,
                BloodGroup.B_POS to 48,
                BloodGroup.B_NEG to 5,
                BloodGroup.AB_POS to 18,
                BloodGroup.AB_NEG to 3,
                BloodGroup.BOMBAY_OH to 0
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 65,
                BloodComponent.PRBC to 58,
                BloodComponent.PLATELETS to 18,
                BloodComponent.PLASMA to 35,
                BloodComponent.CRYOPRECIPITATE to 7
            )
        ),
        BloodCentre(
            id = "BC-DL-DEL-01",
            name = "AIIMS Main Blood Bank & Component Facility",
            state = "Delhi",
            district = "New Delhi",
            city = "Connaught Place",
            address = "Sri Aurobindo Marg, Ansari Nagar, New Delhi",
            officialHelpline = "011-26593201 / 104",
            isGovtAuthorized = true,
            licenseNumber = "DL-SBTC-LIC-001",
            distanceKm = 5.0,
            stockMap = mapOf(
                BloodGroup.O_POS to 220,
                BloodGroup.O_NEG to 25,
                BloodGroup.A_POS to 190,
                BloodGroup.A_NEG to 22,
                BloodGroup.B_POS to 210,
                BloodGroup.B_NEG to 19,
                BloodGroup.AB_POS to 70,
                BloodGroup.AB_NEG to 14,
                BloodGroup.BOMBAY_OH to 3
            ),
            componentStockMap = mapOf(
                BloodComponent.WHOLE_BLOOD to 300,
                BloodComponent.PRBC to 280,
                BloodComponent.PLATELETS to 110,
                BloodComponent.PLASMA to 190,
                BloodComponent.CRYOPRECIPITATE to 50
            )
        )
    )

    // Active Emergency Requests
    val activeRequests: MutableList<EmergencyRequest> = mutableListOf(
        EmergencyRequest(
            id = "REQ-EMG-2026-0891",
            hospitalName = "Chhatrapati Pramila Raje Civil Hospital, Kolhapur",
            doctorOrOfficerName = "Dr. S. K. Deshmukh (Casualty Incharge)",
            bloodGroup = BloodGroup.O_NEG,
            component = BloodComponent.PRBC,
            unitsRequired = 2,
            patientCaseRef = "IPD-TRAUMA-4182",
            state = "Maharashtra",
            district = "Kolhapur",
            city = "Kolhapur City",
            requiredWithinHours = 1,
            urgency = UrgencyLevel.CRITICAL_EMERGENCY,
            status = RequestStatus.DONORS_NOTIFIED,
            createdAt = "Today, 10:15 AM",
            matchedDonors = listOf(
                DonorMatchRecord("DEMO-MH-KOL-0004", 4.1, 1, "10:16 AM", "ALERTED"),
                DonorMatchRecord("DEMO-MH-KOL-0024", 8.5, 2, "10:16 AM", "ALERTED")
            ),
            notes = "Acute polytrauma case requiring immediate O- negative transfusion"
        ),
        EmergencyRequest(
            id = "REQ-EMG-2026-0885",
            hospitalName = "Miraj Government Medical College Hospital, Sangli",
            doctorOrOfficerName = "Dr. Rajesh Patil (ICU Consultant)",
            bloodGroup = BloodGroup.O_POS,
            component = BloodComponent.WHOLE_BLOOD,
            unitsRequired = 4,
            patientCaseRef = "IPD-OBGY-3319",
            state = "Maharashtra",
            district = "Sangli",
            city = "Miraj",
            requiredWithinHours = 2,
            urgency = UrgencyLevel.CRITICAL_EMERGENCY,
            status = RequestStatus.DONOR_ACCEPTED,
            createdAt = "Today, 08:40 AM",
            matchedDonors = listOf(
                DonorMatchRecord("DEMO-MH-SNG-0001", 3.2, 1, "08:42 AM", "ACCEPTED"),
                DonorMatchRecord("DEMO-MH-KOL-0001", 7.8, 3, "08:42 AM", "ACCEPTED")
            ),
            notes = "Emergency post-partum haemorrhage requirement - local Sangli stock critical"
        ),
        EmergencyRequest(
            id = "REQ-EMG-2026-0870",
            hospitalName = "King George's Medical University, Lucknow",
            doctorOrOfficerName = "Dr. Ananya Mishra",
            bloodGroup = BloodGroup.B_POS,
            component = BloodComponent.PLATELETS,
            unitsRequired = 6,
            patientCaseRef = "IPD-HEMA-1092",
            state = "Uttar Pradesh",
            district = "Lucknow",
            city = "Lucknow City",
            requiredWithinHours = 4,
            urgency = UrgencyLevel.URGENT,
            status = RequestStatus.DONORS_NOTIFIED,
            createdAt = "Today, 06:30 AM",
            matchedDonors = listOf(
                DonorMatchRecord("DEMO-UP-LKO-0001", 5.0, 1, "06:35 AM", "ALERTED")
            ),
            notes = "Thrombocytopenia patient undergoing emergency hematology support"
        )
    )

    // Regional Blood Imbalance Alerts (AI / System Decision Advisory)
    val regionalImbalanceAlerts: MutableList<ImbalanceAlert> = mutableListOf(
        ImbalanceAlert(
            id = "IMB-2026-001",
            title = "⚠️ REGIONAL BLOOD IMBALANCE: O+ SURPLUS IN KOLHAPUR VS CRITICAL IN SANGLI",
            state = "Maharashtra",
            primaryGroup = BloodGroup.O_POS,
            surplusDistrict = "Kolhapur",
            surplusUnits = 140, // Adequate
            shortageDistrict = "Sangli",
            shortageUnits = 6,   // Critical
            recommendedAction = "Review authorized blood-centre inter-district transfer from CPR Kolhapur (140 units) to Miraj GMC Sangli (6 units) and mobilize district border donors. Final decision remains with authorized State Blood Transfusion Council.",
            timestamp = "Today, 09:00 AM"
        ),
        ImbalanceAlert(
            id = "IMB-2026-002",
            title = "⚠️ B- NEGATIVE DEFICIT IN KOLHAPUR & SANGLI CLUSTER",
            state = "Maharashtra",
            primaryGroup = BloodGroup.B_NEG,
            surplusDistrict = "Pune (Sassoon Centre)",
            surplusUnits = 12,
            shortageDistrict = "Kolhapur (CPR)",
            shortageUnits = 2,
            recommendedAction = "Alert registered B- negative donors in Karvir and Ichalkaranji talukas for voluntary emergency camp mobilization.",
            timestamp = "Yesterday, 04:30 PM"
        )
    )

    // Audit logs
    val auditLogs: MutableList<AuditLogEntry> = mutableListOf(
        AuditLogEntry("LOG-1001", "Today 10:16 AM", "Hospital Officer (CPR)", "Emergency Request Created", "Created REQ-EMG-2026-0891 for 2 units O- with critical priority"),
        AuditLogEntry("LOG-1002", "Today 10:16 AM", "System Matching Engine", "Donors Alerted", "Sent secure SMS & App alerts to 2 matching verified donors (distance < 9km)"),
        AuditLogEntry("LOG-1003", "Today 08:45 AM", "Donor (DEMO-MH-SNG-0001)", "Emergency Request Accepted", "Donor accepted request REQ-EMG-2026-0885 via mobile app"),
        AuditLogEntry("LOG-1004", "Today 07:00 AM", "Blood Centre Admin", "Stock Synchronisation", "Updated 9 blood groups & 5 component counts at CPR Civil Hospital"),
        AuditLogEntry("LOG-1005", "Yesterday 05:15 PM", "State Admin (MH)", "Inter-district Imbalance Alert Generated", "Generated recommendation for Kolhapur -> Sangli O+ transfer")
    )

    // Search and matching algorithm
    fun matchDonors(
        requiredBloodGroup: BloodGroup,
        state: String,
        district: String,
        cityOrTaluka: String
    ): List<Pair<Donor, Int>> { // Donor and Priority Level (1 to 5)
        val compatibleGroups = getCompatibleDonorGroups(requiredBloodGroup)

        val results = mutableListOf<Pair<Donor, Int>>()

        syntheticDonors
            .filter { it.hasConsent && it.emergencyAvailable && it.status == DonorAvailabilityStatus.AVAILABLE }
            .filter { compatibleGroups.contains(it.bloodGroup) }
            .forEach { donor ->
                val priority = when {
                    // Priority 1: Exact group + same taluka/city + verified
                    donor.bloodGroup == requiredBloodGroup && donor.district.equals(district, true) && donor.cityOrTaluka.equals(cityOrTaluka, true) && donor.isVerified -> 1
                    // Priority 2: Compatible or exact group + same district (<10km)
                    donor.district.equals(district, true) && donor.approximateDistanceKm <= 10.0 -> 2
                    // Priority 3: District level donor
                    donor.district.equals(district, true) -> 3
                    // Priority 4: Nearby district donor (same state)
                    donor.state.equals(state, true) -> 4
                    // Priority 5: State level emergency mobilisation
                    else -> 5
                }
                results.add(Pair(donor, priority))
            }

        return results.sortedWith(compareBy({ it.second }, { it.first.approximateDistanceKm }))
    }

    private fun getCompatibleDonorGroups(recipient: BloodGroup): List<BloodGroup> {
        return when (recipient) {
            BloodGroup.O_NEG -> listOf(BloodGroup.O_NEG)
            BloodGroup.O_POS -> listOf(BloodGroup.O_POS, BloodGroup.O_NEG)
            BloodGroup.A_NEG -> listOf(BloodGroup.A_NEG, BloodGroup.O_NEG)
            BloodGroup.A_POS -> listOf(BloodGroup.A_POS, BloodGroup.A_NEG, BloodGroup.O_POS, BloodGroup.O_NEG)
            BloodGroup.B_NEG -> listOf(BloodGroup.B_NEG, BloodGroup.O_NEG)
            BloodGroup.B_POS -> listOf(BloodGroup.B_POS, BloodGroup.B_NEG, BloodGroup.O_POS, BloodGroup.O_NEG)
            BloodGroup.AB_NEG -> listOf(BloodGroup.AB_NEG, BloodGroup.A_NEG, BloodGroup.B_NEG, BloodGroup.O_NEG)
            BloodGroup.AB_POS -> BloodGroup.entries.filter { !it.isRare }
            BloodGroup.BOMBAY_OH -> listOf(BloodGroup.BOMBAY_OH)
        }
    }

    // Register a new citizen donor
    fun registerDonor(donor: Donor) {
        syntheticDonors.add(0, donor)
        auditLogs.add(0, AuditLogEntry("LOG-${System.currentTimeMillis() % 10000}", "Just now", "Citizen / Donor", "New Donor Registration", "Registered voluntary donor ID ${donor.id} (${donor.bloodGroup.code}) in ${donor.district}"))
    }

    // Create an emergency blood request
    fun createEmergencyRequest(request: EmergencyRequest) {
        activeRequests.add(0, request)
        auditLogs.add(0, AuditLogEntry("LOG-${System.currentTimeMillis() % 10000}", "Just now", "Hospital (${request.hospitalName})", "Emergency Request Created", "Created ${request.id} for ${request.unitsRequired} units ${request.bloodGroup.code} (${request.urgency.label})"))
    }

    fun acceptRequestByDonor(requestId: String, donorId: String) {
        val index = activeRequests.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val req = activeRequests[index]
            val updatedMatches = req.matchedDonors.map {
                if (it.donorId == donorId) it.copy(responseStatus = "ACCEPTED") else it
            }
            activeRequests[index] = req.copy(
                status = RequestStatus.DONOR_ACCEPTED,
                matchedDonors = updatedMatches
            )
            auditLogs.add(0, AuditLogEntry("LOG-${System.currentTimeMillis() % 10000}", "Just now", "Donor ($donorId)", "Accepted Emergency Request", "Donor confirmed emergency donation for $requestId"))
        }
    }
}
