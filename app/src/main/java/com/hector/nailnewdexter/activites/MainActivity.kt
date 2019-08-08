package com.hector.nailnewdexter.activites

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hector.nailnewdexter.R
import com.hector.nailnewdexter.enums.PermissionStatusEnum
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

//When modified colorAccent and add theme of material in style, watch this, because AppCompatActivity is for versions - 5
//The theme Material is for Android >= 5
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setButtonClicks()

    }

    private fun setButtonClicks(){
        buttonCamera.setOnClickListener { checkCameraPermissions() }
        buttonContacts.setOnClickListener { checkContactsPermissions() }
        buttonAudio.setOnClickListener { checkAudioPermissions() }
    }

    private fun checkCameraPermissions() = setPermissionHandler(android.Manifest.permission.CAMERA, textViewCamera)

    private fun checkContactsPermissions() = setPermissionHandler(android.Manifest.permission.READ_CONTACTS, textViewContacts)

    private fun checkAudioPermissions() = setPermissionHandler(android.Manifest.permission.RECORD_AUDIO, textViewAudio)

    private fun setPermissionHandler(permission: String, textView: TextView){
        Dexter.withActivity(this)
            .withPermission(permission)
            .withListener(object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    setPermissionStatus(textView, PermissionStatusEnum.GRANTED)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    //If once denied permission and is checked don't ask any more, but isn't checked, ask another time or well configure this permission for ask or not another time or many times ask
                    token.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Permission denied and denied permanently
                    if(response.isPermanentlyDenied)
                        setPermissionStatus(textView, PermissionStatusEnum.PERMANENTLY_DENIED)
                    else
                        setPermissionStatus(textView, PermissionStatusEnum.DENIED)
                }

            })
            .check()
    }

    private fun setPermissionStatus(textView: TextView, status: PermissionStatusEnum){
        when(status){
            PermissionStatusEnum.GRANTED -> {
                textView.text = getString(R.string.permission_status_granted)
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorPermissionStatusGranted))
            }

            PermissionStatusEnum.DENIED -> {
                textView.text = getString(R.string.permission_status_denied)
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorPermissionStatusDenied))
            }

            PermissionStatusEnum.PERMANENTLY_DENIED -> {
                textView.text = getString(R.string.permission_status_denied_permanently)
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorPermissionStatusPermanentlyDenied))
            }
        }
    }
}
