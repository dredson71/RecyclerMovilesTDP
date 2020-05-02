package com.example.reciclemosdemo.Grafico;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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


public class YearFragment extends Fragment {
    View view;
    private com.example.reciclemosdemo.Grafico.RetrofitMain retrofit;
    private ArrayList<TextView> textList = new ArrayList<>();
    TextView txtPlasticoCount,txtPlasticoPuntos,txtPlasticoPeso;
    TextView txtVidrioCount,txtVidrioPuntos,txtVidrioPeso;
    TextView txtMetalesCount,txtMetalesPuntos,txtMetalesPeso;
    TextView txtPapelCartonCount,txtPapelCartonPuntos,txtPapelCartonPeso,txtResiduosCount,txtPesoResiduos,txtPuntajeResiduos;
    LineChartView lineChartView,lineChartViewBolsa;
    String[] axisDataMonth = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
            "Oct", "Nov", "Dec"};
    int [] yAxisDataYear = {0,0,0,0,0,0,0,0,0,0,0,0};
    int [] yAxisDataYearBolsa = {0,0,0,0,0,0,0,0,0,0,0,0};

    private int totalResiduo,totalPeso,totalPuntos;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_year,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofit = new com.example.reciclemosdemo.Grafico.RetrofitMain();
        txtPlasticoCount =view.findViewById(R.id.txtPlasticoCantidad) ;
        txtPlasticoPuntos =view.findViewById(R.id.txtPlasticoPuntos) ;
        txtPlasticoPeso =view.findViewById(R.id.txtPlasticoPeso) ;
        txtVidrioCount =view.findViewById(R.id.txtVidrioCantidad) ;
        txtVidrioPuntos =view.findViewById(R.id.txtVidrioPuntos) ;
        txtVidrioPeso =view.findViewById(R.id.txtVidrioPeso) ;
        txtMetalesCount =view.findViewById(R.id.txtMetalesCantidad) ;
        txtMetalesPuntos =view.findViewById(R.id.txtMetalesPuntos) ;
        txtMetalesPeso =view.findViewById(R.id.txtMetalesPeso);
        txtPapelCartonCount =view.findViewById(R.id.txtPapelCartonCantidad) ;
        txtPapelCartonPuntos =view.findViewById(R.id.txtPapelCartonPuntos) ;
        txtPapelCartonPeso =view.findViewById(R.id.txtPapelCartonPeso) ;
        lineChartView = view.findViewById(R.id.chart2);
        lineChartViewBolsa = view.findViewById(R.id.chart);
        txtResiduosCount = getView().findViewById(R.id.txtCantBolsasHoy);
        txtPesoResiduos = getView().findViewById(R.id.txtPesoBolsasHoy);
        txtPuntajeResiduos = getView().findViewById(R.id.txtPtosBolsasHoy);
        totalPeso=0;
        totalPuntos=0;
        totalResiduo=0;
        getDataWeek();
        graphicData();
        graphicData2();
        txtResiduosCount.setText(Integer.toString(totalResiduo));
        txtPesoResiduos.setText(Integer.toString(totalPeso/1000));
        txtPuntajeResiduos.setText(Integer.toString(totalPuntos));

    }

    public void getDataWeek() {
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Year' and productoTipo = 'Plastico' ", null);

        if(f.moveToFirst()) {
            txtPlasticoCount.setText(Integer.toString(f.getInt(0)));
            txtPlasticoPeso.setText(Integer.toString((int)f.getDouble(1)/1000)+ " kg");
            txtPlasticoPuntos.setText(Integer.toString((int)f.getDouble(2))+ " ptos");
            totalResiduo+=f.getInt(0);
            totalPuntos+=f.getDouble(2);
            totalPeso+=f.getDouble(1);

        }
        Cursor f2 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Year' and productoTipo = 'Vidrio' ", null);


        if(f2.moveToFirst()) {
            txtVidrioCount.setText(Integer.toString(f2.getInt(0)));
            txtVidrioPeso.setText(Integer.toString((int)f2.getDouble(1)/1000)+ " kg");
            txtVidrioPuntos.setText(Integer.toString((int)f2.getDouble(2))+ " ptos");
            totalResiduo+=f2.getInt(0);
            totalPuntos+=f2.getDouble(2);
            totalPeso+=f2.getDouble(1);
        }

        Cursor f3 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Year' and productoTipo = 'Papel' ", null);
        if(f3.moveToFirst()) {
            txtPapelCartonCount.setText(Integer.toString(f3.getInt(0)));
            txtPapelCartonPeso.setText(Integer.toString((int)f3.getDouble(1)/1000)+ " kg");
            txtPapelCartonPuntos.setText(Integer.toString((int)f3.getDouble(2)) + " ptos");
            totalResiduo+=f3.getInt(0);
            totalPuntos+=f3.getDouble(2);
            totalPeso+=f3.getDouble(1);
        }

        Cursor f4 = db.rawQuery("select cantidad ,peso ,puntuacion from Contador where tendenciaTipo = 'Year' and productoTipo = 'Metal' ", null);

        if(f4.moveToFirst()) {
            txtMetalesCount.setText(Integer.toString(f4.getInt(0)));
            txtMetalesPeso.setText(Integer.toString((int)f4.getDouble(1)/1000)+ " kg");
            txtMetalesPuntos.setText(Integer.toString((int)f4.getDouble(2)) + " ptos");
            totalResiduo+=f4.getInt(0);
            totalPuntos+=f4.getDouble(2);
            totalPeso+=f4.getDouble(1);
        }
    }

    public void graphicData(){
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre from DatosAnuales where tipo = 'probolsa'", null);

        if(f.moveToFirst()) {
            for (int i = 0; i < 12; i++) {
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
        viewport.top = 20;
        lineChartView.animate().alpha(1f).setDuration(250);
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);

    }

    public void graphicData2(){
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select enero,febrero,marzo,abril,mayo,junio,julio,agosto,setiembre,octubre,noviembre,diciembre from DatosAnuales where tipo = 'bolsa'", null);

        if(f.moveToFirst()) {
            for (int i = 0; i < 12; i++) {
                yAxisDataYearBolsa[i] = f.getInt(i);
            }
        }
        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#252525"));
        for (int i = 0; i < axisDataMonth.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisDataMonth[i]));
        }

        for (int i = 0; i < yAxisDataYearBolsa.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisDataYearBolsa[i]));
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

        lineChartViewBolsa.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 20;
        lineChartViewBolsa.animate().alpha(1f).setDuration(250);
        lineChartViewBolsa.setMaximumViewport(viewport);
        lineChartViewBolsa.setCurrentViewport(viewport);

    }



}

