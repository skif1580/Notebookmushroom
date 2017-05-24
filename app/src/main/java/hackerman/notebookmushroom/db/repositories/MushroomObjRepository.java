package hackerman.notebookmushroom.db.repositories;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hackerman.notebookmushroom.app.App;
import hackerman.notebookmushroom.db.MushroomObj;

public class MushroomObjRepository {
    public static List<MushroomObj> getMushroomObjList() {
        List<MushroomObj> mushroomObjs = new ArrayList<>();
        try {
            Pattern pattern;
            Matcher matcher;
            AssetManager assetManager = App.getAppContext().getAssets();
            for (String text : assetManager.list("texts")) {
                MushroomObj mushroomObj = new MushroomObj();
                pattern = Pattern.compile("[\\d]+");
                matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String textPrefix = matcher.group();
                    mushroomObj.title = text.substring(matcher.end());
                    for (String photo : assetManager.list("")) {
                        pattern = Pattern.compile(textPrefix);
                        matcher = pattern.matcher(photo);
                        if (matcher.find()) {
                            mushroomObj.photomushroom = photo;
                            String files[] = assetManager.list(photo);
                            if (files != null) {
                                mushroomObj.file = files;
                            }
                        }
                    }
                    final ArrayList<String> texts = new ArrayList<String>(Arrays.asList(assetManager.list("texts")));
                    Collections.reverse(texts);
                    for (String description : texts) {
                        pattern = Pattern.compile(textPrefix);
                        matcher = pattern.matcher(description);
                        if (matcher.find()) {
                            String name = "texts/";
                            mushroomObj.description = name + description;
                            Log.d("getMushroomObjList", "name::" + description);
                            break;
                        }
                    }
                    final ArrayList<String> images = new ArrayList<String>(Arrays.asList(assetManager.list("images")));
                    Collections.reverse(images);
                    for (String image : images) {
                        pattern = Pattern.compile(textPrefix);
                        matcher = pattern.matcher(image);
                        if (matcher.find()) {
                            mushroomObj.photo = image;
                            Log.d("getMushroomObjList", "iamge::" + image);
                            break;
                        }
                    }
                }
                mushroomObjs.add(mushroomObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mushroomObjs;
    }

}
