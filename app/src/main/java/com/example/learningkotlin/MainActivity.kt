package com.example.learningkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.service.controls.ControlsProviderService
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.learningkotlin.databinding.ActivityMainBinding
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    private var bluetoothAdapter: BluetoothAdapter? = null

    private var bluetoothEnableResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
                    bluetoothAdapter = bluetoothManager.getAdapter()
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
        val bluetoothAdapter: BluetoothAdapter = bluetoothManager.getAdapter()

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


    // bluetooth server socket connection thread
    // opens a serverside socket for a client to begin to initiate a bluetooth connection
    // closes after a connection is formed

    @SuppressLint("MissingPermission")
    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                SecondFragment.NAME,
                SecondFragment.MY_UUID
            )
        }



        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e(ControlsProviderService.TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    // manageMyConnectedSocket(it)
                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e(ControlsProviderService.TAG, "Could not close the connect socket", e)
            }
        }
    }

    // send and get text data from chip
    class BluetoothService(
        private val socket: BluetoothSocket,
    ) : Thread() {
        private val inputStream = socket.inputStream

        //We only need 1Byte for reading 0 or 1 from raspberry result
        private val buffer = ByteArray(1)
        override fun run() {
            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    //Read from the InputStream
                    inputStream.read(buffer)
                } catch (e: IOException) {
                    Log.i(TAG, "Input stream was disconnected", e)
                    break
                }
                // Send the obtained bytes to the UI activity.
                val text = String(buffer)
            }
        }
    }

}