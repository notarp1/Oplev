package Controller;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import DAL.Interfaces.CallbackBitmap;

public class PictureGetter {

    public void getPic(CallbackBitmap callbackBitmap, String url){
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                callbackBitmap.onCallBack(loadedImage);
            }
        });

    }


}
