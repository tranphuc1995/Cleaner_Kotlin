package com.tranphuc.cleanerkt.activities

import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.tranphuc.cleanerkt.R
import com.tranphuc.cleanerkt.adapter.JunkFilesAdapter
import com.tranphuc.cleanerkt.global.MethodStatic
import com.tranphuc.cleanerkt.model.ItemHeaderJunkFiles
import kotlinx.android.synthetic.main.activity_junk_clean.*
import kotlinx.android.synthetic.main.layout_ram_storage_cpu.*
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
    private var totalSize: Long = 0
    private var totalSizeJunkFile: Long = 0
    private var totalSizeJunkImage: Long = 0
    private var totalSizeJunkVideo: Long = 0
    private var totalSizeJunkApk: Long = 0
    private lateinit var junkImage: ItemHeaderJunkFiles
    private lateinit var junkVideo: ItemHeaderJunkFiles
    private lateinit var junkApk: ItemHeaderJunkFiles
    private lateinit var junkFiles: ItemHeaderJunkFiles
    private var colorRed: Int = 0
    private var colorGreen: Int = 0
    private var colorBlue: Int = 0
    private var count: Long = 0
    // recyclerview
    private var listJunkFiles: MutableList<ItemHeaderJunkFiles> = ArrayList();
    private lateinit var junkFilesAdapter: JunkFilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_junk_clean)


        junkImage = ItemHeaderJunkFiles()
        junkImage.Title = "Hình ảnh"
        junkVideo = ItemHeaderJunkFiles()
        junkVideo.Title = "Video"
        junkApk = ItemHeaderJunkFiles()
        junkApk.Title = "Apk"
        junkFiles = ItemHeaderJunkFiles()
        junkFiles.Title = "File rác"

        //   initRvJunkFiles()

        if (isPermissionGranted(PERMISSION_READ_STROGARE)) {
            Thread(Runnable {
                getListFiles(File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath()))
            }).start()
        } else { // xin quyền
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_READ_STROGARE), REQUEST_CODE);
        }

    }


    private fun getListFiles(parentDir: File) {
        //  val inFiles: MutableList<File> = ArrayList<File>()
        val files: Queue<File> = LinkedList<File>() as Queue<File>
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
                this@JunkCleanActivity.runOnUiThread(java.lang.Runnable {
                    if (file.absolutePath.contains(".")) {
                        /* if (isJunkFile(file.absolutePath.substring(file.absolutePath.lastIndexOf(".")).replace(".", ""))) {*/
                        var extension = file.absolutePath.substring(file.absolutePath.lastIndexOf(".")).replace(".", "")
                        colorRed = Random().nextInt(256) // random between 0->255
                        colorGreen = Random().nextInt(256)
                        colorBlue = Random().nextInt(256)
                        totalSize += file.length()
                        var total = MethodStatic.convertBytesToGb(totalSize)
                        var arrayTotal = total.split(" ")
                        textview_quantity.setText("" + arrayTotal[0])
                        textview_unit.setText("" + arrayTotal[1])
                        relative_background.setBackgroundColor(Color.rgb(colorRed, colorGreen, colorBlue))
                        if (extension.equals("png")) {
                            totalSizeJunkImage += file.length()
                        } else if (extension.equals("mp4")) {
                            totalSizeJunkVideo += file.length()
                        } else if (extension.equals("apk")) {
                            totalSizeJunkApk += file.length()
                        } else {
                            totalSizeJunkFile += file.length()
                        }
                    //    inFiles.add(file)
                        //  }
                    }
                })
            }
        }
        try {
            Thread.sleep(50)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        /*this@JunkCleanActivity.runOnUiThread {
            java.lang.Runnable {
                relative_background.setBackgroundColor(Color.rgb(255, 0, 0))
                junkImage.Total = MethodStatic.convertBytesToGb(totalSizeJunkImage)
                junkVideo.Total = MethodStatic.convertBytesToGb(totalSizeJunkVideo)
                junkApk.Total = MethodStatic.convertBytesToGb(totalSizeJunkApk)
                junkFiles.Total = MethodStatic.convertBytesToGb(totalSizeJunkFile)
                listJunkFiles.add(junkImage)
                listJunkFiles.add(junkVideo)
                listJunkFiles.add(junkApk)
                listJunkFiles.add(junkFiles)
                initRvJunkFiles()
            }
        }*/

        handler.post(Runnable {
            relative_background.setBackgroundColor(Color.rgb(255, 0, 0))
            junkImage.Total = MethodStatic.convertBytesToGb(totalSizeJunkImage)
            junkVideo.Total = MethodStatic.convertBytesToGb(totalSizeJunkVideo)
            junkApk.Total = MethodStatic.convertBytesToGb(totalSizeJunkApk)
            junkFiles.Total = MethodStatic.convertBytesToGb(totalSizeJunkFile)
            listJunkFiles.add(junkImage)
            listJunkFiles.add(junkVideo)
            listJunkFiles.add(junkApk)
            listJunkFiles.add(junkFiles)
            initRvJunkFiles()
        })
       // return inFiles
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
                     getListFiles(File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath()))
                    Toast.makeText(this, "" + listJunkFile.size, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isJunkFile(extension: String): Boolean {
        var isJunkfile = false
        val listExtensionJunkFiles = arrayOf(
                "tmp", "bak", "log", "cache", "part", "crdownload", "cvr", "download", "mxdl"
                , "thumbdata", "egt", "csi", "etl", "csd", "tt2", "laccdb", "regtrans-ms"
                , "ewc2", "thumbdata5", "lck", "blf", "sav", "filepart", "adadownload"
                , "dtapart", "sfk", "h64", "lock", "rra", "tec", "imgcache", "steamstart"
                , "bc", "mmsyscache", "partial", "temp", "chkn", "hex,", "pnf", "waf", "tmt"
                , "reapeaks", "rec", "idlk", "swo", "dap", "pft", "little", "glh", "bu", "pkf"
                , "installstate", "fsf", "dmp", "db-wal", "msj", "db-shm", "tv5", "box", "exd", "dat"
                , "rsc_tmp", "onecache", "objectcache", "swn", "cfa", "temporaryitems", "nov"
                , "id3tag", "indexarrays", "bdm", "dca", "Isl", "rld", "wfm", "ptn2", "nb2", "cah"
                , "aso", "dia", "Cos2", "heu", "tof", "meb", "bridgecachet", "thumbdata3", "rsx", "snapdoc"
                , "hrd", "prmdc", "rdn", "ims", "tmd", "phc", "clp", "fuse_hidden", "hax", "ytf"
                , "bsd", "cache-3", "tic", "indexpositions", "cdc", "thumbdata4", "init", "peb", "lai"
                , "bmc", "utpartv", "bom", "adm", "dtf", "bdi", "buf", "pm$", "inprogress", "bmb"
                , "00a", "dinfo", "pkc", "wov", "xp", "m_p", "mbc", "crc", "csstore", "md0", "wcc", "bridgecache", "mex", "ci", "qbt"
        )
        for (i in 0..(listExtensionJunkFiles.size - 1)) {
            if (extension.equals(listExtensionJunkFiles.get(i).toString())) {
                isJunkfile = true
            }
        }
        return isJunkfile
    }


    private fun initRvJunkFiles() {
        var layoutmanager = LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        recyclerview_junk_files.layoutManager = layoutmanager
        junkFilesAdapter = JunkFilesAdapter(listJunkFiles, this)
        recyclerview_junk_files.adapter = junkFilesAdapter
    }
}
