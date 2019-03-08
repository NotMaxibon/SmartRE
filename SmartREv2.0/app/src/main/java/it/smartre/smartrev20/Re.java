package it.smartre.smartrev20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Re extends AppCompatActivity {
    private static final int CAM_REQUEST = 1313;
    ImageButton button;
    ImageButton btn89;
    ImageView imgg;
    ImageButton btn80;
    ImageButton btn84;
    String frase = "aaaa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re);
        button = (ImageButton) findViewById(R.id.imageButton2);
        btn89 = (ImageButton) findViewById(R.id.imageButton3);
        btn84 = (ImageButton) findViewById(R.id.imageButton4);
        btn80 = (ImageButton) findViewById(R.id.imageButton5);
        imgg = (ImageView) findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Re.this,gallery.class);
                startActivity(intent);
            }
        });
        btn89.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Re.this,Main2Activity.class);
                startActivity(intent);
            }
        });
        btn80.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Re.this,user.class);
                startActivity(intent);
            }
        });


       btn84.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Re.this,save.class);
               startActivity(intent);
           }
       });

    }

}


