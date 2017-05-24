package hackerman.notebookmushroom.UI.fragment.descriptionmushroom;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

import hackerman.notebookmushroom.app.App;

/**
 * Created by hackerman on 13.03.17.
 */

public class DescriptionPresenterIml extends DescriptionPresenter {
    @Override
    public void descriptionText(String text) {
        AssetManager assetManager = App.getAppContext().getAssets();
        InputStream stream;
        try {
            stream = assetManager.open(text);
            int size = stream.available();
            byte[] bytes = new byte[size];
            stream.read(bytes);
            stream.close();
            if (isViewAttached()){
                String dani= new String(bytes);
                getView().readText(dani);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
