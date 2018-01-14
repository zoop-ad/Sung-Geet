package fourhorsemen.sung_geet;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.jar.Manifest;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SeekBar seekBar;
    SongAdapter songAdapter;
    ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
    MediaPlayer mediaPlayer;
    Handler handler = new Handler() {
        @Override
        public void publish(LogRecord record) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
       // SongInfo s = new SongInfo("Same Old Love", "Selena Gomez", "https://downpwnew.com/upload_file/5570/6757/Latest%20Bollywood%20Hindi%20Mp3%20Songs%20-%202018/Nirdosh%20%282018%29%20Hindi%20Movie%20Mp3%20Songs/01%20Barf%20Si%20-%20Nirdosh%20%28Armaan%20Malik%29%20190Kbps.mp3");
       // songs.add(s);
        songAdapter = new SongAdapter(this, songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songAdapter);
        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Button b, View v, final SongInfo s, int i) {
                Runnable r=new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (b.getText().toString() == "stop") {
                                b.setText("play");
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            } else {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(s.getSongURL());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                        seekBar.setProgress(0);
                                        seekBar.setMax(mp.getDuration());
                                        b.setText("stop");
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                recyclerView.postDelayed(r,100);

            }
        });
        checkPermission();
        Thread t=new Mythread();
        t.start();

    }
    public class Mythread extends Thread{
        @Override
        public void run() {
            try {

                Thread.sleep(1000);
                if(mediaPlayer!=null)
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkPermission() {
       /* if(Build.VERSION.SDK_INT>=23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return ;
            }

        }
        else{*/
            loadSongs();
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[]permissions,int[]grantResults){
        switch (requestCode){
            case 123:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }else{
                    Toast.makeText(this, "PERMISSION DENIED",Toast.LENGTH_SHORT).show();
                    checkPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }

    }

    private void loadSongs() {
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    String name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    SongInfo s=new SongInfo(name,artist,url);
                    songs.add(s);

                }while (cursor.moveToNext());
                cursor.close();
                songAdapter=new SongAdapter(this,songs);
            }

        }
    }
}
