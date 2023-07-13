package com.wayatech.smartcardlibrary

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import android.util.Log

@SuppressLint("MissingPermission")
class SmartCard(bleManager: BluetoothManager, context: Context) {
    private val bleManager = bleManager
    private var isConnected = false
    private val context = context

    fun scan() {
        val scanFilter = ScanFilter.Builder().setServiceUuid(
            ParcelUuid.fromString("00000001-0000-1000-8000-00805f9b34fb")
        ).build()
        bleManager.adapter.bluetoothLeScanner.startScan(listOf(scanFilter), scanSettings, scanCallback)
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        .build()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result.device) {

                if (!isConnected) {
                    try {
                        SmartCardManager(context).connect(result.device).enqueue()
                        Log.i("ScanCallback", "Connected to " + result.device.name)
                    } catch(e: Exception) {
                        Log.i("ScanCallback", "ERROOOOOOOOOOOOOOOORRRRRRRRRRRRRRRRR")

                    }

                    isConnected = true
                }

                Log.i("ScanCallback", "Found BLE device! Name: ${name ?: "Unnamed"}, address: $address")
            }
        }
    }
}