package com.hector.nailnewdexter.activites

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.hector.nailnewdexter.R
import com.hector.nailnewdexter.enums.PermissionStatusEnum
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import kotlinx.android.synthetic.main.activity_main.*


//When modified colorAccent and add theme of material in style, watch this, because AppCompatActivity is for versions - 5
//The theme Material is for Android >= 5
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setButtonClicks()

    }

    private fun setButtonClicks() {
        buttonCamera.setOnClickListener { checkCameraPermissions() }
        buttonContacts.setOnClickListener { checkContactsPermissions() }
        buttonAudio.setOnClickListener { checkAudioPermissions() }
        buttonAll.setOnClickListener { checkAllPermissions() }
    }

    //Test camera permission's with Composite Dexter and using Dialog and Snack Bar to continue action for this (for Snack Bar is needed add id in container of layout)
    private fun checkCameraPermissions() = setCameraPermissionHandlerWithSnackBar() //setCameraPermissionHandlerWithDialog() //setPermissionHandler(android.Manifest.permission.CAMERA, textViewCamera)

    private fun checkContactsPermissions() = setPermissionHandler(android.Manifest.permission.READ_CONTACTS, textViewContacts)

    private fun checkAudioPermissions() = setPermissionHandler(android.Manifest.permission.RECORD_AUDIO, textViewAudio)

    private fun checkAllPermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.RECORD_AUDIO
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        //report.isAnyPermissionPermanentlyDenied //-> Return boolean if any permission is denied permanently
                        //report.areAllPermissionsGranted() //-> If all permission is granted, boolean

                        for (permission in report.grantedPermissionResponses) {
                            when (permission.permissionName) {
                                Manifest.permission.CAMERA -> setPermissionStatus(
                                    textViewCamera,
                                    PermissionStatusEnum.GRANTED
                                )
                                Manifest.permission.READ_CONTACTS -> setPermissionStatus(
                                    textViewContacts,
                                    PermissionStatusEnum.GRANTED
                                )
                                Manifest.permission.RECORD_AUDIO -> setPermissionStatus(
                                    textViewAudio,
                                    PermissionStatusEnum.GRANTED
                                )
                            }
                        }

                        for (permission in report.deniedPermissionResponses) {
                            when (permission.permissionName) {
                                Manifest.permission.CAMERA -> {
                                    if (permission.isPermanentlyDenied)
                                        setPermissionStatus(textViewCamera, PermissionStatusEnum.PERMANENTLY_DENIED)
                                    else
                                        setPermissionStatus(textViewCamera, PermissionStatusEnum.DENIED)
                                }

                                Manifest.permission.READ_CONTACTS -> {
                                    if (permission.isPermanentlyDenied)
                                        setPermissionStatus(textViewContacts, PermissionStatusEnum.PERMANENTLY_DENIED)
                                    else
                                        setPermissionStatus(textViewContacts, PermissionStatusEnum.DENIED)
                                }

                                Manifest.permission.RECORD_AUDIO -> {
                                    if (permission.isPermanentlyDenied)
                                        setPermissionStatus(textViewAudio, PermissionStatusEnum.PERMANENTLY_DENIED)
                                    else
                                        setPermissionStatus(textViewAudio, PermissionStatusEnum.DENIED)
                                }
                            }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            }).check()
    }

    private fun setPermissionHandler(permission: String, textView: TextView) {
        Dexter.withActivity(this)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    setPermissionStatus(textView, PermissionStatusEnum.GRANTED)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    //If once denied permission and is checked don't ask any more, but isn't checked, ask another time or well configure this permission for ask or not another time or many times ask
                    token.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Permission denied and denied permanently
                    if (response.isPermanentlyDenied)
                        setPermissionStatus(textView, PermissionStatusEnum.PERMANENTLY_DENIED)
                    else
                        setPermissionStatus(textView, PermissionStatusEnum.DENIED)
                }

            })
            .check()
    }

    private fun setPermissionStatus(textView: TextView, status: PermissionStatusEnum) {
        when (status) {
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

    //Composite Dexter -> Merge various "permission listener" in only one permission and use this
    private fun setCameraPermissionHandlerWithDialog() {
        //Show a dialog with text that want us, in base if permission is denied to show
        val dialogPermissionsListener = DialogOnDeniedPermissionListener.Builder
            .withContext(this)
            .withTitle("Camera Permission")
            .withMessage("Camera permission is needed to take pictures!")
            .withButtonText(android.R.string.ok)
            .withIcon(R.mipmap.ic_launcher)
            .build()

        //Permissions Manager's -> Permission Listener
        val permission = object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                setPermissionStatus(textViewCamera, PermissionStatusEnum.GRANTED)
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                token.continuePermissionRequest()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                if (response.isPermanentlyDenied)
                    setPermissionStatus(textViewCamera, PermissionStatusEnum.PERMANENTLY_DENIED)
                else
                    setPermissionStatus(textViewCamera, PermissionStatusEnum.DENIED)
            }

        }

        //Use Composite
        val composite = CompositePermissionListener(permission, dialogPermissionsListener)

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(composite)
            .check()
    }

    //Composite Dexter -> Same than previous function -> Merge various "permission listener" in only one permission and use this.. Now made with a SnackBar, remember that use SnackBar we need add id in layout container
    private fun setCameraPermissionHandlerWithSnackBar() {

        val snackbarPermissionListener =
            SnackbarOnDeniedPermissionListener.Builder.with(root, "Camera is needed to take photos! Fuck!!")
                .withOpenSettingsButton("Settings")
                .withCallback(object : Snackbar.Callback() {
                    override fun onShown(sb: Snackbar?) {
                        //Event handler for when the given Snack Bar is visible

                    }

                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        //Event handler for when the given Snack Bar been dismissed

                    }
                }).build()

        //Permissions Manager's -> Permission Listener
        val permission = object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                setPermissionStatus(textViewCamera, PermissionStatusEnum.GRANTED)
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                token.continuePermissionRequest()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                if (response.isPermanentlyDenied)
                    setPermissionStatus(textViewCamera, PermissionStatusEnum.PERMANENTLY_DENIED)
                else
                    setPermissionStatus(textViewCamera, PermissionStatusEnum.DENIED)
            }

        }

        //Use Composite
        val composite = CompositePermissionListener(permission, snackbarPermissionListener)

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(composite)
            .check()
    }
}
