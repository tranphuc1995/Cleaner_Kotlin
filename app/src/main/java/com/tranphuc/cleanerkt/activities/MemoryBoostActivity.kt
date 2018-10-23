package com.tranphuc.cleanerkt.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ram.speed.booster.RAMBooster
import com.ram.speed.booster.interfaces.CleanListener
import android.util.Log
import com.ram.speed.booster.utils.ProcessInfo
import com.ram.speed.booster.interfaces.ScanListener
import com.tranphuc.cleanerkt.R
import java.util.*


class MemoryBoostActivity : AppCompatActivity() {

    private lateinit var booster : RAMBooster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_boost)

        booster =  RAMBooster(this)
       // booster.setDebug(true)
        booster.setScanListener(object : ScanListener {
            override fun onStarted() {
                Log.d("debug_boost", "Scan started")
            }

            override fun onFinished(availableRam: Long, totalRam: Long, appsToClean: List<ProcessInfo>) {

                Log.d("debug_boost", String.format(Locale.US,
                        "Scan finished, available RAM: %dMB, total RAM: %dMB",
                        availableRam, totalRam))
                val apps = ArrayList<String>()
                for (info in appsToClean) {
                    apps.add(info.processName)
                }
                Log.d("debug_boost", String.format(Locale.US,
                        "Going to clean founded processes: %s", Arrays.toString(apps.toTypedArray())))
                booster.startClean()
            }
        })
        booster.setCleanListener(object : CleanListener {
            override fun onStarted() {
                Log.d("debug_boost", "Clean started")
            }


            override fun onFinished(availableRam: Long, totalRam: Long) {

                Log.d("debug_boost", String.format(Locale.US,
                        "Clean finished, available RAM: %dMB, total RAM: %dMB",
                        availableRam, totalRam))
               // booster = null

            }
        })
        booster.startScan(true)
    }
}
