package com.ecnu.plantyclock.Clock.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ecnu.plantyclock.Clock.Model.AlarmModel;
import com.ecnu.plantyclock.Clock.Model.QuestionModel;
import com.ecnu.plantyclock.R;
import com.ecnu.plantyclock.Clock.activity.PlayAlarmActivity;
import com.ecnu.plantyclock.Clock.data.MyAlarmDataBase;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by ChenLu on 2021/5/24
 */
public class QuestionFragment extends Fragment {

    private TextView question_te,questionTittle,hourText,minuteText;
    private EditText answer_ed;
    private Button sure_btn;
    private String question,answer;
    private PlayAlarmActivity activity;
    private MyAlarmDataBase db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_question,container,false);
        question_te = (TextView) view.findViewById(R.id.question_text);
        answer_ed = (EditText) view.findViewById(R.id.answer_ed);
        sure_btn = (Button) view.findViewById(R.id.fra_sure_btn);
        questionTittle = (TextView) view.findViewById(R.id.textView_quesAlarm_tittle);
        hourText = (TextView) view.findViewById(R.id.time_hour_text_q);
        minuteText = (TextView) view.findViewById(R.id.time_minute_text_q);

        activity = (PlayAlarmActivity) getActivity();

        db = new MyAlarmDataBase(getActivity());
        AlarmModel alarm = db.getAlarm(activity.getmId());
        if (alarm == null){
            Log.d("ala","null");
        }
        Log.d("id", String.valueOf(activity.getmId()));


        questionTittle.setText(alarm.getTitle()+"?????????");

        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = null;

        int showHour = calendar.get(Calendar.HOUR_OF_DAY);
        int showMinute = calendar.get(Calendar.MINUTE);

        if (showMinute < 10){
            minute = "0"+ String.valueOf(showMinute);
        }else if (showHour < 10){
            hour= "0"+ String.valueOf(showHour);
        }else {
            minute = String.valueOf(showMinute);
            hour = String.valueOf(showHour);
        }

        hourText.setText(hour);
        minuteText.setText(minute);

        return view;
    }

    @Override
    public void onStart() {
        final PlayAlarmActivity activity = (PlayAlarmActivity) getActivity();

        QuestionModel qm = db.getQuestion(activity.getmId());

        if (qm == null){
            qm = db.getQuestion(activity.getmId());
        }

        question = qm.getQuestion();
        answer = qm.getAnswer();

        Log.d("QuestionFra","????????????"+answer);
        Log.d("QuestionFra", "????????????" + question);

        if (question.equals("flag_calculate")){
            getCalculateQuestion();
        }

        question_te.setText(question);

        sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("***************?????????********************");
                if (answer_ed.getText().toString().equals(answer)){

                    System.out.println("***************?????????********************");

                    activity.finish();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("????????????");
                    builder.setIcon(R.drawable.ic_error_outline_orange_800_24dp);
                    builder.setMessage("??????????????????????????????????????????");
                    builder.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "???????????? " + answer, Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        super.onStart();
    }

    private void getCalculateQuestion() {
        Random rand = new Random();
        int num1 = rand.nextInt(500)+100;
        int num2 = rand.nextInt(500)+100;
        String randNum1 = String.valueOf(num1);
        String randNum2 = String.valueOf(num2);
        question = randNum1+" + "+randNum2+ " = ?";
        answer = String.valueOf(num1+num2);

    }


}
