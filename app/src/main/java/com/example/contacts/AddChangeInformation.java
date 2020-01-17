package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class AddChangeInformation extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;

    TextView Surname;
    TextView TellPhone;
    SimpleDraweeView draweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_change_information);


        draweeView = (SimpleDraweeView) findViewById(R.id.image_human);
        draweeView.setOnClickListener(new View.OnClickListener() {
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

            Surname = (TextView) findViewById(R.id.surname);
            TellPhone = (TextView) findViewById(R.id.phone);

            Surname.setText(Name);
            TellPhone.setText(PhoneNumber);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData(); // получаю uri фотографий
                    draweeView.setImageURI(selectedImage); // установка изображения
                }
        }
    }
}
