package com.example.reciclemosdemo.Grafico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Inicio.BolsaActivity;
import com.example.reciclemosdemo.Inicio.LectorActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProductByBolsa extends AppCompatActivity implements com.example.reciclemosdemo.Grafico.ListaProductoByBolsaAdapter.OnNoteListener  {
    private static final String TAG ="PRODUCTOS";
    private ArrayList<Probolsa> dataset = new ArrayList<>();
    private com.example.reciclemosdemo.Grafico.RetrofitMain retrofit;
    private RecyclerView recyclerView;
    private com.example.reciclemosdemo.Grafico.ListaProductoByBolsaAdapter listBolsasAdapter;
    private ArrayList<TextView> textList = new ArrayList<>();
    private TextView txt_RecojoFecha;
    private TextView txt_CreadoFecha;
    private TextView txt_PesoTotal;
    private TextView txt_PuntosTotal;
    private RelativeLayout item_product;
    private TextView txt_Reciclador;
    private TextView txt_Observaciones;
    private Dialog myObservacionDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_bolsa);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerProductos);
        listBolsasAdapter = new com.example.reciclemosdemo.Grafico.ListaProductoByBolsaAdapter(this);
        recyclerView.setAdapter(listBolsasAdapter);
        recyclerView.setHasFixedSize(true);
        item_product= findViewById(R.id.relativeProductByBolsa);
        txt_CreadoFecha = findViewById(R.id.txtCreadoFecha);
        txt_RecojoFecha = findViewById(R.id.txtRecoojoFecha);
        txt_PesoTotal = findViewById(R.id.txtPesoTotales);
        txt_PuntosTotal = findViewById(R.id.txtPuntosTotales);
        txt_Reciclador = findViewById(R.id.txtReciclador);
        txt_Observaciones = findViewById(R.id.txtObservaciones);
        textList.add(txt_CreadoFecha);
        textList.add(txt_RecojoFecha);
        textList.add(txt_PesoTotal);
        textList.add(txt_PuntosTotal);
        textList.add(txt_Reciclador);
        textList.add(txt_Observaciones);
        myObservacionDialog = new Dialog(this);
        myObservacionDialog.setContentView(R.layout.editable_profile);
        myObservacionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtObservacionesDialog = myObservacionDialog.findViewById(R.id.txtObservaciones);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        Bundle datos= getIntent().getExtras();
        item_product.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_scale_2));
        int position= datos.getInt("posicion");
        retrofit = new com.example.reciclemosdemo.Grafico.RetrofitMain();
        textList.add(txtObservacionesDialog);

        retrofit.obtenerDatosProductByBolsa(String.valueOf(position),listBolsasAdapter,ProductByBolsa.this::oneNoteClick,textList);

        txt_Reciclador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecicladorActivity.class));
            }
        });
        txt_Observaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myObservacionDialog.show();
            }
        });
        //INICIALIZA APP BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.botton_navigation);

        //SELECCIÓN
        bottomNavigationView.setSelectedItemId(R.id.escaner);

        //CAMBIO DE SELECCIÓN
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.miaporte:
                        startActivity(new Intent(getApplicationContext(), com.example.reciclemosdemo.Grafico.ListBolsas.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.escaner:
                        startActivity(new Intent(getApplicationContext(), LectorActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mibolsa:
                        startActivity(new Intent(getApplicationContext(), BolsaActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.catalogo:
                        return true;
                }
                return false;
            }
        });
    }




    @Override
    public void oneNoteClick(int position) {
    }
}
