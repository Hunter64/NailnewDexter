package com.hector.nailnewdexter

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

//When modified colorAccent and add theme of material in style, watch this, because AppCompatActivity is for versions - 5
//The theme Material is for Android >= 5
class MainActivity : Activity() {

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonCamera.setOnClickListener { checkCameraPermissions() }
        buttonContacts.setOnClickListener { checkContactsPermissions() }
        buttonAudio.setOnClickListener { checkAudioPermissions() }
    }

    private fun checkAudioPermissions() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.RECORD_AUDIO)
            .withListener(object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    textViewAudio.text = getString(R.string.permission_status_granted)
                    textViewAudio.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusGranted))
                }

                override fun onPermissionRationaleShouldBeShown(
                    //If once denied permission and is checked don't ask any more, but isn't checked, ask another time or well configure this permission for ask or not another time or many times ask
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Permission denied and denied permanently
                    if(response.isPermanentlyDenied){
                        textViewAudio.text = getString(R.string.permission_status_denied_permanently)
                        textViewAudio.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusPermanentlyDenied))
                    }
                    else{
                        textViewAudio.text = getString(R.string.permission_status_denied)
                        textViewAudio.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusDenied))
                    }
                }

            })
            .check()
    }

    private fun checkContactsPermissions() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    textViewContacts.text = getString(R.string.permission_status_granted)
                    textViewContacts.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusGranted))
                }

                override fun onPermissionRationaleShouldBeShown(
                    //If once denied permission and is checked don't ask any more, but isn't checked, ask another time or well configure this permission for ask or not another time or many times ask
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Permission denied and denied permanently
                    if(response.isPermanentlyDenied){
                        textViewContacts.text = getString(R.string.permission_status_denied_permanently)
                        textViewContacts.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusPermanentlyDenied))
                    }
                    else{
                        textViewContacts.text = getString(R.string.permission_status_denied)
                        textViewContacts.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusDenied))
                    }
                }

            })
            .check()
    }

    private fun checkCameraPermissions() {

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    textViewCamera.text = getString(R.string.permission_status_granted)
                    textViewCamera.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusGranted))
                }

                override fun onPermissionRationaleShouldBeShown(
                    //If once denied permission and is checked don't ask any more, but isn't checked, ask another time or well configure this permission for ask or not another time or many times ask
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Permission denied and denied permanently
                    if(response.isPermanentlyDenied){
                        textViewCamera.text = getString(R.string.permission_status_denied_permanently)
                        textViewCamera.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusPermanentlyDenied))
                    }
                    else{
                        textViewCamera.text = getString(R.string.permission_status_denied)
                        textViewCamera.setTextColor(ContextCompat.getColor(context, R.color.colorPermissionStatusDenied))
                    }
                }

            })
            .check()
    }
}
