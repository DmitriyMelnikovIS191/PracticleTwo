package com.example.practicleone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button buttonadd;
    private ListView output_elements;

    List<String> countries = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //заполнение списка начальными объектами
        countries.add("Бразилия");
        countries.add("Аргентина");
        countries.add("Колумбия");

        //инициализация объектов дизайна
        output_elements=findViewById(R.id.countriesList);
        inputText=findViewById(R.id.inputText);
        buttonadd=findViewById(R.id.button);

        //создание адаптера
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, countries);
        output_elements.setAdapter(adapter);

        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //проверка заполения поля элемента
                if (inputText.getText().toString().trim().equals(""))
                {
                    //сообщение об отсутствии элемента
                    Toast.makeText(MainActivity.this, R.string.input_error, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //вывод добавленного элемента
                    countries.add(inputText.getText().toString().trim());
                    output_elements.setAdapter(adapter);
                }
            }
        });
    }
}