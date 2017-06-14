package edu.ucsb.cs.cs190i.papertown.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class PaperTownApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();

    Picasso.Builder builder = new Picasso.Builder(this);
    builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
    Picasso built = builder.build();
    built.setLoggingEnabled(true);
    Picasso.setSingletonInstance(built);

    ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
        .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
        .setResizeAndRotateEnabledForNetwork(true)
        .setDownsampleEnabled(true)
        .build();
    Fresco.initialize(this, config);

  }
}
