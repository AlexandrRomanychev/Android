package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AddChangeInformation extends AppCompatActivity {

    TextView Surname;
    TextView TellPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_change_information);

        // Получаю все аргументы
        Bundle arguments = getIntent().getExtras();

        // Получаю действие добавления контакта
        String activity = arguments.getString("Activity");

        // Если это импорт, то подставляем данные в TextView профиля
        if (activity.equalsIgnoreCase("Import")) {
            String id = arguments.getString("Id");
            String Name = arguments.getString("Name");
            String PhoneNumber = arguments.getString("Tellephone");

            Surname = (TextView) findViewById(R.id.surname);
            TellPhone = (TextView) findViewById(R.id.phone);

            Surname.setText(Name);
            TellPhone.setText(PhoneNumber);
        }
        else // Создание в ручную
        {
            Surname.setText("");
            TellPhone.setText("");
        }
    }
}
