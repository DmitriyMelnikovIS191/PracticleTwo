package com.example.practicleone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class OpenDirActivity extends AppCompatActivity {
    ListView listview;
    TextView textPath;
    Context _context;
    int select_id_list = -1;
    String path = "/";
    ArrayList<String> ArrayDir = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_dir);

        listview = findViewById(R.id.listview);
        textPath = findViewById(R.id.textPath);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ArrayDir);
        listview.setAdapter(adapter);

        update_list_dir();

        listview.setOnItemClickListener((parent, view, position, id) -> {
            select_id_list = (int)id;
            update_list_dir();
        });
    }
    public void onClickBack(View view) {
        String tmp = (new File(path)).getParent();
        if (tmp != null) {
                path = tmp;
            update_list_dir();
        } }

    public void onClickGo(View view)
    {
        Intent intent = new Intent();
        intent.putExtra("url", path);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void update_list_dir(){
        if(select_id_list != -1) {
            path = path + "/" + ArrayDir.get(select_id_list);
        }
        select_id_list = -1;
        ArrayDir.clear();
        File[] files = new File(path).listFiles();
        if (files==null) {
            files = Environment.getExternalStorageDirectory().listFiles();
            path = Environment.getExternalStorageDirectory().getPath();
        }
        assert files != null;
        for (File aFile : files){
            if ( aFile.isDirectory() ) {
                if(dir_opened(aFile.getPath())){
                    ArrayDir.add(aFile.getName());
                }
            }
        }
        adapter.notifyDataSetChanged();
        textPath.setText(path);
    }

    private boolean dir_opened(String url){
        try{
            File[] files = new File(url).listFiles();
            assert files != null;
            return true;
        } catch(Exception e){
            return false;
        }
    }

}