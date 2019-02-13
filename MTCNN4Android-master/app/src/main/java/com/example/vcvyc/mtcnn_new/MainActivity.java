package com.example.vcvyc.mtcnn_new;
/*
  MTCNN For Android
  by cjf@xmu 20180625
 */
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    String TAG="MainActivity";
    ImageView imageView;
    Bitmap bitmap;
    TextView TextLog;
    private  Bitmap readFromAssets(String filename){
        Bitmap bitmap;
        AssetManager asm=getAssets();
        try {
            InputStream is=asm.open(filename);
            bitmap= BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            Log.e("MainActivity","[*]failed to open "+filename);
            e.printStackTrace();
            return null;
        }
        return Utils.copyBitmap(bitmap); //Return mutable image
    }
    FaceFeature ff;
    Facenet fn;
    MTCNN mtcnn;
    public void processImage(){
        Bitmap bm= Utils.copyBitmap(bitmap);
        try {
            Vector<Box> boxes=mtcnn.detectFaces(bm,40);
            for (int i=0;i<boxes.size();i++){
                Utils.drawRect(bm,boxes.get(i).transform2Rect());
                Utils.drawPoints(bm,boxes.get(i).landmark);
            }
            fn.recognizeImage(bm);
            imageView.setImageBitmap(bm);
            if(TextLog.getLineCount()>=10){
                TextLog.clearComposingText();
            }
            TextLog.append("Loaded IMG/n");
        }catch (Exception e){
            Log.e(TAG,"[*]detect false:"+e);
        }
    }
    public void myMain(){
        imageView =(ImageView)findViewById(R.id.imageView);
        bitmap=readFromAssets("inputBMP.JPG");
        TextLog = findViewById(R.id.TexLog);
        mtcnn=new MTCNN(getAssets());
        processImage();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, 0x1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(data==null)return;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            processImage();
        }catch (Exception e){
            Log.d("MainActivity","[*]"+e);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMain();
    }
}
