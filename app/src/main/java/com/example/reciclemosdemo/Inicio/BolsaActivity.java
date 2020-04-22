package com.example.reciclemosdemo.Inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Grafico.ListBolsas;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BolsaActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction transaction, transaction2;
    Fragment detalleFragment = new DetalleFragment();
    Fragment escaneaAlgoFragment = new  EscaneaAlgoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolsa);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        dbHelper helper = new dbHelper(this, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor fila2 = db.rawQuery("select codigo from Bolsa where activa = 'true'", null);

        fila2.moveToFirst();
        System.out.println(fila2.getInt(0));

        Cursor fila = db.rawQuery("select cantidad, puntuacion from Probolsa where bolsa = " + fila2.getInt(0), null);

        if(fila.moveToFirst()) {
            transaction.replace(R.id.fragment, detalleFragment);
            transaction.commit();
        } else {
            setEscaneaAlgoFragment();
        }

        //INICIALIZA APP BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.botton_navigation);

        //SELECCIÓN
        bottomNavigationView.setSelectedItemId(R.id.mibolsa);

        //CAMBIO DE SELECCIÓN
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.miaporte:
                        startActivity(new Intent(getApplicationContext(), ListBolsas.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.escaner:
                        startActivity(new Intent(getApplicationContext(), LectorActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mibolsa:
                        return true;
                    case R.id.catalogo:
                        return true;
                }
                return false;
            }
        });
    }

    public void setEscaneaAlgoFragment(){
        fragmentManager = getSupportFragmentManager();
        transaction2 = fragmentManager.beginTransaction();
        transaction2.replace(R.id.fragment, escaneaAlgoFragment);
        transaction2.commit();
    }
}
