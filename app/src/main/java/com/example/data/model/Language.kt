package com.example.data.model

enum class AppLanguage(val code: String, val label: String) {
    MARATHI("mr", "मराठी"),
    HINDI("hi", "हिन्दी"),
    ENGLISH("en", "English")
}

object AppStrings {
    // App Branding
    val appTitleMar = "राष्ट्रीय रक्तसुरक्षा व आपत्कालीन रक्तदाता शोध प्रणाली"
    val appTitleEng = "National Blood Emergency & Donor Finder"
    val appTitleHin = "राष्ट्रीय रक्त सुरक्षा एवं आपातकालीन रक्तदाता खोज प्रणाली"

    val taglineMar = "“योग्य रक्तगट – योग्य व्यक्ती – योग्य ठिकाणी – योग्य वेळी.”"
    val taglineHin = "“सही रक्त समूह – सही व्यक्ति – सही जगह – सही समय पर।”"
    val taglineEng = "“Right blood group – Right person – Right place – Right time.”"

    val heroActionMar = "रक्ताची गरज आहे?"
    val heroActionHin = "रक्त की आवश्यकता है?"
    val heroActionEng = "Need Blood Urgently?"

    val heroSubMar = "एक नोंदणी – अनेक जीवांसाठी मदत."
    val heroSubHin = "एक पंजीकरण – कई जिंदगियों के लिए मदद।"
    val heroSubEng = "One registration – Countless lives saved."

    val finalMsgMar = "रक्तदान करा – जीवन वाचवा"
    val finalMsgHin = "रक्तदान करें – जीवन बचाएं"
    val finalMsgEng = "Donate Blood – Save Lives"

    val finalSubMar = "तुमचा रक्तगट माहित ठेवा. नोंदणी करा. आपत्कालीन वेळी मदतीचा हात द्या."
    val finalSubHin = "अपना रक्त समूह जानें। पंजीकरण करें। आपातकाल में मदद का हाथ बढ़ाएं।"
    val finalSubEng = "Know your blood group. Register voluntarily. Lend a helping hand in emergencies."

    // Disclaimer
    val demoWarningMar = "डेमो डेटा – ही माहिती केवळ सॉफ्टवेअर प्रात्यक्षिकासाठी आहे आणि ही अधिकृत सरकारी रक्त-साठा किंवा नागरिक डेटा नाही."
    val demoWarningHin = "डेमो डेटा – यह जानकारी केवल सॉफ्टवेयर प्रदर्शन के लिए है और यह आधिकारिक सरकारी डेटा नहीं है।"
    val demoWarningEng = "DEMO DATA – This information is for prototype demonstration only and is not official government blood-stock or citizen data."

    val medicalDisclaimerMar = "वैद्यकीय सूचना: हे ॲप समन्वय आणि माहिती व्यासपीठ आहे. अंतिम वैद्यकीय पात्रता आणि रक्त सुसंगतता अधिकृत रक्त केंद्र आणि डॉक्टरांद्वारे ठरवली जाते."
    val medicalDisclaimerHin = "चिकित्सा सूचना: यह केवल समन्वय मंच है। अंतिम पात्रता और रक्त अनुकूलता केवल अधिकृत रक्त केंद्र द्वारा तय की जाती है।"
    val medicalDisclaimerEng = "Medical Notice: This platform provides coordination only. Final eligibility & compatibility are determined by authorized blood centres and doctors."

    fun getTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> appTitleMar
        AppLanguage.HINDI -> appTitleHin
        AppLanguage.ENGLISH -> appTitleEng
    }

    fun getTagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> taglineMar
        AppLanguage.HINDI -> taglineHin
        AppLanguage.ENGLISH -> taglineEng
    }

    fun getHeroAction(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> heroActionMar
        AppLanguage.HINDI -> heroActionHin
        AppLanguage.ENGLISH -> heroActionEng
    }

    fun getHeroSub(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> heroSubMar
        AppLanguage.HINDI -> heroSubHin
        AppLanguage.ENGLISH -> heroSubEng
    }

    fun getDemoWarning(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> demoWarningMar
        AppLanguage.HINDI -> demoWarningHin
        AppLanguage.ENGLISH -> demoWarningEng
    }

    fun getMedicalDisclaimer(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> medicalDisclaimerMar
        AppLanguage.HINDI -> medicalDisclaimerHin
        AppLanguage.ENGLISH -> medicalDisclaimerEng
    }

    fun getFinalMsg(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> finalMsgMar
        AppLanguage.HINDI -> finalMsgHin
        AppLanguage.ENGLISH -> finalMsgEng
    }

    fun getFinalSub(lang: AppLanguage): String = when (lang) {
        AppLanguage.MARATHI -> finalSubMar
        AppLanguage.HINDI -> finalSubHin
        AppLanguage.ENGLISH -> finalSubEng
    }
}
