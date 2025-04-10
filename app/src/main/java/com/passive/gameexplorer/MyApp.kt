package com.passive.gameexplorer

import android.app.Application
import android.os.Build
import android.util.Log
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.google.firebase.firestore.FirebaseFirestore
import com.passive.gameexplorer.repository.DeviceIdRepository
import dagger.hilt.android.HiltAndroidApp
import java.time.Instant

@HiltAndroidApp
class MyApp : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()
        val deviceId = DeviceIdRepository(this).getDeviceId()
        FirebaseFirestore.getInstance()
            .collection("profiles")
            .document(deviceId)
            .update(mapOf("lastLog" to Instant.now().toString()))
            .addOnFailureListener {
                createNewProfile(deviceId)
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MyApp", "Device info updated successfully.")
                } else {
                    Log.e("MyApp", "Error updating device info", task.exception)
                }
            }
    }

    private fun createNewProfile(deviceId: String) {
        // Get device information
        val deviceModel = Build.MODEL
        val deviceManufacturer = Build.MANUFACTURER
        val androidVersion = Build.VERSION.RELEASE

        val deviceInfo = mapOf(
            "lastLog" to Instant.now().toString(),
            "deviceModel" to deviceModel,
            "deviceManufacturer" to deviceManufacturer,
            "androidVersion" to androidVersion,
            "deviceId" to deviceId
        )

        FirebaseFirestore.getInstance()
            .collection("profiles")
            .document(deviceId) // Use the deviceId as the document ID
            .set(deviceInfo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MyApp", "New Device info created successfully.")
                } else {
                    Log.e("MyApp", "Error creating device info", task.exception)
                }
            }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .logger(DebugLogger())
            .crossfade(true)
            .build()
    }

}