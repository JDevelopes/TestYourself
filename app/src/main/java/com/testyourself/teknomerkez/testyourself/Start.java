package com.testyourself.teknomerkez.testyourself;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testyourself.teknomerkez.testyourself.Common.Common;
import com.testyourself.teknomerkez.testyourself.Model.Question;

import java.util.Collections;

import info.hoang8f.widget.FButton;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Start extends AppCompatActivity {

    FButton btnPlay;
    FirebaseDatabase database;
    DatabaseReference questions_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btnPlayEvents();

        database = FirebaseDatabase.getInstance();
        questions_reference = database.getReference("Questions");

        loadQuestions(Common.categoryID);
        String textType = "fonts/walter.ttf";
        new Calligrapher(this).setFont(this, textType, true);    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Start.this,Home.class);
        startActivity(intent);
        finish();
    }

    private void loadQuestions(String categoryID) {
        //first clear if list have old question
        if (Common.questionList.size() > 0) {
            Common.questionList.clear();
        }
        questions_reference.orderByChild("categoryID").equalTo(categoryID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Question question = postSnapshot.getValue(Question.class);
                            Common.questionList.add(question);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        //Random list
        Collections.shuffle(Common.questionList);
    }

    private void btnPlayEvents() {
        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this,Playing.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
