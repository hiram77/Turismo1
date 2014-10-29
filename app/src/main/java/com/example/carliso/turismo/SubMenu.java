package com.example.carliso.turismo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by Carliso on 27/10/2014.
 */
public class SubMenu extends Activity {
    protected static final int REQUEST_CODE = 20;
    GridView gridView;
    String[] gastro={"tienda1","tienda2","tienda3"};
    String[] muse={"museo1","museo2","museo3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_grid);
        Bundle extras=getIntent().getExtras();
        TextView d=(TextView)findViewById(R.id.textCat);

        d.setText(extras.getString("categoria"));
        gridView=(GridView)findViewById(R.id.gridCat);
        if(extras !=null) {
            String cate=extras.getString("categoria");
            Log.d("categoria",cate);
            //cate="gastronomia";
            if(cate.equals("Gastronomia")) {
                gridView.setAdapter(new ItemGrid(this, gastro, cate));
            }
            else
                gridView.setAdapter(new ItemGrid(this, muse, cate));
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                Intent intent = new Intent(SubMenu.this, Objeto.class);
                intent.putExtra("id",id);
                intent.putExtra("posicion",posicion);
                intent.putExtra("cate",posicion);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });



    }
}
