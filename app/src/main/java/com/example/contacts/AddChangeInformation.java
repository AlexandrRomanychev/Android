package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.contacts.async.AsyncAddContact;
import com.example.contacts.database.entity.Contact;

public class AddChangeInformation extends AppCompatActivity {

    private ImageButton image;
    private EditText surname, name, patronymic, phone, date;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_change_information);

        surname = findViewById(R.id.surname);
        name = findViewById(R.id.name);
        patronymic = findViewById(R.id.patronymic);
        phone = findViewById(R.id.phone);
        date = findViewById(R.id.birthday);

        save = findViewById(R.id.save);

       // final AppDatabase db = Room.inMemoryDatabaseBuilder(AddChangeInformation.this, AppDatabase.class).build();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncAddContact(AddChangeInformation.this, MainActivity.db, new Contact(surname.getText().toString(), name.getText().toString(),
                        patronymic.getText().toString(), date.getText().toString(), phone.getText().toString())).execute();
                finish();
            }
        });


    }
}
