package Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import DAL.Interfaces.CallbackBitmap;

public class PictureGetter {
    Context ctx;
    public PictureGetter(Context ctx){
        this.ctx = ctx;

    }

    public void getPic(CallbackBitmap callbackBitmap, String url){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).build();
        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                callbackBitmap.onCallBack(loadedImage);
            }
        });

    }


}
