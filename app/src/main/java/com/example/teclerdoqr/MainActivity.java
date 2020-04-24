package com.example.teclerdoqr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
    }
    public void ScanButton(View view){
        IntentIntegrator lectorqr = new IntentIntegrator(this);
        lectorqr.setBeepEnabled(true);
        lectorqr.setPrompt("Coloque el código QR en el interior del rectángulo.");
        lectorqr.setOrientationLocked(false);
        lectorqr.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //resultado de el intent
        IntentResult datos = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //hace cosas hasta que detecte QR o datos sea diferente a nulo
        if (datos != null){
            if (datos.getContents() == null){ //si se cancela y los datos son nulos.
                Toast toast = Toast.makeText(MainActivity.this,"No se capturaron datos.",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,250);
                toast.show();
            }else {
                //codigo para vibrar telefono
                if (Build.VERSION.SDK_INT >= 26) { //checa si la versiones mayor o igual a 26, anteriores no funcionan de lo contrario saldra error
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
                } else {
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
                }
                //formateo de datos, borrado de caracteres
                String[] datosSplit = datos.getContents().split(",");
                String nombre = datosSplit[0].substring(11).replaceAll("\"","").trim();
                String numerocontrol = datosSplit[1].substring(20,29).replaceAll("\"","").trim();

                //creacion del tost, centrado, long
                Toast toastS = Toast.makeText(MainActivity.this,nombre + "\n" + numerocontrol,Toast.LENGTH_LONG);
                toastS.setGravity(Gravity.CENTER,0,250);
                toastS.show();
                //cambia texto del textview
                textView.setText("Nombre: "+ nombre + "\nNúmero de control: " + numerocontrol);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
