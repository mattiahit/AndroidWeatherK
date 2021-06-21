package pl.mattiahit.androidweatherk

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LocationListener {

    var locationPermissionGranted = false
    @Inject lateinit var locationManagerInstance: LocationManager

    companion object {
        const val PERMISSION_REQUEST_LOCATION = 123;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as WeatherApplication).getAppComponent().inject(this)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        //this.initLocationListener()
        super.onResume()
    }

//    private fun initLocationListener(){
//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            //this.locationPermissionGranted = false
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
//                PERMISSION_REQUEST_LOCATION)
//        }else{
//            Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show()
//           //this.locationPermissionGranted = true
//            //this.locationManagerInstance.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0f, this)
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when(requestCode){
//            PERMISSION_REQUEST_LOCATION -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
////                    locationPermissionGranted = true
////                    //this.initLocationListener()
////                }else{
////                    locationPermissionGranted = false
//                }
//            }
//        }
//    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }

    override fun onProviderDisabled(provider: String) {
        Toast.makeText(this, "$provider is disabled", Toast.LENGTH_SHORT).show()
    }
}