package com.example.printechsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    String em;
Button button_login;
EditText email,pass;
ConstraintLayout settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        button_login=(Button)findViewById(R.id.button_login);

       email.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
       em=email.getText().toString();

//       pass.setOnKeyListener(new View.OnKeyListener() {
//           @Override
//           public boolean onKey(View v, int keyCode, KeyEvent event) {
//               if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//                       (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                   Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                   startActivity(intent);
//                   return true;
//               }
//               return true;
//           }
//       });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });
        settings=(ConstraintLayout) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
            }
        });

    }
}
