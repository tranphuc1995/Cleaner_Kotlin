package com.tranphuc.cleanerkt.activities

import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.tranphuc.cleanerkt.R
import com.tranphuc.cleanerkt.global.MethodStatic
import kotlinx.android.synthetic.main.activity_junk_clean.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class JunkCleanActivity : AppCompatActivity() {
    //permission
    private val PERMISSION_READ_STROGARE: String = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val REQUEST_CODE: Int = 1
    //
    private lateinit var listJunkFile: MutableList<File>
    private val handler: Handler = Handler()
    private var totalSizeJunkFile: Long = 0
    private var colorRed: Int = 0
    private var colorGreen: Int = 0
    private var colorBlue: Int = 0
    private var count: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_junk_clean)

        if (isPermissionGranted(PERMISSION_READ_STROGARE)) {
            Thread(Runnable {
                listJunkFile = getListFiles(File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath()))
            }).start()
        } else { // xin quyền
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_READ_STROGARE), REQUEST_CODE);
        }

    }


    private fun getListFiles(parentDir: File): MutableList<File> {
        val inFiles: MutableList<File> = ArrayList<File>()
        val files: Queue<File> = LinkedList<File>()
        files.addAll(parentDir.listFiles().toList())
        while (!files.isEmpty()) {
            var file: File = files.remove()
            if (file.isDirectory) {
                files.addAll(file.listFiles().toList())
            } else {
                try {
                    Thread.sleep(0, 1)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                handler.post(Runnable {
                    colorRed = Random().nextInt(256) // random between 0->255
                    colorGreen = Random().nextInt(256)
                    colorBlue = Random().nextInt(256)
                    totalSizeJunkFile += file.length()
                    var total = MethodStatic.convertBytesToGb(totalSizeJunkFile)
                    var arrayTotal = total.split(" ")
                    textview_quantity.setText("" + arrayTotal[0])
                    textview_unit.setText("" + arrayTotal[1])
                    relative_background.setBackgroundColor(Color.rgb(colorRed, colorGreen, colorBlue))
                    inFiles.add(file)
                })
            }
        }
        return inFiles
    }

    private fun isPermissionGranted(permission: String): Boolean {
        val result: Int = ContextCompat.checkSelfPermission(this, permission)
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission has been denied by user", Toast.LENGTH_SHORT).show()
                } else {
                    listJunkFile = getListFiles(File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath()))
                    Toast.makeText(this, "" + listJunkFile.size, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
