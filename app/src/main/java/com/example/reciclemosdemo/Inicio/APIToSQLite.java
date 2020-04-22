package com.example.reciclemosdemo.Inicio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.Departamento;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Entities.Producto;
import com.example.reciclemosdemo.Entities.Usuario;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIToSQLite {

    dbHelper helper;
    SQLiteDatabase db;
    Retrofit retrofit;
    Context context;
    private double plasticoCount,pesoPlastico,puntosPlastico;
    private double vidrioCount,pesoVidrio,puntosVidrio;
    private double papelCartonCount,pesoPapelCarton,puntosPapelCarton;
    private double metalCount,pesoMetal,puntosMetal;
    private int residuosTotal;
    private int [] yAxisDataMonth= {0,0,0,0,0,0,0};
    private int [] yAxisDataYear= {0,0,0,0,0,0,0,0,0,0,0,0};

    public APIToSQLite(Context context){
        helper = new dbHelper(context,"Usuario.sqlite", null, 1);
        db = helper.getWritableDatabase();
        helper.DropCreate(db);
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void InsertUsuario(String email, String password) throws IOException, InterruptedException {
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Usuario> call=jsonPlaceHolderApi.getUsuarioByEmailPassword("/usuario/correo/" + email + "/" + password);
        Response<Usuario> response = call.execute();
        String[] ayuda = new String[10];
        ayuda[0] = response.body().getNombre();
        ayuda[1] = response.body().getApellido();
        ayuda[2] = response.body().getCondominio().getNombre();
        ayuda[3] = response.body().getDireccion();
        ayuda[4] = response.body().getDni();
        ayuda[5] = response.body().getEmail();
        ayuda[6] = response.body().getFechanacimiento();
        ayuda[7] = response.body().getSexo().getNombre();
        ayuda[8] = response.body().getTelefono();
        ayuda[9] = response.body().getCodigo().toString();
        String query = "insert into Usuario (nombre, apellido, condominio, direccion, dni, email, fecha_Nacimiento, sexo, telefono, codigo) " +
                "values ('" + ayuda[0] + "', '" + ayuda[1] + "', '" + ayuda[2] + "', '" + ayuda[3] + "', '" + ayuda[4] + "', '" + ayuda[5] + "', '"
                + ayuda[6] + "', '" + ayuda[7] + "', '" + ayuda[8] + "', " + ayuda[9] + ")";
        db.execSQL(query);
        System.out.println(query);
        Log.e("TAG","onResponse:" + response.toString());
        /*call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()) {
                    String[] ayuda = new String[10];
                    ayuda[0] = response.body().getNombre();
                    ayuda[1] = response.body().getApellido();
                    ayuda[2] = response.body().getCondominio().getNombre();
                    ayuda[3] = response.body().getDireccion();
                    ayuda[4] = response.body().getDni();
                    ayuda[5] = response.body().getEmail();
                    ayuda[6] = response.body().getFechanacimiento();
                    ayuda[7] = response.body().getSexo().getNombre();
                    ayuda[8] = response.body().getTelefono();
                    ayuda[9] = response.body().getCodigo().toString();
                    String query = "insert into Usuario (nombre, apellido, condominio, direccion, dni, email, fecha_Nacimiento, sexo, telefono, codigo) " +
                            "values ('" + ayuda[0] + "', '" + ayuda[1] + "', '" + ayuda[2] + "', '" + ayuda[3] + "', '" + ayuda[4] + "', '" + ayuda[5] + "', '"
                            + ayuda[6] + "', '" + ayuda[7] + "', '" + ayuda[8] + "', " + ayuda[9] + ")";
                    db.execSQL(query);
                    System.out.println(query);
                    Log.e("TAG","onResponse:" + response.toString());
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });*/
    }

    public void InsertProductos() throws IOException, InterruptedException {
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Producto>> call=jsonPlaceHolderApi.getProductos("/producto");
        Response<List<Producto>> response = call.execute();
        for (Producto p: response.body()) {
            String query = "insert into Producto (barcode , categoria , codigo , contenido , descripcion , nombre , peso , tipo_contenido , urlimagen) " +
                    "values ('" + p.getBarcode() + "', " + p.getCategoria().getCodigo() + ", " + p.getCodigo() + ", " + p.getContenido() + ", " + p.getDescripcion()
                    + ", '" + p.getNombre() + "', " + p.getPeso() + ", '" + p.getTipo_Contenido().getAbreviatura() + "', '" + p.getUrlImage() + "')";
            db.execSQL(query);
            System.out.println(query);
        }
        Log.e("TAG","onResponse:" + response.toString());
        /*call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if(response.isSuccessful()) {
                    for (Producto p: response.body()) {
                        String query = "insert into Producto (barcode , categoria , codigo , contenido , descripcion , nombre , peso , tipo_contenido , urlimagen) " +
                                "values ('" + p.getBarcode() + "', " + p.getCategoria().getCodigo() + ", " + p.getCodigo() + ", " + p.getContenido() + ", " + p.getDescripcion()
                                + ", '" + p.getNombre() + "', " + p.getPeso() + ", '" + p.getTipo_Contenido().getAbreviatura() + "', " + p.getUrlimagen() + ")";
                        db.execSQL(query);
                        System.out.println(query);
                    }
                    Log.e("TAG","onResponse:" + response.toString());
                }else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });*/
    }

    public void InsertBolsas() throws IOException, InterruptedException {
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor fila = db.rawQuery("select codigo from Usuario", null);

        System.out.println(fila.getColumnNames().toString());

        fila.moveToFirst();

        String codigo = fila.getString(0);

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Bolsa>> call=jsonPlaceHolderApi.getBolsasByUsuario("/bolsa/usuario/" + codigo);
        Response<List<Bolsa>> response = call.execute();
        for (Bolsa p: response.body()) {
            if(p.getCreadoFecha() == null){
                String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha) " +
                        "values (" + p.getCodigo() + ", '" + p.getActiva() + "', " + null + ", " + p.getPuntuacion() + ", " + null
                        + ", " + null + ")";
                db.execSQL(query);
                System.out.println(query);
            } else{
                String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha) " +
                        "values (" + p.getCodigo() + ", '" + p.getActiva() + "', '" + p.getCreadoFecha() + "', " + p.getPuntuacion() + ", " + p.getQrCode().getCodigo()
                        + ", '" + p.getRecojoFecha() + "')";
                db.execSQL(query);
                System.out.println(query);
            }
        }
        Log.e("TAG","onResponse:" + response.toString());
        /*call.enqueue(new Callback<List<Bolsa>>() {
            @Override
            public void onResponse(Call<List<Bolsa>> call, Response<List<Bolsa>> response) {
                if(response.isSuccessful()) {
                    for (Bolsa p: response.body()) {
                        if(p.getCreadoFecha() == null){
                            String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha) " +
                                    "values (" + p.getCodigo() + ", " + p.getActiva() + ", " + null + ", " + p.getPuntuacion() + ", " + null
                                    + ", " + null + ")";
                            db.execSQL(query);
                            System.out.println(query);
                        } else{
                            String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha) " +
                                    "values (" + p.getCodigo() + ", '" + p.getActiva() + "', '" + p.getCreadoFecha() + "', " + p.getPuntuacion() + ", " + p.getQrCode().getCodigo()
                                    + ", '" + p.getRecojoFecha() + "')";
                            db.execSQL(query);
                            System.out.println(query);
                        }
                    }
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Bolsa>> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });*/
    }

    public void InsertProbolsa() throws IOException, InterruptedException {
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor fila = db.rawQuery("select codigo from Usuario", null);

        fila.moveToFirst();

        String codigo = fila.getString(fila.getColumnIndex("codigo"));

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getProbolsaByUsuario("/probolsa/usuario/" + codigo);
        Response<List<Probolsa>> response = call.execute();
        /*call.enqueue(new Callback<List<Probolsa>>() {
            @Override
            public void onResponse(Call<List<Probolsa>> call, Response<List<Probolsa>> response) {
                if(response.isSuccessful()) {*/
                    for (Probolsa p: response.body()) {
                        String query = "insert into Probolsa (bolsa, cantidad, codigo, peso, producto, puntuacion) " +
                                "values (" + p.getBolsa().getCodigo() + ", " + p.getCantidad() + ", " + p.getCodigo() + ", "
                                + p.getPeso() + ", " + p.getProducto().getCodigo() + ", " + p.getPuntuacion() + ")";
                        db.execSQL(query);
                        System.out.println(query);
                    }
                    db.close();
                    Log.e("TAG","onResponse:" + response.toString());
                /*}else{
                    Log.e("TAG","onResponse:" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Probolsa>> call, Throwable t) {
                Log.e("TAG","onFailure:" + t.getMessage());
            }
        });*/
    }

    public void insertBolsasByMonthOrWeek(String urlDate) throws IOException, InterruptedException{
        initialCounter();
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor fila = db.rawQuery("select codigo from Usuario", null);

        fila.moveToFirst();
        int valor =0;
        String codigo = fila.getString(fila.getColumnIndex("codigo"));
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/"+urlDate+codigo);
        Response<List<Probolsa>> response = call.execute();
        for(Probolsa bolsasbydate: response.body()){
            valor++;
            if (bolsasbydate.getBolsa().getRecojoFecha() != null) {

                if(urlDate.equals("bolsasWeek/") || urlDate.equals("bolsasMonth/")) {
                    Date dia = bolsasbydate.getBolsa().getRecojoFecha();
                    SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");


                    if (simpleDateformat.format(dia).equals("Monday")) {
                        yAxisDataMonth[0] += 1;
                    }
                    else if (simpleDateformat.format(dia).equals("Tuesday"))
                        yAxisDataMonth[1] += 1;
                    else if (simpleDateformat.format(dia).equals("Wednesday"))
                        yAxisDataMonth[2] += 1;
                    else if (simpleDateformat.format(dia).equals("Thursday")) {
                        yAxisDataMonth[3] += 1;
                    }
                    else if (simpleDateformat.format(dia).equals("Friday"))
                        yAxisDataMonth[4] += 1;
                    else if (simpleDateformat.format(dia).equals("Saturday"))
                        yAxisDataMonth[5] += 1;
                    else
                        yAxisDataMonth[6] += 1;

                    addingValuestoText(bolsasbydate);
                }
            }
        }
        if(valor>0) {
            if (urlDate.equals("bolsasWeek/")) {
                generateQuery("Semana", "Plastico", plasticoCount, pesoPlastico, puntosPlastico);
                generateQuery("Semana", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio);
                generateQuery("Semana", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton);
                generateQuery("Semana", "Metal", metalCount, pesoMetal, puntosMetal);
                String query = "insert into DatosDiarios(tipo,lunes,martes,miercoles,jueves,viernes,sabado,domingo) " +
                        "values ('Semana', " + yAxisDataMonth[0] + ", " + yAxisDataMonth[1] + ", "
                        + yAxisDataMonth[2] + ", " + yAxisDataMonth[3] + ", " + yAxisDataMonth[4] + "," + yAxisDataMonth[5] + "," + yAxisDataMonth[6] + ")";
                db.execSQL(query);
                System.out.println(query);
            } else {
                generateQuery("Mes", "Plastico", plasticoCount, pesoPlastico, puntosPlastico);
                generateQuery("Mes", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio);
                generateQuery("Mes", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton);
                generateQuery("Mes", "Metal", metalCount, pesoMetal, puntosMetal);
                String query = "insert into DatosDiarios(tipo,lunes,martes,miercoles,jueves,viernes,sabado,domingo) " +
                        "values ('Mes', " + yAxisDataMonth[0] + ", " + yAxisDataMonth[1] + ", "
                        + yAxisDataMonth[2] + ", " + yAxisDataMonth[3] + ", " + yAxisDataMonth[4] + "," + yAxisDataMonth[5] + "," + yAxisDataMonth[6] + ")";
                db.execSQL(query);
                System.out.println(query);
            }
        }



        db.close();
        Log.e("TAG","onResponse:" + response.toString());
    }

    public void obtenerBolsasByYear(String urlDate)throws IOException, InterruptedException{
        initialCounter();
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor fila = db.rawQuery("select codigo from Usuario", null);

        fila.moveToFirst();
        int valor =0;
        String codigo = fila.getString(fila.getColumnIndex("codigo"));
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/"+urlDate+codigo);
        Response<List<Probolsa>> response = call.execute();
       for (Probolsa bolsasbydate : response.body()) {
           valor++;
               if (bolsasbydate.getBolsa().getRecojoFecha() != null) {

                   Date dia = bolsasbydate.getBolsa().getRecojoFecha();
                   Calendar cal = Calendar.getInstance();
                   cal.setTime(dia);
                   if (cal.get(Calendar.MONTH) == 1)
                       yAxisDataYear[0] += 1;
                   else if (cal.get(Calendar.MONTH) == 2)
                       yAxisDataYear[1] += 1;
                   else if (cal.get(Calendar.MONTH) == 3)
                       yAxisDataYear[2] += 1;
                   else if (cal.get(Calendar.MONTH) == 4)
                       yAxisDataYear[3] += 1;
                   else if (cal.get(Calendar.MONTH) == 5)
                       yAxisDataYear[4] += 1;
                   else if (cal.get(Calendar.MONTH) == 6)
                       yAxisDataYear[5] += 1;
                   else if (cal.get(Calendar.MONTH) == 7)
                       yAxisDataYear[6] += 1;
                   else if (cal.get(Calendar.MONTH) == 8)
                       yAxisDataYear[7] += 1;
                   else if (cal.get(Calendar.MONTH) == 9)
                       yAxisDataYear[8] += 1;
                   else if (cal.get(Calendar.MONTH) == 10)
                       yAxisDataYear[9] += 1;
                   else if (cal.get(Calendar.MONTH) == 11)
                       yAxisDataYear[10] += 1;
                   else if (cal.get(Calendar.MONTH) == 12)
                       yAxisDataYear[11] += 1;

                   addingValuestoText(bolsasbydate);
               }

           }
        if(valor>0) {
                generateQuery("Year", "Plastico", plasticoCount, pesoPlastico, puntosPlastico);
                generateQuery("Year", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio);
                generateQuery("Year", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton);
                generateQuery("Year", "Metal", metalCount, pesoMetal, puntosMetal);
                String query = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre) " +
                        "values ( " + yAxisDataYear[0] + ", " + yAxisDataYear[1] + ", "
                        + yAxisDataYear[2] + ", " + yAxisDataYear[3] + ", " + yAxisDataYear[4] + "," + yAxisDataYear[5]  + ", "
                        + yAxisDataYear[6] + ", " + yAxisDataYear[7] + ","+ + yAxisDataYear[8] + ","+yAxisDataYear[9] + ", " + yAxisDataYear[10] +","+ yAxisDataYear[11] +  ")";
                db.execSQL(query);
                System.out.println(query);

        }

        db.close();
        Log.e("TAG","onResponse:" + response.toString());
    }

    public void initialCounter()
    {
        plasticoCount=0;
        pesoPlastico=0;
        puntosPlastico=0;
        vidrioCount=0;
        pesoVidrio=0;
        puntosVidrio=0;
        papelCartonCount=0;
        pesoPapelCarton=0;
        puntosPapelCarton=0;
        metalCount=0;
        pesoMetal=0;
        puntosMetal=0;
        residuosTotal=0;
    }
    public void addingValuestoText(Probolsa bolsasbydate ){
        if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Plastico")) {
            pesoPlastico += bolsasbydate.getProducto().getPeso();
            puntosPlastico += bolsasbydate.getPuntuacion();
            plasticoCount++;
        } else if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Vidrio")) {
            pesoVidrio += bolsasbydate.getProducto().getPeso();
            puntosVidrio += bolsasbydate.getPuntuacion();
            vidrioCount++;
        } else if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Papel/Carton")) {
            pesoPapelCarton += bolsasbydate.getProducto().getPeso();
            puntosPapelCarton += bolsasbydate.getPuntuacion();
            papelCartonCount++;
        } else if (bolsasbydate.getProducto().getCategoria().getNombre().equals("Metal")) {
            pesoMetal += bolsasbydate.getProducto().getPeso();
            puntosMetal += bolsasbydate.getPuntuacion();
            metalCount++;
        }
    }
    public void generateQuery(String tipo,String producto,double cantidad,double peso,double puntuacion ){
        String query = "insert into Contador (tendenciaTipo,productoTipo,cantidad,peso,puntuacion) " +
                "values ('" + tipo + "', '" + producto + "', " + cantidad + ", "
                + peso + ", " + puntuacion + ")";
        db.execSQL(query);
        System.out.println(query);
    }

}
