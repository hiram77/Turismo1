package com.example.carliso.turismo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Carliso on 27/10/2014.
 */
public class ItemGrid extends BaseAdapter {
    private Context context;
    private final String[] valores;
    private  final String cat;
    public ItemGrid(Context c, String[] elements,String categoria)
    {
        context=c;
        valores=elements;
        cat=categoria;
    }

    @Override
    public int getCount() {
        return valores.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.adapter_grid, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(valores[position]);

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String img = valores[position];


            imageView.setImageResource(devolverImagen(position,valores,cat));

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    private int devolverImagen(int position, String[] valores, String cat) {
        int res=R.drawable.earth;
        if(cat.equals("Gastronomia"))
        {
            return R.drawable.jupiter;
        }
        else if(cat.equals("Museo"))
        {
            return R.drawable.mars;
        }
        else {
            return res;
        }
    }
}
