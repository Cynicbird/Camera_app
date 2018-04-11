package com.example.projet_x3;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.widget.Filterable;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.Filter;
import android.widget.Filterable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import java.util.Scanner;
import java.util.UUID;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    //variable global + fichier
    private File[] files;
    private String[] filesPaths;
    private String[] filesNames;
    private ImageView nice;
    private Button btnCapture;
    private TextureView textureView;
    private TextView textView;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private ImageView imagev;
    String objet,nom_photo,x,y;
    ArrayList<String> mesmots=new ArrayList<String>();
    //Check state orientation of output image
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;

    //Save to FILE
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private ViewFlipper viewFlipper;

    private ImageView ihi;
    private Button _myButton;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;

    private float _viewX;
    //MON DOSSIER OU Y A LES PHOTO
    String here = Environment.getExternalStorageDirectory() + "/NOTMINE/";
    File f = new File(here);
    File fi[] = f.listFiles();
    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SearchView simpleSearchView = (SearchView) findViewById(R.id.search); // inititate a search view
        CharSequence query = simpleSearchView.getQuery(); // get the query string currently in the text field
        CharSequence queryHint = simpleSearchView.getQueryHint(); // get the hint text that will be displayed in the query text field
        final ListView maListView = (ListView) findViewById(R.id.listview);
     getthem();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mesmots);

        maListView.setAdapter(itemsAdapter);
        maListView.setTextFilterEnabled(true);


        String path = here;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        if (directory.isDirectory()) {
            files = directory.listFiles();
            filesPaths = new String[files.length];
            filesNames = new String[files.length];

            for (int i = 0; i < files.length; i++) {
                filesPaths[i] = files[i].getAbsolutePath();
                filesNames[i] = files[i].getName();
            }
        }

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
//liste photos en grille application

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, filesNames, filesPaths));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), Main2.class);
                i.putExtra("Nom_photo", filesNames[position]);
                startActivity(i);

            }
        });
//bar de recherche avec widget
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //fonction de recherche après avoir cliquer
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query,
                        Toast.LENGTH_SHORT).show();
                String mon_objet, mon_mon_photo,mon_x, mon_y;


                if (TextUtils.isEmpty(query))
                {
                    maListView.clearTextFilter();
                }
                else
                {
                    maListView.setFilterText(query.toString());
                }
                //verification recherche
                if(true==recherche(query)){

                 mon_objet= objet ;
                    mon_mon_photo=  nom_photo;
                   mon_x= x;
                    mon_y = y;

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (simpleSearchView.getQuery().length() == 0) {
                    Toast.makeText(getApplicationContext(),  newText,
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        //cliquer des element de la liste view
        maListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String mon_objet, mon_mon_photo,mon_x, mon_y;
             CharSequence value =((TextView) view).getText();
                String monmot=String.valueOf(value);
                if(recherche(monmot)){

                    mon_objet= objet ;
                    mon_mon_photo=  nom_photo;
                    mon_x= x;
                    mon_y = y;

                }
                Toast.makeText(getApplicationContext(),  nom_photo,
                        Toast.LENGTH_SHORT).show();
                Intent e = new Intent(getApplicationContext(), Main3.class);
                e.putExtra("Nom_photo", nom_photo);
                e.putExtra("x", x);
                e.putExtra("y", y);
                e.putExtra("Nom_objet", objet);
                startActivity(e);
            }
        });
/*

//ma source pour apprendre à utiliser des fichier (données)
https://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory
        String path = here;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

*/

        //liste vue

        b1 = (Button) findViewById(R.id.But1);
        b2 = (Button) findViewById(R.id.But2);
        //vue cam
        b3 = (Button) findViewById(R.id.But3);
        //vue imagz
        b4 = (Button) findViewById(R.id.But4);
        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewFlipper.setDisplayedChild(0);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewFlipper.setDisplayedChild(1);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewFlipper.setDisplayedChild(2);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewFlipper.setDisplayedChild(3);
            }
        });
        textView = (TextView) findViewById(R.id.textView);
    TextView acceuil =(TextView) findViewById(R.id.acceuil);
        acceuil.setText("Mon projet genial de photo, réaliser par moi MATTHIEU");


        textureView = (TextureView) findViewById(R.id.textureView);
        //From Java 1.4 , you can use keyword 'assert' to check expression true or false
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        btnCapture = (Button) findViewById(R.id.btnCapture);

        textureView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    takePicture();
                }
                return true;
            }
        });




/*
        imagev.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    float x = (float) (event.getX()/v.getHeight());
                    float y = (float) (event.getY()/v.getWidth());
                    Toast toast = Toast.makeText(getApplicationContext(), "Mon x est :"+y+"Mon y est : "+x, Toast.LENGTH_SHORT); toast.show();
                    //takePicture();
                    AjouePoint(x, y);
                }
                return true;
            }
        });
        */
//        textureView.setOnClickListener(new View.OnClickListener() {


        //          public void onClick(View view) {


        //          }
        //    });
    }

//ajoue point dans le fichier text
    public void AjouePoint(float UnX, float UnY) {
        try {
            String kapa = "MesPoints.txt";
            String folder_main = Environment.getExternalStorageDirectory() + "/NOTMINE/";

            File point = new File(folder_main, kapa);
            if (!point.exists()) {
                point.createNewFile();
            }
            if (point.exists()) {


                FileWriter writer = new FileWriter(point, true);
                BufferedWriter out = new BufferedWriter(writer);
                writer.append("mont point X est :" + UnX + "Mon point Y est :" + UnY);
                writer.flush();
                writer.close();
                Toast toast = Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//prendre photo
    private void takePicture() {
        if (cameraDevice == null)
            return;
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null)
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);

            //Capture image with custom size
            int width = 1980;
            int height = 1080;
            if (jpegSizes != null && jpegSizes.length > 0) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            final ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //orientation du telephone (90)
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            String folder_main = "NOTMINE";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File f = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!f.exists()) {
                f.mkdirs();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/" + folder_main + "/" + timeStamp + ".jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        {
                            if (image != null)
                                image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(file);
                        outputStream.write(bytes);
                    } finally {
                        if (outputStream != null)
                            outputStream.close();
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(MainActivity.this, "Saved " + file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            }, mBackgroundHandler);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

//Preview de la camera

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (cameraDevice == null)
                        return;
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if (cameraDevice == null)
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            //Check realtime permission if run higher API 23
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
//surface biew

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {


        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    //pause pour pas casser l"application

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (textureView.isAvailable())
            openCamera();
        else
            textureView.setSurfaceTextureListener(textureListener);
    }

    @Override
    protected void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
//recherche d'un moT
    //LECTURE PAR LIGNE
    //SI MOT TROUVER PREND LA LIGNE
    private boolean recherche(String h) {
        try {
            String kapa = "MesPoints.txt";
            String folder_main = Environment.getExternalStorageDirectory() + "/NOTMINE/";

            File point = new File(folder_main, kapa);
            if (!point.exists()) {
            }
            if (point.exists()) {


                FileReader read = new FileReader(point);
                BufferedReader out = new BufferedReader(read);

                String line;

                String foundWord = h;
                while ((line = out.readLine()) != null) {
                    String[] words = line.split(" ");
                    for (String word : words) {
                        if (word.equals(foundWord)) {
                            objet = words[0];
                            nom_photo = words[1];
                            x = words[2];
                            y = words[3];
                        }
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    //RECUPERATION TOUTS LES MORTS
    private void getthem(){
        String kapa = "MesPoints.txt";
        String folder_main = Environment.getExternalStorageDirectory() + "/NOTMINE/";
        File point = new File(folder_main, kapa);


        int i=0;
        try
        {
            if (!point.exists()) {
            }
            if (point.exists()) {
                FileReader read = new FileReader(point);
                BufferedReader out = new BufferedReader(read);
                String line;
                while ((line = out.readLine()) != null) {
                    String[] delims = line.split(" ");
                    mesmots.add(delims[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
