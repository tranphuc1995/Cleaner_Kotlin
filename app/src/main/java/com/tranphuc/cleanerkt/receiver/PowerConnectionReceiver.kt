package com.tranphuc.cleanerkt.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Handler
import android.widget.TextView
import com.github.lzyzsd.circleprogress.DonutProgress

class PowerConnectionReceiver : BroadcastReceiver() {
    private lateinit var progressBattery: DonutProgress
    private lateinit var textview_percent_battery: TextView
    private var isRunThread: Boolean = false
    public var ProgressBattery: DonutProgress
        get() {
            return progressBattery
        }
        set(value) {
            progressBattery = value
        }

    public var TextViewPercentBattey: TextView
        get() {
            return textview_percent_battery
        }
        set(value) {
            textview_percent_battery = value
        }

    override fun onReceive(context: Context?, intent: Intent?) {
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryLevel = (level?.toFloat()!! / scale!!.toFloat() * 100.0f).toInt()

        val handler: Handler = Handler()
        var progressStatus: Int = 0

        if (!isRunThread) {
            isRunThread = true
            Thread(Runnable {
                while (progressStatus < batteryLevel) {
                    progressStatus += 1
                    try {
                        Thread.sleep(25)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    handler.post(Runnable {
                        progressBattery.progress = progressStatus.toFloat()
                        textview_percent_battery.text = progressStatus.toString() + "%"
                    })
                }
            }).start()
        } else {
            progressBattery.progress = batteryLevel.toFloat()
            textview_percent_battery.text = batteryLevel.toString() + "%"
        }
    }
}