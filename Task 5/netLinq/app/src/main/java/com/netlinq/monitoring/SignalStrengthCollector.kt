package com.netlinq.monitoring

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.CellInfo
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class SignalReading(
    val strengthDbm: Int?,
    val quality: Int?
)

@Singleton
class SignalStrengthCollector @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    fun collect(): SignalReading {
        if (!hasPhonePermission()) return SignalReading(null, null)

        val cellInfos: List<CellInfo> = try {
            telephonyManager.allCellInfo ?: emptyList()
        } catch (_: SecurityException) {
            emptyList()
        }

        val registered = cellInfos.firstOrNull { it.isRegistered } ?: cellInfos.firstOrNull()
            ?: return SignalReading(null, null)

        return when (registered) {
            is CellInfoLte -> SignalReading(
                strengthDbm = registered.cellSignalStrength.dbm,
                quality = registered.cellSignalStrength.rsrp
            )
            is CellInfoNr -> SignalReading(
                strengthDbm = registered.cellSignalStrength.dbm,
                quality = null
            )
            is CellInfoGsm -> SignalReading(
                strengthDbm = registered.cellSignalStrength.dbm,
                quality = registered.cellSignalStrength.bitErrorRate
            )
            is CellInfoWcdma -> SignalReading(
                strengthDbm = registered.cellSignalStrength.dbm,
                quality = null
            )
            is CellInfoCdma -> SignalReading(
                strengthDbm = registered.cellSignalStrength.dbm,
                quality = null
            )
            else -> SignalReading(null, null)
        }
    }

    private fun hasPhonePermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
}
