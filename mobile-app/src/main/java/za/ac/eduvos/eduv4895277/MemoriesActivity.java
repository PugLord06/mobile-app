package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MemoriesActivity extends Activity {
    private static final int REQ_PICK_IMAGE = 1001;
    private static final int REQ_PICK_AUDIO = 1002;

    private Uri selectedPhoto;
    private Uri selectedAudio;

    private Button pickPhotoBtn;
    private Button pickAudioBtn;
    private Button saveMemoryBtn;
    private EditText notesInput;
    private ListView listView;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);
        setTitle(getString(R.string.memories));

        pickPhotoBtn = new Button(this);
        pickPhotoBtn.setText("Pick Photo");
        pickAudioBtn = new Button(this);
        pickAudioBtn.setText("Pick Music");
        saveMemoryBtn = new Button(this);
        saveMemoryBtn.setText("Save Memory");
        notesInput = new EditText(this);
        listView = findViewById(R.id.memories_list);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        ViewGroup container = (ViewGroup) ((ViewGroup) root.getChildAt(0));
        container.addView(pickPhotoBtn, 1);
        container.addView(pickAudioBtn, 2);
        container.addView(notesInput, 3);
        container.addView(saveMemoryBtn, 4);

        pickPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i, REQ_PICK_IMAGE);
            }
        });
        pickAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("audio/*");
                startActivityForResult(i, REQ_PICK_AUDIO);
            }
        });
        saveMemoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (selectedPhoto == null) return;
                MemoryModel m = new MemoryModel();
                m.photoUri = selectedPhoto.toString();
                m.audioUri = selectedAudio == null ? null : selectedAudio.toString();
                m.notes = notesInput.getText() == null ? null : notesInput.getText().toString();
                m.createdAt = System.currentTimeMillis();
                new DatabaseHelper(MemoriesActivity.this).insertMemory(m);
                selectedPhoto = null; selectedAudio = null;
                refreshList();
            }
        });

        refreshList();
    }

    private void refreshList() {
        final List<MemoryModel> data = new DatabaseHelper(this).getAllMemories();
        listView.setAdapter(new BaseAdapter() {
            @Override public int getCount() { return data.size(); }
            @Override public Object getItem(int position) { return data.get(position); }
            @Override public long getItemId(int position) { return position; }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = convertView;
                if (row == null) {
                    row = LayoutInflater.from(MemoriesActivity.this).inflate(android.R.layout.simple_list_item_2, parent, false);
                }
                TextView t1 = row.findViewById(android.R.id.text1);
                TextView t2 = row.findViewById(android.R.id.text2);
                final MemoryModel m = data.get(position);
                t1.setText("Memory");
                t2.setText(m.notes == null ? "" : m.notes);
                row.startAnimation(AnimationUtils.loadAnimation(MemoriesActivity.this, R.anim.fade_in));

                row.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent i = new Intent(MemoriesActivity.this, PhotoViewerActivity.class);
                        i.putExtra("photo", m.photoUri);
                        i.putExtra("audio", m.audioUri);
                        startActivity(i);
                    }
                });
                return row;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        Uri uri = data.getData();
        if (uri == null) return;
        if (requestCode == REQ_PICK_IMAGE) {
            selectedPhoto = uri;
        } else if (requestCode == REQ_PICK_AUDIO) {
            selectedAudio = uri;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMusic();
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            try { mediaPlayer.stop(); } catch (Exception ignored) {}
            try { mediaPlayer.release(); } catch (Exception ignored) {}
            mediaPlayer = null;
        }
    }
} 