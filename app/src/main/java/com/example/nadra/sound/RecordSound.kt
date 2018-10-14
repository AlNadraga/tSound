package com.example.nadra.sound

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_record_sound.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

//permission code
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecordSound : AppCompatActivity() {
    private var filename: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var permissionToRecord = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    //request for RecordPermission
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecord = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecord) finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_sound)
        filename = "${externalCacheDir.absolutePath}/record.3gp"
        ActivityCompat.requestPermissions(this,permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        textView3.text = filename
    }

    //Create Filename by date
    private fun createFileName():String{
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return "REC_${timestamp}"
    }

    //release the media recorder, set record param and start record
    fun recordStart(v:View) {
        val outFile: File = File(filename)
        if (outFile.exists()) {
            outFile.delete()
        }
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(filename)
            try {
                prepare()
            } catch (e: IOException){
                Log.e("RecordTest","<====prepare() failed=====>")
            }
            start()
        }
    }

    //release recorder and stop recording
    fun recordStop (v:View){
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    //set path to the file to listening and start playing
    fun playStart (v:View){
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(filename)
                prepare()
                start()
            } catch (e: IOException){
                Log.e("RecordTest","<====prepare() failed ====>")
            }
        }
    }
    //stop playing file
    fun playStop (v:View){
        mediaPlayer?.release()
        mediaPlayer = null
    }

}
