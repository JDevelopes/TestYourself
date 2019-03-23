package com.testyourself.teknomerkez.testyourself.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.testyourself.teknomerkez.testyourself.Common.Common;
import com.testyourself.teknomerkez.testyourself.Interface.ItemClickListener;
import com.testyourself.teknomerkez.testyourself.Interface.RankingCallBack;
import com.testyourself.teknomerkez.testyourself.Model.QuestionScore;
import com.testyourself.teknomerkez.testyourself.Model.Ranking;
import com.testyourself.teknomerkez.testyourself.R;
import com.testyourself.teknomerkez.testyourself.ScoreDetail;
import com.testyourself.teknomerkez.testyourself.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {

    RecyclerView rankingList;
    View myRatingFragment;
    FirebaseDatabase database;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;
    DatabaseReference questionScore, rankingTable;
    int sum = 0 ;
    LinearLayoutManager layoutManager;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTable = database.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myRatingFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingList = myRatingFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        rankingList.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        //implementing callback
        updateScore(Common.currentUser.getDisplayName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                //update to ranking table
                rankingTable.child(ranking.getUserName()).setValue(ranking);
                showRanking(); // after upload, we will sort ranking table and show result
            }
        });

        // set adapter

        loadRankingTable();
        return myRatingFragment;
    }

    private void showRanking() {
        rankingTable.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Ranking local = data.getValue(Ranking.class);
                    Log.d("DEBUG",local.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadRankingTable() {
        Query query = rankingTable.orderByChild("score");
        FirebaseRecyclerOptions<Ranking> ranking = new FirebaseRecyclerOptions.Builder<Ranking>()
                .setQuery(query, Ranking.class).build();

        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(ranking) {
            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_ranking, parent, false);
                return new RankingViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RankingViewHolder holder, int position, @NonNull final Ranking model) {
                holder.txt_name.setText(model.getUserName());
                holder.txt_score.setText(String.valueOf(model.getScore()));

                //fixed crash when click to item

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLong) {
                        Intent scoreDetail = new Intent(getActivity(), ScoreDetail.class);
                        scoreDetail.putExtra("viewUser", model.getUserName());
                        startActivity(scoreDetail);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
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

    private void updateScore(final String userName, final RankingCallBack<Ranking> callBack) {
        questionScore.orderByChild("user")
                .equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    QuestionScore ques = data.getValue(QuestionScore.class);
                    sum += Integer.parseInt(ques.getScore());

                }
                //After sumary all score, we need process sum variable here
                //Because firebase is async db, so if process is outside, our sum will be reset to 0
                Ranking ranking = new Ranking(sum, userName);
                callBack.callBack(ranking);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
