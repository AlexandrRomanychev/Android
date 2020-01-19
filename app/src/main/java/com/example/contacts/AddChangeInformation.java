package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contacts.async.AsyncContactAction;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.entity.Contact;

import com.facebook.drawee.view.SimpleDraweeView;

public class AddChangeInformation extends AppCompatActivity {

    private EditText name, phone, date;
    private Button save;
    static final int GALLERY_REQUEST = 1;
    private SimpleDraweeView ImageProfile;
    private Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_change_information);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        date = findViewById(R.id.birthday);

        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = selectedImage == null? "" : selectedImage.toString();
                new AsyncContactAction(MainActivity.db, null,
                        new Contact( name.getText().toString(),
                        date.getText().toString(), phone.getText().toString(),
                        uri), "%", DataBaseComands.CONTACT_ADD).execute();
                finish();
            }
        });

        ImageProfile = findViewById(R.id.image_human);
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

            name = findViewById(R.id.name);
            phone = findViewById(R.id.phone);

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
        outState.putString("nameSave", name.getText().toString());
        outState.putString("birthdaySave", date.getText().toString());
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
        name.setText(savedInstanceState.getString("nameSave"));
        date.setText(savedInstanceState.getString("birthdaySave"));
        phone.setText(savedInstanceState.getString("phoneSave"));
    }
}
