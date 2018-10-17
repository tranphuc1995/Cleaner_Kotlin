package com.tranphuc.cleanerkt

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StatFs
import com.bumptech.glide.Glide
import com.tranphuc.cleanerkt.global.MethodStatic
import kotlinx.android.synthetic.main.layout_ram_storage_cpu.*
import com.tranphuc.cleanerkt.broadcast_receiver.PowerConnectionReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_direction.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addControls()
        addEvents()
    }

    private fun addEvents() {
        linear_memory_boost_click();
    }

    private fun linear_memory_boost_click() {
        linear_memory_boost.setOnClickListener {
            var intent: Intent = Intent(MainActivity@ this, MemoryBoostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addControls() {
        setupLayout()
    }

    private fun setupLayout() {
        showInfoStorage()
        showInfoRam()
        showInfoBattery()
        Glide.with(this).load(R.drawable.ic_bin).into(image_bin)
        Glide.with(this).load(R.drawable.ic_memory).into(image_memory_boost)
        Glide.with(this).load(R.drawable.ic_app_manager).into(image_app_manager)
    }

    private fun showInfoBattery() {
        var intentFilter: IntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        var broadcastReceiver: PowerConnectionReceiver = PowerConnectionReceiver()
        broadcastReceiver.ProgressBattery = progress_battery
        broadcastReceiver.TextViewPercentBattey = textview_percent_battery
        MainActivity@ this.registerReceiver(broadcastReceiver, intentFilter)
    }


    private fun showInfoRam() {
        var activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var memoryInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        var totalRam: Long = memoryInfo.totalMem
        var availableRam: Long = memoryInfo.availMem
        textview_total_ram.text = "/" + MethodStatic.convertBytesToGb(totalRam)
        textview_used_ram.text = MethodStatic.convertBytesToGb(totalRam - availableRam)
        val handler: Handler = Handler()
        var progressStatus: Int = 0

        Thread(Runnable {
            while (progressStatus < percentUsed(totalRam, availableRam)) {
                progressStatus += 1
                try {
                    Thread.sleep(25)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                handler.post(Runnable {
                    progress_ram.progress = progressStatus.toFloat()
                    textview_percent_ram.text = progressStatus.toString() + "%"
                })
            }
        }).start()
    }

    private fun showInfoStorage() {
        var statFs: StatFs = StatFs(Environment.getExternalStorageDirectory().getAbsolutePath())
        var blockSize: Long = statFs.blockSizeLong;
        var totalSize: Long = statFs.blockCount * blockSize
        var availableSize: Long = statFs.availableBlocks * blockSize
        textview_total_storage.text = "/" + MethodStatic.convertBytesToGb(totalSize)
        textview_used_storage.text = MethodStatic.convertBytesToGb(totalSize - availableSize)

        val handler: Handler = Handler()
        var progressStatus: Int = 0

        Thread(Runnable {
            while (progressStatus < percentUsed(totalSize, availableSize)) {
                progressStatus += 1
                try {
                    Thread.sleep(25)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                handler.post(Runnable {
                    progress_storage.progress = progressStatus.toFloat()
                    textview_percent_storage.text = progressStatus.toString() + "%"
                })
            }
        }).start()

    }

    private fun percentUsed(total: Long, available: Long): Long {
        var percent: Long = 0
        percent = Math.round((((total - available).toDouble()) / total.toDouble()) * 100)
        return percent
    }
}
