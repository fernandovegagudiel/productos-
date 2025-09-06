package com.example.vibecoding1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private ProductoDbHelper dbHelper;
    private Spinner spinnerEstado;
    private EditText editFecha;
    private Button btnFiltrar, btnAgregar, btnReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new ProductoDbHelper(this);
        recyclerView = findViewById(R.id.recycler_productos);
        spinnerEstado = findViewById(R.id.spinner_estado);
        editFecha = findViewById(R.id.edit_fecha);
        btnFiltrar = findViewById(R.id.btn_filtrar);
        btnAgregar = findViewById(R.id.btn_agregar);
        btnReporte = findViewById(R.id.btn_reporte);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductoAdapter();
        recyclerView.setAdapter(adapter);

        btnFiltrar.setOnClickListener(v -> cargarProductos());
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditActivity.class);
            startActivityForResult(intent, 1);
        });
        btnReporte.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("estado", spinnerEstado.getSelectedItem().toString());
            intent.putExtra("fecha", editFecha.getText().toString().trim());
            startActivity(intent);
        });

        cargarProductos();
    }

    private void cargarProductos() {
        String estado = spinnerEstado.getSelectedItem() != null ? spinnerEstado.getSelectedItem().toString() : "";
        String fecha = editFecha.getText().toString().trim();
        List<Producto> productos = dbHelper.obtenerProductosFiltrados(estado, fecha);
        adapter.setProductos(productos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            cargarProductos();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Función para alternar entre modo claro y oscuro
    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_theme) {
            toggleTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}