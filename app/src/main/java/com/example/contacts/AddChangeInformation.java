package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class AddChangeInformation extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;

    private EditText surname;
    private EditText name;
    private EditText patronymic;
    private EditText phone;
    private EditText birthday;
    private SimpleDraweeView ImageProfile;
    private Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_change_information);

        surname = (EditText)findViewById(R.id.surname);
        patronymic = (EditText)findViewById(R.id.patronymic);
        birthday = (EditText)findViewById(R.id.birthday);

        ImageProfile = (SimpleDraweeView) findViewById(R.id.image_human);
        ImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход к галпереи фотографий
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        // Получаю все аргументы
        Bundle arguments = getIntent().getExtras();

        // Подставляем данные в TextView профиля
        if (arguments != null)
        {
            String Name = arguments.getString("Name");
            String PhoneNumber = arguments.getString("Tellephone");

            name = (EditText) findViewById(R.id.name);
            phone = (EditText) findViewById(R.id.phone);

            name.setText(Name);
            phone.setText(PhoneNumber);
        }

        // Востановление аватарки при повороте
        if (savedInstanceState != null)
        {
            String uriSave = savedInstanceState.getString("uriSave", null);
            if (uriSave != null)
            {
                selectedImage = Uri.parse(uriSave);
                ImageProfile.setImageURI(selectedImage);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData(); // получаю uri фотографий
                    ImageProfile.setImageURI(selectedImage); // установка изображения
                }
        }
    }

    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("surnameSave", surname.getText().toString());
        outState.putString("nameSave", name.getText().toString());
        outState.putString("patronymicSave", patronymic.getText().toString());
        outState.putString("birthdaySave", birthday.getText().toString());
        outState.putString("phoneSave", phone.getText().toString());

        if (selectedImage != null)
        {
            outState.putString("uriSave", selectedImage.toString());
        }

        super.onSaveInstanceState(outState);
    }

    // получение сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        surname.setText(savedInstanceState.getString("surnameSave"));
        name.setText(savedInstanceState.getString("nameSave"));
        patronymic.setText(savedInstanceState.getString("patronymicSave"));
        birthday.setText(savedInstanceState.getString("birthdaySave"));
        phone.setText(savedInstanceState.getString("phoneSave"));
    }
}
