package com.activityhub.fragment.results


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.AssessmentAdapter
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.model.Assesment
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.other.ASSESSMENT_LIST
import com.activityhub.utils.other.STATUS_CODE


class Frag_Assessment : Frag_Base(), View.OnClickListener, ApiResponseInterface {

    private lateinit var assessmentAdapter: AssessmentAdapter

    private var assessmentList: ArrayList<Assesment.Data.MainForm.Question> = ArrayList()

    private var formNo: Int = 1
    private var pageNo: Int = 1
    var rootView: View? = null

    private lateinit var recycler_assessment: RecyclerView
    private lateinit var text_page_count: TextView
    private lateinit var text_form_title: TextView
    private lateinit var text_page_next: TextView
    private lateinit var layout_next: LinearLayout
    private lateinit var layout_progress: FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_assessment, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        callAssessmentListAPI()

        return rootView
    }

    override fun onClick(view: View?) {
        when (view) {
            layout_next -> {
                callAssessmentListAPI()
            }
        }
    }

    override fun initComponent() {

    }

    override fun initToolbar() {
        setHasOptionsMenu(true)
    }

    override fun initListeners() {
        layout_next.setOnClickListener(this)
        recycler_assessment.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
    }

    override fun initData() {

        assessmentAdapter = AssessmentAdapter(activity!!, assessmentList)
        recycler_assessment.adapter = assessmentAdapter

    }

    override fun initIDs(rootView: View) {
        recycler_assessment = rootView.findViewById(R.id.recycler_assessment)
        text_page_count = rootView.findViewById(R.id.text_page_count)
        text_form_title = rootView.findViewById(R.id.text_form_title)
        text_page_next = rootView.findViewById(R.id.text_page_next)
        layout_next = rootView.findViewById(R.id.layout_next)
        layout_progress = rootView.findViewById(R.id.layout_progress)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
              /*  (activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
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

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            ASSESSMENT_LIST -> {
                val assesmentModel = apiResponseManager.response as Assesment

                initFormData(assesmentModel)

                if (assesmentModel.statusCode == STATUS_CODE) {
                    assessmentList.addAll(assesmentModel.data.mainForm.question)
                    assessmentAdapter.notifyDataSetChanged()
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initFormData(assesmentModel: Assesment) {
        formNo = assesmentModel.data.formNo.toInt()
        if (pageNo == assesmentModel.data.mainForm.totalPageBreak + 1) {
            text_page_next.text = activity!!.resources.getString(R.string.done)
            formNo += 1
        }
        pageNo += 1

        text_form_title.text = assesmentModel.data.mainForm.formName
        text_page_count.text = "Page " + assesmentModel.data.formNo + " of " + assesmentModel.data.noOfForm
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        showSnackBar(error_message)
    }

    private fun callAssessmentListAPI() {

        if (isNetWork(activity!!)) {
            ApiRequest(activity!!,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getAssessmentList(
                            auth_token,
                            formNo,
                            pageNo),
                    ASSESSMENT_LIST, true, this)
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }

    }


}

