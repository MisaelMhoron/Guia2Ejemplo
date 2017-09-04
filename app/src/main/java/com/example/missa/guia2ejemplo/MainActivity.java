package com.example.missa.guia2ejemplo;



import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RadioButton rbnCambiarNombre, rbnNoCambiar;
    private ProgressBar progressBar;
    private EditText txtURL;
    private TextView lblEstado;
    private Button btnDescargar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializar
        rbnCambiarNombre = (RadioButton) findViewById(R.id.btnCambiar);
        rbnNoCambiar = (RadioButton) findViewById(R.id.btnNoCmabiar);
        txtURL = (EditText) findViewById(R.id.txtURL);
        lblEstado = (TextView) findViewById(R.id.lblEstado);
        btnDescargar = (Button) findViewById(R.id.btnDescargar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnDescargar.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                progressStatus = 0;
                                                new Thread(new Runnable() {
                                                    public void run() {
                                                        while (progressStatus < 100) {
                                                            progressStatus += 1;
                                                            handler.post(new Runnable() {
                                                                public void run() {
                                                                    progressBar.setProgress(progressStatus);
                                                                    lblEstado.setText("Progress:" + progressStatus + "/" + progressBar.getMax());
                                                                }
                                                            });
                                                            try {
                                                                Thread.sleep(100);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }).start();

                                                new Descargar(
                                                        MainActivity.this,

                                                        lblEstado,
                                                        btnDescargar
                                                ).execute(txtURL.getText().toString());



                                            }


                                        }
        );


        verifyStoragePermissions(this);
    }




    //esto es para activar perimiso de escritura y lectura en versiones de android 6 en adelante
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }



}

