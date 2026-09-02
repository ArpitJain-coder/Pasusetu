package com.example.ui.util

object AppStrings {
    const val HINDI = "हिंदी"
    const val ENGLISH = "English"
    const val MARATHI = "मराठी"
    const val GUJARATI = "ગુજરાતી"
    const val PUNJABI = "ਪੰਜਾਬੀ"

    val SUPPORTED_LANGUAGES = listOf(HINDI, ENGLISH, MARATHI, GUJARATI, PUNJABI)

    fun t(key: String, isHindi: Boolean): String {
        return t(key, if (isHindi) HINDI else ENGLISH)
    }

    fun t(key: String, lang: String): String {
        val entry = strings[key] ?: return key
        return when (lang) {
            ENGLISH -> entry[1]
            MARATHI -> entry[2]
            GUJARATI -> entry[3]
            PUNJABI -> entry[4]
            else -> entry[0] // Default Hindi
        }
    }

    // Dynamic word translator for statuses, breeds, diseases, and common terms
    fun translateWord(word: String, lang: String): String {
        val trimmed = word.trim()
        val match = wordDict[trimmed]
        if (match != null) {
            return when (lang) {
                ENGLISH -> match[1]
                MARATHI -> match[2]
                GUJARATI -> match[3]
                PUNJABI -> match[4]
                else -> match[0]
            }
        }
        return word
    }

    // Table of 5 translations: [Hindi, English, Marathi, Gujarati, Punjabi]
    private val strings = mapOf(
        // App Identity & Header
        "app_name" to listOf("पशुसेतु", "PashuSetu", "पशूसेतू", "પશુસેતુ", "ਪਸ਼ੂਸੇਤੂ"),
        "tagline" to listOf(
            "पशुओं का स्वास्थ्य, किसानों की समृद्धि",
            "Animal Health, Farmer's Prosperity",
            "पशूंचे आरोग्य, बळीराजाची समृद्धी",
            "પશુઓનું સ્વાસ્થ્ય, ખેડૂતોની સમૃદ્ધિ",
            "ਪਸ਼ੂਆਂ ਦੀ ਸਿਹਤ, ਕਿਸਾਨਾਂ ਦੀ ਖੁਸ਼ਹਾਲੀ"
        ),
        "login_register" to listOf("लॉगिन / रजिस्टर करें", "Login / Register", "लॉगिन / नोंदणी करा", "લૉગિન / નોંધણી કરો", "ਲਾਗਇਨ / ਰਜਿਸਟਰ ਕਰੋ"),
        "continue_mobile" to listOf("मोबाइल नंबर से जारी रखें", "Continue with Mobile Number", "मोबाईल क्रमांकाने सुरू ठेवा", "મોબાઇલ નંબરથી આગળ વધો", "ਮੋਬਾਈਲ ਨੰਬਰ ਨਾਲ ਜਾਰੀ ਰੱਖੋ"),
        "select_language" to listOf("भाषा चुनें", "Select Language", "भाषा निवडा", "ભાષા પસંદ કરો", "ਭਾਸ਼ਾ ਚੁਣੋ"),

        // Roles
        "role_farmer" to listOf("किसान", "Farmer", "शेतकरी", "ખેડૂત", "ਕਿਸਾਨ"),
        "role_farmer_full" to listOf("किसान (Farmer)", "Livestock Farmer", "शेतकरी (Farmer)", "ખેડૂત (Farmer)", "ਕਿਸਾਨ (Farmer)"),
        "role_vet" to listOf("पशु चिकित्सक", "Veterinarian", "पशू वैद्यकीय अधिकारी", "પશુ ચિકિત્સક", "ਪਸ਼ੂ ਡਾਕਟਰ"),
        "role_vet_full" to listOf("पशु चिकित्सक (Vet)", "Veterinary Doctor", "पशू वैद्य (Vet)", "પશુ ચિકિત્સક (Vet)", "ਪਸ਼ੂ ਡਾਕਟਰ (Vet)"),
        "role_officer" to listOf("जिला अधिकारी", "District Officer", "जिल्हा अधिकारी", "જિલ્લા અધિકારી", "ਜ਼ਿਲ੍ਹਾ ਅਧਿਕਾਰੀ"),
        "role_officer_full" to listOf("जिला अधिकारी (Officer)", "District Animal Health Officer", "जिल्हा अधिकारी (Officer)", "જિલ્લા અધિકારી (Officer)", "ਜ਼ਿਲ੍ਹਾ ਅਧਿਕਾਰੀ (Officer)"),

        "role_select_title" to listOf("आप किस रूप में जुड़ना चाहते हैं?", "How would you like to continue?", "तुम्ही कोणत्या भूमिकेत सामील होऊ इच्छिता?", "તમે કઈ ભૂમિકામાં જોડાવા માંગો છો?", "ਤੁਸੀਂ ਕਿਸ ਭੂਮਿਕਾ ਵਿੱਚ ਸ਼ਾਮਲ ਹੋਣਾ ਚਾਹੁੰਦੇ ਹੋ?"),
        "role_select_subtitle" to listOf("अपनी भूमिका चुनें", "Select your role to get started", "आपली भूमिका निवडा", "તમારી ભૂમિકા પસંદ કરો", "ਆਪਣੀ ਭੂਮਿਕਾ ਚੁਣੋ"),
        "farmer_desc" to listOf("अपने पशुओं की जानकारी, लक्षण जाँच और स्वास्थ्य प्रबंधन करें", "Manage livestock records, AI disease diagnosis, and vet care", "पशूंची माहिती, लक्षण तपासणी आणि आरोग्य व्यवस्थापन करा", "તમારા પશુઓની માહિતી, લક્ષણ તપાસ અને આરોગ્ય સંભાળ રાખો", "ਆਪਣੇ ਪਸ਼ੂਆਂ ਦੀ ਜਾਣਕਾਰੀ, ਲੱਛਣ ਜਾਂਚ ਅਤੇ ਸਿਹਤ ਸੰਭਾਲ ਕਰੋ"),
        "vet_desc" to listOf("पशुओं की जाँच, इलाज के मामले और पर्चा/सलाह प्रदान करें", "Review farmer consultation cases, prescribe medicines, and follow-up", "पशूंची तपासणी, उपचारांची प्रकरणे आणि औषधोपचार सल्ला द्या", "પશુઓની તપાસ, સારવાર કેસ અને પ્રિસ્ક્રિપ્શન આપો", "ਪਸ਼ੂਆਂ ਦੀ ਜਾਂਚ, ਇਲਾਜ ਦੇ ਕੇਸ ਅਤੇ ਦਵਾਈਆਂ ਦੀ ਸਲਾਹ ਦਿਓ"),
        "officer_desc" to listOf("रोग प्रकोप निगरानी, ब्लॉक मैपिंग और त्वरित दल रवाना करें", "Monitor disease outbreaks, vaccination rates & dispatch response units", "रोग प्रादुर्भाव देखरेख, गट नकाशा आणि जलद प्रतिसाद पथक पाठवा", "રોગચાળો દેખરેખ, બ્લોક મેપિંગ અને ઝડપી ટીમ મોકલો", "ਬਿਮਾਰੀ ਨਿਗਰਾਨੀ, ਬਲਾਕ ਮੈਪਿੰਗ ਅਤੇ ਤੁਰੰਤ ਟੀਮ ਰਵਾਨਾ ਕਰੋ"),

        // Navigation & Drawer
        "switch_role" to listOf("भूमिका बदलें", "Switch Role", "भूमिका बदला", "ભૂમિકા બદલો", "ਭੂਮਿਕਾ ਬਦਲੋ"),
        "live_map_gps" to listOf("लाइव जीपीएस मैप", "Live GPS Map", "थेट जीपीएस नकाशा", "લાઇવ જીપીએસ મેપ", "ਲਾਈਵ ਜੀਪੀਐਸ ਨਕਸ਼ਾ"),
        "helpline_1962" to listOf("पशु हेल्पलाइन 1962", "Livestock Ambulance (1962)", "पशू रुग्णवाहिका 1962", "પશુ એમ્બ્યુલન્સ 1962", "ਪਸ਼ੂ ਐਂਬੂਲੈਂਸ 1962"),
        "change_language" to listOf("भाषा बदलें", "Change Language", "भाषा बदला", "ભાષા બદલો", "ਭਾਸ਼ਾ ਬਦਲੋ"),
        "help_faq" to listOf("सहायता व अक्सर पूछे जाने वाले प्रश्न", "Help & FAQs", "मदत व वारंवार विचारले जाणारे प्रश्न", "મદદ અને પ્રશ્નોત્તરી", "ਮਦਦ ਅਤੇ ਆਮ ਸਵਾਲ"),
        "logout" to listOf("लॉगआउट", "Logout", "लॉगआउट करा", "લૉગઆઉટ", "ਲਾਗਆਉਟ"),
        "user_profile" to listOf("उपयोगकर्ता प्रोफाइल", "User Profile", "वापरकर्ता प्रोफाइल", "વપરાશકર્તા પ્રોફાઇલ", "ਵਰਤੋਂਕਾਰ ਪ੍ਰੋਫਾਈਲ"),

        // Bottom Navigation Tabs
        "nav_home" to listOf("होम", "Home", "मुख्यपृष्ठ", "હોમ", "ਮੁੱਖ ਪੰਨਾ"),
        "nav_cattle" to listOf("मेरे पशु", "My Cattle", "माझे पशू", "મારા પશુઓ", "ਮੇਰੇ ਪਸ਼ੂ"),
        "nav_action" to listOf("जाँच", "Diagnose", "तपासणी", "તપાસ", "ਜਾਂਚ"),
        "nav_alerts" to listOf("अलर्ट", "Alerts", "सूचना", "ચેતવણી", "ਅਲਰਟ"),
        "nav_profile" to listOf("प्रोफ़ाइल", "Profile", "प्रोफाइल", "પ્રોફાઇલ", "ਪ੍ਰੋਫਾਈਲ"),
        "nav_cases" to listOf("केस", "Cases", "प्रकरणे", "કેસ", "ਕੇਸ"),
        "nav_medicines" to listOf("दवा", "Medicines", "औषधे", "દવા", "ਦਵਾਈ"),
        "nav_dashboard" to listOf("डैशबोर्ड", "Dashboard", "डॅशबोर्ड", "ડેશબોર્ડ", "ਡੈਸ਼ਬੋਰਡ"),
        "nav_reports" to listOf("रिपोर्ट", "Reports", "अहवाल", "અહેવાલો", "ਰਿਪੋਰਟਾਂ"),
        "nav_more" to listOf("अधिक", "More", "अधिक", "વધુ", "ਹੋਰ"),

        // Login Screen
        "login_portal" to listOf("पशुसेतु लॉगिन पोर्टल", "PashuSetu Login Portal", "पशूसेतू लॉगिन पोर्टल", "પશુસેતુ લૉગિન પોર્ટલ", "ਪਸ਼ੂਸੇਤੂ ਲਾਗਇਨ ਪੋਰਟਲ"),
        "farmer_tab" to listOf("किसान (Farmer)", "Farmer", "शेतकरी (Farmer)", "ખેડૂત (Farmer)", "ਕਿਸਾਨ (Farmer)"),
        "vet_tab" to listOf("पशु चिकित्सक (Vet)", "Veterinarian", "पशू वैद्य (Vet)", "પશુ ચિકિત્સક (Vet)", "ਪਸ਼ੂ ਡਾਕਟਰ (Vet)"),
        "officer_tab" to listOf("जिला अधिकारी (Officer)", "Officer", "जिल्हा अधिकारी (Officer)", "જિલ્લા અધિકારી (Officer)", "ਜ਼ਿਲ੍ਹਾ ਅਧਿਕਾਰੀ (Officer)"),
        "mobile_login_desc" to listOf("पंजीकृत मोबाइल नंबर दर्ज करें व OTP से सत्यापन करें", "Enter mobile number and verify via OTP", "नोंदणीकृत मोबाईल क्रमांक भरा व OTP द्वारे पडताळणी करा", "નોંધાયેલ મોબાઇલ નંબર દાખલ કરો અને OTP થી ચકાસો", "ਰਜਿਸਟਰਡ ਮੋਬਾਈਲ ਨੰਬਰ ਦਰਜ ਕਰੋ ਅਤੇ OTP ਨਾਲ ਤਸਦੀਕ ਕਰੋ"),
        "demo_fill" to listOf("⚡ डेमो विवरण भरें", "⚡ Auto-Fill Demo", "⚡ डेमो तपशील भरा", "⚡ ડેમો વિગતો ભરો", "⚡ ਡੈਮੋ ਵੇਰਵੇ ਭਰੋ"),
        "full_name" to listOf("पूरा नाम *", "Full Name *", "पूर्ण नाव *", "પૂરું નામ *", "ਪੂਰਾ ਨਾਮ *"),
        "enter_name" to listOf("उदा. राम किसान", "e.g. Ram Kisan", "उदा. राम शेतकरी", "દા.ત. રામ ખેડૂત", "ਉਦਾ. ਰਾਮ ਕਿਸਾਨ"),
        "mobile_number" to listOf("मोबाइल नंबर *", "Mobile Number *", "मोबाईल क्रमांक *", "મોબાઇલ નંબર *", "ਮੋਬਾਈਲ ਨੰਬਰ *"),
        "village_address" to listOf("गाँव / ग्राम पंचायत *", "Village / Gram Panchayat *", "गाव / ग्रामपंचायत *", "ગામ / ગ્રામ પંચાયત *", "ਪਿੰਡ / ਗ੍ਰਾਮ ਪੰਚਾਇਤ *"),
        "district" to listOf("जिला *", "District *", "जिल्हा *", "જિલ્લો *", "ਜ਼ਿਲ੍ਹਾ *"),
        "pincode" to listOf("पिन कोड *", "Pincode *", "पिन कोड *", "પિન કોડ *", "ਪਿੰਨ ਕੋਡ *"),
        "send_otp" to listOf("OTP प्राप्त करें", "Get OTP", "OTP मिळवा", "OTP મેળવો", "OTP ਪ੍ਰਾਪਤ ਕਰੋ"),
        "enter_otp" to listOf("4-अंकों का OTP दर्ज करें *", "Enter 4-Digit OTP *", "4-अंकी OTP प्रविष्ट करा *", "4-અંકનો OTP દાખલ કરો *", "4-ਅੰਕਾਂ ਦਾ OTP ਦਰਜ ਕਰੋ *"),
        "resend_otp" to listOf("OTP पुनः भेजें", "Resend OTP", "OTP पुन्हा पाठवा", "OTP ફરીથી મોકલો", "OTP ਦੁਬਾਰਾ ਭੇਜੋ"),
        "otp_verified" to listOf("OTP सत्यापित ✓", "OTP Verified ✓", "OTP पडताळले ✓", "OTP ચકાસાયેલ ✓", "OTP ਤਸਦੀਕ ਹੋਇਆ ✓"),
        "login_proceed" to listOf("पोर्टल में प्रवेश करें", "Login to Portal", "पोर्टलमध्ये प्रवेश करा", "પોર્ટલમાં પ્રવેશ કરો", "ਪੋਰਟਲ ਵਿੱਚ ਦਾਖਲ ਹੋਵੋ"),
        "email_address" to listOf("ईमेल पता *", "Email Address *", "ईमेल पत्ता *", "ઇમેઇલ સરનામું *", "ਈਮੇਲ ਪਤਾ *"),
        "password" to listOf("पासवर्ड *", "Password *", "पासवर्ड *", "પાસવર્ડ *", "ਪਾਸਵਰਡ *"),
        "council_reg" to listOf("पशु चिकित्सा परिषद पंजीकरण संख्या *", "Veterinary Council Reg. No. *", "पशू वैद्यकीय परिषद नोंदणी क्रमांक *", "પશુ ચિકિત્સા પરિષદ નોંધણી નંબર *", "ਵੈਟਰਨਰੀ ਕੌਂਸਲ ਰਜਿਸਟ੍ਰੇਸ਼ਨ ਨੰਬਰ *"),
        "govt_email" to listOf("विभागीय सरकारी ईमेल (Govt Email) *", "Department Govt Email *", "विभागीय शासकीय ईमेल *", "સરકારી વિભાગીય ઇમેઇલ *", "ਵਿਭਾਗੀ ਸਰਕਾਰੀ ਈਮੇਲ *"),
        "assigned_district" to listOf("कार्यक्षेत्र जिला / संभाग *", "Assigned District / Division *", "कार्यक्षेत्र जिल्हा *", "કાર્યક્ષેત્ર જિલ્લો *", "ਕਾਰਜ ਖੇਤਰ ਜ਼ਿਲ੍ਹਾ *"),
        "security_badge" to listOf("सरकारी SSO प्रमाणीकरण व डेटा सुरक्षा एन्क्रिप्टेड", "Govt SSO & 256-bit Encrypted Animal Health Registry", "शासकीय SSO प्रमाणीकरण व डेटा सुरक्षा एन्क्रिप्टेड", "સરકારી SSO પ્રમાણીકરણ અને ડેટા સુરક્ષા સુરક્ષિત", "ਸਰਕਾਰੀ SSO ਪ੍ਰਮਾਣੀਕਰਨ ਅਤੇ ਡਾਟਾ ਸੁਰੱਖਿਆ ਐਨਕ੍ਰਿਪਟਡ"),
        "helpdesk_call" to listOf("सहायता हेतु कॉल करें: 1962 (पशु चिकित्सा एम्बुलेंस)", "Helpdesk: 1962 (Livestock Ambulance)", "मदतीसाठी कॉल करा: 1962 (पशू रुग्णवाहिका)", "મદદ માટે કૉલ કરો: 1962 (પશુ એમ્બ્યુલન્સ)", "ਮਦਦ ਲਈ ਕਾਲ ਕਰੋ: 1962 (ਪਸ਼ੂ ਐਂਬੂਲੈਂਸ)"),

        // Kisan Home Screen
        "hello_kisan" to listOf("नमस्ते", "Hello", "नमस्कार", "નમસ્તે", "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ"),
        "location" to listOf("स्थान", "Location", "ठिकाण", "સ્થળ", "ਸਥਾਨ"),
        "emergency_ambulance_banner" to listOf("पशु चिकित्सा एम्बुलेंस 1962", "Livestock Ambulance 1962", "पशू रुग्णवाहिका 1962", "પશુ એમ્બ્યુલન્સ 1962", "ਪਸ਼ੂ ਐਂਬੂਲੈਂਸ 1962"),
        "ambulance_desc" to listOf("गंभीर पशु आपातकाल में तुरंत 1962 डायल करें - निःशुल्क सेवा", "In critical cattle emergencies dial 1962 toll-free", "गंभीर पशू आणीबाणीत त्वरित 1962 डायल करा - विनामूल्य सेवा", "ગંભીર પશુ કટોકટીમાં તાત્કાલિક 1962 ડાયલ કરો - મફત સેવા", "ਗੰਭੀਰ ਪਸ਼ੂ ਐਮਰਜੈਂਸੀ ਵਿੱਚ ਤੁਰੰਤ 1962 ਡਾਇਲ ਕਰੋ - ਮੁਫ਼ਤ ਸੇਵਾ"),
        "call_now" to listOf("कॉल करें", "Call Now", "कॉल करा", "કૉલ કરો", "ਕਾਲ ਕਰੋ"),
        "ai_diagnosis_card" to listOf("एआई पशु रोग पहचान व निदान", "AI Livestock Disease Diagnosis", "एआय पशुरोग ओळख व निदान", "AI પશુ રોગ ઓળખ અને નિદાન", "AI ਪਸ਼ੂ ਰੋਗ ਪਛਾਣ ਅਤੇ ਜਾਂਚ"),
        "ai_diagnosis_desc" to listOf("लक्षण चुनें, फोटो लें और तुरंत प्राथमिक उपचार व परामर्श प्राप्त करें", "Select symptoms, snap a photo and receive instant first-aid & guidance", "लक्षणे निवडा, फोटो काढा आणि त्वरित प्रथमोपचार व सल्ला मिळवा", "લક્ષણો પસંદ કરો, ફોટો લો અને તાત્કાલિક પ્રાથમિક સારવાર મેળવો", "ਲੱਛਣ ਚੁਣੋ, ਫੋਟੋ ਲਵੋ ਅਤੇ ਤੁਰੰਤ ਮੁੱਢਲੀ ਸਹਾਇਤਾ ਪ੍ਰਾਪਤ ਕਰੋ"),
        "start_diagnosis" to listOf("जाँच शुरू करें", "Start Diagnosis", "तपासणी सुरू करा", "તપાસ શરૂ કરો", "ਜਾਂਚ ਸ਼ੁਰੂ ਕਰੋ"),
        "quick_actions" to listOf("त्वरित सेवाएँ", "Quick Services", "जलद सेवा", "ઝડપી સેવાઓ", "ਤੁਰੰਤ ਸੇਵਾਵਾਂ"),
        "my_cattle_btn" to listOf("मेरे पशु", "My Cattle", "माझे पशू", "મારા પશુઓ", "ਮੇਰੇ ਪਸ਼ੂ"),
        "medicines_btn" to listOf("दवाइयाँ", "Medicines", "औषधे", "દવાઓ", "ਦਵਾਈਆਂ"),
        "vaccination_btn" to listOf("टीकाकरण", "Vaccination", "लसीकरण", "રસીકરણ", "ਟੀਕਾਕਰਨ"),
        "nearest_clinic_btn" to listOf("नजदीकी अस्पताल", "Nearest Clinic", "जवळचे रुग्णालय", "નજીકનું દવાખાનું", "ਨੇੜਲਾ ਹਸਪਤਾਲ"),
        "my_registered_cattle" to listOf("मेरे पंजीकृत पशु", "My Registered Cattle", "माझे नोंदणीकृत पशू", "મારા નોંધાયેલા પશુઓ", "ਮੇਰੇ ਰਜਿਸਟਰਡ ਪਸ਼ੂ"),
        "total_count" to listOf("कुल संख्या", "Total Count", "एकूण संख्या", "કુલ સંખ્યા", "ਕੁੱਲ ਗਿਣਤੀ"),
        "add_new_cattle" to listOf("+ नया पशु जोड़ें", "+ Add Cattle", "+ नवीन पशू जोडा", "+ નવું પશુ ઉમેરો", "+ ਨਵਾਂ ਪਸ਼ੂ ਜੋੜੋ"),
        "daily_health_tips" to listOf("दैनिक पशु स्वास्थ्य सलाह", "Daily Livestock Health Advice", "दैनिक पशू आरोग्य सल्ला", "દૈનિક પશુ આરોગ્ય સલાહ", "ਰੋਜ਼ਾਨਾ ਪਸ਼ੂ ਸਿਹਤ ਸਲਾਹ"),
        "weather_advisory" to listOf("मौसम व देखभाल परामर्श", "Weather & Seasonal Advisory", "हवामान व काळजी सल्ला", "હવામાન અને સંભાળ સલાહ", "ਮੌਸਮ ਅਤੇ ਸੰਭਾਲ ਸਲਾਹ"),
        "read_more" to listOf("और पढ़ें", "Read More", "अधिक वाचा", "વધુ વાંચો", "ਹੋਰ ਪੜ੍ਹੋ"),

        // Diagnosis Screen
        "diagnosis_title" to listOf("पशु रोग पहचान (AI डायग्नोसिस)", "Cattle Disease AI Diagnosis", "पशुरोग ओळख (एआय निदान)", "પશુ રોગ નિદાન (AI નિદાન)", "ਪਸ਼ੂ ਰੋਗ ਜਾਂਚ (AI ਜਾਂਚ)"),
        "step_1_title" to listOf("1. पशु चुनें", "1. Select Cattle", "1. पशू निवडा", "1. પશુ પસંદ કરો", "1. ਪਸ਼ੂ ਚੁਣੋ"),
        "step_2_title" to listOf("2. लक्षण चुनें", "2. Select Symptoms", "2. लक्षणे निवडा", "2. લક્ષણો પસંદ કરો", "2. ਲੱਛਣ ਚੁਣੋ"),
        "step_3_title" to listOf("3. फोटो व आवाज", "3. Photo & Voice", "3. फोटो आणि आवाज", "3. ફોટો અને અવાજ", "3. ਫੋਟੋ ਅਤੇ ਆਵਾਜ਼"),
        "step_4_title" to listOf("4. विश्लेषण", "4. Analysis", "4. विश्लेषण", "4. વિશ્લેષણ", "4. ਵਿਸ਼ਲੇਸ਼ਣ"),
        "select_cattle_prompt" to listOf("किस पशु की जाँच करनी है?", "Which animal needs examination?", "कोणत्या पशूची तपासणी करायची आहे?", "કયા પશુની તપાસ કરવાની છે?", "ਕਿਸ ਪਸ਼ੂ ਦੀ ਜਾਂਚ ਕਰਨੀ ਹੈ?"),
        "select_symptoms_prompt" to listOf("दिखाई देने वाले लक्षण चुनें (एक या अधिक):", "Select observed symptoms (one or more):", "दिसणारी लक्षणे निवडा (एक किंवा अधिक):", "જોવા મળતા લક્ષણો પસંદ કરો (એક અથવા વધુ):", "ਦਿਸਣ ਵਾਲੇ ਲੱਛਣ ਚੁਣੋ (ਇੱਕ ਜਾਂ ਵੱਧ):"),
        "photo_voice_prompt" to listOf("प्रभावित हिस्से की फोटो लें या आवाज में समस्या बताएं", "Capture photo of affected area or speak symptoms", "बाधित भागाचा फोटो काढा किंवा आवाजात समस्या सांगा", "અસરગ્રસ્ત ભાગનો ફોટો લો અથવા અવાજમાં સમસ્યા કહો", "ਪ੍ਰਭਾਵਿਤ ਹਿੱਸੇ ਦੀ ਫੋਟੋ ਲਵੋ ਜਾਂ ਆਵਾਜ਼ ਵਿੱਚ ਸਮੱਸਿਆ ਦੱਸੋ"),
        "capture_photo" to listOf("कैमरा से फोटो लें", "Take Live Photo", "कॅमेराने फोटो काढा", "કૅમેરાથી ફોટો લો", "ਕੈਮਰੇ ਤੋਂ ਫੋਟੋ ਲਵੋ"),
        "speak_symptoms" to listOf("बोलकर लक्षण बताएं", "Speak Symptoms", "बोलून लक्षणे सांगा", "બોલીને લક્ષણો કહો", "ਬੋਲ ਕੇ ਲੱਛਣ ਦੱਸੋ"),
        "listening_now" to listOf("सुन रहे हैं... बोलिए", "Listening... Please speak", "ऐकत आहोत... बोला", "સાંભળી રહ્યા છીએ... બોલો", "ਸੁਣ ਰਹੇ ਹਾਂ... ਬੋਲੋ"),
        "start_ai_analysis" to listOf("एआई विश्लेषण शुरू करें", "Start AI Analysis", "एआय विश्लेषण सुरू करा", "AI વિશ્લેષણ શરૂ કરો", "AI ਵਿਸ਼ਲੇਸ਼ਣ ਸ਼ੁਰੂ ਕਰੋ"),

        // Diagnosis Result
        "result_header" to listOf("जाँच परिणाम एवं प्राथमिक उपचार", "Diagnosis Result & First-Aid", "तपासणी निकाल व प्रथमोपचार", "તપાસ પરિણામ અને પ્રાથમિક સારવાર", "ਜਾਂਚ ਨਤੀਜਾ ਅਤੇ ਮੁੱਢਲੀ ਸਹਾਇਤਾ"),
        "identified_disease" to listOf("संभावित रोग", "Identified Condition", "संभाव्य आजार", "સંભવિત રોગ", "ਸੰਭਾਵੀ ਰੋਗ"),
        "risk_level_label" to listOf("जोखिम स्तर", "Risk Level", "धोक्याची पातळी", "જોખમ સ્તર", "ਜੋਖਮ ਪੱਧਰ"),
        "listen_audio" to listOf("आवाज में सुनें", "Listen Audio", "आवाजात ऐका", "અવાજમાં સાંભળો", "ਆਵਾਜ਼ ਵਿੱਚ ਸੁਣੋ"),
        "stop_audio" to listOf("आवाज रोकें", "Stop Audio", "आवाज थांबवा", "અવાજ રોકો", "ਆਵਾਜ਼ ਰੋਕੋ"),
        "precautions_heading" to listOf("सावधानियाँ एवं देखभाल उपाय", "Precautions & Care Guidelines", "काळजी व उपाययोजना", "સાવચેતી અને સંભાળના પગલાં", "ਸਾਵਧਾਨੀਆਂ ਅਤੇ ਸੰਭਾਲ ਉਪਾਅ"),
        "medicines_heading" to listOf("अनुशंसित प्राथमिक दवाइयाँ", "Recommended First-Aid Medicines", "शिफारस केलेली प्रथमोपचार औषधे", "ભલામણ કરેલ પ્રાથમિક દવાઓ", "ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀਆਂ ਮੁੱਢਲੀਆਂ ਦਵਾਈਆਂ"),
        "book_vet_appointment" to listOf("पशु चिकित्सक से परामर्श बुक करें", "Book Vet Consultation", "पशूवैद्यकीय सल्ला बुक करा", "પશુ ચિકિત્સક સાથે મુલાકાત બુક કરો", "ਪਸ਼ੂ ਡਾਕਟਰ ਨਾਲ ਸਲਾਹ ਬੁੱਕ ਕਰੋ"),

        // My Cattle Screen
        "my_cattle_title" to listOf("पशुधन प्रबंधन", "Livestock Management", "पशुधन व्यवस्थापन", "પશુધન સંચાલન", "ਪਸ਼ੂਧਨ ਪ੍ਰਬੰਧਨ"),
        "filter_all" to listOf("सभी", "All", "सर्व", "બધા", "ਸਾਰੇ"),
        "filter_cow" to listOf("गाय", "Cow", "गाय", "ગાય", "ਗਾਂ"),
        "filter_buffalo" to listOf("भैंस", "Buffalo", "म्हैस", "ભેંસ", "ਮੱਝ"),
        "filter_goat" to listOf("बकरी", "Goat", "शेळी", "બકરી", "ਬੱਕਰੀ"),
        "tag_no" to listOf("टैग संख्या", "Tag Number", "टॅग क्रमांक", "ટૅગ નંબર", "ਟੈਗ ਨੰਬਰ"),
        "breed" to listOf("नस्ल", "Breed", "जात", "નસલ", "ਨਸਲ"),
        "age" to listOf("आयु", "Age", "वय", "ઉંમર", "ਉਮਰ"),
        "daily_milk" to listOf("दैनिक दूध (लीटर)", "Daily Milk (Liters)", "दैनिक दूध (लिटर)", "દૈનિક દૂધ (લિટર)", "ਰੋਜ਼ਾਨਾ ਦੁੱਧ (ਲਿਟਰ)"),
        "health_status" to listOf("स्वास्थ्य स्थिति", "Health Status", "आरोग्य स्थिती", "આરોગ્ય સ્થિતિ", "ਸਿਹਤ ਸਥਿਤੀ"),
        "add_cattle_dialog_title" to listOf("नया पशु जोड़ें", "Add New Cattle", "नवीन पशू जोडा", "નવું પશુ ઉમેરો", "ਨਵਾਂ ਪਸ਼ੂ ਜੋੜੋ"),
        "save" to listOf("सुरक्षित करें", "Save", "जतन करा", "સાચવો", "ਸੰਭਾਲੋ"),
        "cancel" to listOf("रद्द करें", "Cancel", "रद्द करा", "રદ કરો", "ਰੱਦ ਕਰੋ"),

        // Vet Screen
        "vet_home_title" to listOf("पशु चिकित्सालय क्लिनिक", "Veterinary Clinic", "पशू रुग्णालय क्लिनिक", "પશુ દવાખાનું ક્લિનિક", "ਪਸ਼ੂ ਹਸਪਤਾਲ ਕਲੀਨਿਕ"),
        "today_appointments" to listOf("आज की अपॉइंटमेंट", "Today's Consultations", "आजच्या भेटी", "આજની એપોઇન્ટમેન્ટ", "ਅੱਜ ਦੀਆਂ ਮੁਲਾਕਾਤਾਂ"),
        "active_cases" to listOf("उपचारधीन केस", "Active Cases", "सक्रिय प्रकरणे", "ચાલુ કેસ", "ਸਰਗਰਮ ਕੇਸ"),
        "prescribe_treatment" to listOf("उपचार व दवा लिखें", "Prescribe Treatment", "उपचार व औषधे लिहा", "સારવાર અને દવા લખો", "ਇਲਾਜ ਅਤੇ ਦਵਾਈ ਲਿਖੋ"),
        "patient_details" to listOf("पशु व किसान विवरण", "Patient & Farmer Details", "पशू व शेतकरी तपशील", "પશુ અને ખેડૂત વિગતો", "ਪਸ਼ੂ ਅਤੇ ਕਿਸਾਨ ਵੇਰਵੇ"),

        // District Officer Screen
        "officer_title" to listOf("जिला पशुपालन निगरानी डैशबोर्ड", "District Livestock Surveillance", "जिल्हा पशूसंवर्धन देखरेख डॅशबोर्ड", "જિલ્લા પશુપાલન દેખરેખ ડેશબોર્ડ", "ਜ਼ਿਲ੍ਹਾ ਪਸ਼ੂ ਪਾਲਣ ਨਿਗਰਾਨੀ ਡੈਸ਼ਬੋਰਡ"),
        "select_district_label" to listOf("जिला चुनें:", "Select District:", "जिल्हा निवडा:", "જિલ્લો પસંદ કરો:", "ਜ਼ਿਲ੍ਹਾ ਚੁਣੋ:"),
        "outbreak_alert" to listOf("रोग प्रकोप अलर्ट जारी करें", "Issue Outbreak Alert", "रोग प्रादुर्भाव अलर्ट जारी करा", "રોગચાળો ચેતવણી જાહેર કરો", "ਬਿਮਾਰੀ ਅਲਰਟ ਜਾਰੀ ਕਰੋ"),
        "dispatch_team" to listOf("त्वरित प्रतिक्रिया दल रवाना करें", "Dispatch Response Unit", "जलद प्रतिसाद पथक पाठवा", "ઝડપી પ્રતિસાદ ટીમ મોકલો", "ਤੁਰੰਤ ਕਾਰਵਾਈ ਟੀਮ ਭੇਜੋ"),
        "vaccine_coverage" to listOf("टीकाकरण कवरेज", "Vaccination Coverage", "लसीकरण कव्हरेज", "રસીકરણ કવરેજ", "ਟੀਕਾਕਰਨ ਕਵਰੇਜ"),
        "high_risk_cluster" to listOf("उच्च जोखिम क्षेत्र", "High Risk Cluster", "उच्च जोखीम क्षेत्र", "ઉચ્ચ જોખમ વિસ્તાર", "ਉੱਚ ਜੋਖਮ ਖੇਤਰ"),

        // Alerts & Common
        "alerts_title" to listOf("स्वास्थ्य अलर्ट व सूचनाएं", "Health Alerts & Notices", "आरोग्य सूचना व सूचना", "આરોગ્ય ચેતવણી અને સૂચનાઓ", "ਸਿਹਤ ਅਲਰਟ ਅਤੇ ਨੋਟਿਸ"),
        "profile_title" to listOf("प्रोफ़ाइल एवं सेटिंग्स", "Profile & Settings", "प्रोफाइल आणि सेटिंग्ज", "પ્રોફાઇલ અને સેટિંગ્સ", "ਪ੍ਰੋਫਾਈਲ ਅਤੇ ਸੈਟਿੰਗਾਂ"),
        "back" to listOf("वापस", "Back", "मागे", "પાછા", "ਵਾਪਸ"),
        "close" to listOf("बंद करें", "Close", "बंद करा", "બંધ કરો", "ਬੰਦ ਕਰੋ"),
        "confirm" to listOf("पुष्टि करें", "Confirm", "पुष्टी करा", "ખાતરી કરો", "ਪੁਸ਼ਟੀ ਕਰੋ")
    )

    // Dynamic translation dictionary for words and phrases
    private val wordDict = mapOf(
        // Animals
        "गाय" to listOf("गाय", "Cow", "गाय", "ગાય", "ਗਾਂ"),
        "भैंस" to listOf("भैंस", "Buffalo", "म्हैस", "ભેંસ", "ਮੱਝ"),
        "बकरी" to listOf("बकरी", "Goat", "शेळी", "બકરી", "ਬੱਕਰੀ"),
        "बैल" to listOf("बैल", "Bull / Ox", "बैल", "બળદ", "ਬਲਦ"),

        // Health Statuses
        "स्वस्थ" to listOf("स्वस्थ", "Healthy", "निरोगी", "સ્વસ્થ", "ਤੰਦਰੁਸਤ"),
        "निगरानी में" to listOf("निगरानी में", "Under Observation", "निरीक्षणाखाली", "નિરીક્ષણ હેઠળ", "ਨਿਗਰਾਨੀ ਹੇਠ"),
        "बीमार" to listOf("बीमार", "Sick", "आजारी", "બીમાર", "ਬਿਮਾਰ"),
        "गर्भवती" to listOf("गर्भवती", "Pregnant", "गाभण", "સગર્ભા", "ਗਰਭਵਤੀ"),
        "उपचार जारी" to listOf("उपचार जारी", "Under Treatment", "उपचार सुरू", "સારવાર ચાલુ", "ਇਲਾਜ ਜਾਰੀ"),

        // Risk Levels
        "उच्च" to listOf("उच्च (गंभीर)", "High (Severe)", "उच्च (गंभीर)", "ઉચ્ચ (ગંભીર)", "ਉੱਚਾ (ਗੰਭੀਰ)"),
        "मध्यम" to listOf("मध्यम", "Moderate", "मध्यम", "મધ્યમ", "ਦਰਮਿਆਨਾ"),
        "कम" to listOf("कम (सामान्य)", "Low (Mild)", "कमी (सौम्य)", "ઓછું (હળવું)", "ਘੱਟ (ਹਲਕਾ)"),

        // Symptoms
        "मुंह में छाले (Mouth blisters)" to listOf(
            "मुंह में छाले",
            "Mouth blisters",
            "तोंडात फोड",
            "મોંમાં ફોલ્લા",
            "ਮੂੰਹ ਵਿੱਚ ਛਾਲੇ"
        ),
        "लार गिरना (Excessive salivation)" to listOf(
            "लार गिरना",
            "Excessive salivation",
            "लाळ गळणे",
            "લાળ પડવી",
            "ਲਾਰ ਵਗਣਾ"
        ),
        "तेज बुखार (High fever)" to listOf(
            "तेज बुखार",
            "High fever",
            "तीव्र ताप",
            "તીવ્ર તાવ",
            "ਤੇਜ਼ ਬੁਖਾਰ"
        ),
        "लंगड़ाना / खुरों में घाव (Limping/Hoof lesions)" to listOf(
            "लंगड़ाना / खुरों में घाव",
            "Limping / Hoof lesions",
            "लंगडणे / खुरांमध्ये जखमा",
            "લંગડાપણું / ખરીમાં ઘા",
            "ਲੰਗੜਾਉਣਾ / ਖੁਰਾਂ ਵਿੱਚ ਜ਼ਖ਼ਮ"
        ),
        "खाने में कमी / सुस्ती (Loss of appetite)" to listOf(
            "भूख न लगना / सुस्ती",
            "Loss of appetite / Dullness",
            "भूक मंदावणे / सुस्ती",
            "ભૂખ ન લાગવી / સુસ્તી",
            "ਭੁੱਖ ਨਾ ਲੱਗਣਾ / ਸੁਸਤੀ"
        ),
        "दूध उत्पादन में अचानक गिरावट (Drop in milk)" to listOf(
            "दूध में अचानक कमी",
            "Sudden drop in milk",
            "दुधात अचानक घट",
            "દૂધમાં અચાનક ઘટાડો",
            "ਦੁੱਧ ਵਿੱਚ ਅਚਾਨਕ ਕਮੀ"
        ),
        "सांस लेने में कठिनाई (Breathing difficulty)" to listOf(
            "सांस लेने में कठिनाई",
            "Breathing difficulty",
            "श्वास घेण्यास त्रास",
            "શ્વાસ લેવામાં તકલીફ",
            "ਸਾਹ ਲੈਣ ਵਿੱਚ ਔਖ"
        ),

        // Common Breeds
        "गिर (Gir)" to listOf("गिर", "Gir", "गिर", "ગીર", "ਗੀਰ"),
        "साहीवाल (Sahiwal)" to listOf("साहीवाल", "Sahiwal", "साहिवाल", "સાહીવાલ", "ਸਾਹੀਵਾਲ"),
        "थारपारकर (Tharparkar)" to listOf("थारपारकर", "Tharparkar", "थारपारकर", "થારપારકર", "ਥਾਰਪਾਰਕਰ"),
        "मुर्रा (Murrah)" to listOf("मुर्रा", "Murrah", "मुऱ्हा", "મુર્રા", "ਮੁਰਰਾ"),
        "सिरोही (Sirohi)" to listOf("सिरोही", "Sirohi", "सिरोही", "સિરોહી", "ਸਿਰੋਹੀ")
    )
}
