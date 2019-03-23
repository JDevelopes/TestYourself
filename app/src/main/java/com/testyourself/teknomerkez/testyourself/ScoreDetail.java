package com.testyourself.teknomerkez.testyourself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.testyourself.teknomerkez.testyourself.Common.Common;
import com.testyourself.teknomerkez.testyourself.Model.QuestionScore;
import com.testyourself.teknomerkez.testyourself.ViewHolder.ScoreDetailViewHolder;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ScoreDetail extends AppCompatActivity {

    String viewUser = "";
    Context context = this;
    FirebaseDatabase database;
    DatabaseReference question_score;
    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        // view
        scoreList = findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);


        if (getIntent() != null) {
            viewUser = getIntent().getStringExtra("viewUser");
        }
        if (!viewUser.isEmpty()) {
            loadScoreDetail(viewUser);
        }
        String textType = "fonts/walter.ttf";
        new Calligrapher(this).setFont(this, textType, true);
    }

    private void loadScoreDetail(String viewUser) {
        Query query = question_score.orderByChild("user").equalTo(viewUser);
        FirebaseRecyclerOptions<QuestionScore> options = new FirebaseRecyclerOptions.Builder<QuestionScore>()
                .setQuery(query, QuestionScore.class).build();
        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ScoreDetailViewHolder holder, int position, @NonNull QuestionScore model) {
                holder.txt_category_name.setText(model.getCategoryName());
                holder.txt_category_score.setText(model.getScore());
            }

            @NonNull
            @Override
            public ScoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.score_detail_layout, parent, false);
                return new ScoreDetailViewHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}
