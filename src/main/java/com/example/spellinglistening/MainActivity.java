package com.example.spellinglistening;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String word;
    private char letters[];
    private TextToSpeech textToSpeech;
    private String currentWord;
    public static int guessCounter = 0;
    public static int failedCounter = 0;
    private EditText editWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // if No error is found then only it will run
                if (status != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        letters = new char[36];
        int counter = 48;
        for (
                int i = 0;
                i < 36; i++) {
            if ((i + 97) < 123) {
                letters[i] = (char) (i + 97);
            } else {
                letters[i] = (char) counter;
                counter++;
            }
        }

        createWord();

        System.out.println(String.valueOf(letters));
        System.out.println(word);

        setContentView(R.layout.activity_main);

        editWord = findViewById(R.id.edit_word);
        editWord.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    checkResultHelper();
                    return true;
                }
                // Return true if you have consumed the action, else false.
                return false;
            }
        });

    }

    public void refreshWord(View view) {
        createWord();
    }

    public void playWord(View view) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void createWord() {
        Random random = new Random();

        int word_size = java.util.concurrent.ThreadLocalRandom.current().nextInt(4, 16);

        word = "";
        currentWord = "";

        for (int i = 0; i < word_size; i++) {
            int value = random.nextInt(letters.length);
            System.out.println("value: " + value);
            word += letters[value];
            currentWord += letters[value];
            if(i != word_size - 1)
                word+= ",";
        }
    }

    public void checkResult(View view) {
        checkResultHelper();
    }

    private void checkResultHelper(){
        String resultTitle = "";
        String currentGuess = editWord.getText().toString();

        if(currentGuess.isEmpty()){
            return;
        }

        boolean isAGuess = currentWord.equalsIgnoreCase(currentGuess);
        resultTitle = isAGuess ? "Correct" : "Incorrect";

        guessCounter += isAGuess ? 1 : 0;
        failedCounter += isAGuess ? 0 : 1;

        ((TextView)findViewById(R.id.guessedCounter)).setText(String.valueOf(guessCounter));
        ((TextView)findViewById(R.id.failedCounter)).setText(String.valueOf(failedCounter));

        String resultMessage = "Your word was: " + currentGuess;
        resultMessage += isAGuess ? " your answer is correct." : " your answer is incorrect the word was " + currentWord;

        new AlertDialog.Builder(this)
                .setTitle(resultTitle)
                .setMessage(resultMessage)
                .setPositiveButton("OK", null)
                .setIcon(isAGuess ? android.R.drawable.ic_dialog_info :  android.R.drawable.ic_dialog_alert)
                .show();

        editWord.setText("");
        createWord();
    }
}
