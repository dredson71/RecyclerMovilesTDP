package com.example.reciclemosdemo.Grafico;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Inicio.APIToSQLite;
import com.example.reciclemosdemo.Inicio.BolsaActivity;
import com.example.reciclemosdemo.Inicio.CatalogoActivity;
import com.example.reciclemosdemo.Inicio.LoginActivity;
import com.example.reciclemosdemo.R;

import java.io.IOException;

public class ExitosoTest extends AppCompatActivity {
    private APIToSQLite sqlitedb;
    private ProgressDialog progressDialog, progressDialog2;

    dbHelper helper;
    SQLiteDatabase db;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case 2:
                    progressDialog2.cancel();
                    Toast.makeText(getApplicationContext(), "Información cargada", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exitoso_test);



        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Cargando información...");
        progressDialog2.setCancelable(false);


        sqlitedb = new APIToSQLite(this,"actualizar");

        Message msg2 = new Message();
        msg2.what = 4;
        mHandler.sendMessage(msg2);

    }

    private class BackgroundJob extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sqlitedb.InsertProductos();
                sqlitedb.InsertBolsas();
                sqlitedb.InsertProbolsa();
                sqlitedb.insertBolsasByMonthOrWeek("bolsasWeek/");
                sqlitedb.insertBolsasByMonthOrWeek("bolsasMonth/");
                sqlitedb.obtenerBolsasByYear("bolsasYear/");
                sqlitedb.obtenerBolsasByDay();
                sqlitedb.obtenerUltimasBolsas();
                sqlitedb.obtenerDatosProductByBolsa();
                sqlitedb.InsertReciclador();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }
    }
}
