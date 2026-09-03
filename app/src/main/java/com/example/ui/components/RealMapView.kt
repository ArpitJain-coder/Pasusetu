package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.util.LocationHelper
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.util.AppStrings
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sinh
import kotlin.math.tan

data class MapLocationPin(
    val id: String,
    val titleHindi: String,
    val titleEnglish: String,
    val type: PinType,
    val latitude: Double,
    val longitude: Double,
    val phone: String = "1962",
    val doctorOrCases: String,
    val distanceKm: Double,
    val isEmergency: Boolean = false,
    val descriptionHindi: String = "",
    val descriptionEnglish: String = ""
)

enum class PinType {
    VET_HOSPITAL,
    OUTBREAK_ZONE,
    MOBILE_UNIT,
    MY_FARM
}

@Composable
fun RealMapView(
    isHindi: Boolean,
    modifier: Modifier = Modifier,
    isOfficerMode: Boolean = false,
    onDispatchUnit: ((MapLocationPin) -> Unit)? = null
) {
    val context = LocalContext.current

    // Base coordinates: Jaipur / Rajasthan rural livestock center (26.9124° N, 75.7873° E)
    var centerLat by remember { mutableDoubleStateOf(26.9180) }
    var centerLon by remember { mutableDoubleStateOf(75.7950) }
    var zoomLevel by remember { mutableIntStateOf(13) }

    var selectedFilter by remember { mutableStateOf("ALL") }
    var selectedPin by remember { mutableStateOf<MapLocationPin?>(null) }
    var showDispatchDialog by remember { mutableStateOf(false) }
    var isLocationLoading by remember { mutableStateOf(false) }

    // Preset veterinary hospitals & real outbreak clusters
    val pins = remember {
        listOf(
            MapLocationPin(
                id = "hosp_1",
                titleHindi = "राजकीय पशु चिकित्सालय (भाटी)",
                titleEnglish = "Govt. Veterinary Hospital (Bhati)",
                type = PinType.VET_HOSPITAL,
                latitude = 26.9215,
                longitude = 75.7920,
                phone = "0141-2748900",
                doctorOrCases = "डॉ. महेश शर्मा (वरिष्ठ पशु चिकित्सक)",
                distanceKm = 1.2,
                descriptionHindi = "24x7 आपातकालीन सेवा, FMD वैक्सीन उपलब्ध, सर्जिकल वार्ड",
                descriptionEnglish = "24x7 Emergency, FMD vaccines available, Surgical ward"
            ),
            MapLocationPin(
                id = "hosp_2",
                titleHindi = "पशु पॉलीक्लिनिक (जयपुर उत्तर)",
                titleEnglish = "Animal Polyclinic (Jaipur North)",
                type = PinType.VET_HOSPITAL,
                latitude = 26.9380,
                longitude = 75.8010,
                phone = "0141-2601122",
                doctorOrCases = "डॉ. अनीता राव (सर्जन)",
                distanceKm = 3.6,
                descriptionHindi = "एक्स-रे, सोनोग्राफी और रक्त परीक्षण प्रयोगशाला",
                descriptionEnglish = "X-Ray, Ultrasound & blood test lab"
            ),
            MapLocationPin(
                id = "van_1",
                titleHindi = "मोबाइल पशु चिकित्सा एम्बुलेंस #4",
                titleEnglish = "Mobile Vet Ambulance Unit #4",
                type = PinType.MOBILE_UNIT,
                latitude = 26.9050,
                longitude = 75.7720,
                phone = "1962",
                doctorOrCases = "फील्ड डॉक्टर: डॉ. विकास सैनी",
                distanceKm = 2.4,
                descriptionHindi = "गाँवों में ऑन-फार्म उपचार एवं दवा वितरण पर तैनात",
                descriptionEnglish = "Active on-field village treatment & medicine distribution"
            ),
            MapLocationPin(
                id = "outbreak_1",
                titleHindi = "खुरपका-मुंहपका (FMD) प्रकोप क्षेत्र",
                titleEnglish = "FMD Outbreak Zone - Bhati",
                type = PinType.OUTBREAK_ZONE,
                latitude = 26.9140,
                longitude = 75.7890,
                phone = "1962",
                doctorOrCases = "14 सक्रिय केस (क्वारंटाइन लागू)",
                distanceKm = 0.8,
                isEmergency = true,
                descriptionHindi = "3 गाँव सील, 1.5 किमी परिधि में टीकाकरण अभियान जारी",
                descriptionEnglish = "3 villages sealed, ring vaccination active within 1.5 km"
            ),
            MapLocationPin(
                id = "outbreak_2",
                titleHindi = "लम्पी स्किन (Lumpy Skin) अलर्ट",
                titleEnglish = "Lumpy Skin Alert Zone",
                type = PinType.OUTBREAK_ZONE,
                latitude = 26.9310,
                longitude = 75.8150,
                phone = "1962",
                doctorOrCases = "5 संदेहास्पद केस",
                distanceKm = 4.1,
                isEmergency = true,
                descriptionHindi = "मक्खी-मच्छर नियंत्रण एवं एंटीसेप्टिक छिड़काव अभियान",
                descriptionEnglish = "Fly/mosquito vector control & spray drive in progress"
            )
        )
    }

    val locationHelper = remember { LocationHelper(context) }
    val coroutineScope = rememberCoroutineScope()

    // Permission launcher for Location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isLocationLoading = true
            coroutineScope.launch {
                try {
                    val loc = locationHelper.getCurrentLocation() ?: locationHelper.getLastLocation()
                    if (loc != null) {
                        centerLat = loc.latitude
                        centerLon = loc.longitude
                    }
                } finally {
                    isLocationLoading = false
                }
            }
        }
    }

    fun requestLocation() {
        if (locationHelper.hasPermission()) {
            isLocationLoading = true
            coroutineScope.launch {
                try {
                    val loc = locationHelper.getCurrentLocation() ?: locationHelper.getLastLocation()
                    if (loc != null) {
                        centerLat = loc.latitude
                        centerLon = loc.longitude
                    } else {
                        centerLat = LocationHelper.DEFAULT_FARM_LAT
                        centerLon = LocationHelper.DEFAULT_FARM_LON
                    }
                } finally {
                    isLocationLoading = false
                }
            }
        } else {
            locationPermissionLauncher.launch(LocationHelper.REQUIRED_PERMISSIONS)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE8ECE9))
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()
        val density = LocalDensity.current

        // OpenStreetMap slippy tile calculation
        val tileSize = 256
        val n = 2.0.pow(zoomLevel.toDouble())
        val xCenter = (centerLon + 180.0) / 360.0 * n
        val latRad = centerLat * PI / 180.0
        val yCenter = (1.0 - ln(tan(latRad) + 1.0 / cos(latRad)) / PI) / 2.0 * n

        val tileXCenter = floor(xCenter).toInt()
        val tileYCenter = floor(yCenter).toInt()

        val pixelOffsetX = ((xCenter - tileXCenter) * tileSize).toFloat()
        val pixelOffsetY = ((yCenter - tileYCenter) * tileSize).toFloat()

        // Drag gesture handling for panning
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(zoomLevel) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val degreesPerPixelLon = 360.0 / (tileSize * n)
                        val degreesPerPixelLat = 180.0 / (tileSize * n)

                        centerLon -= dragAmount.x * degreesPerPixelLon
                        centerLat += dragAmount.y * degreesPerPixelLat
                    }
                }
        ) {
            // Render 3x3 Tile Grid from OpenStreetMap
            for (dx in -1..1) {
                for (dy in -1..1) {
                    val tileX = tileXCenter + dx
                    val tileY = tileYCenter + dy
                    val maxTile = n.toInt()

                    if (tileX in 0 until maxTile && tileY in 0 until maxTile) {
                        val tileUrl = "https://tile.openstreetmap.org/$zoomLevel/$tileX/$tileY.png"
                        val leftOffsetPx = (widthPx / 2) + ((dx * tileSize) - pixelOffsetX)
                        val topOffsetPx = (heightPx / 2) + ((dy * tileSize) - pixelOffsetY)

                        val leftDp = with(density) { leftOffsetPx.toDp() }
                        val topDp = with(density) { topOffsetPx.toDp() }

                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(tileUrl)
                                .addHeader("User-Agent", "PashuSetuAndroidApp/1.0")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Map Tile",
                            modifier = Modifier
                                .offset(x = leftDp, y = topDp)
                                .size(256.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }

            // Render Geographic Markers & Pins
            val filteredPins = pins.filter {
                when (selectedFilter) {
                    "VET" -> it.type == PinType.VET_HOSPITAL || it.type == PinType.MOBILE_UNIT
                    "OUTBREAK" -> it.type == PinType.OUTBREAK_ZONE
                    else -> true
                }
            }

            filteredPins.forEach { pin ->
                val pinX = (pin.longitude + 180.0) / 360.0 * n
                val pinLatRad = pin.latitude * PI / 180.0
                val pinY = (1.0 - ln(tan(pinLatRad) + 1.0 / cos(pinLatRad)) / PI) / 2.0 * n

                val pinPixelX = (widthPx / 2) + ((pinX - xCenter) * tileSize).toFloat()
                val pinPixelY = (heightPx / 2) + ((pinY - yCenter) * tileSize).toFloat()

                val pinLeftDp = with(density) { pinPixelX.toDp() } - 20.dp
                val pinTopDp = with(density) { pinPixelY.toDp() } - 40.dp

                // Pin Composable
                Box(
                    modifier = Modifier
                        .offset(x = pinLeftDp, y = pinTopDp)
                        .clickable { selectedPin = pin }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = when (pin.type) {
                                PinType.OUTBREAK_ZONE -> Color(0xFFD32F2F)
                                PinType.VET_HOSPITAL -> GreenDark
                                PinType.MOBILE_UNIT -> Color(0xFF0288D1)
                                PinType.MY_FARM -> Color(0xFFF57C00)
                            },
                            shadowElevation = 6.dp,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = when (pin.type) {
                                        PinType.OUTBREAK_ZONE -> Icons.Default.Warning
                                        PinType.VET_HOSPITAL -> Icons.Default.LocalHospital
                                        PinType.MOBILE_UNIT -> Icons.Default.Navigation
                                        PinType.MY_FARM -> Icons.Default.LocationOn
                                    },
                                    contentDescription = pin.titleEnglish,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Small label below pin
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.White.copy(alpha = 0.92f),
                            shadowElevation = 2.dp,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = if (isHindi) pin.titleHindi.take(15) + "..." else pin.titleEnglish.take(15) + "...",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // User's own Live GPS Location Center Pin
            val centerLeftDp = with(density) { (widthPx / 2).toDp() } - 12.dp
            val centerTopDp = with(density) { (heightPx / 2).toDp() } - 12.dp
            Box(
                modifier = Modifier
                    .offset(x = centerLeftDp, y = centerTopDp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1976D2).copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1976D2))
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }

        // Top Controls: Filter Chips & Location Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .align(Alignment.TopCenter)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isHindi) "जयपुर ग्रामीण लाइव पशुधन मैप" else "Jaipur Rural Livestock Live Map",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B241C)
                        )
                        Text(
                            text = if (isHindi) "अक्षांश: ${String.format("%.4f", centerLat)}, देशांतर: ${String.format("%.4f", centerLon)}"
                            else "Lat: ${String.format("%.4f", centerLat)}, Lon: ${String.format("%.4f", centerLon)}",
                            fontSize = 11.sp,
                            color = Color(0xFF616161)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Text(
                            text = "Zoom: $zoomLevel",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" },
                    label = { Text(if (isHindi) "सभी (${pins.size})" else "All (${pins.size})", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GreenDark,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    selected = selectedFilter == "VET",
                    onClick = { selectedFilter = "VET" },
                    label = { Text(if (isHindi) "पशु चिकित्सालय" else "Vet Clinics", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GreenDark,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    selected = selectedFilter == "OUTBREAK",
                    onClick = { selectedFilter = "OUTBREAK" },
                    label = { Text(if (isHindi) "रोग प्रकोप (Alerts)" else "Outbreaks", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD32F2F),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Floating Action Buttons (Zoom +, Zoom -, My Location)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = { if (zoomLevel < 18) zoomLevel++ },
                containerColor = Color.White,
                contentColor = Color(0xFF212121),
                modifier = Modifier.size(44.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }

            FloatingActionButton(
                onClick = { if (zoomLevel > 10) zoomLevel-- },
                containerColor = Color.White,
                contentColor = Color(0xFF212121),
                modifier = Modifier.size(44.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }

            FloatingActionButton(
                onClick = { requestLocation() },
                containerColor = GreenDark,
                contentColor = Color.White,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location")
            }
        }

        // Bottom Card for Selected Pin
        selectedPin?.let { pin ->
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (pin.isEmergency) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (pin.isEmergency) Icons.Default.Warning else Icons.Default.LocalHospital,
                                        contentDescription = null,
                                        tint = if (pin.isEmergency) Color(0xFFD32F2F) else GreenDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = if (isHindi) pin.titleHindi else pin.titleEnglish,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B241C)
                                )
                                Text(
                                    text = "${pin.doctorOrCases} • ${pin.distanceKm} ${if (isHindi) "किमी दूर" else "km away"}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF616161)
                                )
                            }
                        }

                        IconButton(onClick = { selectedPin = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isHindi) pin.descriptionHindi else pin.descriptionEnglish,
                        fontSize = 13.sp,
                        color = Color(0xFF424242)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Call Button
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${pin.phone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isHindi) "कॉल करें" else "Call", fontSize = 13.sp)
                        }

                        // Directions via Google Maps / External Map
                        OutlinedButton(
                            onClick = {
                                val mapIntent = LocationHelper.getDirectionsIntent(
                                    destLat = pin.latitude,
                                    destLon = pin.longitude,
                                    label = if (isHindi) pin.titleHindi else pin.titleEnglish
                                )
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isHindi) "रास्ता देखें" else "Directions", fontSize = 13.sp)
                        }

                        // For District Officers: Dispatch Rapid Response
                        if (isOfficerMode && pin.isEmergency) {
                            Button(
                                onClick = {
                                    showDispatchDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isHindi) "दल भेजें" else "Dispatch", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDispatchDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDispatchDialog = false },
            title = { Text(if (isHindi) "त्वरित प्रतिक्रिया दल तैनात करें" else "Deploy Rapid Response Unit") },
            text = {
                Text(
                    if (isHindi) "क्या आप सचल पशु चिकित्सा वैन #4 को ${selectedPin?.titleHindi} की ओर तत्काल रवाना करना चाहते हैं?"
                    else "Confirm dispatch of Mobile Vet Unit #4 to ${selectedPin?.titleEnglish} immediately?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedPin?.let { onDispatchUnit?.invoke(it) }
                        showDispatchDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text(if (isHindi) "हाँ, तत्काल रवाना करें" else "Confirm Dispatch")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDispatchDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }
}
