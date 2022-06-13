package com.nanit.happybirthday.feature.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class Permissions {
    companion object {
        val REQUIRED_PERMISSIONS_CAMERA = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        fun permissionsCamera(context: Context) = REQUIRED_PERMISSIONS_CAMERA.all {
            ContextCompat.checkSelfPermission(
                context, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}