package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.contacts.async.AsyncAddContact;
import com.example.contacts.async.AsyncGetAllContact;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.dao.ContactDao;
import com.example.contacts.database.entity.Contact;

import java.util.List;

public class Profile extends AppCompatActivity {

    private Button btn_import, btn_new;
    private ImageButton btn_add;
    private Boolean flag = false;
    private List<Contact> contacts;

    @Override
    protected void onResume(){
        super.onResume();
        final AppDatabase db = Room.databaseBuilder(Profile.this, AppDatabase.class, "contacts").build();
        AsyncGetAllContact getAllContact = new AsyncGetAllContact(db);
        getAllContact.execute();
        contacts = getAllContact.getContacts();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_profiles);

        // Создание кнопок добавления контактов и их обработчиков
        btn_import = findViewById(R.id.import_profile);
        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // переход на страницу импорта контакта
            }
        });

        btn_new = findViewById(R.id.new_profile);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на страницу добавления профиля
                Intent intent = new Intent(Profile.this, AddChangeInformation.class);
                startActivity(intent);
            }
        });

        btn_add = findViewById(R.id.add_profile);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Если была нажата только один раз, то варианты добавления контакта появяться.
                // При повторном - исчезают
                if(!flag){
                    btn_new.setVisibility(View.VISIBLE);
                    btn_import.setVisibility(View.VISIBLE);
                    btn_add.setBackgroundResource(R.drawable.img_button_add_profile_select_true);
                } else {
                    btn_new.setVisibility(View.INVISIBLE);
                    btn_import.setVisibility(View.INVISIBLE);
                    btn_add.setBackgroundResource(R.drawable.img_button_add_profile_select_false);
                }
                flag = !flag;
            }
        });
    }
}
