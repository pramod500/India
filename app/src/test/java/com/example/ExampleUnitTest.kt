package com.example

import com.example.data.model.BloodGroup
import com.example.data.model.StockStatus
import com.example.data.repository.BloodDatabase
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun syntheticDatabase_hasOver100Donors() {
        assertTrue(
            "Donor database must have at least 100 synthetic records as per specification",
            BloodDatabase.syntheticDonors.size >= 100
        )
    }

    @Test
    fun bloodMatchingAlgorithm_matchesCorrectPriorities() {
        val matches = BloodDatabase.matchDonors(
            requiredBloodGroup = BloodGroup.O_POS,
            state = "Maharashtra",
            district = "Kolhapur",
            cityOrTaluka = "Kolhapur City"
        )

        assertFalse("Matches for O+ in Kolhapur should not be empty", matches.isEmpty())
        val topMatch = matches.first()
        assertEquals("Priority 1 should be awarded to matching local donor", 1, topMatch.second)
    }

    @Test
    fun rareBombayOh_matchesOnlyBombayOhDonors() {
        val matches = BloodDatabase.matchDonors(
            requiredBloodGroup = BloodGroup.BOMBAY_OH,
            state = "Maharashtra",
            district = "Kolhapur",
            cityOrTaluka = "Karvir"
        )

        assertTrue(matches.all { it.first.bloodGroup == BloodGroup.BOMBAY_OH })
    }

    @Test
    fun bloodCentreStock_calculatesAdequacyCorrectly() {
        val cpr = BloodDatabase.syntheticBloodCentres.first { it.id == "BC-MH-KOL-01" }
        assertEquals(StockStatus.ADEQUATE, cpr.getStatusFor(BloodGroup.O_POS))

        val gmcMiraj = BloodDatabase.syntheticBloodCentres.first { it.id == "BC-MH-SNG-01" }
        assertEquals(StockStatus.SHORTAGE, gmcMiraj.getStatusFor(BloodGroup.O_POS))
    }
}
