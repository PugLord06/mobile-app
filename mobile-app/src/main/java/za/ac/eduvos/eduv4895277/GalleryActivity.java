package za.ac.eduvos.eduv4895277;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class GalleryActivity extends Activity {
    private static final int REQ_ADD_IMAGE = 2001;
    private GridView grid;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTitle(getString(R.string.gallery));

        grid = findViewById(R.id.gallery_grid);
        addBtn = new Button(this);
        addBtn.setText("Add Photo");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i, REQ_ADD_IMAGE);
            }
        });
        ((android.widget.RelativeLayout) findViewById(android.R.id.content).getRootView()).addView(addBtn);

        refresh();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<MemoryModel> list = MemoryStore.getAll(GalleryActivity.this);
                MemoryModel m = list.get(position);
                Intent i = new Intent(GalleryActivity.this, PhotoViewerActivity.class);
                i.putExtra("photo", m.photoUri);
                i.putExtra("audio", m.audioUri);
                startActivity(i);
            }
        });
    }

    private void refresh() {
        final List<MemoryModel> list = new DatabaseHelper(this).getAllMemories();
        grid.setAdapter(new BaseAdapter() {
            @Override public int getCount() { return list.size(); }
            @Override public Object getItem(int position) { return list.get(position); }
            @Override public long getItemId(int position) { return position; }
            @Override public View getView(int position, View convertView, android.view.ViewGroup parent) {
                ImageView iv = convertView == null ? new ImageView(GalleryActivity.this) : (ImageView) convertView;
                iv.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 240));
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setImageURI(Uri.parse(list.get(position).photoUri));
                iv.setAnimation(android.view.animation.AnimationUtils.loadAnimation(GalleryActivity.this, R.anim.fade_in));
                return iv;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        if (requestCode == REQ_ADD_IMAGE) {
            Uri uri = data.getData();
            if (uri != null) {
                MemoryModel m = new MemoryModel();
                m.photoUri = uri.toString();
                m.createdAt = System.currentTimeMillis();
                new DatabaseHelper(this).insertMemory(m);
                refresh();
            }
        }
    }
} 