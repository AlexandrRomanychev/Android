package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    Button btn_import, btn_new;
    ImageButton btn_add;
    Integer flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_profiles);

        // Создание кнопок добавления контактов и их обработчиков
        btn_import = (Button) findViewById(R.id.import_profile);
        btn_import.setOnClickListener(this);

        btn_new = (Button) findViewById(R.id.new_profile);
        btn_new.setOnClickListener(this);

        btn_add = (ImageButton) findViewById(R.id.add_profile);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_profile:
                // Если была нажата только один раз, то варианты добавления контакта появяться.
                // При повторном исчезают
                if(btn_add.isPressed()&& flag == 0){
                   btn_new.setVisibility(View.VISIBLE);
                   btn_import.setVisibility(View.VISIBLE);
                   btn_add.setPressed(true);
                   btn_add.setBackgroundResource(R.drawable.img_button_add_profile_select_true);
                   flag = 1;
                }
                else if (btn_add.isPressed()&& flag == 1 )
                {
                    btn_new.setVisibility(View.INVISIBLE);
                    btn_import.setVisibility(View.INVISIBLE);
                    btn_add.setPressed(false);
                    btn_add.setBackgroundResource(R.drawable.img_button_add_profile_select_false);
                    flag = 0;
                }
                break;

            default:
                break;
        }
    }
}
