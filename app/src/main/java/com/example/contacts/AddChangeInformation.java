package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private SimpleDraweeView ImageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_change_information);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData(); // получаю uri фотографий
                    ImageProfile.setImageURI(selectedImage); // установка изображения
                }
        }
    }
}
