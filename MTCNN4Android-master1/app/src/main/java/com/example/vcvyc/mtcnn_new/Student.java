package com.example.vcvyc.mtcnn_new;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;

public class Student extends AppCompatActivity {
    private final String  TAG = "Student_Class";
    public boolean isPresent=false;
    public String Name,Surname;
    public Double result=-1d;
    public ArrayList<Bitmap> bitmaps =new ArrayList<>();
    private MainActivity main = new MainActivity();

    public Student(String Filenames[],String n,String s,MainActivity main){
        Name = n;
        Surname = s;
        this.main = main;
        loadReferenceImages(Filenames);

    }
    private void loadReferenceImages(String Filenames[]){
        try {
            for (String file : Filenames) {
                if(file.contains(Name)&&file.contains(Surname)) {
                    bitmaps.add(Utils.copyBitmap(main.readFromAssets(file)));
                }
            }
        }
        catch(Exception e){
            Log.e(TAG+Name+"_"+Surname+" ","AssetLoad:"+ e+"error");
        }
    }
    public void setMaxResult(Double r) {
        if (r > result)
            result = r;
    }

}
