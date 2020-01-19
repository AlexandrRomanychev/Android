package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.contacts.async.AsyncGetAllContact;
import com.example.contacts.database.entity.Contact;

import java.util.List;
import android.widget.SearchView;
import android.widget.Spinner;

public class Profile extends AppCompatActivity {

    private Button btn_import, btn_new;
    private ImageButton btn_add;
    private Boolean flag = false;
    private LinearLayout profiles;
    private SearchView search;
    private Spinner sort;

    private static final int CONTACT_PICK_RESULT = 1;
    private static final int REQUEST_CODE_PERMISSION_READ_CONTACTS = 1;

    public void showListOfProfiles(List<Contact> contacts){
        profiles.removeAllViews();
        for (Contact contact: contacts) {
            ContactInfo contactInfo = new ContactInfo(Profile.this, contact);
            profiles.addView(contactInfo.getView());
        }
    }

    public void refreshListOfContacts(){
        new AsyncGetAllContact(MainActivity.db, Profile.this).execute();
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshListOfContacts();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_profiles);

        profiles = findViewById(R.id.profiles);
        sort = findViewById(R.id.spinner_sort);

        search = findViewById(R.id.search_profile);

        //Поиск по ФИО
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Создание кнопок добавления контактов и их обработчиков
        btn_import = findViewById(R.id.import_profile);
        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // переход на страницу импорта контакта
                int permissionStatus = ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_CONTACTS);

                //Проверка на разрешение чтения контактов
                if (permissionStatus == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent2 = new Intent();
                    Intent contactPickerIntent = new Intent(intent2.ACTION_PICK,
                            android.provider.ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(contactPickerIntent, CONTACT_PICK_RESULT);
                }
                else {
                    ActivityCompat.requestPermissions(Profile.this, new String[] {Manifest.permission.READ_CONTACTS},
                            REQUEST_CODE_PERMISSION_READ_CONTACTS);
                }
            }
        });

        btn_new = findViewById(R.id.new_profile);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на страницу добавления профиля
                Intent intent = new Intent(v.getContext(), AddChangeInformation.class);

                // передача информации о выбранном контакте
                intent.putExtra("ID", -1);
                intent.putExtra("Name", "");
                intent.putExtra("Tellephone", "");
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String PhoneNumber = "";

        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case CONTACT_PICK_RESULT:
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToNext())
                    {
                        String Id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String Name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        int CountPhone = Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                        if (CountPhone > 0)
                        {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{Id},
                                    null);

                            while (phones.moveToNext())
                            {
                                // Берем по умолчанию первый телефон
                                PhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                break;
                            }
                            phones.close();
                        }
                        // создание объекта Intent для запуска add_change_information.xml
                        Intent intent = new Intent(this, AddChangeInformation.class);

                        // передача информации о выбранном контакте
                        intent.putExtra("ID", Integer.parseInt(Id));
                        intent.putExtra("Name", Name);
                        intent.putExtra("Tellephone", PhoneNumber);
                        startActivity(intent);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("searchSave", search.getQuery().toString());
        outState.putInt("sortIndexSave", sort.getSelectedItemPosition());

        super.onSaveInstanceState(outState);
    }

    // получение сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        search.setQuery(savedInstanceState.getString("searchSave"), true);
        sort.setSelection(savedInstanceState.getInt("sortIndexSave"));
    }
}
