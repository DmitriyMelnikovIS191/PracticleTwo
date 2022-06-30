package com.example.practicleone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<User> Users = new ArrayList<User>();
    User user;
    List<String> Client = new ArrayList<String>();
    private Button buttonadd;
    private ListView output_elements;
    private int lastposition;
    Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //инициализация объектов дизайна
        output_elements = findViewById(R.id.countriesList);
        buttonadd = findViewById(R.id.button);

        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println();
               Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("Size", Client.size());
                startActivityForResult(intent, 1);
            }
        });

        output_elements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastposition=position;
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("Size", position);
                intent.putExtra("NameUser", Users.get(position).Name);
                intent.putExtra("FirstNameUser", Users.get(position).FirstName);
                intent.putExtra("SecondNameUser", Users.get(position).SecondName);
                intent.putExtra("PhoneNumberUser", Users.get(position).PhoneNumber);
                intent.putExtra("ImageUser", Users.get(position).Picture);

                startActivityForResult(intent,2);
            }
        });
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

                    User NewUser = new  User (data.getExtras().get("NameUser").toString(),data.getExtras().get("FirstNameUser").toString(),
                            data.getExtras().get("SecondNameUser").toString(),data.getExtras().get("PhoneNumberUser").toString(),data.getExtras().get("Picture").toString());
                Users.add(NewUser);

                    Client.clear();
                    for (User user:Users) {
                        Client.add(user.FirstName+" "+user.Name+" "+user.SecondName);
                    }
                ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Client);
                output_elements.setAdapter(adapter);
                break;
            }
        }
        else if (requestCode == 2) {
            switch (resultCode) {
                case 0:
                    super.onResume();
                    break;
                default:
                    User NewUser = new User(data.getExtras().get("NameUser").toString(), data.getExtras().get("FirstNameUser").toString(),
                            data.getExtras().get("SecondNameUser").toString(), data.getExtras().get("PhoneNumberUser").toString(),data.getExtras().get("Picture").toString());
                    Users.remove(lastposition);
                    Users.add(lastposition, NewUser);

                    Client.clear();

                    for (User user : Users) {
                        Client.add(user.FirstName + " " + user.Name + " " + user.SecondName);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Client);
                    output_elements.setAdapter(adapter);
            }
        }
        else {
            System.out.println();
        }

    }
}

