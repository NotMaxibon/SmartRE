
/*
  MTCNN For Android
  by cjf@xmu 20180625
 */
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

public class MainActivity extends AppCompatActivity {
    private static final int NUM_FACES = 5;
    public MainActivity main = this;
    String TAG="MainActivity";
    List<Bitmap> Base = new ArrayList<Bitmap>();
    List<Student> Students = new ArrayList<Student>();
    ImageView imageView;
    Button RecBtn;
    Bitmap bitmap;
    TextView TextLog;
    Vector<Box> boxes;
    MTCNN mtcnn;
	//Metodo principale,prende come parametro l'immagine da analizzare
    public void MyMain(Bitmap img){
        bitmap= img;
		//genera le classi di tutti gli studenti registrati
        final String Filenames[] = {"Maximilian_Mastrogiacomo_001.jpeg","Maximilian_Mastrogiacomo_002.JPG","Maximilian_Mastrogiacomo_003.jpg","Maximilian_Mastrogiacomo_004.jpg","Riccardo_Pace_001.jpeg","Riccardo_Pace_002.jpg","Riccardo_Pace_003.jpg","Riccardo_Pace_004.jpg","Samuel_Leon_001.jpeg","Samuel_Leon_002.jpg","Samuel_Leon_003.jpg","Samuel_Leon_004.jpg","Gianmarco_Boco_001.jpeg","Gianmarco_Boco_003.jpg","Gianmarco_Boco_004.jpg","Francesco_Natoli_001.jpeg","Francesco_Natoli_002.jpg","Francesco_Natoli_003.jpg","Francesco_Natoli_004.jpg"};
        mtcnn=new MTCNN(getAssets());
        Students.add(new Student(Filenames,"Maximilian","Mastrogiacomo",main));
        Students.add(new Student(Filenames,"Riccardo","Pace",main));
        Students.add(new Student(Filenames,"Samuel","Leon",main));
        Students.add(new Student(Filenames,"Gianmarco","Boco",main));
        Students.add(new Student(Filenames,"Francesco","Natoli",main));
		//individua le facce nell'immagine da anlizzare 
        processImage();
        try {
            AssetManager asm = getAssets();
            Facenet fn = new Facenet(asm);
            FaceFeature ffa;
            FaceFeature ffb;
            Base.clear();
            int box_count=0;
			//ritaglia tutte le facce individuate
            for (Box b : boxes) {
                if(box_count<NUM_FACES) {
                    Base.add(crop_res(Utils.copyBitmap(bitmap), 48, b));
                    box_count++;
                }
            }
            Student winner = new Student(Filenames, " ", " ",main);
			//Per tutte le facce individuate riconosce a quale delle classi studente può appartenere
            for (int i = 0; i < Base.size(); i++) {
                Double max = -1d;
                for (Student S : Students) {
                    ffa = fn.recognizeImage(Base.get(i));
                    for (Bitmap bm : S.bitmaps) {
                        ffb = fn.recognizeImage(bm);//rallenta
                        S.setMaxResult(ffa.compare(ffb));
                    }
                    if (S.result > max) {
                        max = S.result;
                        winner = S;
                    }
                }
				//segna lo studente che e stato individuato come presente
                winner.isPresent = true;
                cleanStudents();
            }
        }
        catch(Exception e)
        {
            Log.e(TAG, "Recognisation:" + e + "error");
        }
    }
    //funzione che individua le facce e ci disegna dei rettangoli rossi intorno
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
    //funzione che ritaglia la faccia individuata dall BOX dall'immagine
	private Bitmap crop_res(Bitmap bitmapIn,int size,Box box){
        Matrix matrix = new Matrix();
        float scale=1.0f*size/box.width();
        matrix.postScale(scale, scale);
        Bitmap croped=Bitmap.createBitmap(bitmapIn, box.left(),box.top(),box.width(), box.height(),matrix,true);
        return croped;
    }
	//Funzione che rimuove gli studenti che sono già stati segnati come presenti
    private void cleanStudents() {
        for (int i=0;i<Students.size();i++) {
            if (Students.get(i).isPresent) {
                Students.remove(i);
            }
        }
    }
	//Funzione che legge un file dagli asset dato il suo nome (funziona solo nell'activity principale,POTREBBE DARE ERRORE)
	/*Nel caso dia errore bisogna:
		1.mettere questa funzione nella classe principale del programma.
		2.passare al costruttore di questa classe la classe principale del programma.
		3.aggiungere la classe principale tutte le volte che viene usato readFromAssets() 
		ES. var = readFromAssets(filename) -> var = MainClass.readFromAssets(filename)
	*/
    public Bitmap readFromAssets(String filename){
        Bitmap bitmap;
        AssetManager asm=getAssets();
        try {
            InputStream is=asm.open(filename);
            bitmap= BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            Log.e("ONCreate","[*]failed to open "+filename);
            e.printStackTrace();
            return null;
        }
        return Utils.copyBitmap(bitmap); //Return mutable image
    }
    }
