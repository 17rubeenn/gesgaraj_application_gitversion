package es.gtec.gesgaraj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import es.gtec.gesgaraj.conexion.ConexionBD;

public class MainActivity extends AppCompatActivity {

EditText txt_usuario;
EditText txt_contrasena;
Button btn_entrar;
Connection conexion;

public MainActivity(){
    ConexionBD instanceConnection = new ConexionBD();
    conexion = instanceConnection.connect();
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_usuario=(EditText) findViewById(R.id.txt_usuario);
        txt_contrasena=(EditText) findViewById(R.id.txt_contrasena);
        btn_entrar=(Button) findViewById(R.id.btn_entrar);

        btn_entrar.setOnClickListener(new View.OnClickListener() { //CUANDO EL BOTON SEA ACTIVADO REALIZARA TODO LO ANTERIOR PROGRAMADO EN EL METODO
            @Override
            public void onClick(View v) {
                new MainActivity.log().execute("");
            }
        });

    }

    public class log extends AsyncTask<String, String, String>{
    String z = null;
    Boolean exito = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            if(conexion==null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "COMPRUEBA LA CONEXION", Toast.LENGTH_SHORT).show();
                    }
                });
                z= "en conexion";
            }else {
                try{
                    String sql= "SELECT * FROM USUARIO WHERE usuario = '" + txt_usuario.getText() + "' AND  contrasena = '" + txt_contrasena.getText()+ "'";
                    Statement stm = conexion.createStatement();
                    ResultSet rs = stm.executeQuery(sql);

                    if(rs.next()){ //VERIFICA LA CONSULTA DE ARRIBA
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "ACCEDIENDO CON EXITO", Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(getApplicationContext(), MainPage.class);
                                startActivity(intent);
                            }
                        });

                        txt_usuario.setText(""); // LIMPIA LOS DATOS
                        txt_contrasena.setText("");
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "USUARIO O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                            }
                        });
                        txt_usuario.setText(""); // LIMPIA LOS DATOS
                        txt_contrasena.setText("");
                    }

                } catch (Exception e){
                    exito=false;
                    Log.e("Error de conexion: ", e.getMessage());
                }
            }

            return z;
        }
    }
}