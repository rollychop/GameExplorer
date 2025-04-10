package com.passive.gameexplorer.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import java.security.MessageDigest

class DeviceIdRepository(private val appContext: Context) {

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        val androidId = Settings.Secure.getString(
            appContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        return androidId?.let {
            "android_id_$it"
        } ?: generateDeviceSpecificFallbackId()
    }

    private fun generateDeviceSpecificFallbackId(): String {
        val base = "${Build.BOARD}_${Build.BOOTLOADER}_${Build.DEVICE}_" +
                "${Build.HARDWARE}_${Build.MANUFACTURER}_${Build.MODEL}"

        return "fallback_id_" + hashString(base)
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }.take(16)
    }
}
