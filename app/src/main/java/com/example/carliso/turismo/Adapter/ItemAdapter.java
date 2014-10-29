package com.example.carliso.turismo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carliso.turismo.R;

import java.util.ArrayList;

/**
 * Created by Carliso on 27/10/2014.
 */
public class ItemAdapter extends ArrayAdapter<String> {
    public ArrayList<String> opciones;

    String[] menuP;
    Context contexto=null;
    public ItemAdapter(Context c,int cate)
    {
        super(c, R.layout.add_item);

        opciones=new ArrayList<String>();


        if(cate==1) {
            menuP=c.getResources().getStringArray(R.array.opciones);
            for (String i : menuP) {
                opciones.add(i);
            }
        }
        else if(cate==2)
        {
            menuP=c.getResources().getStringArray(R.array.cultura);
            for (String i : menuP) {
                opciones.add(i);
            }
        }
        else if(cate==3)
        {
            menuP=c.getResources().getStringArray(R.array.diversion);
            for (String i : menuP) {
                opciones.add(i);
            }
        }
        else if(cate==4)
        {
            menuP=c.getResources().getStringArray(R.array.religioso);
            for (String i : menuP) {
                opciones.add(i);
            }
        }
        else if(cate==5)
        {
            menuP=c.getResources().getStringArray(R.array.naturaleza);
            for (String i : menuP) {
                opciones.add(i);
            }
        }
        else
        {
            menuP=c.getResources().getStringArray(R.array.opciones);
            for (String i : menuP) {
                opciones.add(i);
            }
        }
        contexto=c;

    }
    @Override
    public int getCount() {
        return opciones.size();
    }



    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View item=convertView;
        if (item == null || !(item.getTag() instanceof ViewHolder)) {

            LayoutInflater mInflater = LayoutInflater.from(contexto);
            item = mInflater.inflate( R.layout.add_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) item.findViewById(R.id.imageIconMP);
            holder.simpleText = (TextView) item.findViewById(R.id.text);
            String opc=opciones.get(position);
            holder.simpleText.setText(opc);
            holder.image.setImageResource(devolverImage(position));
            //Save holder
            item.setTag(holder);
        }
        else {
            holder = (ViewHolder) item.getTag();
        }

        return item;
    }

    private int devolverImage(int position) {
        int res=R.drawable.error;
        switch (position)
        {
            case 0 :
                res=R.drawable.mp_map;
                return res;
            case 1 :
                res=R.drawable.mp_cul;
                return res;

            case 2:
                res=R.drawable.mp_diver;
                return res;
            case 3:
                res=R.drawable.mp_reli;
                return res;
            case 4:
                res=R.drawable.mp_natu;
                return res;

            default:
                return res;
        }


    }

    class ViewHolder{
        TextView simpleText;
        ImageView image;
    }
    static class Cache{
        static Drawable add = null;
    }
}
