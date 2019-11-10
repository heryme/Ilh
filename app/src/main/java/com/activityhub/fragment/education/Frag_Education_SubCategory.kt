package com.activityhub.fragment.education


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import com.activityhub.R
import com.activityhub.adapter.Adp_Education_Subcategory
import com.activityhub.utils.extension.isNetWork
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.model.Education_Subcategory
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.Common.DATA
import com.activityhub.utils.other.Common.ID
import com.activityhub.utils.other.Common.NAME
import com.activityhub.utils.other.EDUCATION_SUB_CATEGORY
import java.util.ArrayList
import java.util.HashMap
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.ImageView
import com.activityhub.activity.home.Act_Home
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.utils.other.hideSoftKeyboard
import com.activityhub.utils.other.showSoftKeyboard


class Frag_Education_SubCategory : Frag_Base(), ApiResponseInterface {

    private lateinit var recycler_education_subcategory: RecyclerView
    private lateinit var text_subcategory_title: TextView
    private lateinit var edit_search: EditText
    private lateinit var image_close: ImageView

    private var search_edu_subcat_list = ArrayList<Education_Subcategory.Data>()
    private var education_subcat_list: List<Education_Subcategory.Data> = ArrayList<Education_Subcategory.Data>()

    internal lateinit var adp_education_subcategory: Adp_Education_Subcategory

    var map = HashMap<String, String>()
    var category_id = ""
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_education_subcategory, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        callEducationSubCategory()

        return rootView
    }

    private fun callEducationSubCategory() {

        Log.e("callEducationSubCategor", "callEducationSubCategor = $category_id")
        if (isNetWork(activity!!)) {
            ApiRequest(activity!!,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getEducationSubcategory(
                            auth_token, category_id),
                    EDUCATION_SUB_CATEGORY, true, this)
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }

    }

    override fun initComponent() {
    }

    override fun initToolbar() {
        //(activity as Act_Home).setToolbarTitle(getString(R.string.education), true, true)
        setHasOptionsMenu(true)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListeners() {

        image_close.setOnClickListener {
            edit_search.isCursorVisible = false
            hideSoftKeyboard(activity!!)
            edit_search.setText("")
            image_close.visibility = View.GONE
            edit_search.clearFocus()
            setAdapter()
        }

        edit_search.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    edit_search.isCursorVisible = true
                    edit_search.isFocusable = true
                    edit_search.requestFocus()
                    showSoftKeyboard(activity!!)
                }
                MotionEvent.ACTION_UP -> v.performClick()
                else -> {
                }
            }
            true
        }

        edit_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(search_hint: Editable?) {
                if (search_hint!!.isEmpty()) {
                    image_close.visibility = View.GONE
                    setAdapter()
                } else {
                    setSearchData(search_hint.toString())
                    image_close.visibility = View.VISIBLE
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        edit_search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == IME_ACTION_SEARCH) {
                hideSoftKeyboard(activity!!)
                setSearchData(edit_search.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun setSearchData(search_hint: String) {

        search_edu_subcat_list.clear()

        for (i in 0 until education_subcat_list.size) {
            if (education_subcat_list[i].subCategoryName.toLowerCase().contains(search_hint.toLowerCase())) {
                search_edu_subcat_list.add(education_subcat_list[i])
            }
        }

        if (search_edu_subcat_list.isEmpty()) {
            recycler_education_subcategory.visibility = View.GONE
        } else {
            adp_education_subcategory = Adp_Education_Subcategory(search_edu_subcat_list, category_id, activity!!, this)
            recycler_education_subcategory.adapter = adp_education_subcategory
        }
    }

    private fun setAdapter() {
        if (education_subcat_list.isNotEmpty()) {
            recycler_education_subcategory.visibility = View.VISIBLE
            recycler_education_subcategory.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adp_education_subcategory = Adp_Education_Subcategory(education_subcat_list, category_id, activity!!, this)
            recycler_education_subcategory.adapter = adp_education_subcategory
        }

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        val bundle = arguments
        map = bundle?.getSerializable(DATA) as HashMap<String, String>
        category_id = map[ID]!!
        text_subcategory_title.text = map[NAME] + " " + activity!!.resources.getString(R.string.education)

    }

    override fun initIDs(rootView: View) {
        recycler_education_subcategory = rootView.findViewById(R.id.recycler_education_subcategory)
        text_subcategory_title = rootView.findViewById(R.id.text_subcategory_title)
        edit_search = rootView.findViewById(R.id.edit_search)
        image_close = rootView.findViewById(R.id.image_close)
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            EDUCATION_SUB_CATEGORY -> {
                val education_subcategory = apiResponseManager.response as Education_Subcategory

                if (education_subcategory.statusCode == 200) {
                    if (education_subcategory.data.isNotEmpty()) {
                        education_subcat_list = education_subcategory.data
                        setAdapter()
                    } else {

                    }
                }
            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.profile_item -> {
               /* (activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
                        false,
                        "",
                        false,
                        false)*/

                (activity as Act_Home).changeFragment(Frag_Account(),
                        false,
                        "",
                        false,
                        false,
                        false)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}

