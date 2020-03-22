package com.example.projekcijafilmova9.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekcijafilmova9.R;
import com.example.projekcijafilmova9.db.model.Filmovi;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RepertoarAdapter extends RecyclerView.Adapter<RepertoarAdapter.MyViewHolder> {

    private Context context;
    private List<Filmovi> filmItem;
    private OnItemClickListener listener;


    public RepertoarAdapter(Context context, List<Filmovi> film, OnItemClickListener listener) {
        this.context = context;
        this.filmItem = film;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.repertoar_row, parent, false );

        return new MyViewHolder( view, listener );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNaziv.setText( filmItem.get( position ).getmNaziv() );
        holder.tvCenaKarte.setText( filmItem.get( position ).getmCena() );
        holder.tvVremePrikazivanja.setText( filmItem.get( position ).getmVreme() );
        Picasso.with( context ).load( filmItem.get( position ).getmImage() ).into( holder.ivSlika );

    }

    @Override
    public int getItemCount() {
        return filmItem.size();
    }

    public Filmovi get(int position) {
        return filmItem.get( position );
    }

    public void removeAll() {
        filmItem.clear();
    }

    public void remove(int position) {
        filmItem.remove( position );
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener/*, View.OnLongClickListener */{


        private TextView tvNaziv;
        private TextView tvCenaKarte;
        private TextView tvVremePrikazivanja;
        private ImageView ivSlika;
        private OnItemClickListener vhListener;


        MyViewHolder(@NonNull View itemView, OnItemClickListener vhListener) {
            super( itemView );

            ivSlika = itemView.findViewById( R.id.ivSlika );
            tvNaziv = itemView.findViewById( R.id.tvNazivFilma );
            tvCenaKarte = itemView.findViewById( R.id.tvCenaKarte );
            tvVremePrikazivanja = itemView.findViewById( R.id.tvVremePrikazivanja );

            this.vhListener = vhListener;
            itemView.setOnClickListener( this );
           // itemView.setOnLongClickListener( this );

        }

        @Override
        public void onClick(View v) {
            vhListener.onItemClick( getAdapterPosition() );
        }


      /*  @Override
        public boolean onLongClick(View v) {
            vhListener.onLongClick( getAdapterPosition() );
            return true;

        }*/
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

      //  boolean onLongClick(int position);
    }
}
