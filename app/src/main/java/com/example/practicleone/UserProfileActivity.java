package com.example.practicleone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicleone.db.MyDBManager;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private EditText InputFirstName;
    private EditText InputName;
    private EditText InputSecondName;
    private EditText InputPhone;
    private Button ButtonBack;
    private Button ButtonSave;
    private Button btcall;
    private ImageView imageView;
    private Bitmap bitmap = null;
    private MyDBManager myDBManager;

    int pos = -1 ;
    User user;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        myDBManager=new MyDBManager(this);
        //инициализация объектов дизайна
        InputFirstName=findViewById(R.id.InputFirstName);
        InputName=findViewById(R.id.InputName);
        InputSecondName=findViewById(R.id.InputSecondName);
        InputPhone=findViewById(R.id.InputPhone);
        ButtonBack=findViewById(R.id.ButtonBack);
        ButtonSave=findViewById(R.id.ButtonSave);
        imageView= (ImageView) findViewById(R.id.imageView);
        btcall=findViewById(R.id.btcall);


        //вывод при открытии
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            pos = (int) arguments.get("Size");
                if(arguments.containsKey("FIO")) {
                    String FAM = arguments.get("FIO").toString();
                    String[] words = FAM.split(" ");
                    myDBManager.openDB();
                    user =  myDBManager.readPersonalInf(words[0],words[1]);
                    btcall.setVisibility(View.VISIBLE);
                    InputName.setText(user.Name);
                    InputFirstName.setText(user.FirstName);
                    InputSecondName.setText(user.SecondName);
                    InputPhone.setText(user.PhoneNumber);

                    try {
                        imageView.setImageBitmap(BitmapFactory.decodeStream(this.openFileInput(user.Picture)));
                        path = user.Picture;
                    } catch (Exception ignored) {

                    }
                }
        }

        //выбор изображения
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        //возвращение к главной странице
        ButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                setResult(0,intent);
                closedActivity();
            }
        });

        //вызов
        btcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText number=(EditText)findViewById(R.id.InputPhone);
                String toDial="tel:"+number.getText().toString();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(toDial)));
            }
        });

        //сохранение пользователя
        ButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                if (InputName.getText().length() == 0 || InputFirstName.getText().length() == 0|| InputSecondName.getText().length() == 0|| InputPhone.getText().length() == 0) {
                    Toast.makeText(UserProfileActivity.this, R.string.error_add_user, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //проверка номера телефона, вывод ошибки и его запись
                    String tempPhoneNumber = InputPhone.getText().toString();
                    String pattern = "^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$";
                    ;
                    if (tempPhoneNumber.matches(pattern)) {

                        String PhoneNumberUser=tempPhoneNumber;
                        String NameUser=InputName.getText().toString().trim();
                        String FirstNameUser=InputFirstName.getText().toString().trim();
                        String SecondNameUser=InputSecondName.getText().toString().trim();

                        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        path=createImageFromBitmap(bitmap);
                        //здесь запись в БД
                        if(myDBManager.CheckUser(FirstNameUser,NameUser,SecondNameUser,PhoneNumberUser))
                            myDBManager.UpdateUserINF(FirstNameUser,NameUser,SecondNameUser,PhoneNumberUser);
                        else
                        myDBManager.insertDB(NameUser,FirstNameUser,SecondNameUser,PhoneNumberUser,path);

                        setResult(1,intent);
                        closedActivity();

                        Toast.makeText(UserProfileActivity.this, R.string.accept_relode, Toast.LENGTH_SHORT).show();
                        ;
                    } else {
                        Toast.makeText(UserProfileActivity.this, R.string.phone_error, Toast.LENGTH_SHORT).show();
                        ;
                    }
                }
            }
        });
    }

    static final int GALLERY_REQUEST = 1;

    @Override
    protected void onResume() {
        super.onResume();
        myDBManager.openDB();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDBManager.closeDB();
    }
    //загрузка изображения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        bitmap = null;
        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    path=createImageFromBitmap(bitmap);
                    imageView.setImageBitmap(bitmap);
                }
        }
    }

    private String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage"+pos;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
    void closedActivity()
    {
      this.finish();
    }
}