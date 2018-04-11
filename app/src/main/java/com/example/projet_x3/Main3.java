package com.example.projet_x3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by mear on 03/03/18.03/03/18
 */


//nouvelle activité pour voir l'imagea avec le point

public class Main3 extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final String image = intent.getExtras().getString("Nom_photo");
        final String xe =  intent.getExtras().getString("x");
        final String ye = intent.getExtras().getString("y");
        final String Nom_objet = intent.getExtras().getString("Nom_objet");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagee);
        final ImageView Myimage = (ImageView) findViewById(R.id.image_seul);
        final ImageView mask = (ImageView) findViewById(R.id.image_top);


//Crée une image bit map puis puis ajoute un point
        String here = Environment.getExternalStorageDirectory() + "/NOTMINE/" + image;
        final Bitmap myBitmap = BitmapFactory.decodeFile(here);
        Myimage.setImageBitmap(myBitmap);
        Toast toast = Toast.makeText(getApplicationContext(), "Pouvoir l'objet "+Nom_objet +"CLiquer sur l'écran", Toast.LENGTH_SHORT); toast.show();


        //Creation de mask + ajoue de de ce masque avec le point
        mask.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {      float x = Float.parseFloat(xe);
                    float y = Float.parseFloat(ye);

                    Bitmap imageBitmap = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(imageBitmap);
                    float scale = getResources().getDisplayMetrics().density;
                    Paint p = new Paint();
                    p.setColor(Color.RED);
                    p.setTextSize(50 * scale);
                    canvas.drawText(".", x, y, p);
                    mask.setImageBitmap(imageBitmap);


                }
                return true;
            }

            ;
        });


    }
}
