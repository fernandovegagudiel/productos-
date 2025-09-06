package com.example.vibecoding1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        TextView textDetalle = findViewById(R.id.text_detalle_reporte);
        Button btnVolver = findViewById(R.id.btn_volver);

        Intent intent = getIntent();
        String estado = intent.getStringExtra("estado");
        String fecha = intent.getStringExtra("fecha");

        ProductoDbHelper dbHelper = new ProductoDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "1=1";
        java.util.List<String> args = new java.util.ArrayList<>();
        if (estado != null && !estado.isEmpty() && !estado.equals("Todos")) {
            selection += " AND estado = ?";
            args.add(estado);
        }
        if (fecha != null && !fecha.isEmpty()) {
            selection += " AND fecha = ?";
            args.add(fecha);
        }
        int pendientes = 0, comprados = 0, total = 0;
        Cursor c = db.query(ProductoDbHelper.TABLE_NAME, null, selection, args.toArray(new String[0]), null, null, null);
        while (c.moveToNext()) {
            String est = c.getString(c.getColumnIndexOrThrow(ProductoDbHelper.COLUMN_ESTADO));
            if (est.equals("Pendiente de compra")) pendientes++;
            if (est.equals("Comprado")) comprados++;
            total++;
        }
        c.close();
        StringBuilder reporte = new StringBuilder();
        reporte.append("Total de productos: ").append(total).append("\n");
        reporte.append("Pendientes de compra: ").append(pendientes).append("\n");
        reporte.append("Comprados: ").append(comprados).append("\n");
        if (estado != null && !estado.isEmpty() && !estado.equals("Todos")) {
            reporte.append("\nFiltro por estado: ").append(estado);
        }
        if (fecha != null && !fecha.isEmpty()) {
            reporte.append("\nFiltro por fecha: ").append(fecha);
        }
        textDetalle.setText(reporte.toString());

        btnVolver.setOnClickListener(v -> finish());
    }
}
