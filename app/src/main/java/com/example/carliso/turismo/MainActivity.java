package com.example.carliso.turismo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carliso.turismo.Adapter.ItemAdapter;
import com.example.carliso.turismo.Api.GPSTracker;
//import com.example.carliso.turismo.Api.GooglePlaces;
import com.example.carliso.turismo.YelpAdapter.Business;
import com.example.carliso.turismo.YelpAdapter.YelpSearchResult;
import com.example.carliso.turismo.YelpAdapter.YelpV2API;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
/**
 * Created by Carliso on 27/10/2014.
 */

    public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    protected static final int REQUEST_CODE = 10;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] lmenu;
    private ItemAdapter adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hiram", "No se ha obtenido la localizacion");
        /*
        OBTENCION DE LA LOCALIZACION
         */
        // GPSTracker class
        GPSTracker gps;
        // create class object
        gps = new GPSTracker(MainActivity.this);
        double latitude=0;
        double longitude=0;
        // check if GPS enabled
        if(gps.canGetLocation()){

             latitude= gps.getLatitude();
             longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            Log.d("hiram","Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        /*GooglePlaces googlePlaces=new GooglePlaces();
        PlacesList placesList;
        try {
            placesList=googlePlaces.search(latitude,longitude,100,"store");
            Log.d("hiram",placesList.status);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        setContentView(R.layout.activity_main);
        //Obtencion de titulo
        mTitle = mDrawerTitle = getTitle();
        //Obtiene la lista del menu
        lmenu = getResources().getStringArray(R.array.opciones);
        //DrawerLayout se  determina el contenedor principal
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Lista para el menu lateral
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, lmenu));
        adaptador = new ItemAdapter(getApplicationContext(), 1);
        mDrawerList.setAdapter(adaptador);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        consultaYelp();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            //Despliegue del submenu

        }
    }

    private void selectItem(int position) {

        // update the main content by replacing fragments
        Fragment fragment= new ListaFragment();
        //FragmentActivity fragmentActivity = new Localizacion();
        Bundle args = new Bundle();
        //args.putInt(MenuDiversion.ARG_PLANET_NUMBER, position);
        ArrayList<String> lista =new ArrayList<String>();
        if(position==0)
        {

            //fragmentActivity = new Localizacion();
            fragment = new MapaFragment();
            args.putInt(MapaFragment.ARG_PLANET_NUMBER, position);
            fragment.setArguments(args);
        }
        else {

            switch (position) {

                case 1://Cultura
                    lista.add("Gastronomia");
                    lista.add("Museo");
                    break;
                case 2://Diversion
                    lista.add("Parque Acuatico");
                    lista.add("Parque Tematico");
                    lista.add("Zoologico");

                    break;
                case 3://religioso
                    lista.add("Catedrales");
                    lista.add("Iglesias");
                    lista.add("Monasterios");
                    break;
                case 4://Naturaleza
                    lista.add("Areas Protegidas");
                    lista.add("Parque");
                    lista.add("Playas");
                    break;
            }
            fragment = new ListaFragment();
            args.putInt(ListaFragment.ARG_PLANET_NUMBER, position);
            args.putStringArrayList("lista",lista);
            fragment.setArguments(args);
        }



        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(lmenu[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void consultaYelp()
    {

        //final int cont=listaMeteo.getCount()+1;
        Log.d("hiram","nueva consulta");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Define your keys, tokens and secrets.  These are available from the Yelp website.
                String CONSUMER_KEY = "coR5hQcj34fJfuZ_FH87iA";
                String CONSUMER_SECRET = "YJ6d-Vct05RtIu5KjCwjCTaZ6NM";
                String TOKEN = "C8Y-szrHWkmXos1f489-CZANVBXRkVan";
                String TOKEN_SECRET = "j78o5KK3wFJSG1eFPgqGY8i9r4I";

                // Some example values to pass into the Yelp search service.
                String lat = "39.4629388";
                String lng = "-0.4009171";
                String category = "food,restaurant";

                // Execute a signed call to the Yelp service.
                OAuthService service = new ServiceBuilder().provider(YelpV2API.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
                Token accessToken = new Token(TOKEN, TOKEN_SECRET);
                OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
                //request.addQuerystringParameter("location","Valencia");
                request.addQuerystringParameter("ll", lat + "," + lng);
                //request.addQuerystringParameter("city", "Valencia");
                //request.addQuerystringParameter("language", "es");
                //request.addQuerystringParameter("country", "es");
                request.addQuerystringParameter("sort", "1");
                request.addQuerystringParameter("category", category);

                service.signRequest(accessToken, request);
                org.scribe.model.Response response = request.send();
                System.out.println(response.toString());
                String rawData = response.getBody();

                // Sample of how to turn that text into Java objects.
                try {
                    YelpSearchResult places = new Gson().fromJson(rawData, YelpSearchResult.class);
                    Log.d("hiram",rawData.toString());
                    System.out.println("Your search found " + places.getTotal() + " results.");
                    System.out.println("Yelp returned " + places.getBusinesses().size() + " businesses in this request.");
                    System.out.println();
                    String latitud;
                    JSONObject objeto;
                    for(Business biz : places.getBusinesses()) {
                        System.out.println(biz.getName());
                        latitud=biz.getLocation().getAddress().toString();
                        for(String address : biz.getLocation().getAddress()) {
                            System.out.println("  " + address);
                        }
                        System.out.print("  " + biz.getLocation().getCity());
                        System.out.println(biz.getUrl());
                        objeto=getLocationInfo(latitud);
                        getLatLong(objeto);
                        System.out.println();
                    }
                    //Region region=places.getRegion();



                } catch(Exception e) {
                    System.out.println("Error, could not parse returned data!");
                    System.out.println(rawData);
                }

            }
        }).start();



    }

    private static JSONObject getLocationInfo(String address)
    {

        StringBuilder stringBuilder = new StringBuilder();
        try {

            address = address.replaceAll(" ","%20");

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            Log.i("getLocationInfo ClientProtocolException", e.toString());
        } catch (IOException e) {

            Log.i("getLocationInfo IOException", e.toString());
        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.i("getLocationInfo JSONException", e.toString());
        }

        return jsonObject;
    }

    private static boolean getLatLong(JSONObject jsonObject)
    {

        double longitute;
        double latitude;
        try {

            longitute = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            Log.i("hiram", longitute+"");
            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            Log.i("hiram", latitude+"");
        } catch (JSONException e) {

            longitute=0;
            latitude = 0;
            Log.i("getLatLong", e.toString());
            return false;

        }

        return true;
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class MenuFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        //private ListView elementos;
        //ItemAdapter componente;
        //String[] infoT;
        //ArrayAdapter<String> listado;

        public MenuFragment() {
            // Empty constructor required for fragment subclasses
            //infoT=objetos;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.opciones)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);

            MainActivity act=(MainActivity)getActivity();

            return rootView;
        }


    }

    public static class MenuCultura extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        Context context;

        public MenuCultura() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_submenu, container, false);

            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String categoria = getResources().getStringArray(R.array.opciones)[i];
            String[] cul = getResources().getStringArray(R.array.cultura);
            TextView item1, item2;
            context = rootView.getContext();
            ((TextView) rootView.findViewById(R.id.it1)).setText(cul[0]);

            ((TextView) rootView.findViewById(R.id.tituloC)).setText(categoria);
            //((TextView)rootView.findViewById(R.id.it1)).setText(cul[0]);
            ((TextView) rootView.findViewById(R.id.it2)).setText(cul[1]);
            item1 = (TextView) rootView.findViewById(R.id.it1);
            item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Pulsado Gatronomia!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SubMenu.class);
                    intent.putExtra("categoria", "gastronomia");
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
            item2 = (TextView) rootView.findViewById(R.id.it2);
            item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Pulsado museo!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SubMenu.class);
                    intent.putExtra("categoria", "museo");
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
            getActivity().setTitle(categoria);
            return rootView;
        }


    }

    public static class MenuDiversion extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public MenuDiversion() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_submenu, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String categoria = getResources().getStringArray(R.array.opciones)[i];
            String[] cat = getResources().getStringArray(R.array.diversion);
            ((TextView) rootView.findViewById(R.id.tituloC)).setText(categoria);
            ((TextView) rootView.findViewById(R.id.it1)).setText(cat[0]);
            ((TextView) rootView.findViewById(R.id.it2)).setText(cat[1]);
            ((TextView) rootView.findViewById(R.id.it3)).setText(cat[2]);
            getActivity().setTitle(categoria);
            return rootView;
        }



    }

    public static class MenuReligioso extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public MenuReligioso() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_submenu, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String categoria = getResources().getStringArray(R.array.opciones)[i];
            String[] cat = getResources().getStringArray(R.array.religioso);
            ((TextView) rootView.findViewById(R.id.tituloC)).setText(categoria);
            ((TextView) rootView.findViewById(R.id.it1)).setText(cat[0]);
            ((TextView) rootView.findViewById(R.id.it2)).setText(cat[1]);
            ((TextView) rootView.findViewById(R.id.it3)).setText(cat[2]);
            getActivity().setTitle(categoria);
            return rootView;
        }
    }

    public static class MenuNaturaleza extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public MenuNaturaleza() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_submenu, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String categoria = getResources().getStringArray(R.array.opciones)[i];
            String[] cat = getResources().getStringArray(R.array.naturaleza);
            ((TextView) rootView.findViewById(R.id.tituloC)).setText(categoria);
            ((TextView) rootView.findViewById(R.id.it1)).setText(cat[0]);
            ((TextView) rootView.findViewById(R.id.it2)).setText(cat[1]);
            ((TextView) rootView.findViewById(R.id.it3)).setText(cat[2]);


            getActivity().setTitle(categoria);
            return rootView;
        }
    }

    public static class MapaFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        MapView mMapView;
        private GoogleMap googleMap;

        public MapaFragment() {

        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_mapa, container,
                    false);
            mMapView = (MapView) v.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume();// needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            googleMap = mMapView.getMap();
            // latitude and longitude
            double latitude = 17.385044;
            double longitude = 78.486671;

            // create marker
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(latitude, longitude)).title("Hello Maps");

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));

            // adding marker
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            // Perform any camera updates here
            return v;
        }
    }
    public static class ListaFragment extends ListFragment
    {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        Context context;

        String[] countries = new String[] {
                "India",
                "Pakistan",
                "Sri Lanka",
                "China",
                "Bangladesh",
                "Nepal",
                "Afghanistan",
                "North Korea",
                "South Korea",
                "Japan"
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            //View rootView = inflater.inflate(R.layout.fragment_lista, container, false);
            Bundle bundle=this.getArguments();

            ArrayList<String>sub=bundle.getStringArrayList("lista");
            String[] items = new String[sub.size()];
            items = sub.toArray(items);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,items);
            setListAdapter(adapter);
            View rootView = inflater.inflate(R.layout.fragment_submenu, container, false);
            context=rootView.getContext();
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        public void onListItemClick(ListView l, View v, int position, long id) {
            Toast.makeText(getActivity(),
                    getListView().getItemAtPosition(position).toString(),
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, SubMenu.class);
            String Categoria=getListView().getItemAtPosition(position).toString();
            intent.putExtra("categoria",Categoria);
            startActivityForResult(intent, REQUEST_CODE);
        }


    }


}
