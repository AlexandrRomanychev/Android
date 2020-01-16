package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddChangeInformation extends AppCompatActivity {

    private ImageButton image;
    private EditText surname,nam,patronymic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_change_information);
    }
}
