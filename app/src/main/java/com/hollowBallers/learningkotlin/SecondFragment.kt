package com.hollowBallers.learningkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.MacAddress
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.hollowBallers.learningkotlin.databinding.FragmentSecondBinding
import java.util.UUID.fromString
import java.util.concurrent.Executor

/**
 * The connection page
 */
class SecondFragment : Fragment() {

    // bluetooth connection variables
    // requires min api version 26, default min seems to be 24 (i changed that in the settings here) so be careful when building
    private val deviceManager: CompanionDeviceManager by lazy {
        requireContext().getSystemService(Context.COMPANION_DEVICE_SERVICE) as CompanionDeviceManager
    }

    // a way of filtering which devices are visible in the pop up when searching
    private val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
        // theres not a lot here now but maybe in the future we get all smart plants to
        // use similar names and use setNamePattern() to filter out things like
        // headsets or whatever? rn it just allows everything
        //.setNamePattern()
        .build()

    private val pairingRequest: AssociationRequest = AssociationRequest.Builder()
        // Find only devices that match this request filter.
        .addDeviceFilter(deviceFilter)
        // doesn't stop search after something is found
        .setSingleDevice(false)
        .build()

    private val executor: Executor = Executor { it.run() }

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach {
            Log.d("Permission Request", "${it.key} = ${it.value}")
        }
    }

    private val requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // granted
        } else {
            // denied
        }
    }

    private var _binding: FragmentSecondBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT
            ))
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }

      _binding = FragmentSecondBinding.inflate(inflater, container, false)
      return binding.root

    }

    // callback listens for result form bluetooth connection attempt
    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SELECT_DEVICE_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_OK -> {
                    // The user chose to pair the app with a Bluetooth device.
                    val deviceToPair: BluetoothDevice? =
                        data?.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE)
                    deviceToPair?.createBond()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFindPlant.setOnClickListener {
            // Shows bluetooth pairing popup
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                paringDialogNew()
            } else {
                paringDialogOld()
            }

            leveling()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   // leveling function that is called when the find the plant button is clicked
    private fun leveling(){
       val levelingPref = activity?.getSharedPreferences(
           getString(R.string.preference_file_key), Context.MODE_PRIVATE)

       val gainedXP = 25
       var xp: Int = levelingPref!!.getInt((R.string.xp_key).toString(), -1)
       var level: Int = levelingPref.getInt((R.string.level_key).toString(), -1)

       if (xp != -1 && level != -1)
       {
           xp+=gainedXP
           if (xp >= 100)
           {
               level++
               xp -= 100
               binding.textNextLevel.text = "Leveled Up! New Level:"
               binding.textLevelInfo.text = level.toString()
           }
           else
           {
               binding.textNextLevel.text = "XP until next level:"
               binding.textLevelInfo.text = (100-xp).toString()
           }
           with (levelingPref.edit()) {
               putInt((R.string.xp_key).toString(), xp)
               putInt((R.string.level_key).toString(), level)
               commit()
           }
       }
       binding.textXPGained.text = gainedXP.toString()
   }

    // creates device paring dialog for Android 33+
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun paringDialogNew(){deviceManager.associate(
        pairingRequest,
        executor,
        object : CompanionDeviceManager.Callback() {
            // Called when a device is found. Launch the IntentSender so the user
            // can select the device they want to pair with.
            override fun onAssociationPending(intentSender: IntentSender) {
                intentSender?.let {
                    startIntentSenderForResult(
                        it,
                        SELECT_DEVICE_REQUEST_CODE,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                }
            }

            override fun onAssociationCreated(associationInfo: AssociationInfo) {
                // The association is created.
                var associationId = associationInfo.id
                var macAddress: MacAddress? = associationInfo.deviceMacAddress
            }

            override fun onFailure(errorMessage: CharSequence?) {
                // Handle the failure.
            }
        })

    }

    // creates device paring dialog for Android 32 and below
    private fun paringDialogOld(){

        deviceManager.associate(pairingRequest,
            object : CompanionDeviceManager.Callback() {
                // Called when a device is found. Launch the IntentSender so the user
                // can select the device they want to pair with.
                override fun onDeviceFound(chooserLauncher: IntentSender) {
                    startIntentSenderForResult(
                        chooserLauncher,
                        SELECT_DEVICE_REQUEST_CODE,
                        null,
                        0,
                        0,
                        0,
                        null)
                }

                override fun onFailure(error: CharSequence?) {
                    // Handle error
                }
            }, null)

    }

    companion object {
        private const val SELECT_DEVICE_REQUEST_CODE = 0
        private const val BLUETOOTH_DEVICE_NAME_REGEX_TO_FILTER_FOR = ""
        public const val NAME = "SmartPlants"
        public val MY_UUID = fromString("7bb4ac1c-0fc9-4b2f-99d0-362bf30e6293")
    }
}