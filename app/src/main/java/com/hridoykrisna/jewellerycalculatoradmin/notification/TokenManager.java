package com.hridoykrisna.jewellerycalculatoradmin.notification;

import android.content.Context;
import android.os.AsyncTask;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.InputStream;
import java.util.Collections;

public class TokenManager {
    private static final String TAG = "TokenManager";
    private static final String SCOPE = "https://www.googleapis.com/auth/firebase.messaging";

    // Define a callback interface to return the token asynchronously
    public interface TokenCallback {
        void onTokenReceived(String token);
        void onError(Exception e);
    }

    // Fetch the access token asynchronously
    public static void getAccessToken(Context context, TokenCallback callback) {
        new GetTokenTask(context, callback).execute();
    }

    // AsyncTask to perform network operations in the background
    private static class GetTokenTask extends AsyncTask<Void, Void, String> {
        private final Context context;
        private final TokenCallback callback;
        private Exception exception;

        GetTokenTask(Context context, TokenCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                InputStream stream = context.getAssets().open("service-account-file.json");
                GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                        .createScoped(Collections.singletonList(SCOPE));
                credentials.refreshIfExpired();
                return credentials.getAccessToken().getTokenValue();
            } catch (Exception e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String token) {
            if (exception != null) {
                callback.onError(exception);
            } else {
                callback.onTokenReceived(token);
            }
        }
    }
}
