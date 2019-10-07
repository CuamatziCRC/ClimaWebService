package com.example.cuamatzi.climawebservice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView Nombredelaciudad,Reslutado;
    Button Botondebusqueda;

    class Weather extends AsyncTask<String,Void,String>{



        @Override
        protected String doInBackground(String... address) {
            try{
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                //Se establece la conexion con la direccion
                connection.connect();

                //Se recupera la informacion del URL
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                 //Se recupera la informacion y se retorna en una cadena
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char)data;
                    content = content + ch ;
                    data = isr.read();
                }
                return content;
            }
            catch (MalformedURLException e){e.printStackTrace();}
            catch (IOException e){e.printStackTrace();}
            return null;
        }
    }

    public void  search (View view){


        String content;
        Weather weather = new Weather();
        //Obtencion de datos
        Nombredelaciudad = findViewById(R.id.NombreCiudad);
        Botondebusqueda = findViewById(R.id.busqueda);
        Reslutado = findViewById(R.id.Resultado);

        //remplazamos la ciudad
        String CNombre = Nombredelaciudad.getText().toString();



        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="+CNombre+ "&appid=b6907d289e10d714a6e88b30761fae22"+"&lang=es").get();

            //se verifica si se envia o no la informacion
            Log.i("contentData",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperatura = jsonObject.getString("main"); //este principal no es parte del arreglo del clima, es una variable separada como el clima

            //Log.i("weathertData",weatherData);


            //Se ordena en un arreglo  los datos
            JSONArray array = new JSONArray(weatherData);

            //Datos del JSON
            String main = "";
            String description = "";
            String temperatura = "";
            double visibilidad;




            for (int i= 0 ; i<array.length();i++){
                JSONObject weatherPart =array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");

            }
            JSONObject mainPart = new JSONObject(mainTemperatura);
            temperatura = mainPart.getString("temp");

            visibilidad = Double.parseDouble(jsonObject.getString("visibility"));
            //casteamos la visibilidad
            int vk = (int)visibilidad/1000;

            Log.i("Temperature",temperatura);

            if ( main.equals("Clouds")){
                main = "Nublado";
            }else  if (main.equals("Rain")){
                main = "LLuvia";
            }else if (main.equals("Drizzle")){
                main = "Llovizna";
            }else if (main.equals("Snow")){
            main = "nevar";
            } else  if (main.equals("Sunny")){
                main = "Soleado";
            } else  if (main.equals("Humid")){
                main = "Humedo";
            }else if(main.equals("Clear")){
                main = "Despejado";
            }else  if(main.equals("Heat")){
                main = "Caluroso";
            }else  if(main.equals("Cool")){
                main = "Fresco";
            }else  if(main.equals("Windy")){
                main = "Con Viento";
            }
            if (description.equals("clear sky")){

                description = "Cielo despejado";

            }else if (description.equals("few clouds")){

                description = "Pocas nubes";

            }else  if (description.equals("scattered clouds")){

                description = "Nubes dispersas";

            }else  if (description.equals("broken clouds")){

                description = "Nubes rotas";

            }else if (description.equals("shower rain")){

                description = "Muchas lluvia";

            }else if (description.equals("rain")){

                description = "Lluvia";

            }else if (description.equals("thunderstorm")){

                description = "Tormenta electrica";

            }else if (description.equals("snow")){

                description = "Nieve";

            }else if (description.equals("mist")){

                description = "Niebla";

            }else  if (description.equals("light rain")){

                description = "LLuvia ligera";
            }

                String resultado = "Principal : "+ main + "\nDescripción : "+description+"\n Temperatura :"+temperatura+"°C"+"\nViento :" +vk+"KM";

                Reslutado.setText(resultado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
