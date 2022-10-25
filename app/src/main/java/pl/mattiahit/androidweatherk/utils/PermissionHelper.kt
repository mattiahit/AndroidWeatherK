package pl.mattiahit.androidweatherk.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHelper(private val fragment: Fragment, private val permissionListener: PermissionListener) {

    private val requiredPermissions = listOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestPermissionsLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if(it.value) {
                permissionListener.isPermissionGranted(true)
            }
        }
    }


    fun checkForRequiredPermissions() {
        for(permission in requiredPermissions) {
            fragment.requireContext().let {
                if(ContextCompat.checkSelfPermission(it, permission) == PackageManager.PERMISSION_GRANTED) {
                    permissionListener.isPermissionGranted(true)
                } else {
                    permissionListener.isPermissionGranted(false)
                    requestPermissionsLauncher.launch(requiredPermissions.toTypedArray())
                }
            }
        }
    }
}