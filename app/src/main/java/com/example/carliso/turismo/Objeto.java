package com.example.carliso.turismo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Carliso on 27/10/2014.
 */
public class Objeto extends Activity {
    protected static final int REQUEST_CODE = 20;
    String nameObject ="Mi objecto";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_object);
        final View pie= View.inflate(this,R.layout.object_pie,null);
        final View cabecera=View.inflate(this,R.layout.object_header,null);
        ListView lista=(ListView) findViewById(R.id.listaObjeto);
        AdapterObject adapterObject=new AdapterObject(getApplicationContext(),"Primer Object");
        lista.addFooterView(pie);
        lista.addHeaderView(cabecera);
        lista.setAdapter(adapterObject);
        //Acciones del Header
        TextView tit=(TextView)cabecera.findViewById(R.id.TH);
        tit.setText("Titulo del objeto que se pretende visualizar");
        ImageButton imageButton=(ImageButton)cabecera.findViewById(R.id.imageButtonH);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( );
                //intent.putExtra("ciudad",ciudad.getText().toString() );
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        //Listener de los botones de pie
        Button p1= (Button) pie.findViewById(R.id.imageOInfo);
        Button p2= (Button) pie.findViewById(R.id.imageOMap);
        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Se ha pulsado info! ", Toast.LENGTH_LONG).show();

            }
        });
        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Se ha pulsado mapa! ", Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                MainActivity.MapaFragment fragmentS1 = new MainActivity.MapaFragment();
                fragmentTransaction.replace(R.id.fragmentO,fragmentS1 );
                fragmentTransaction.commit();
            }
        });


    }
}
