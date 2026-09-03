package com.example.data.repository

import com.example.data.model.Cattle
import com.example.ui.viewmodel.DiagnosisResult

object ClinicalRuleEngine {

    fun diagnose(
        cattle: Cattle?,
        symptoms: Set<String>,
        language: String
    ): DiagnosisResult {
        val s = symptoms.joinToString(" ").lowercase()
        val isEn = language == "English"
        val isMr = language == "मराठी"
        val isGu = language == "ગુજરાતી"
        val isPa = language == "ਪੰਜਾਬੀ"

        val hasMouth = s.contains("छाले") || s.contains("blister") || s.contains("mouth") || s.contains("फोड") || s.contains("ચાંદા")
        val hasSaliva = s.contains("लार") || s.contains("saliv") || s.contains("drool") || s.contains("लाळ") || s.contains("લાળ")
        val hasFever = s.contains("बुखार") || s.contains("fever") || s.contains("ताप") || s.contains("તાવ") || s.contains("ਬੁਖ਼ਾਰ")
        val hasLimping = s.contains("लंगड़ा") || s.contains("खुर") || s.contains("limp") || s.contains("hoof") || s.contains("लंगड") || s.contains("ખરી")
        val hasBreathing = s.contains("सांस") || s.contains("breath") || s.contains("श्वास") || s.contains("શ્વાસ") || s.contains("ਸਾਹ")
        val hasMilkDrop = s.contains("दूध") || s.contains("milk") || s.contains("દૂધ") || s.contains("ਦੁੱਧ")
        val hasAppetiteLoss = s.contains("खाना") || s.contains("सुस्ती") || s.contains("appetite") || s.contains("भूख") || s.contains("ભૂખ")

        return when {
            // Foot and Mouth Disease (FMD)
            (hasMouth || hasSaliva) && (hasLimping || hasFever) -> DiagnosisResult(
                diseaseName = "खुरपका मुंहपका (FMD)",
                englishName = "Foot and Mouth Disease (FMD)",
                riskLevel = if (hasFever) "उच्च" else "मध्यम",
                riskColor = if (hasFever) 0xFFD32F2F else 0xFFF57C00,
                precautions = listOf(
                    "पशु को तत्काल अन्य सभी पशुओं से अलग (quarantine) करें।",
                    "मुंह के छालों को 1% फिटकरी या पोटैशियम परमैंगनेट (लाल दवा) के घोल से दिन में 2 बार धोएं।",
                    "खुरों के घावों को साफ कर नीम का तेल या एंटीसेप्टिक मरहम लगाएं।",
                    "नरम, सुपाच्य हरा चारा और गुनगुना दलिया दें।",
                    "पशुशाला को चूने व फिनाइल से पूर्णतः विसंक्रमित करें।"
                ),
                recommendedMedicines = listOf(
                    "Melonex Plus (सूजन व दर्द निवारक)",
                    "Amoxicillin / Tetracycline स्प्रे (खुरों के घाव हेतु)",
                    "विटामिन बी-कॉम्प्लेक्स और लिवर टॉनिक"
                ),
                clinicalSummary = "लक्षणों में मुंह में छाले, अत्यधिक लार गिरना और खुरों में दर्द/लंगड़ाना शामिल हैं, जो खुरपका-मुंहपका (FMD) विषाणु संक्रमण के विशिष्ट लक्षण हैं।",
                differentialDiagnosis = listOf("वेसिकुलर स्टोमेटाइटिस (Vesicular Stomatitis)", "ब्लू टंग (Bluetongue)"),
                confidenceScore = 92,
                isAiPowered = false
            )

            // Hemorrhagic Septicemia (HS - गलघोंटू)
            hasBreathing && hasFever -> DiagnosisResult(
                diseaseName = "गलघोंटू (HS - Hemorrhagic Septicemia)",
                englishName = "Hemorrhagic Septicemia (HS)",
                riskLevel = "उच्च",
                riskColor = 0xFFD32F2F,
                precautions = listOf(
                    "यह अत्यंत तीव्र जानलेवा रोग है, तत्काल 1962 पशु एम्बुलेंस को कॉल करें।",
                    "पशु को हवादार, शांत एवं छायादार स्थान पर रखें।",
                    "गले में अत्यधिक सूजन होने पर ठंडे पानी की पट्टियां रखें।",
                    "संक्रमित पशु के पास अन्य पशुओं को न जाने दें।"
                ),
                recommendedMedicines = listOf(
                    "Sulfadimidine / Enrofloxacin इंजेक्शन (पशु चिकित्सक द्वारा)",
                    "Meloxicam (तेज बुखार रोधी)",
                    "डेक्सामेथासोन (श्वास नलिका की सूजन कम करने हेतु)"
                ),
                clinicalSummary = "तेज बुखार और सांस लेने में घुरघुराहट गलघोंटू (HS) जीवाणु के प्रकोप का संकेत देते हैं, जिसमें तत्काल एंटीबायोटिक उपचार आवश्यक है।",
                differentialDiagnosis = listOf("ब्लैक क्वार्टर (Black Quarter)", "निमोनिया (Aspiration Pneumonia)"),
                confidenceScore = 90,
                isAiPowered = false
            )

            // Mastitis / Milk drop
            hasMilkDrop && (hasFever || hasAppetiteLoss) -> DiagnosisResult(
                diseaseName = "थनैला रोग / दुग्ध ग्रंथि शोथ (Mastitis)",
                englishName = "Bovine Mastitis",
                riskLevel = "मध्यम",
                riskColor = 0xFFF57C00,
                precautions = listOf(
                    "अयन (Udder) को पोटैशियम परमैंगनेट के हल्के गुलाबी पानी से धोएं।",
                    "संक्रमित थन से दूध बार-बार निकालें और उसे जमीन पर न फेंकें (नष्ट करें)।",
                    "दूध निकालने के बाद थनों को टीट डिप (Teat Dip) घोल में डुबोएं।",
                    "स्वच्छ व सूखे फर्श पर ही पशु को बिठाएं।"
                ),
                recommendedMedicines = listOf(
                    "Intramammary Infusion ट्यूब (पशु चिकित्सक निर्देशानुसार)",
                    "Masticare पाउडर / ट्राइसोडियम साइट्रेट",
                    "विटामिन ई व सेलेनियम सप्लीमेंट"
                ),
                clinicalSummary = "दूध उत्पादन में अचानक गिरावट और सुस्ती प्राथमिक थनैला रोग के लक्षण हैं। समय पर उपचार न मिलने से थन हमेशा के लिए खराब हो सकता है।",
                differentialDiagnosis = listOf("थनों में चोट (Udder Trauma)", "मेटाबोलिक हाइपोकैल्सीमिया"),
                confidenceScore = 86,
                isAiPowered = false
            )

            // General High Fever / Bacterial infection
            hasFever -> DiagnosisResult(
                diseaseName = "तीव्र जीवाणु बुखार / टिक्स बुखार",
                englishName = "Acute Bovine Fever / Tick-borne Pyrexia",
                riskLevel = "मध्यम",
                riskColor = 0xFFF57C00,
                precautions = listOf(
                    "पशु के शरीर पर चीचड़ (ticks) की जांच करें।",
                    "सिर पर ठंडा पानी डालें और छायादार बाड़े में रखें।",
                    "इलेक्ट्रोलाइट व ओआरएस का घोल पिलाएं।"
                ),
                recommendedMedicines = listOf(
                    "Paracetamol + Meloxicam बोलस",
                    "एंटीबायोटिक (Oxytetracycline)",
                    "चीचड़ नाशक स्प्रे (Deltamethrin)"
                ),
                clinicalSummary = "तेज तापमान और सुस्ती किसी आंतरिक संक्रमण या चीचड़-जनित बुखार (Theileriosis/Babesiosis) का संकेत है।",
                differentialDiagnosis = listOf("बबेसिओसिस (Babesiosis)", "एनाप्लास्मोसिस (Anaplasmosis)"),
                confidenceScore = 84,
                isAiPowered = false
            )

            // Digestive Disorder / Indigestion
            else -> DiagnosisResult(
                diseaseName = "अपच / पेट का विकार (Simple Indigestion)",
                englishName = "Bovine Indigestion / Ruminal Stasis",
                riskLevel = "सामान्य",
                riskColor = 0xFF388E3C,
                precautions = listOf(
                    "24 घंटे तक सूखा भूसा व भारी दाना बंद रखें।",
                    "गुनगुने पानी में अजवाइन और हींग का काढ़ा दें।",
                    "सुपाच्य हरा चारा थोड़ी मात्रा में दें।",
                    "पशु को हल्का टहलाएं।"
                ),
                recommendedMedicines = listOf(
                    "Himalayan Batisa (पाचक चूर्ण)",
                    "Rume-plus बोलस (रूमेन फ्लोरा सुधार हेतु)",
                    "ईस्ट कल्चर पाउडर (Yeast culture)"
                ),
                clinicalSummary = "लक्षणों में भूख में कमी व सुस्ती है, जो संभवतः मौसम परिवर्तन या चारे में असंतुलन से होने वाली सामान्य अपच है।",
                differentialDiagnosis = listOf("माइल्ड अफारा (Mild Bloat)", "एसिडोसिस (Subacute Acidosis)"),
                confidenceScore = 80,
                isAiPowered = false
            )
        }
    }
}
