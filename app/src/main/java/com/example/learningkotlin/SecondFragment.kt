package com.example.learningkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.example.learningkotlin.databinding.FragmentSecondBinding
import java.util.concurrent.Executor

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

  private var _binding: FragmentSecondBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentSecondBinding.inflate(inflater, container, false)
      return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFindPlant.setOnClickListener {
            // bluetooth connection
            // requires min api version 26, default min seems to be 24 (i changed that in the settings here) so be careful when building

            val deviceManager =
                requireContext().getSystemService(Context.COMPANION_DEVICE_SERVICE)

            val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
                // theres not a lot here now but maybe in the future we get all smart plants to
                // use similar names and use setNamePattern() to filter out things like
                // headsets or whatever? just a thought
                .build()

            val pairingRequest: AssociationRequest = AssociationRequest.Builder()
                // Find only devices that match this request filter.
                .addDeviceFilter(deviceFilter)
                .build()


            // buggy mess territory proceed with caution
            /* deviceManager.associate(pairingRequest,
                object : CompanionDeviceManager.Callback() {
                    // Called when a device is found. Launch the IntentSender so the user
                    // can select the device they want to pair with.
                    override fun onDeviceFound(chooserLauncher: IntentSender) {
                        startIntentSenderForResult(chooserLauncher,
                            SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0)
                    }

                    override fun onFailure(error: CharSequence?) {
                        // Handle the failure.
                    }
                }, null)

            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()

            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address
            }
            */


            // leveling
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
    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}