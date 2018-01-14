package fourhorsemen.sung_geet;

import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by hp on 10-01-2018.
 */


public class SongInfo {
    public String SongName,ArtistName,songURL;

    public SongInfo(){

    }
    public SongInfo(String Songname,String Artist,String url){
        this.SongName=Songname;
        this.ArtistName=Artist;
        this.songURL=url;
    }
    public String getArtistName() {
        return ArtistName;
    }

    public String getSongName() {
        return SongName;
    }

    public String getSongURL() {
        return songURL;
    }
}
