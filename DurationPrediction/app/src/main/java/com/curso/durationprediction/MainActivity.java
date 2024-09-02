package com.curso.durationprediction;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "OkHttpExample";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String URL = "http://127.0.0.1:5001/predict";
    private OkHttpClient client;

    private EditText editTextCode, editTextLat, editTextLon, editTextDist, editTextDur;
    private Button requestButton;
    private TextView textViewRealValue;
    private TextView textViewPrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();

        editTextCode = findViewById(R.id.editTextCode);
        editTextLat = findViewById(R.id.editTextLat);
        editTextLon = findViewById(R.id.editTextLon);
        editTextDist = findViewById(R.id.editTextDist);
        editTextDur = findViewById(R.id.editTextDur);

        requestButton = findViewById(R.id.buttonRequest);

        textViewPrediction = findViewById(R.id.textViewPrediction);
        textViewRealValue = findViewById(R.id.textViewRealValue);

        addNegativeNumberTextWatcher(editTextCode);
        addNegativeNumberTextWatcher(editTextLat);
        addNegativeNumberTextWatcher(editTextLon);
        addNegativeNumberTextWatcher(editTextDist);
        addNegativeNumberTextWatcher(editTextDur);

        requestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                makePostRequest();
            }
        });
    }

    private void addNegativeNumberTextWatcher(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().startsWith("-")) {
                    if(s.length() == 1){
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);
                    } else {
                        editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                } else {
                    editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void makePostRequest() {
        try {
            int inputCod = Integer.parseInt(editTextCode.getText().toString());
            double inputLat = Double.parseDouble(editTextLat.getText().toString());
            double inputLon = Double.parseDouble(editTextLon.getText().toString());
            double inputDist = Double.parseDouble(editTextDist.getText().toString());
            double inputDur = Double.parseDouble(editTextDur.getText().toString());

            JSONObject jsonObject = new JSONObject();
            JSONArray inputArray = new JSONArray();
            inputArray.put(inputCod);
            inputArray.put(inputLat);
            inputArray.put(inputLon);
            inputArray.put(inputDist);
            jsonObject.put("input", inputArray);

            String jsonString = jsonObject.toString();

            RequestBody body = RequestBody.create(JSON, jsonString);

            textViewRealValue.setText("Real Value: " + inputDur);

            Request request = new Request.Builder()
                    .url(URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()) {
                        final String responseData = response.body().string();
                        Log.d(TAG, "Response: " + responseData);

                        try {
                            JSONObject jsonResponse = new JSONObject(responseData);
                            final double prediction = jsonResponse.getDouble("prediction");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewPrediction.setText("Prediction: " + prediction);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewPrediction.setText("Error parsing response");
                                }
                            });
                        }


                    } else {
                        Log.e(TAG, "Error: " + response.code() + " " + response.message());
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        };
    }
}
