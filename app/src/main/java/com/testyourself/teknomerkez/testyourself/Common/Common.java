package com.testyourself.teknomerkez.testyourself.Common;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.testyourself.teknomerkez.testyourself.Model.Question;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Common {

    //intent
    public static String Category = "Category";
    public static String Score = "SCORE";
    public static String Total = "TOTAL";
    public static String Correct = "CORRECT";
    //values
    public static String categoryID, categoryName;
    public static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public static String STR_NOTİFİCATİON = "push_Notification";
    public static Context context;

    //global question list
    public static List<Question> questionList = new ArrayList<>();


}
