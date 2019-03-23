package com.testyourself.teknomerkez.testyourself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.testyourself.teknomerkez.testyourself.Common.Common;
import com.testyourself.teknomerkez.testyourself.Model.QuestionScore;

import info.hoang8f.widget.FButton;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Done extends AppCompatActivity {

    FButton btn_try_again;
    TextView txtResultScore, getTxtResultQuestion;
    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        //Firebase

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        init();
        btn_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        //get bundle data
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt(Common.Score);
            int totalQuestion = extra.getInt(Common.Total);
            int correctAnswer = extra.getInt(Common.Correct);

            txtResultScore.setText(String.format("SKOR : %d", score));
            getTxtResultQuestion.setText(String.format("ÇÖZEBİLDİN : %d / %d", correctAnswer, totalQuestion));

            question_score.child(String.format("%s_%s", Common.currentUser.getDisplayName(),
                    Common.categoryID))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getDisplayName(), Common.categoryID),
                            Common.currentUser.getDisplayName(),
                            String.valueOf(score),
                            Common.categoryID,
                            Common.categoryName));
        }

        String textType = "fonts/walter.ttf";
        new Calligrapher(this).setFont(this, textType, true);
    }

    private void init() {
        txtResultScore = findViewById(R.id.textTotalScore);
        getTxtResultQuestion = findViewById(R.id.textTotalQuestion);
        btn_try_again = findViewById(R.id.try_again);
    }
}
