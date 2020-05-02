package com.example.reciclemosdemo.Inicio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Entities.Producto;
import com.example.reciclemosdemo.Entities.Reciclador;
import com.example.reciclemosdemo.Entities.Usuario;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import retrofit2.Call;
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
    private  double pesoCount,bolsasCount,puntosCount;
    private int residuosTotal;
    private int [] yAxisDataMonth= {0,0,0,0,0,0,0};
    private int [] yAxisDataYear= {0,0,0,0,0,0,0,0,0,0,0,0};
    private int [] yAxisDataYearBolsa= {0,0,0,0,0,0,0,0,0,0,0,0};
    private  Set<Integer> bolsasLast = new HashSet<>();


    public APIToSQLite(Context context,String tipo){
        helper = new dbHelper(context,"Usuario.sqlite", null, 1);
        db = helper.getWritableDatabase();
        if(!tipo.equals("actualizar")) {
            helper.DropCreate(db);
        }
        else{
            helper.UpdateTable(db);
        }
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
        String[] ayuda = new String[13];
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
        ayuda[10] = response.body().getCondominio().getDistrito().getNombre();
        ayuda[11] = response.body().getCondominio().getDireccion();
        ayuda[12] = response.body().getCondominio().getDistrito().getDepartamento().getNombre();
        String query = "insert into Usuario (nombre, apellido, condominio, direccion, dni, email, fecha_Nacimiento, sexo, telefono, codigo,distrito_name,condominio_direccion,departamento_name) " +
                "values ('" + ayuda[0] + "', '" + ayuda[1] + "', '" + ayuda[2] + "', '" + ayuda[3] + "', '" + ayuda[4] + "', '" + ayuda[5] + "', '"
                + ayuda[6] + "', '" + ayuda[7] + "', '" + ayuda[8] + "', '" + ayuda[9] + "', '" + ayuda[10] + "', '" + ayuda[11] + "', '" + ayuda[12] + "')";
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

    public void InsertReciclador()throws  IOException, InterruptedException{
        dbHelper helper = new dbHelper(context,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor f1 = db.rawQuery("select distrito_name from Usuario ",null);
        f1.moveToFirst();
        String nombreDistrito = f1.getString(0);
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<Reciclador> call=jsonPlaceHolderApi.getReciclador("/reciclador/distrito/"+nombreDistrito);
        System.out.println(nombreDistrito);
        Response<Reciclador> response = call.execute();
        Reciclador p = response.body();
        String query = "insert into Reciclador (codigo,distrito ,asociacion ,zona , password ,salt ,apellido ,direccion ,dni ,email ,fecha_Nacimiento ,nombre ,codFormalizado ,celular ,imagen) " +
                "values ("+p.getCodigo()+",'"+p.getDistrito()+"','"+p.getAsociacion().getNombre()+"','"+p.getZona()+"','"+p.getPassword()+"','"+p.getSalt()+"','"+p.getApellido()+"','"+p.getDireccion()+"',+'"+
        p.getDni()+"','"+p.getEmail()+"','"+p.getFecha_Nacimiento()+"','"+p.getNombre()+"','"+p.getCodFormalizado()+"','"+p.getCelular()+"','"+p.getRecilador_Imagen()+"')";
        db.execSQL(query);
        System.out.println(query);
        db.close();
        Log.e("TAG","onResponse:" + response.toString());
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

    public void obtenerDatosProductByBolsa() throws IOException, InterruptedException{
        initialCounter();
        dbHelper helper = new dbHelper(context,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor f1 = db.rawQuery("select codigo from LastBolsas ",null);
        int codigoBolsa=0;
        if(f1.moveToFirst()){
            do {
                codigoBolsa = f1.getInt(0);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<List<Probolsa>> call = jsonPlaceHolderApi.getProductoByIdBolsa("probolsa/bolsa/" + codigoBolsa);
                Response<List<Probolsa>> response = call.execute();
                for (Probolsa probolsas : response.body()) {
                    if (probolsas.getProducto().getCategoria().getNombre().equals("Plastico")) {
                        plasticoCount += probolsas.getCantidad();
                        puntosPlastico += probolsas.getPuntuacion();
                        pesoPlastico += probolsas.getPeso();
                    } else if (probolsas.getProducto().getCategoria().getNombre().equals("Vidrio")) {
                        vidrioCount += probolsas.getCantidad();
                        puntosVidrio += probolsas.getPuntuacion();
                        pesoVidrio += probolsas.getPeso();
                    } else if (probolsas.getProducto().getCategoria().getNombre().equals("Metal")) {
                        metalCount += probolsas.getCantidad();
                        puntosMetal += probolsas.getPuntuacion();
                        pesoMetal += probolsas.getPeso();
                    } else {
                        papelCartonCount += probolsas.getCantidad();
                        puntosPapelCarton += probolsas.getPuntuacion();
                        pesoPapelCarton += probolsas.getPeso();
                    }
                    String query = "insert into LastProbolsas (codigo,bolsa) " +
                            "values (" + probolsas.getCodigo() + ","+probolsas.getBolsa().getCodigo()+")";
                    db.execSQL(query);
                    System.out.println(query);
                }
                generateQuery("LastBolsas", "Plastico", plasticoCount, pesoPlastico, puntosPlastico, codigoBolsa);
                generateQuery("LastBolsas", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio, codigoBolsa);
                generateQuery("LastBolsas", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton,codigoBolsa);
                generateQuery("LastBolsas", "Metal", metalCount, pesoMetal, puntosMetal, codigoBolsa);

            }while(f1.moveToNext()); }
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
                        "values (" + p.getCodigo() + ", 'true', " + null + ", " + p.getPuntuacion() + ", " + null
                        + ", " + null + ")";
                db.execSQL(query);
                System.out.println(query);
            } else{
                String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha,observaciones) " +
                        "values (" + p.getCodigo() + ", '" + p.getActiva() + "', '" + p.getCreadoFecha() + "', " + p.getPuntuacion() + ", " + p.getQrCode().getCodigo()
                        + ", '" + p.getRecojoFecha() + "','"+p.getObservaciones()+"')";
                db.execSQL(query);
                System.out.println(query);
            }
        }
        Log.e("TAG","onResponse:" + response.toString());
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
                    SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE",Locale.ENGLISH);


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
                generateQuery("Semana", "Plastico", plasticoCount, pesoPlastico, puntosPlastico,0);
                generateQuery("Semana", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio,0);
                generateQuery("Semana", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton,0);
                generateQuery("Semana", "Metal", metalCount, pesoMetal, puntosMetal,0);
                String query = "insert into DatosDiarios(tipo,lunes,martes,miercoles,jueves,viernes,sabado,domingo) " +
                        "values ('Semana', " + yAxisDataMonth[0] + ", " + yAxisDataMonth[1] + ", "
                        + yAxisDataMonth[2] + ", " + yAxisDataMonth[3] + ", " + yAxisDataMonth[4] + "," + yAxisDataMonth[5] + "," + yAxisDataMonth[6] + ")";
                db.execSQL(query);
                System.out.println(query);
            } else {
                generateQuery("Mes", "Plastico", plasticoCount, pesoPlastico, puntosPlastico,0);
                generateQuery("Mes", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio,0);
                generateQuery("Mes", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton,0);
                generateQuery("Mes", "Metal", metalCount, pesoMetal, puntosMetal,0);
                String query = "insert into DatosDiarios(tipo,lunes,martes,miercoles,jueves,viernes,sabado,domingo) " +
                        "values ('Mes', " + yAxisDataMonth[0] + ", " + yAxisDataMonth[1] + ", "
                        + yAxisDataMonth[2] + ", " + yAxisDataMonth[3] + ", " + yAxisDataMonth[4] + "," + yAxisDataMonth[5] + "," + yAxisDataMonth[6] + ")";
                db.execSQL(query);
                System.out.println(query);
            }
        }

        Log.e("TAG","onResponse:" + response.toString());
    }

    public void obtenerBolsasByDay()throws IOException, InterruptedException{
        pesoCount=0;
        bolsasCount=0;
        puntosCount=0;
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor fila = db.rawQuery("select codigo from Usuario", null);
        fila.moveToFirst();
        String codigo = fila.getString(fila.getColumnIndex("codigo"));
        Set<Integer> bolsas = new HashSet<>();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/bolsasDay/"+codigo);
        Response<List<Probolsa>> response = call.execute();
        for(Probolsa probolsas : response.body()){
            bolsas.add(probolsas.getBolsa().getCodigo());
            pesoCount+=probolsas.getPeso();
            puntosCount+=probolsas.getPuntuacion();
        }
        String query = "insert into Contador (tendenciaTipo,cantidad ,peso ,puntuacion ) " +
                "values ('Dia', " +  bolsas.size()+ ", " + pesoCount + ", "
                + puntosCount + ")";
        db.execSQL(query);
        System.out.println(query);


        Log.e("TAG","onResponse:" + response.toString());


    }

    public void obtenerUltimasBolsas()throws IOException, InterruptedException{
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor fila = db.rawQuery("select codigo from Usuario", null);
        fila.moveToFirst();
        int valor =0;
        String codigo = fila.getString(fila.getColumnIndex("codigo"));
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Bolsa>> call=jsonPlaceHolderApi.getBolsasByUsuario("bolsa/last/"+codigo);
        Response<List<Bolsa>> response = call.execute();
        for(Bolsa lastBolsas : response.body()){
            String query = "insert into LastBolsas (codigo) " +
                    "values (" + lastBolsas.getCodigo() + ")";
            db.execSQL(query);
            System.out.println(query);
        }

        Log.e("TAG","onResponse:" + response.toString());
    }

    public void obtenerBolsasByYear(String urlDate)throws IOException, InterruptedException{
        initialCounter();
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Set<Integer> bolsas = new HashSet<>();
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
                bolsasLast.add(bolsasbydate.getBolsa().getCodigo());
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
                bolsas.add(bolsasbydate.getBolsa().getCodigo());
                addingValuestoText(bolsasbydate);
            }

        }
        if(valor>0) {
            generateQuery("Year", "Plastico", plasticoCount, pesoPlastico, puntosPlastico,bolsas.size());
            generateQuery("Year", "Vidrio", vidrioCount, pesoVidrio, puntosVidrio,bolsas.size());
            generateQuery("Year", "Papel", papelCartonCount, pesoPapelCarton, puntosPapelCarton,bolsas.size());
            generateQuery("Year", "Metal", metalCount, pesoMetal, puntosMetal,bolsas.size());
            String query = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo) " +
                    "values ( " + yAxisDataYear[0] + ", " + yAxisDataYear[1] + ", "
                    + yAxisDataYear[2] + ", " + yAxisDataYear[3] + ", " + yAxisDataYear[4] + "," + yAxisDataYear[5]  + ", "
                    + yAxisDataYear[6] + ", " + yAxisDataYear[7] + ","+ + yAxisDataYear[8] + ","+yAxisDataYear[9] + ", " + yAxisDataYear[10] +","+ yAxisDataYear[11] +  ",'probolsa')";
            db.execSQL(query);
            System.out.println(query);

        }

        Log.e("TAG","onResponse:" + response.toString());
        System.out.println(bolsasLast.size());
        try {
            obtenerLasBolsasMonth("bolsasYear/");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void obtenerLasBolsasMonth(String urlDate) throws ParseException {
        initialCounter();
        dbHelper helper = new dbHelper(context, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        int valor = 0;
        for (Integer bolsasLast : bolsasLast) {
            Cursor f1 = db.rawQuery("select recojoFecha from Bolsa where codigo = "+ bolsasLast,null);
            if(f1.moveToFirst()){
                do {
                    valor++;
                    if(!f1.getString(0).equals("null") || !f1.getString(0).equals(null)) {
                        String sDate1 = f1.getString(0);
                        Date dia=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(sDate1);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dia);
                        if (cal.get(Calendar.MONTH) == 1)
                            yAxisDataYearBolsa[0] += 1;
                        else if (cal.get(Calendar.MONTH) == 2)
                            yAxisDataYearBolsa[1] += 1;
                        else if (cal.get(Calendar.MONTH) == 3)
                            yAxisDataYearBolsa[2] += 1;
                        else if (cal.get(Calendar.MONTH) == 4)
                            yAxisDataYearBolsa[3] += 1;
                        else if (cal.get(Calendar.MONTH) == 5)
                            yAxisDataYearBolsa[4] += 1;
                        else if (cal.get(Calendar.MONTH) == 6)
                            yAxisDataYearBolsa[5] += 1;
                        else if (cal.get(Calendar.MONTH) == 7)
                            yAxisDataYearBolsa[6] += 1;
                        else if (cal.get(Calendar.MONTH) == 8)
                            yAxisDataYearBolsa[7] += 1;
                        else if (cal.get(Calendar.MONTH) == 9)
                            yAxisDataYearBolsa[8] += 1;
                        else if (cal.get(Calendar.MONTH) == 10)
                            yAxisDataYearBolsa[9] += 1;
                        else if (cal.get(Calendar.MONTH) == 11)
                            yAxisDataYearBolsa[10] += 1;
                        else if (cal.get(Calendar.MONTH) == 12)
                            yAxisDataYearBolsa[11] += 1;
                    }

                }while(f1.moveToNext());
            }

        }
        if(valor>0) {
            String query = "insert into DatosAnuales(enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre,tipo) " +
                    "values ( " + yAxisDataYearBolsa[0] + ", " + yAxisDataYearBolsa[1] + ", "
                    + yAxisDataYearBolsa[2] + ", " + yAxisDataYearBolsa[3] + ", " + yAxisDataYearBolsa[4] + "," + yAxisDataYearBolsa[5]  + ", "
                    + yAxisDataYearBolsa[6] + ", " + yAxisDataYearBolsa[7] + ","+ + yAxisDataYearBolsa[8] + ","+yAxisDataYearBolsa[9] + ", " + yAxisDataYearBolsa[10] +","+ yAxisDataYearBolsa[11] +  ",'bolsa')";
            db.execSQL(query);
            System.out.println(query);

        }
    }

    public void initialCounter(){
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

    public void generateQuery(String tipo,String producto,double cantidad,double peso,double puntuacion,int bolsaID){
        String query = "insert into Contador (tendenciaTipo,productoTipo,cantidad,peso,puntuacion,bolsa) " +
                "values ('" + tipo + "', '" + producto + "', " + cantidad + ", "
                + peso + ", " + puntuacion + ","+bolsaID+")";
        db.execSQL(query);
        System.out.println(query);
    }

}
