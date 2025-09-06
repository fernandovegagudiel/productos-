package com.example.vibecoding1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ProductoDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "productos.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "productos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_ESTADO = "estado";
    public static final String COLUMN_FECHA = "fecha";

    public ProductoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_ESTADO + " TEXT, " +
                COLUMN_FECHA + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<Producto> obtenerProductosFiltrados(String estado, String fecha) {
        List<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = "";
        List<String> args = new ArrayList<>();
        if (!estado.equals("Todos")) {
            selection += COLUMN_ESTADO + "=?";
            args.add(estado);
        }
        if (!fecha.isEmpty()) {
            if (!selection.isEmpty()) selection += " AND ";
            selection += COLUMN_FECHA + "=?";
            args.add(fecha);
        }
        Cursor c = db.query(TABLE_NAME, null, selection.isEmpty() ? null : selection, args.isEmpty() ? null : args.toArray(new String[0]), null, null, COLUMN_FECHA + " DESC");
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(COLUMN_ID));
            String nombre = c.getString(c.getColumnIndexOrThrow(COLUMN_NOMBRE));
            String estadoDb = c.getString(c.getColumnIndexOrThrow(COLUMN_ESTADO));
            String fechaDb = c.getString(c.getColumnIndexOrThrow(COLUMN_FECHA));
            lista.add(new Producto(id, nombre, estadoDb, fechaDb));
        }
        c.close();
        return lista;
    }
}


