package com.example.learningkotlin

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.learningkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

    private var bluetoothEnableResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    // success!
                }
                Activity.RESULT_CANCELED -> {
                    // oh no :(
                }
            }
        }

    private val requestBluetoothPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                bluetoothEnableResultLauncher.launch(enableBluetoothIntent)
            } else {
                // some sort of retry
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // bluetooth set up
        setupViews()
        checkBluetoothEnabled()


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_main)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }

    private fun setupViews() {
        // Here we setup the behavior of the button in our rationale dialog: basically we need to
        // rerun the permissions check logic if it was already denied
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestBluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            requestBluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH)
        }
    }

    private fun checkBluetoothEnabled() {
        // bluetooth set up
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()

        // displays message if the device doesn't support bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(
                this,
                "Your device does not support bluetooth. This may lead to limited functionality of this application",
                Toast.LENGTH_SHORT).show()
        }

        // check to see if user grants permission to bluetooth
        val registerForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data

            }
        }


        // activate bluetooth permission
        if (bluetoothAdapter?.isEnabled == false) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                /**
                 * We DON'T have Bluetooth permissions. We have to get them before we can ask the
                 *  user to enable Bluetooth
                 */

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.BLUETOOTH_CONNECT)) {
                        //put toast about why need bluetooth
                    }

                    } else {
                        requestBluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.BLUETOOTH)) {
                        //put toast about why need bluetooth
                    } else {
                        requestBluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH)
                    }
                }

            }
    }

}