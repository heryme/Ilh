package com.activityhub.fragment.auth


import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.activityhub.R
import com.activityhub.activity.auth.Act_Login
import com.activityhub.activity.auth.Act_Profile
import com.activityhub.activity.home.Act_Home
import com.activityhub.utils.extension.Toast
import com.activityhub.utils.extension.getImageRequestBody
import com.activityhub.utils.extension.isNetWork
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.other.Frag_Manage_Devices
import com.activityhub.fragment.other.Frag_Notification
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.adv_progress.CircularProgressBar
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.activityhub.utils.other.ImageUtility
import com.activityhub.utils.other.MarshMallowPermissionUtility
import com.activityhub.utils.other.STATUS_CODE
import com.activityhub.utils.other.UPLOAD_PROFILE_IMAGE
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.mikhaellopez.circularimageview.CircularImageView
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File


class Frag_Account : Frag_Base(), View.OnClickListener, ApiResponseInterface {


    private val TAG = Frag_Account::class.java.name
    private var rootView: View? = null
    private lateinit var progress_profile: CircularProgressBar
    private lateinit var ivYourAccountFragmentProfile: CircularImageView
    private lateinit var tvYourAccountFragmentNames:TextView
    private lateinit var tvYourAccountFragmentEmail:TextView
    private lateinit var tvYourAccountFragmentAccUserName:TextView
    private lateinit var tvYourAccountFragmentAccEmailAddress:TextView
    private lateinit var llYourAccountFragmentEmailAddress: RelativeLayout
    private lateinit var llYourAccountFragmentResetPassword: LinearLayout
    private lateinit var llYourAccountFragmentService:LinearLayout
    private lateinit var llYourAccountFragmentNotification:LinearLayout
    private lateinit var llYourAccountFragmentYourFragment:LinearLayout
    private lateinit var btnYourAccountFragmentLogout : Button

    private var REQUEST_CAMERA_PROFILE_PIC = 400
    private var SELECT_FILE = 100
    private lateinit var file: File


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_account, container, false)
        }

        initToolbar()
        initIDs(rootView!!)
        initComponent()
        initListeners()
        initData()
        return rootView

       /*(activity as Act_Home).setToolbarTitle("", false, false)
        return inflater.inflate(R.layout.frag_account, container, false)*/
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
    }


    override fun initComponent() {

    }

    override fun initToolbar() {
        (activity as Act_Home).setToolbarTitle("", false, false)
        setHasOptionsMenu(true)

    }

    override fun initData() {
        progress_profile.visibility = View.VISIBLE
        Glide.with(appcontroller.getAppContext())
                .load(sessionManager[getString(R.string.profileImage), ""])
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(false)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: com.bumptech.glide.request.target.Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        progress_profile.visibility = View.GONE
                        return false
                    }

                    override fun onException(e: java.lang.Exception?, model: String?, target: com.bumptech.glide.request.target.Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        ivYourAccountFragmentProfile.setImageResource(R.drawable.ic_profile)
                        progress_profile.visibility = View.GONE
                        return false
                    }

                })
                .error(R.drawable.ic_profile).into(ivYourAccountFragmentProfile)

        tvYourAccountFragmentNames.text = sessionManager[getString(R.string.name), ""]
        tvYourAccountFragmentEmail.text = sessionManager[getString(R.string.emailId), ""]

        tvYourAccountFragmentAccUserName.text = sessionManager[getString(R.string.name), ""]
        tvYourAccountFragmentAccEmailAddress.text = sessionManager[getString(R.string.emailId), ""]
    }

    override fun initIDs(rootView: View) {
        progress_profile = rootView.findViewById(R.id.progress_profile)
        ivYourAccountFragmentProfile = rootView.findViewById(R.id.ivYourAccountFragmentProfile)
        tvYourAccountFragmentNames = rootView.findViewById(R.id.tvYourAccountFragmentNames)
        tvYourAccountFragmentEmail = rootView.findViewById(R.id.tvYourAccountFragmentEmail)
        tvYourAccountFragmentAccUserName = rootView.findViewById(R.id.tvYourAccountFragmentAccUserName)
        tvYourAccountFragmentAccEmailAddress = rootView.findViewById(R.id.tvYourAccountFragmentAccEmailAddress)
        llYourAccountFragmentEmailAddress = rootView.findViewById(R.id.llYourAccountFragmentEmailAddress)
        llYourAccountFragmentResetPassword = rootView.findViewById(R.id.llYourAccountFragmentResetPassword)
        llYourAccountFragmentService = rootView.findViewById(R.id.llYourAccountFragmentService)
        llYourAccountFragmentNotification = rootView.findViewById(R.id.llYourAccountFragmentNotification)
        llYourAccountFragmentYourFragment = rootView.findViewById(R.id.llYourAccountFragmentYourFragment)
        btnYourAccountFragmentLogout = rootView.findViewById(R.id.btnYourAccountFragmentLogout)



    }

    override fun initListeners() {
        llYourAccountFragmentEmailAddress.setOnClickListener(this)
        llYourAccountFragmentResetPassword.setOnClickListener(this)
        ivYourAccountFragmentProfile.setOnClickListener(this)
        llYourAccountFragmentYourFragment.setOnClickListener(this)
        btnYourAccountFragmentLogout.setOnClickListener(this)
        llYourAccountFragmentService.setOnClickListener(this)
        llYourAccountFragmentNotification.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            llYourAccountFragmentEmailAddress -> {
               /* (activity as Act_Home).loadFragmentwithAnim(Frag_Update_Email(),
                        false, resources.getString(R.string.setting), true, false)*/

                (activity as Act_Home).changeFragment(Frag_Update_Email(),
                        false,
                        resources.getString(R.string.setting),
                        true,
                        false,
                        false)
            }

            llYourAccountFragmentResetPassword -> {
               /* (activity as Act_Home).loadFragmentwithAnim(Frag_Update_Password(),
                        false, resources.getString(R.string.setting), true, false)*/

                (activity as Act_Home).changeFragment(Frag_Update_Password(),
                        false, resources.getString(R.string.setting), true, false,false)


            }

            llYourAccountFragmentYourFragment -> {
                startActivity(Intent(activity, Act_Profile::class.java))
            }

            btnYourAccountFragmentLogout -> {
                sessionManager.logout()
                val intent = Intent(activity, Act_Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                activity!!.startActivity(intent)
                activity!!.finishAffinity()
            }

            ivYourAccountFragmentProfile -> {
                if (!MarshMallowPermissionUtility.checkPermissionForCamera(activity)) {
                    MarshMallowPermissionUtility.requestPermissionForCamera(activity)
                } else if (!MarshMallowPermissionUtility.checkPermissionForExternalStorage(activity)) {
                    MarshMallowPermissionUtility.requestPermissionForExternalStorage(activity)
                } else if (!MarshMallowPermissionUtility.checkPermissionForReadExternalStorage(activity)) {
                    MarshMallowPermissionUtility.requestPermissionForReadExternalStorage(activity)
                } else {
                    selectImage()
                }

            }

            llYourAccountFragmentService -> {
               /* (activity as Act_Home).loadFragmentwithAnim(Frag_Manage_Devices(),
                        false, resources.getString(R.string.setting), true, false)*/
                (activity as Act_Home).changeFragment(Frag_Manage_Devices(),
                        false,
                        resources.getString(R.string.setting),
                        true,
                        false,
                        true)
            }

            llYourAccountFragmentNotification -> {
               /* (activity as Act_Home).loadFragmentwithAnim(Frag_Notification(),
                        false, resources.getString(R.string.notification), true, false)*/

                (activity as Act_Home).changeFragment(Frag_Notification(),
                        false,
                        resources.getString(R.string.notification),
                        true,
                        false,
                        true)
            }
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            UPLOAD_PROFILE_IMAGE -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                Log.e(TAG, "Upload Profile Picture Response:-$responseValue")

                val response = JSONObject(responseValue)
                val statusCode = response.optInt("status_code")
                val message = response.optString("message")
                val data = response.optJSONObject("data")

                if (statusCode == STATUS_CODE) {
                    Toast(message, true, activity!!)
                    sessionManager.put(getString(R.string.profileImage), data.getString("profile_picture"))
                }
            }
        }

    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e(TAG, "requestCode-->$requestCode")
        Log.e(TAG, "resultCode-->$resultCode")
        Log.e(TAG, "Call On Activity Result---->")

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                var selectedImageUri = data?.data
                if (ImageUtility.getPath(activity, selectedImageUri) != null) {

                    file = File(ImageUtility.getPath(activity, selectedImageUri))
                    Log.e(TAG, "Image Path---->" + ImageUtility.getPath(activity, selectedImageUri))
                }

                if (selectedImageUri == null && data?.extras != null) {
                    selectedImageUri = ImageUtility.getImageUri(activity, data.extras?.get("data") as Bitmap)
                }
                Log.e(TAG, "if-->$selectedImageUri")
                if (ImageUtility.getPath(activity, selectedImageUri) != null) {
                    Log.e(TAG, "selectedImageUriRRR-->$selectedImageUri")

                    Glide.with(activity).load(selectedImageUri)
                            .error(R.mipmap.ic_launcher)
                            .into(ivYourAccountFragmentProfile)

//                    loadImage(selectedImageUri.toString(), activity!!,ivYourAccountFragmentProfile, R.mipmap.ic_launcher)

                    uploadProfilePicture()
                }


            } else if (requestCode == REQUEST_CAMERA_PROFILE_PIC) {
                var photosUri = data?.data
                if (photosUri == null && data?.extras != null) {
                    photosUri = ImageUtility.getImageUri(activity, data.extras?.get("data") as Bitmap)/*AndroidUtility.getLastCapturedImg(getContext());*/
                }

                Glide.with(activity).load(photosUri)
                        .error(R.mipmap.ic_launcher)
                        .into(ivYourAccountFragmentProfile)

//                loadImage(photosUri.toString(), activity!!,ivYourAccountFragmentProfile, R.mipmap.ic_launcher)

                file = File(ImageUtility.getRealPathFromURI(context, photosUri))
                Log.e(TAG, "Camera Photo Path---->$file")

                uploadProfilePicture()
            }

        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(getString(R.string.lbl_take_photo), getString(R.string.lbl_choose_from_gallery), getString(R.string.lbl_cancel))
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.addPhoto))
        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
            if (items[item] == getString(R.string.lbl_take_photo)) {
                cameraIntent()
            } else if (items[item] == getString(R.string.lbl_choose_from_gallery)) {
                galleryIntent()
            } else if (items[item] == getString(R.string.lbl_cancel)) {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    private fun cameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity!!.startActivityForResult(intent, REQUEST_CAMERA_PROFILE_PIC)

    }

    private fun galleryIntent() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity!!.startActivityForResult(i, SELECT_FILE)
    }

    private fun uploadProfilePicture() {
        if (isNetWork(activity!!)) {
            ApiRequest(activity!!,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).updateProfilePicture(sessionManager[AUTH_TOKEN, ""]
                            , getImageRequestBody(file.path, "profile_picture")), UPLOAD_PROFILE_IMAGE,
                    true,
                    this)
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }


    }

}

