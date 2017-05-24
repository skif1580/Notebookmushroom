package hackerman.notebookmushroom.db;


import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.io.Serializable;
import java.util.List;

@Table
public class TodoObj implements Serializable {
    @PrimaryKey
    public String key;
    @Column
    public String name;
    @Column
    public long date;
    @Column
    public boolean status;
    @Column
    @Nullable
    public String photo;
    @Column
    public double latitude ;
    @Column
    public double longitude ;
    @Column
    @Nullable
    public String notes;
    @Column
    @Nullable
    public String address;

    public TodoObj() {
        this.key = String.valueOf(System.currentTimeMillis());
    }

    public TodoObj(String name, long date) {
        this.key = String.valueOf(System.currentTimeMillis());
        this.name = name;
        this.date = date;
    }


}
