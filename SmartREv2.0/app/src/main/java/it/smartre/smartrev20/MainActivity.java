package it.smartre.smartrev20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText username;
    private EditText password;
    private int counter =5;
    private TextView info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.Et3);
        password = (EditText) findViewById(R.id.Et2);
        info=(TextView)findViewById(R.id.info);
        info.setText("tentativi rimasti:5");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(username.getText().toString(), password.getText().toString());
            }
        });
    }
    private void validate(String userName, String userPassword) {

            if ((userName.equals("A")) && (userPassword.equals("a"))) {
                Intent intent = new Intent(MainActivity.this, Re.class);
                startActivity(intent);
            } else {
                counter--;
                info.setText("tentativi rimasti:" + String.valueOf(counter));
                if (counter == 0) {
                    info.setText("Riprova tra:");
                    button.setEnabled(false);
                }


            }


    }
    }

