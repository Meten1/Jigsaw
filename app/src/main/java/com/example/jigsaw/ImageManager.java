package com.example.jigsaw;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class ImageManager {

    private final String assetPath;
    private String[] imageNames;
    private final AssetManager assetManager;

    public ImageManager(AssetManager assetManager, String assetPath) {
        this.assetPath = assetPath;
        this.assetManager = assetManager;
        try {
            imageNames = assetManager.list(assetPath);
        } catch (IOException e) {
            imageNames = null;
        }
    }

    public Bitmap get(int index) {
        Bitmap image = null;
        try {
            InputStream stream = assetManager.open(assetPath + imageNames[index]);
            image = BitmapFactory.decodeStream(stream);
            stream.close();
        } catch (IOException ignored) {
        }
        return image;
    }

    public String getName(int n) {
        return imageNames[n].replace(".jpg", "")
                .replace(".png", "")
                .replace("_", " ");
    }

    public int count() {
        return imageNames.length;
    }


}
