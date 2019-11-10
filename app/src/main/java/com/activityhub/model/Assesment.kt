package com.activityhub.model

import com.google.gson.annotations.SerializedName

data class Assesment(
        @SerializedName("status_code") var statusCode: Int = 0, //200
        @SerializedName("message") var message: String = "", //Form Data
        @SerializedName("data") var data: Data = Data()
) {
    data class Data(
            @SerializedName("no_of_form") var noOfForm: Int = 0, //8
            @SerializedName("form_no") var formNo: String = "", //1
            @SerializedName("main_form") var mainForm: MainForm = MainForm()
    ) {
        data class MainForm(
                @SerializedName("form_id") var formId: Int = 0, //1
                @SerializedName("form_name") var formName: String = "", //Form 1 Test
                @SerializedName("form_sub_title") var formSubTitle: String = "", //Form Sub Title
                @SerializedName("form_information") var formInformation: String = "",
                @SerializedName("form_slug") var formSlug: String = "", //Normal
                @SerializedName("total_page_break") var totalPageBreak: Int = 0, //1
                @SerializedName("question") var question: List<Question> = listOf()
        ) {
            data class Question(
                    @SerializedName("question_id") var questionId: Int = 0, //3
                    @SerializedName("form_id") var formId: Int = 0, //1
                    @SerializedName("question_name") var questionName: String = "", //QQues3
                    @SerializedName("question_instuction") var questionInstuction: String = "",
                    @SerializedName("question_type") var questionType: String = "", //Text
                    @SerializedName("answer") var answer: List<answerData> = listOf(),
                    @SerializedName("page_no_data") var pageNoData: Int = 0 //5
            ) {
                data class answerData(
                        @SerializedName("form_answer_id") var formAnswerId: Int = 0,
                        @SerializedName("form_id") var formId: Int = 0,
                        @SerializedName("form_question_id") var formQuestionId: Int = 0,
                        @SerializedName("form_question_value") var formQuestionValue: String = "",
                        @SerializedName("form_question_value_instruction") var formQuestionValueInstruction: String = "",
                        @SerializedName("mark") var mark: String = ""
                )
            }
        }
    }
}