package hackerman.notebookmushroom.db;


import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.util.ArrayList;
import java.util.List;

@Table
public class PlaceObj {
    @PrimaryKey
    public String dateAdded;
    @Column
    public double lat;
    @Column
    public double lng;
    @Column(indexed = true)
    public String parentKey;
    @Column
    @Nullable
    public String photo;



    public PlaceObj(String dateAdded, double lat, double lng, String parentKey) {
        this.dateAdded = dateAdded;
        this.lat = lat;
        this.lng = lng;
        this.parentKey = parentKey;
    }


    public PlaceObj() {

    }
}
