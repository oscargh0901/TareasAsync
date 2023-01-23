package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText tiempoEdT;
    private Button btnArrancar;
    private Button btnParar;
    private TextView resultados;

    Thread hilo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tiempoEdT = (EditText) findViewById(R.id.in_time);
        btnArrancar = (Button) findViewById(R.id.btn_arrancar);
        btnParar = (Button) findViewById(R.id.btn_parar);
        resultados = (TextView) findViewById(R.id.tv_result);

        btnArrancar.setOnClickListener(new View.OnClickListener() { // Arranca el hilo
            @Override
            public void onClick(View v) {

                if (hilo != null && hilo.isAlive()) {
                    Toast.makeText(MainActivity.this, "El hilo ya esta en ejecucion", Toast.LENGTH_SHORT).show();
                    return;
                }

                resultados.setText("");
                String tiempoDormido = tiempoEdT.getText().toString(); // Obtiene el tiempo

                // Comprueba que el tiempo no sea vacio
                if (tiempoDormido.matches("")) {
                    Toast.makeText(MainActivity.this, "Introduce un número de segundos", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    cierraTeclado();
                    resultados.append("Ejecutando Tarea pesada: " + tiempoDormido + " sg --> ");

                    // Crea un hilo
                    try {
                        tareaPesada(Integer.parseInt(tiempoDormido), resultados);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Para el proceso de la tarea en segundo plano
        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cierraTeclado();
                resultados.append("Parando tarea pesada... \n");

                // Si el hilo está en ejecución, lo interrumpe
                if (hilo != null && hilo.isAlive()) {
                    hilo.interrupt();
                }
            }
        });
    }

    // Metodo para realizar una tarea pesada con Runnables
    protected void tareaPesada(int tiempo, TextView result){

        // Creamos un objeto de la clase Runnable
        Runnable tarea = new Runnable() {
            @Override
            public void run() {
                try
                {
                    for (int i = 1; i < tiempo+1; i++) { // pongo tiempo+1 para que se ejecute el tiempo indicado (no desde 0)
                        Thread.sleep(1000);

                        int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                result.append(Integer.toString(finalI).concat("s... "));

                            }
                        });
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.append("Tarea finalizada");
                        }
                    });
                }
            }
        };

        // Creamos un objeto de la clase Thread y le pasamos el objeto Runnable
        hilo = new Thread(tarea);
        // Arrancamos el hilo
        hilo.start();
    }

    protected void cierraTeclado(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}