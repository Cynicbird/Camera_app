package com.example.projet_x3;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by mear on 03/03/18.03/03/18
 */

public class Main2 extends AppCompatActivity{

    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final String image = intent.getExtras().getString("Nom_photo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagee);
        final ImageView Myimage = (ImageView)findViewById(R.id.image_seul);
        final ImageView mask = (ImageView) findViewById(R.id.image_top);





        String here = Environment.getExternalStorageDirectory()+"/NOTMINE/"+image;
        final Bitmap myBitmap = BitmapFactory.decodeFile(here);
        Myimage.setImageBitmap(myBitmap);

//Crée une image bit map puis puis ajoute un point
        mask.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    float x = (float) (event.getX());
                    float y = (float) (event.getY());

                Bitmap imageBitmap = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(imageBitmap);
                float scale = getResources().getDisplayMetrics().density;
                Paint p = new Paint();
                p.setColor(Color.RED);
                p.setTextSize(50*scale);
                canvas.drawText(".",x,y, p);
                mask.setImageBitmap(imageBitmap);
                    fk(x, y, image);

                }
                return true;
            }
            ; });
        Myimage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    float x = (float) (event.getX());
                    float y = (float) (event.getY());
                    Toast toast = Toast.makeText(getApplicationContext(), "Mon x est :"+y+"Mon y est : "+x, Toast.LENGTH_SHORT); toast.show();
                    //takePicture();
                                             //smooth edge

                }
                return true;
            }
        });

    }
//ajout de point
    public void AjouePoint(float UnX, float UnY, String j, String fuck) {
        try {
            String kapa = "MesPoints.txt";
            String folder_main = Environment.getExternalStorageDirectory() + "/NOTMINE/";


            File point = new File(folder_main, kapa);
if(!point.exists()) {
                point.createNewFile();
            }
            else  {

                FileWriter writer = new FileWriter(point, true);
                BufferedWriter out = new BufferedWriter(writer);
                writer.append(fuck + " " + j + " " + UnX + " " + UnY);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                writer.close();
                Toast toast = Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT);
                toast.show();


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void fk(final float UnXe, final float UnYe, final String je){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Title");
        alert.setMessage("Message");

// recuperer les donnée entrer par l'utilisateur
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable bla = input.getText();
                String value = bla.toString();
                // ajoute un point
                AjouePoint(UnXe,UnYe,je,value);
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }

        });

        alert.show();

    }



}
