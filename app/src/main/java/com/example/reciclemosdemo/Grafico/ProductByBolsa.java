package com.example.reciclemosdemo.Grafico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Inicio.BolsaActivity;
import com.example.reciclemosdemo.Inicio.LectorActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    com.example.reciclemosdemo.Entities.CategoriaDivided plasticoDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided vidrioDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided metalDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    com.example.reciclemosdemo.Entities.CategoriaDivided papelDivided = new com.example.reciclemosdemo.Entities.CategoriaDivided();
    ArrayList<com.example.reciclemosdemo.Entities.CategoriaDivided> categoriaDivideds = new ArrayList<com.example.reciclemosdemo.Entities.CategoriaDivided>();


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

        try {
            obtenerDatosProductByBolsa(String.valueOf(position),textList);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    public void obtenerDatosProductByBolsa(String posicion, ArrayList<TextView> textViewsList) throws ParseException {
        dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select codigo , creadoFecha , puntuacion  , recojoFecha ,observaciones  from Bolsa where codigo = "+posicion;
        System.out.println(query);
        Cursor f = db.rawQuery(query,null);
        f.moveToFirst();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if(f.getString(3).equals("null") || f.getString(3).equals(null)) {
            textViewsList.get(1).setText("Recojo : Pendiente");
        }else {
            Date date1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(f.getString(3));
            Date recojo = date1;
            String recojoFecha;
            recojoFecha = formatter.format(recojo);
            textViewsList.get(1).setText("Recojo : "+recojoFecha);
        }
        Date creado = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(f.getString(1));
        Date creadoDate = creado;
        String creadoFecha;
        creadoFecha = formatter.format(creadoDate);
        textViewsList.get(0).setText("Creada : "+creadoFecha);
        textViewsList.get(4).setText("Juan Torres");
        if(f.getString(4).equals("null") || f.getString(4).equals(null)) {
            textViewsList.get(5).setText("NO");
            textViewsList.get(6).setText("No hay observaciones");
        }
        else {
            textViewsList.get(5).setText("SI");
            textViewsList.get(6).setText(f.getString(4));
        }

        String query2 = "select tendenciaTipo  ,productoTipo ,cantidad ,peso ,puntuacion ,bolsa   from Contador where bolsa = "+ posicion + " and tendenciaTipo = 'LastBolsas'";
        Cursor f1 = db.rawQuery(query2,null);
        f1.moveToFirst();
        do {
            if ( f1.getString(1).equals("Plastico")  && f1.getInt(2) > 0) {
                plasticoDivided.setCantidad( f1.getInt(2));
                plasticoDivided.setPeso( f1.getInt(3));
                plasticoDivided.setPuntos( f1.getInt(4));
                plasticoDivided.setTipo("Plastico");
                categoriaDivideds.add(plasticoDivided);
            }
            if ( f1.getString(1).equals("Vidrio")  && f1.getInt(2) > 0) {
                vidrioDivided.setCantidad( f1.getInt(2));
                vidrioDivided.setPeso( f1.getInt(3));
                vidrioDivided.setPuntos( f1.getInt(4));
                vidrioDivided.setTipo("Vidrio");
                categoriaDivideds.add(vidrioDivided);
            }
            if ( f1.getString(1).equals("Metal")  && f1.getInt(2) > 0) {
                metalDivided.setCantidad( f1.getInt(2));
                metalDivided.setPeso( f1.getInt(3));
                metalDivided.setPuntos( f1.getInt(4));
                metalDivided.setTipo("Metal");
                categoriaDivideds.add(metalDivided);
            }
            if ( f1.getString(1).equals("Papel")  && f1.getInt(2) > 0) {
                papelDivided.setCantidad( f1.getInt(2));
                papelDivided.setPeso( f1.getInt(3));
                papelDivided.setPuntos( f1.getInt(4));
                papelDivided.setTipo("Papel/Carton");
                categoriaDivideds.add(papelDivided);
            }
        }while(f1.moveToNext());
        listBolsasAdapter.adicionarProductoBolsas(categoriaDivideds, ProductByBolsa.this);

        textViewsList.get(2).setText(Integer.toString(papelDivided.getPeso()+metalDivided.getPeso()+plasticoDivided.getPeso()+vidrioDivided.getPeso()));
        textViewsList.get(3).setText(Integer.toString(papelDivided.getPuntos()+metalDivided.getPuntos()+plasticoDivided.getPuntos()+vidrioDivided.getPuntos()));
    }



    @Override
    public void oneNoteClick(int position) {
    }
}
