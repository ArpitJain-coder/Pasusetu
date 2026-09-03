package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.LocationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LocationHelperTest {

    @Test
    fun testCalculateDistanceKm() {
        val dist = LocationHelper.calculateDistanceKm(26.9124, 75.7873, 26.9215, 75.7920)
        assertTrue("Distance should be positive", dist > 0.0)
        assertTrue("Distance between nearby points in Jaipur should be around 1 km", dist in 0.5..2.5)
    }

    @Test
    fun testFormatDistance() {
        val meterStr = LocationHelper.formatDistance(0.45)
        assertEquals("450 m", meterStr)

        val kmStr = LocationHelper.formatDistance(3.64)
        assertEquals("3.6 km", kmStr)
    }

    @Test
    fun testFindNearestDestination() {
        data class Clinic(val name: String, val lat: Double, val lon: Double)

        val clinics = listOf(
            Clinic("Far Clinic", 27.5000, 76.5000),
            Clinic("Near Clinic", 26.9200, 75.7900),
            Clinic("Medium Clinic", 27.1000, 75.9000)
        )

        val context = ApplicationProvider.getApplicationContext<Context>()
        val nearest = LocationHelper(context)
            .findNearestDestination(
                currentLat = 26.9180,
                currentLon = 75.7950,
                destinations = clinics,
                latSelector = { it.lat },
                lonSelector = { it.lon }
            )

        assertNotNull(nearest)
        assertEquals("Near Clinic", nearest?.first?.name)
    }
}
