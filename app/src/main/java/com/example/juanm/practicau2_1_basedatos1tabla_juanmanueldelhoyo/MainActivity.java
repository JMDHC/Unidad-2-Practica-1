package com.example.juanm.practicau2_1_basedatos1tabla_juanmanueldelhoyo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    EditText id, dias, descripcion, calorias;
    Button nuevo, buscar, eliminar, actualizar;
    ConexionBase base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id=findViewById(R.id.id);
        dias=findViewById(R.id.dias);
        descripcion=findViewById(R.id.descripcion);
        calorias=findViewById(R.id.calorias);
        nuevo=findViewById(R.id.nuevo);
        buscar=findViewById(R.id.buscar);
        eliminar=findViewById(R.id.eliminar);
        actualizar=findViewById(R.id.actualizar);

        base=new ConexionBase(MainActivity.this, "Registro_Rutinas", null, 1);

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertar();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
            }
        });
    }

    void limpiarCampos(){
        id.setText("");
        dias.setText("");
        descripcion.setText("");
        calorias.setText("");
    }

    void mensaje(String m){
        Toast.makeText(this, m,Toast.LENGTH_SHORT).show();
    }

    private void insertar() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText dias = new EditText(this);
        dias.setHint("Dias...");
        final EditText descripcion = new EditText(this);
        descripcion.setHint("Descripción...");
        final EditText calorias = new EditText(this);
        calorias.setHint("Calorias quemadas...");
        layout.addView(dias);
        layout.addView(descripcion);
        layout.addView(calorias);
        alerta.setTitle("Insertar")
                .setMessage("Ingrese los datos a insertar:")
                .setView(layout)
                .setPositiveButton("Guardar", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    SQLiteDatabase base_ = base.getWritableDatabase();
                                    ContentValues datos=new ContentValues();
                                    datos.put("Dias", dias.getText().toString());
                                    datos.put("Descripcion", descripcion.getText().toString());
                                    datos.put("CaloriasQuemadas", calorias.getText().toString());
                                    base_.insert("Rutinas","Id", datos);
                                    base.close();
                                    mensaje("Se inserto correctamente el dato");
                                }catch (Exception e){
                                    mensaje(e.getMessage());
                                }
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("Cancelar", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

    private void buscar() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        final EditText campo = new EditText(this);
        alerta.setTitle("Buscar")
                .setMessage("Escriba el Id a buscar:")
                .setView(campo)
                .setPositiveButton("Buscar", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                actualizar.setEnabled(true);
                                eliminar.setEnabled(true);
                                buscarId(campo.getText().toString());
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("Cancelar", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

    void buscarId(String dato){
        try{
            SQLiteDatabase base_ = base.getReadableDatabase();
            String[] claves = {dato};
            Cursor c = base_.rawQuery("SELECT * FROM Rutinas WHERE Id = ?",claves);
            if(c.moveToFirst()){
                id.setText(c.getString(0));
                dias.setText(c.getString(1));
                descripcion.setText(c.getString(2));
                calorias.setText(c.getString(3));
            } else {
                mensaje("No se encontró coincidencia con "+dato);
                eliminar.setEnabled(false);
                actualizar.setEnabled(false);
            }
            base.close();
        } catch (SQLiteException e){
            mensaje(e.getMessage());
        }
    }

    private void eliminar(){
        try{
            SQLiteDatabase base_ = base.getWritableDatabase();
            String SQL = "DELETE FROM Rutinas WHERE Id = '"+id.getText().toString()+"'";
            base_.execSQL(SQL);
            base.close();
            mensaje("Se elimino correctamente el dato");
            limpiarCampos();
            eliminar.setEnabled(false);
            actualizar.setEnabled(false);
        }catch (SQLiteException e){
            mensaje(e.getMessage());
        }
    }

    private void actualizar() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText dias = new EditText(this);
        dias.setText(this.dias.getText().toString());
        final EditText descripcion = new EditText(this);
        descripcion.setText(this.descripcion.getText().toString());
        final EditText calorias = new EditText(this);
        calorias.setText(this.calorias.getText().toString());
        layout.addView(dias);
        layout.addView(descripcion);
        layout.addView(calorias);
        alerta.setTitle("Actualizar")
                .setMessage("Puede modificar los datos:")
                .setView(layout)
                .setPositiveButton("Actualizar", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    SQLiteDatabase base_ = base.getWritableDatabase();
                                    ContentValues cv = new ContentValues();
                                    cv.put("Dias", dias.getText().toString());
                                    cv.put("Descripcion", descripcion.getText().toString());
                                    cv.put("CaloriasQuemadas", calorias.getText().toString());
                                    base_.update("Rutinas",cv,"Id = "+id.getText().toString(), null);
                                    base.close();
                                    mensaje("Se Actualizo correctamente el dato");
                                    buscarId(id.getText().toString());
                                }catch (SQLiteException e){
                                    mensaje(e.getMessage());
                                }
                            }
                        }).setNegativeButton("Cancelar", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

}
