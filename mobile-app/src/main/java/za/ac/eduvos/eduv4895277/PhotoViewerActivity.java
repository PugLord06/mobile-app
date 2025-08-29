package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoViewerActivity extends Activity {
    private MediaPlayer mediaPlayer;
    private String audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        String photo = getIntent().getStringExtra("photo");
        audio = getIntent().getStringExtra("audio");

        ImageView image = findViewById(R.id.viewer_image);
        image.setImageURI(Uri.parse(photo));

        Button play = findViewById(R.id.btn_play);
        Button pause = findViewById(R.id.btn_pause);
        Button stop = findViewById(R.id.btn_stop);

        play.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startAudio(); }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause(); }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { stopAudio(); }
        });
    }

    private void startAudio() {
        if (audio == null) return;
        stopAudio();
        try {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(audio));
            if (mediaPlayer != null) mediaPlayer.start();
        } catch (Exception ignored) {}
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            try { mediaPlayer.stop(); } catch (Exception ignored) {}
            try { mediaPlayer.release(); } catch (Exception ignored) {}
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAudio();
    }
} 