package com.example.reciclemosdemo.Grafico;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class WeekFragment extends Fragment {
    private com.example.reciclemosdemo.Grafico.RetrofitMain retrofit;
    private ArrayList<TextView> textList = new ArrayList<>();
    TextView txtPlasticoCount,txtPlasticoPuntos,txtPlasticoPeso;
    TextView txtVidrioCount,txtVidrioPuntos,txtVidrioPeso;
    TextView txtMetalesCount,txtMetalesPuntos,txtMetalesPeso;
    TextView txtPapelCartonCount,txtPapelCartonPuntos,txtPapelCartonPeso,txtResiduosCount,txtPesoResiduos,txtPuntajeResiduos;
    LineChartView lineChartView;
    private int totalResiduo,totalPeso,totalPuntos;
    private int [] yAxisDataYear= {0,0,0,0,0,0,0};
    String[] axisDataMonth = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofit = new com.example.reciclemosdemo.Grafico.RetrofitMain();
        txtPlasticoCount =getView().findViewById(R.id.txtPlasticoCantidad) ;
        txtPlasticoPuntos =getView().findViewById(R.id.txtPlasticoPuntos) ;
        txtPlasticoPeso =getView().findViewById(R.id.txtPlasticoPeso) ;
        txtVidrioCount =getView().findViewById(R.id.txtVidrioCantidad) ;
        txtVidrioPuntos =getView().findViewById(R.id.txtVidrioPuntos) ;
        txtVidrioPeso =getView().findViewById(R.id.txtVidrioPeso) ;
        txtMetalesCount =getView().findViewById(R.id.txtMetalesCantidad) ;
        txtMetalesPuntos =getView().findViewById(R.id.txtMetalesPuntos) ;
        txtMetalesPeso =getView().findViewById(R.id.txtMetalesPeso);
        txtPapelCartonCount =getView().findViewById(R.id.txtPapelCartonCantidad) ;
        txtPapelCartonPuntos =getView().findViewById(R.id.txtPapelCartonPuntos) ;
        txtPapelCartonPeso =getView().findViewById(R.id.txtPapelCartonPeso) ;
        lineChartView = getView().findViewById(R.id.chart);
        txtResiduosCount = getView().findViewById(R.id.txtCantBolsasHoy);
        txtPesoResiduos = getView().findViewById(R.id.txtPesoBolsasHoy);
        txtPuntajeResiduos = getView().findViewById(R.id.txtPtosBolsasHoy);
        totalPeso=0;
        totalPuntos=0;
        totalResiduo=0;
        getDataWeek();
        graphicData();
        txtResiduosCount.setText(Integer.toString(totalResiduo));
        txtPesoResiduos.setText(Integer.toString(totalPeso));
        txtPuntajeResiduos.setText(Integer.toString(totalPuntos));
    }

    public void getDataWeek() {
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Semana' and productoTipo = 'Plastico' ", null);

        if(f.moveToFirst()) {
            txtPlasticoCount.setText(Integer.toString(f.getInt(0)));
            txtPlasticoPeso.setText(Double.toString(f.getDouble(1))+ " g");
            txtPlasticoPuntos.setText(Double.toString(f.getDouble(2))+ " ptos");
            totalResiduo+=f.getInt(0);
            totalPuntos+=f.getDouble(2);
            totalPeso+=f.getDouble(1);

        }
        Cursor f2 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Semana' and productoTipo = 'Vidrio' ", null);


        if(f2.moveToFirst()) {
            txtVidrioCount.setText(Integer.toString(f2.getInt(0)));
            txtVidrioPeso.setText(Double.toString(f2.getDouble(1))+ " g");
            txtVidrioPuntos.setText(Double.toString(f2.getDouble(2))+ " ptos");
            totalResiduo+=f2.getInt(0);
            totalPuntos+=f2.getDouble(2);
            totalPeso+=f2.getDouble(1);
        }

        Cursor f3 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Semana' and productoTipo = 'Papel' ", null);
        if(f3.moveToFirst()) {
            txtPapelCartonCount.setText(Integer.toString(f3.getInt(0)));
            txtPapelCartonPeso.setText(Double.toString(f3.getDouble(1)) + " g");
            txtPapelCartonPuntos.setText(Double.toString(f3.getDouble(2))+ " ptos");
            totalResiduo+=f3.getInt(0);
            totalPuntos+=f3.getDouble(2);
            totalPeso+=f3.getDouble(1);
        }

        Cursor f4 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Semana' and productoTipo = 'Metal' ", null);

        if(f4.moveToFirst()) {
            txtMetalesCount.setText(Integer.toString(f4.getInt(0)));
            txtMetalesPeso.setText(Double.toString(f4.getDouble(1))+ " g");
            txtMetalesPuntos.setText(Double.toString(f4.getDouble(2))+ " ptos");
            totalResiduo+=f4.getInt(0);
            totalPuntos+=f4.getDouble(2);
            totalPeso+=f4.getDouble(1);
        }
    }

    public void graphicData(){
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select lunes,martes,miercoles,jueves,viernes,sabado,domingo from DatosDiarios where tipo = 'Semana' ", null);
        if(f.moveToFirst()) {

            for (int i = 0; i < 7; i++) {
                yAxisDataYear[i] = f.getInt(i);
            }
        }
        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#252525"));
        for (int i = 0; i < axisDataMonth.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisDataMonth[i]));
        }

        for (int i = 0; i < yAxisDataYear.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisDataYear[i]));
        }
        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(13);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(13);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 5;
        lineChartView.animate().alpha(1f).setDuration(250);
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);


    }

}