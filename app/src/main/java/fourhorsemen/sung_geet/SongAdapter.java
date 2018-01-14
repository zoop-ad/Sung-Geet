package fourhorsemen.sung_geet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.*;
import static android.view.View.Z;

/**
 * Created by hp on 10-01-2018.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    ArrayList<SongInfo> songs;
    Context context;

    SongAdapter(Context context, ArrayList<SongInfo> songs) {
        this.context = context;
        this.songs = songs;
    }

    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(Button b, View v, SongInfo s, int i);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener =  onItemClickListener;
    }

    @Override
    public SongAdapter.SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.song_row, parent, false);
        return new SongHolder(myview);
    }

    @Override
    public void onBindViewHolder(final SongHolder holder, final int position) {
        final SongInfo c = songs.get(position);
        holder.songName.setText(c.SongName);
        holder.artistname.setText(c.ArtistName);
        holder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.btnAction, v, c, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        public TextView songName, artistname;
        public Button btnAction;

        public SongHolder(View itemView) {
            super(itemView);
            songName = (TextView) itemView.findViewById(R.id.tvSongName);
            artistname = (TextView) itemView.findViewById(R.id.tvArtistName);
            btnAction = (Button) itemView.findViewById(R.id.buttonAction);


        }
    }
}
