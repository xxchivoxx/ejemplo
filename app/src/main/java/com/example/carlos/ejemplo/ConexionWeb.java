package com.example.carlos.ejemplo;


import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConexionWeb extends AsyncTask<URL,String,String> {
    List<String[]> variables; //vector dinamico
    AsyncResponse delegado;


    public ConexionWeb(AsyncResponse p){
        delegado = p;
        variables = new ArrayList<>();
    }


    public void agregarVariables(String nombreVariable, String contenidoVariable){
        String[] temporal ={nombreVariable,contenidoVariable};
        variables.add(temporal);
    }
    private String generarCadenaPost(){
        String post="";
        try {
            for (int i = 0; i < variables.size(); i++) {
                String[] temporal = variables.get(i);
                post += temporal[0] + "=" + URLEncoder.encode(temporal[1], "UTF-8") + " ";
            }
        }catch (Exception e){

        }
        post=post.trim();
        post = post.replaceAll(" ","&");
        return post;
    }
    @Override
    protected String doInBackground(URL...params)
    {
        String POST = "?"+generarCadenaPost();
        URL url = null; //Url de donde queremos obtener la informacion
        String devuelve = "";
        //COnsultar a todos los alumnos

            try {
                url = new URL(params[0].toString()+POST);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexion
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                }
                devuelve = result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return devuelve;
    }
    public void onPostExecute(String r){

        delegado.procesarRespuesta(r);
    }
}
