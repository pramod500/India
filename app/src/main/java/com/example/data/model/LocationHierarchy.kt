package com.example.data.model

data class StateInfo(
    val name: String,
    val isUnionTerritory: Boolean = false,
    val districts: List<String>
)

object LocationHierarchy {
    // 28 Indian States & 8 Union Territories
    val states = listOf(
        // 28 States
        StateInfo("Maharashtra", false, listOf("Kolhapur", "Sangli", "Satara", "Pune", "Mumbai City", "Mumbai Suburban", "Thane", "Solapur", "Nashik", "Chhatrapati Sambhajinagar", "Nagpur", "Amravati", "Nanded")),
        StateInfo("Andhra Pradesh", false, listOf("Visakhapatnam", "Vijayawada", "Guntur", "Tirupati", "Kurnool")),
        StateInfo("Arunachal Pradesh", false, listOf("Itanagar", "Tawang", "Pasighat", "Ziro")),
        StateInfo("Assam", false, listOf("Guwahati", "Dibrugarh", "Silchar", "Jorhat", "Tezpur")),
        StateInfo("Bihar", false, listOf("Patna", "Gaya", "Bhagalpur", "Muzaffarpur", "Darbhanga")),
        StateInfo("Chhattisgarh", false, listOf("Raipur", "Bhilai", "Bilaspur", "Korba", "Durg")),
        StateInfo("Goa", false, listOf("North Goa (Panaji)", "South Goa (Margao)")),
        StateInfo("Gujarat", false, listOf("Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar")),
        StateInfo("Haryana", false, listOf("Gurugram", "Faridabad", "Panipat", "Ambala", "Karnal")),
        StateInfo("Himachal Pradesh", false, listOf("Shimla", "Dharamshala", "Mandi", "Solan", "Kullu")),
        StateInfo("Jharkhand", false, listOf("Ranchi", "Jamshedpur", "Dhanbad", "Bokaro", "Deoghar")),
        StateInfo("Karnataka", false, listOf("Belagavi", "Bengaluru Urban", "Mysuru", "Hubballi-Dharwad", "Mangaluru", "Kalaburagi")),
        StateInfo("Kerala", false, listOf("Thiruvananthapuram", "Kochi", "Kozhikode", "Thrissur", "Kollam")),
        StateInfo("Madhya Pradesh", false, listOf("Bhopal", "Indore", "Jabalpur", "Gwalior", "Ujjain")),
        StateInfo("Manipur", false, listOf("Imphal East", "Imphal West", "Churachandpur")),
        StateInfo("Meghalaya", false, listOf("East Khasi Hills (Shillong)", "West Garo Hills")),
        StateInfo("Mizoram", false, listOf("Aizawl", "Lunglei", "Champhai")),
        StateInfo("Nagaland", false, listOf("Kohima", "Dimapur", "Mokokchung")),
        StateInfo("Odisha", false, listOf("Bhubaneswar", "Cuttack", "Rourkela", "Berhampur", "Sambalpur")),
        StateInfo("Punjab", false, listOf("Amritsar", "Ludhiana", "Jalandhar", "Patiala", "Bathinda")),
        StateInfo("Rajasthan", false, listOf("Jaipur", "Jodhpur", "Kota", "Udaipur", "Bikaner")),
        StateInfo("Sikkim", false, listOf("Gangtok", "Namchi", "Gyalshing")),
        StateInfo("Tamil Nadu", false, listOf("Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem")),
        StateInfo("Telangana", false, listOf("Hyderabad", "Warangal", "Nizamabad", "Karimnagar")),
        StateInfo("Tripura", false, listOf("Agartala", "Udaipur", "Dharmanagar")),
        StateInfo("Uttar Pradesh", false, listOf("Lucknow", "Varanasi", "Kanpur", "Noida", "Agra", "Prayagraj")),
        StateInfo("Uttarakhand", false, listOf("Dehradun", "Haridwar", "Nainital", "Rishikesh")),
        StateInfo("West Bengal", false, listOf("Kolkata", "Howrah", "Siliguri", "Durgapur", "Asansol")),

        // 8 Union Territories
        StateInfo("Andaman and Nicobar Islands", true, listOf("South Andaman (Port Blair)", "North and Middle Andaman")),
        StateInfo("Chandigarh", true, listOf("Chandigarh")),
        StateInfo("Dadra and Nagar Haveli and Daman and Diu", true, listOf("Daman", "Diu", "Silvassa")),
        StateInfo("Delhi", true, listOf("New Delhi", "Central Delhi", "South Delhi", "North Delhi", "East Delhi")),
        StateInfo("Jammu and Kashmir", true, listOf("Srinagar", "Jammu", "Anantnag", "Baramulla")),
        StateInfo("Ladakh", true, listOf("Leh", "Kargil")),
        StateInfo("Lakshadweep", true, listOf("Kavaratti", "Agatti")),
        StateInfo("Puducherry", true, listOf("Puducherry", "Karaikal", "Mahe", "Yanam"))
    )

    // Detailed Talukas / Tehsils for demonstration districts
    val talukaMap = mapOf(
        "Kolhapur" to listOf("Kolhapur City", "Ichalkaranji", "Kagal", "Panhala", "Hatkanangle", "Karvir", "Radhanagari", "Shahuwadi", "Shirol", "Chandgad", "Ajara", "Bhudargad", "Gaganbawda"),
        "Sangli" to listOf("Sangli City", "Miraj", "Tasgaon", "Walwa (Islampur)", "Shirala", "Khanapur (Vita)", "Atpadi", "Jath", "Kadegaon", "Palus"),
        "Satara" to listOf("Satara City", "Karad", "Phaltan", "Wai", "Mahabaleshwar", "Patan", "Koregaon", "Khandala", "Jaoli", "Man"),
        "Pune" to listOf("Pune City", "Haveli", "Pimpri-Chinchwad", "Baramati", "Shirur", "Maval", "Mulshi", "Khed"),
        "Belagavi" to listOf("Belagavi City", "Chikkodi", "Gokak", "Athani", "Bailhongal", "Savadatti", "Ramdurg", "Khanapur"),
        "Ahmedabad" to listOf("Ahmedabad City", "Daskroi", "Sanand", "Dholka", "Bavla"),
        "Lucknow" to listOf("Lucknow City", "Bakshi Ka Talab", "Malihabad", "Mohanlalganj", "Sarojini Nagar"),
        "New Delhi" to listOf("Connaught Place", "Chanakyapuri", "Parliament Street", "Vasant Vihar"),
        "Chennai" to listOf("Chennai Central", "Egmore", "Guindy", "Mylapore", "T. Nagar")
    )

    fun getDistrictsForState(stateName: String): List<String> {
        return states.firstOrNull { it.name.equals(stateName, ignoreCase = true) }?.districts ?: listOf("Central District")
    }

    fun getTalukasForDistrict(districtName: String): List<String> {
        return talukaMap[districtName] ?: listOf("$districtName City", "North Tehsil", "South Tehsil", "East Block")
    }
}
