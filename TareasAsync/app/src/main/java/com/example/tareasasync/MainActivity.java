package com.example.tareasasync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tiempoEdT = (EditText) findViewById(R.id.in_time);
        btnArrancar = (Button) findViewById(R.id.btn_arrancar);
        btnParar = (Button) findViewById(R.id.btn_parar);
        resultados = (TextView) findViewById(R.id.tv_result);

        btnArrancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultados.setText("");
                String tiempoDormido = tiempoEdT.getText().toString();
                if (tiempoDormido.matches("")) {
                    Toast.makeText(MainActivity.this, "Introduce un número de segundos", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    cierraTeclado();
                    resultados.append("Ejecutando Tarea pesada: "+tiempoDormido+" sg --> ");
                    tareaPesada(Integer.parseInt(tiempoDormido),resultados);
                    resultados.append("Terminada tarea pesada \n");
                }
            }
        });

        // Para el proceso de la tarea en segundo plano
        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cierraTeclado();
                resultados.append("Parando tarea pesada \n");
                finish();
            }
        });
    }


    // Metodo para realizar una tarea pesada con Runnables
    protected void tareaPesada(int tiempo, TextView result){

        // Creamos un objeto de la clase Runnable
        Runnable tarea = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(tiempo*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result.append("Tarea pesada realizada en "+tiempo+" sg \n");
            }
        };
        // Creamos un objeto de la clase Thread y le pasamos el objeto Runnable
        Thread hilo = new Thread(tarea);
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



