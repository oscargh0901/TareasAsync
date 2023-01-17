package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ExecutorService service = Executors.newSingleThreadExecutor(); // Crea un hilo
        service.execute(new Runnable() { // Ejecuta el hilo
            @Override
            public void run() {
                runOnUiThread(new Runnable() { // Ejecuta el hilo en el hilo principal
                    @Override
                    public void run() { // Ejecuta el hilo principal
                        try
                        {
                            // Simula una tarea pesada
                            Toast.makeText(SplashActivity.this, "Arrancando la aplicacion...", Toast.LENGTH_SHORT).show();
                            Thread.sleep(4000);
                            progressDialog.dismiss();
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            // Cierra el hilo
                            service.shutdown();
                        }
                    }
                });
            }
        });
    }
}