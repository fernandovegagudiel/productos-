package com.example.vibecoding1;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {
    private EditText editNombre, editFecha;
    private Spinner spinnerEstado;
    private Button btnGuardar;
    private ProductoDbHelper dbHelper;
    private long productoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dbHelper = new ProductoDbHelper(this);
        editNombre = findViewById(R.id.edit_nombre);
        editFecha = findViewById(R.id.edit_fecha);
        spinnerEstado = findViewById(R.id.spinner_estado);
        btnGuardar = findViewById(R.id.btn_guardar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.estados_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            productoId = intent.getLongExtra("id", -1);
            editNombre.setText(intent.getStringExtra("nombre"));
            editFecha.setText(intent.getStringExtra("fecha"));
            String estado = intent.getStringExtra("estado");
            if (estado != null) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).toString().equals(estado)) {
                        spinnerEstado.setSelection(i);
                        break;
                    }
                }
            }
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editNombre.getText().toString().trim();
                String estado = spinnerEstado.getSelectedItem().toString();
                String fecha = editFecha.getText().toString().trim();
                if (nombre.isEmpty() || fecha.isEmpty()) {
                    Toast.makeText(AddEditActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                values.put(ProductoDbHelper.COLUMN_NOMBRE, nombre);
                values.put(ProductoDbHelper.COLUMN_ESTADO, estado);
                values.put(ProductoDbHelper.COLUMN_FECHA, fecha);
                if (productoId == -1) {
                    dbHelper.getWritableDatabase().insert(ProductoDbHelper.TABLE_NAME, null, values);
                } else {
                    dbHelper.getWritableDatabase().update(
                        ProductoDbHelper.TABLE_NAME, values, ProductoDbHelper.COLUMN_ID + "=?", new String[]{String.valueOf(productoId)});
                }
                setResult(RESULT_OK);
                finish();
            }
        });

        // Botón para alternar modo claro/oscuro
        Button btnToggleTheme = findViewById(R.id.btn_toggle_theme);
        btnToggleTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTheme();
            }
        });
    }

    // Función para alternar entre modo claro y oscuro
    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }
}
