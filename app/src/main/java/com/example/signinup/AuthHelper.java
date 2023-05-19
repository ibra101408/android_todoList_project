package com.example.signinup;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


public class AuthHelper {
    private static Integer loggedInUserId;

    public static void setLoggedInUserId(Integer user_id) {
        loggedInUserId = user_id;
    }

    public static Integer getLoggedInUserId() {
        return loggedInUserId;
    }
}

/*

    private static String loggedInUserId;

    public static void setLoggedInUserId(String userId) {
        loggedInUserId = userId;
    }

    public static String getLoggedInUserId() {
        return loggedInUserId;
    }
}


*/
