package com.example.buddy2;


import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity2 extends AppCompatActivity {


    private EditText userInput;
    private Button sendButton;
    private TextView chatGPTResponse;


    // Replace with your actual OpenAI API key
    private final String OPENAI_API_KEY = "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";


    private String lastPrompt; // Variable to keep track of the last prompt


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        userInput = findViewById(R.id.userInputEditText);
        sendButton = findViewById(R.id.sendButton);
        chatGPTResponse = findViewById(R.id.responseTextView);


        // Retrieve the slider value
        int sliderValue = getIntent().getIntExtra("SliderPosition", 0);
        int maxSliderValue = 100; // Adjust this based on your SeekBar's max value


        // Generate the initial prompt
        lastPrompt = generateInitialPrompt(sliderValue, maxSliderValue);
        new ChatGPTTask().execute(lastPrompt);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = "(Background: you are a AI therapist named buddy that gives detailed responses and useful help. Don't mention your identity because it is already known. Keep your response under 30 words.) " + userInput.getText().toString();
                lastPrompt = input; // Update the last prompt with the new user input
                new ChatGPTTask().execute(input); // Execute with the new input
            }
        });
    }


    private class ChatGPTTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String prompt = strings[0];
            try {
                return callChatGPTAPI(prompt);
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            chatGPTResponse.setText(extractContentFromResponse(result));
        }
    }


    private String callChatGPTAPI(String prompt) throws IOException {
        URL url = new URL("https://api.openai.com/v1/chat/completions");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);


        String jsonPayload = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";


        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8")) {
            writer.write(jsonPayload);
            writer.flush();
        }


        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }


        return response.toString();
    }


    private String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content") + 11;
        int endMarker = response.indexOf("\"", startMarker);
        return response.substring(startMarker, endMarker);
    }


    private String generateInitialPrompt(int sliderValue, int maxSliderValue) {
        return "(Background: You are now Buddy, an AI chatbot that acts as a therapist. Don't mention the numbers provided in the prompt. Keep response under 30 words. ): [[Hi. I am feeling " + sliderValue + " out of " + maxSliderValue +
                " on a scale from bad to good. ]]";
    }
}



