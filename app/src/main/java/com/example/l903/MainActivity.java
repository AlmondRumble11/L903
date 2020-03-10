package com.example.l903;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.Date;



public class MainActivity extends AppCompatActivity {
    private Context context;
    private  String nimi;
    private  ListView elokuvat;
    private String ajankohta;
    private Spinner teatteriValikko;
    private TeatteriLista tl = TeatteriLista.getInstance();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        teatteriValikko = (Spinner) findViewById(R.id.teatteriValikko);
        tl.lueXML();//saadaan teatterien tiedot

        //dopdown menun tekeminen annetuista tiedoista
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tl.getTeatterilista());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teatteriValikko.setAdapter(adapter);

        //elokuva tietojen hankkiminen
        nimi = teatteriValikko.getSelectedItem().toString();




    }

    public void ilmotus(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public void nappi(View v) {
        nimi = teatteriValikko.getSelectedItem().toString();
        try{
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();
            System.out.println(dateFormat.format(date));
            ajankohta = dateFormat.format(date);


            //otetaan aloitus ja lopetus ajat erilleen

            System.out.println("pvm " + ajankohta);

            tl.luePvm(nimi, ajankohta);

            elokuvat = (ListView) findViewById(R.id.listview);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, tl.getElokuvalista());
            elokuvat.setAdapter(arrayAdapter);
            if (tl.getElokuvalista().size() == 0) {
                ilmotus("Sorry no movies this time");
            }
        } catch(ArrayIndexOutOfBoundsException aioobe) {//tulee jos käyttäj ei osaa antaa oikeassa formaatissa aikaväliä
            ilmotus("Wrong timespan format");
            elokuvat.setAdapter(null);
        }
    }
}
