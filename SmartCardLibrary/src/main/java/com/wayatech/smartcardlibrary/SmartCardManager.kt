package com.wayatech.smartcardlibrary

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import no.nordicsemi.android.ble.BleManager
import java.util.UUID


internal class SmartCardManager(context: Context) : BleManager(context) {
    // ==== Logging =====
    override fun getMinLogPriority(): Int {
        // Use to return minimal desired logging priority.
        return Log.VERBOSE
    }

    override fun log(priority: Int, message: String) {
        // Log from here.
        Log.println(priority, TAG, message)
    }

    // ==== Required implementation ====
    // This is a reference to a characteristic that the manager will use internally.
    private var smartCardControlPoint: BluetoothGattCharacteristic? = null
    override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
        // Here obtain instances of your characteristics.
        // Return false if a required service has not been discovered.
        val smartCardService = gatt.getService(UUID.fromString("00000001-0000-1000-8000-00805f9b34fb"))
        if (smartCardService != null) {
            smartCardControlPoint = smartCardService.getCharacteristic(UUID.fromString("00000003-0000-1000-8000-00805f9b34fb"))
        }
        return smartCardControlPoint != null
    }

    override fun initialize() {
        // Initialize your device.
        // This means e.g. enabling notifications, setting notification callbacks, or writing
        // something to a Control Point characteristic.
        // Kotlin projects should not use suspend methods here, as this method does not suspend.
        requestMtu(517)
            .enqueue()
    }

    override fun onServicesInvalidated() {
        // This method is called when the services get invalidated, i.e. when the device
        // disconnects.
        // References to characteristics should be nullified here.
        smartCardControlPoint = null
    }

    // ==== Public API ====
    // Here you may add some high level methods for your device:
    fun enablesmartCard() {
        // Do the magic.
        /*writeCharacteristic(
            smartCardControlPoint,
            Flux.enable(),
            BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
        )
            .enqueue()*/
    }

    companion object {
        private const val TAG = "SmartCardManager"
    }
}