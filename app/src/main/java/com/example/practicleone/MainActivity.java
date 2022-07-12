package com.example.practicleone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.practicleone.db.MyDBManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter ;
    String[] SearchUser;
    ArrayList<User> Users = new ArrayList<User>();
    List<String> Client = new ArrayList<String>();
    List<String> NEWClient = new ArrayList<String>();
    private Button buttonadd;
    private Button buttonexp;
    private ListView output_elements;
    private EditText txtsearch;
    private int lastposition;
    public MyDBManager myDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDBManager=new MyDBManager(this);
        ReadFromDB();
        //инициализация объектов дизайна
        output_elements = findViewById(R.id.countriesList);
        buttonadd = findViewById(R.id.button);
        txtsearch = findViewById(R.id.txtsearch);
        buttonexp=findViewById(R.id.buttonexp);

        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println();
               Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("Size", Client.size());
                startActivityForResult(intent, 1);
            }
        });

        buttonexp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OpenDirActivity.class);
                startActivityForResult(intent, 2);
            }
        });


        output_elements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastposition=position;
                String FIO= (String) output_elements.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("Size", position);
                intent.putExtra("FIO",FIO);

                startActivityForResult(intent,1);
            }
        });
        output_elements.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)

                        .setTitle("Удалить?")

                        .setMessage("Удалить выбранный контакт?")

                        .setPositiveButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })

                        .setNegativeButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String FIO= (String) output_elements.getItemAtPosition(pos);
                                String[] words = FIO.split(" ");
                                myDBManager.DeleteFromDB(words[0],words[1]);
                                Client.remove(pos);
                                //set what should happen when negative button is clicked
                                Toast.makeText(getApplicationContext(),"Контакт удалён!",Toast.LENGTH_LONG).show();
                                onResume();
                            }
                        })
                        .show();

                return true;
            }
        });

        //поиск
        txtsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    // reset listview
                    onResume();
                } else {
                    // perform search
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
    }

    public void importList(View view)
    {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Выберите список контактов");
        startActivityForResult(chooseFile, 1);

    }
    public void searchItem(String textToSearch){
        for(String item:SearchUser){
            if(!item.contains(textToSearch)){
                NEWClient.remove(item);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, NEWClient);
        adapter.notifyDataSetChanged();
        output_elements.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        myDBManager.openDB();
        Client.clear();
        for(String title : myDBManager.readfromDBforList()){
            Client.add(title);
        }

        SearchUser = new String[Client.size()];
        for (int i =0;i<Client.size();i++)
        {
            SearchUser[i] = Client.get(i);
        }

        NEWClient=new ArrayList<>(Arrays.asList(SearchUser));

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Client);
        output_elements.setAdapter(adapter);
    }

    private void ReadFromDB(){
        myDBManager.openDB();
        Client.clear();
        for(String title : myDBManager.readfromDBforList()){
            Client.add(title);
            myDBManager.closeDB();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode){
                case 0:
                    super.onResume();
                    break;
                case 1:
                    Client.clear();
                    for(String title : myDBManager.readfromDBforList()){
                        Client.add(title);
                    }
                    //ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Client);
                    output_elements.setAdapter(adapter);
                break;
                case RESULT_OK:
                    Uri uri = data.getData();
                    myDBManager.openDB();
                    for (User user: myDBManager.getPerson()) {
                        myDBManager.delete(user.getId());
                    }
                    for (User user: fileWorker.importPersons(fileWorker.getPathFromUri(this,uri))) {
                        myDBManager.insert(user);
                    }
                    myDBManager.closeDB();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Контакты загружены", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
            }
        }
        else if (requestCode == 2) {
            if (data == null) {return;}
            String url = data.getStringExtra("url");
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "/contacts-"+ n +".lst";
            File file = new File (url, fname);
            Users.clear();
            for (User user: myDBManager.getPerson()) {
                Users.add(user);
            }
            fileWorker.exportPersons(Users,file.getAbsolutePath());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Файл contacts.lst сохранен!", Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDBManager.closeDB();
    }

}

