package com.example.buddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.StrictMode;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;


public class FriendDetails extends AppCompatActivity {

    TextView friendName, friendDob, friendPhone, friendGender, friendEmail;
    Button btnEdit, btnMessage;
    Friend friend;
    ActivityResultLauncher<Intent> editFriendLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);

        // Initialize views
        friendName = findViewById(R.id.friendName);
        friendDob = findViewById(R.id.friendDob);
        friendPhone = findViewById(R.id.friendPhone);
        friendGender = findViewById(R.id.friendGender);
        friendEmail = findViewById(R.id.friendEmail);
        btnEdit = findViewById(R.id.btnEdit);

        // Get the Friend object from Intent
        Intent intent = getIntent();
        friend = (Friend) intent.getSerializableExtra("friend");

        // Null check
        if (friend == null) {
            finish(); // don't continue
            return;
        }

        showFriendDetails(friend);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        editFriendLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String action = result.getData().getStringExtra("action");
                        if ("deleted".equals(action)) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("action", "deleted");
                            setResult(RESULT_OK, resultIntent);
                            finish();
                            return;
                        }
                        // Friend was updated — reload from DB
                        DatabaseHelper db = new DatabaseHelper(FriendDetails.this);
                        friend = db.getFriendById(friend.getId());
                        if (friend != null) {
                            showFriendDetails(friend);
                        }
                    }
                }
        );



        btnEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(FriendDetails.this, updateNDelete.class);
            editIntent.putExtra("friend", friend);
            editFriendLauncher.launch(editIntent);
        });

        btnMessage = findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(v -> {
            String phoneNumber = friend.getPhone();
            String prompt = "Write a 150 words, sweet birthday message for my friend named " + friend.getName() + "and the date is"+ friend.getDob()+"only take the month and day.";

            new Thread(() -> {
                try {
                    String apiKey = getString(R.string.gemini_api_key);

                    OkHttpClient client = new OkHttpClient();

                    // Construct request
                    JSONObject textPart = new JSONObject();
                    textPart.put("text", prompt);

                    JSONArray parts = new JSONArray();
                    parts.put(textPart);

                    JSONObject content = new JSONObject();
                    content.put("role", "user"); // ✅ ADD THIS LINE
                    content.put("parts", parts);

                    JSONArray contents = new JSONArray();
                    contents.put(content);

                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("contents", contents);

                    RequestBody body = RequestBody.create(
                            jsonRequest.toString(),
                            MediaType.get("application/json")
                    );

                    Request request = new Request.Builder()
                            .url("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey)
                            .post(body)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();

                            JSONObject obj = new JSONObject(responseBody);
                            String message = obj
                                    .getJSONArray("candidates")
                                    .getJSONObject(0)
                                    .getJSONObject("content")
                                    .getJSONArray("parts")
                                    .getJSONObject(0)
                                    .getString("text");

                            runOnUiThread(() -> {
                                String url = "https://wa.me/+6" + phoneNumber + "?text=" + Uri.encode(message);
                                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent1);
                            });

                        } else {
                            String errorResponse = response.body().string();
                            Log.e("GeminiAPI", "Error: " + errorResponse);
                            runOnUiThread(() -> Toast.makeText(this, "Gemini error: " + errorResponse, Toast.LENGTH_LONG).show());
                        }
                    }

                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    e.printStackTrace();
                }
            }).start();
        });



    }

    private void showFriendDetails(Friend f) {
        friendName.setText(f.getName());
        friendDob.setText("Born at " + f.getDob());
        friendPhone.setText(f.getPhone());
        friendGender.setText(f.getGender());
        friendEmail.setText(f.getEmail());
    }

}
