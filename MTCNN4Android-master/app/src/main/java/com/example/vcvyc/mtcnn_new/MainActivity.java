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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_IMGS = 5;
    String TAG="MainActivity";
    ImageView imageView;
    Button RecBtn;
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
    Vector<Box> boxes;
    MTCNN mtcnn;
    public void processImage(){
        Bitmap bm= Utils.copyBitmap(bitmap);
        try {
            boxes=mtcnn.detectFaces(bm,40);
            for (int i=0;i<boxes.size();i++){
                Utils.drawRect(bm,boxes.get(i).transform2Rect());
                Utils.drawPoints(bm,boxes.get(i).landmark);
            }
            imageView.setImageBitmap(bm);
        }catch (Exception e){
            Log.e(TAG,"[*]detect false:"+e);
        }
    }
    public void myMain(){
        RecBtn = (findViewById(R.id.RecBtn));
        imageView =(findViewById(R.id.imageView));
        bitmap=readFromAssets("inputBMP.JPG");
        final String Filenames[] = {"Maximilian_Mastrogiacomo_001.jpeg","Riccardo_Pace_001.jpeg","Samuel_Leon_001.jpeg","Gianmarco_Boco_001.jpeg","Francesco_Natoli_001.jpeg"};
        TextLog = findViewById(R.id.TexLog);
        mtcnn=new MTCNN(getAssets());
        processImage();
        TextLog.append("Loaded IMG");
        RecBtn.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Double> results = new ArrayList();
                AssetManager asm=getAssets();
                Facenet fn = new Facenet(asm);
                FaceFeature ffa = fn.recognizeImage(bitmap);
                FaceFeature ffb;
                for(int i=0;i<boxes.size();i++){
                    for(String file : Filenames) {
                        ffb = fn.recognizeImage(Utils.copyBitmap(readFromAssets(file)));
                        results.add(ffa.compare(ffb));
                    }
                    double max=0;
                    int ID=0;
                    TextLog.setText("");
                    for(int j=0;j< results.size();j++){

                        TextLog.append(Integer.toString(j)+":"+Filenames[ID]+Double.toString(results.get(j))+"\n");
                        ID++;
                    }

                }
            }

        });
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
