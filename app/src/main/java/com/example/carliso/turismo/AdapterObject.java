package com.example.carliso.turismo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Carliso on 27/10/2014.
 */
public class AdapterObject extends ArrayAdapter
{
    public ArrayList<String> opciones;

    String[] menuP;
    String descrip="Lorem ipsum ad his scripta blandit partiendo, eum fastidii accumsan euripidis in, eum liber hendrerit an. Qui ut wisi vocibus suscipiantur, quo dicit ridens inciderint id. Quo mundi lobortis reformidans eu, legimus senserit definiebas an eos. Eu sit tincidunt incorrupte definitionem, vis mutat affert percipit cu, eirmod consectetuer signiferumque eu per. In usu latine equidem dolores. Quo no falli viris intellegam, ut fugit veritus placerat per.";
    Context contexto=null;
    String nameO;
    public AdapterObject(Context c,String cate)
    {
        super(c,R.layout.add_item);
        nameO=cate;
        opciones=new ArrayList<String>();
        opciones.add(cate);
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
            item = mInflater.inflate( R.layout.objeto, null);
            holder = new ViewHolder();
            holder.image = (ImageView) item.findViewById(R.id.imgO);
            holder.titulo = (TextView) item.findViewById(R.id.t01);
            holder.descipcion = (TextView) item.findViewById(R.id.t02);
            //Save holder
            item.setTag(holder);
        }
        else {
            holder = (ViewHolder) item.getTag();
        }

        holder.titulo.setText(nameO);
        holder.descipcion.setText(descrip);
        if (Cache.add == null){
            Cache.add = contexto.getResources().getDrawable(R.drawable.neptune);
        }
        holder.image.setBackgroundDrawable(Cache.add);
        return item;
    }
    class ViewHolder{
        ImageView image;
        TextView titulo;
        TextView descipcion;


    }
    static class Cache{
        static Drawable add = null;
    }
}
