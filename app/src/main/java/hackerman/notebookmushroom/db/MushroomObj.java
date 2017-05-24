package hackerman.notebookmushroom.db;


public class MushroomObj {

    public String photo;
    public String description;

    public String title;

    public long key;
    public String photomushroom;
    public String file[] = new String[3];

    public MushroomObj() {
        this.key = System.currentTimeMillis();
    }
}
