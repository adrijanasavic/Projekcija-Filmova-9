package com.example.projekcijafilmova9.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.projekcijafilmova9.R;
import com.example.projekcijafilmova9.adapters.RepertoarAdapter;
import com.example.projekcijafilmova9.db.DatabaseHelper;
import com.example.projekcijafilmova9.db.model.Filmovi;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RepertoarActivity extends AppCompatActivity implements RepertoarAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private RepertoarAdapter adapterLista;
    private List<Filmovi> filmovi;
    private SharedPreferences prefs;

    private TextView textView;

    private Toolbar toolbar;

    public static String KEY = "KEY";
    public static final String NOTIF_CHANNEL_ID = "notif_channel_007";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_repertoar );

        setupToolbar();
        createNotificationChannel();
        prefs = PreferenceManager.getDefaultSharedPreferences( this );
        recyclerView = findViewById( R.id.rvRepertoarLista );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        try {
            filmovi = getDataBaseHelper().getFilmoviDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        adapterLista = new RepertoarAdapter( this, filmovi, this );
        recyclerView.setAdapter( adapterLista );
        textView = findViewById( R.id.textLista );

    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "Repertoar" );
        //toolbar.setLogo(R.mipmap.ic_launcher);


        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.back );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_repertoar, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_film:
                deleteFilmove();
                startActivity( new Intent( this, MainActivity.class ) );
                break;
            case android.R.id.home:
                startActivity( new Intent( this, MainActivity.class ) );
                break;
        }

        return super.onOptionsItemSelected( item );
    }

    public void deleteFilmove() {

        try {
            ArrayList<Filmovi> filmoviZaBrisanje = (ArrayList<Filmovi>) getDataBaseHelper().getFilmoviDao().queryForAll();
            getDataBaseHelper().getFilmoviDao().delete( filmoviZaBrisanje );

            adapterLista.removeAll();
            adapterLista.notifyDataSetChanged();

            String tekstNotifikacije = "Repertoar obrisan";
            boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
            boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

            if (toast) {
                Toast.makeText( RepertoarActivity.this, tekstNotifikacije, Toast.LENGTH_LONG ).show();

            }

            if (notif) {
                NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                NotificationCompat.Builder builder = new NotificationCompat.Builder( RepertoarActivity.this, NOTIF_CHANNEL_ID );
                builder.setSmallIcon( R.drawable.delete );
                builder.setContentTitle( "Notifikacija" );
                builder.setContentText( tekstNotifikacije );

                Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher );

                builder.setLargeIcon( bitmap );
                notificationManager.notify( 1, builder.build() );

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        Filmovi film = adapterLista.get( position );

        String tekstNotifikacije = "Projekcija u " + film.getmVreme() + " za " + film.getmNaziv() +
                " je uspesno reservisana";

        boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
        boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

        if (toast) {
            Toast.makeText( RepertoarActivity.this, tekstNotifikacije, Toast.LENGTH_LONG ).show();

        }

        if (notif) {
            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            NotificationCompat.Builder builder = new NotificationCompat.Builder( RepertoarActivity.this, NOTIF_CHANNEL_ID );
            builder.setSmallIcon( R.drawable.heart );
            builder.setContentTitle( "Notifikacija" );
            builder.setContentText( tekstNotifikacije );

            Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher );

            builder.setLargeIcon( bitmap );
            notificationManager.notify( 1, builder.build() );

        }
    }

   /* @Override
    public boolean onLongClick(int position){

        try {
            getDataBaseHelper().getFilmoviDao().deleteById( adapterLista.get(position) );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        adapterLista.remove(position);
        adapterLista.notifyDataSetChanged();
        return true;
    }*/

    private void refresh() {

        RecyclerView recyclerView = findViewById( R.id.rvRepertoarLista );
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
            recyclerView.setLayoutManager( layoutManager );
            List<Filmovi> film = null;
            try {

                film = getDataBaseHelper().getFilmoviDao().queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            RepertoarAdapter adapter = new RepertoarAdapter( this, film, this );
            recyclerView.setAdapter( adapter );
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public DatabaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper( this, DatabaseHelper.class );
        }
        return databaseHelper;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Description of My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( NOTIF_CHANNEL_ID, name, importance );
            channel.setDescription( description );

            NotificationManager notificationManager = getSystemService( NotificationManager.class );
            notificationManager.createNotificationChannel( channel );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Filmovi> film = null;
        try {
            film = (ArrayList<Filmovi>) getDataBaseHelper().getFilmoviDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (film.size() != 0) {
            textView.setVisibility( View.GONE );

        } else {
            textView.setText( "Lista je prazna" );
        }
        refresh();
    }
    /* @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }*/
}
