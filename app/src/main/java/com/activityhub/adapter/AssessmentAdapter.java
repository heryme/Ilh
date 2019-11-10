package com.activityhub.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activityhub.R;
import com.activityhub.model.Assesment;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int DROPDOWN = 1, RADIO = 2, NUMBER = 3, TEXT = 4;
    private Context context;
    private List<Assesment.Data.MainForm.Question> questionList;
    private RadioTypeAdapter radioTypeAdapter;


    public AssessmentAdapter(Context context, List<Assesment.Data.MainForm.Question> questionList) {
        this.questionList = questionList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return this.questionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*if (questionList.get(position).getQuestionType().equalsIgnoreCase("Dropdown")) {
            return DROPDOWN;
        } else if (questionList.get(position).getQuestionType().equalsIgnoreCase("Radio")) {*/
        return RADIO;
        /*}
        return -1;*/
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
           /* case DROPDOWN:
                View v1 = inflater.inflate(R.layout.layout_viewholder1, viewGroup, false);
                viewHolder = new ViewHolder1(v1);
                break;*/
            case RADIO:
                View view = inflater.inflate(R.layout.row_item_assessment_radio_main_view, viewGroup, false);
                viewHolder = new RadioViewHolder(view);
                break;
         /*   default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;*/
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
           /* case DROPDOWN:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;*/
            case RADIO:
                /*ViewHolderRadioButton vh2 = (ViewHolderRadioButton) viewHolder;
                configureViewHolder2(vh2, position);*/

                RadioViewHolder radioViewHolder = (RadioViewHolder) viewHolder;
                radioViewHolder.textTitleMainView.setText(questionList.get(position).getQuestionName());
                radioTypeAdapter = new RadioTypeAdapter(context, questionList.get(position).getAnswer());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                radioViewHolder.recyclerViewRowRadioMainView.setLayoutManager(mLayoutManager);
                radioViewHolder.recyclerViewRowRadioMainView.setItemAnimator(new DefaultItemAnimator());
                radioViewHolder.recyclerViewRowRadioMainView.setAdapter(radioTypeAdapter);
                break;
         /*   default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;*/
        }
    }

    public class RadioViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewRowRadioMainView;

        private TextView textTitleMainView;

        public RadioViewHolder(View v) {
            super(v);
            recyclerViewRowRadioMainView = v.findViewById(R.id.recyclerViewRowRadioMainView);
            textTitleMainView = v.findViewById(R.id.textTitleMainView);
        }


    }


}



