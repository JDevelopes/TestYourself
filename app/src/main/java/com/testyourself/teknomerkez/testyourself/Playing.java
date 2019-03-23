package com.testyourself.teknomerkez.testyourself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.testyourself.teknomerkez.testyourself.Common.Common;

import info.hoang8f.widget.FButton;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Playing extends AppCompatActivity implements View.OnClickListener {


    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;

    ImageView question_image;
    FButton answerA, answerB, answerC, answerD;
    TextView textScore, textQuestionNum, question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        init();
        answerA.setOnClickListener(this);
        answerB.setOnClickListener(this);
        answerC.setOnClickListener(this);
        answerD.setOnClickListener(this);

        String textType = "fonts/walter.ttf";
        new Calligrapher(this).setFont(this, textType, true);
    }

    @Override
    public void onClick(View v) {

        if (index < totalQuestion) {
            // still we have question to answer
            Button clickedButton = (Button) v;
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {
                // choose correct answer
                score += 10;
                correctAnswer++;
                showQuestion(++index); // next question
            } else {
                //wrong answer
                Intent wrongAnswer = new Intent(this, Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt(Common.Score, score);
                dataSend.putInt(Common.Total, totalQuestion);
                dataSend.putInt(Common.Correct, correctAnswer);
                wrongAnswer.putExtras(dataSend);
                startActivity(wrongAnswer);
                finish();
            }
            textScore.setText(String.format("%d", score));
        }
    }

    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            textQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {
                //if there is image in question
                Picasso picasso = Picasso.get();
                picasso.load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            } else {
                question_text.setText(Common.questionList.get(index).getQuestion());
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }
            answerA.setText(Common.questionList.get(index).getAnswerA());
            answerB.setText(Common.questionList.get(index).getAnswerB());
            answerC.setText(Common.questionList.get(index).getAnswerC());
            answerD.setText(Common.questionList.get(index).getAnswerD());
        } else {
            Intent timeIsDone = new Intent(this, Done.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Common.Score, score);
            bundle.putInt(Common.Total, totalQuestion);
            bundle.putInt(Common.Correct, correctAnswer);
            timeIsDone.putExtras(bundle);
            startActivity(timeIsDone);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        totalQuestion = Common.questionList.size();
        showQuestion(index);
    }

    private void init() {
        textScore = findViewById(R.id.textScore);
        textQuestionNum = findViewById(R.id.totalQuestion);
        question_text = findViewById(R.id.question_text);
        question_text.setMovementMethod(new ScrollingMovementMethod());
        question_image = findViewById(R.id.question_image);
        answerA = findViewById(R.id.btnAnswerA);
        answerB = findViewById(R.id.btnAnswerB);
        answerC = findViewById(R.id.btnAnswerC);
        answerD = findViewById(R.id.btnAnswerD);
    }
}
