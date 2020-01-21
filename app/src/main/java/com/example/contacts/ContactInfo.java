package com.example.contacts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.example.contacts.async.AsyncContactAction;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.converter.DateConverter;
import com.example.contacts.database.entity.Contact;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class ContactInfo {

    private Profile context;
    private Contact contact;
    private final float TEXT_SIZE = 24f;
    private final float BUTTON_TEXT_SIZE = 8f;
    private final int IMAGE_WIDTH = 200;
    private final int IMAGE_HEIGHT = 300;
    private final int TEXT_MARGINE = 20;
    private final int VIEW_MARGINE = 50;
    private final AppDatabase db;

    private static final int REQUEST_CODE_PERMISSION_CALL_PHONE= 1;

    public ContactInfo(Profile context, Contact contact) {
        this.context = context;
        this.contact = contact;
        db = Room.databaseBuilder(context, AppDatabase.class, "contacts").build();
    }

    private TextView generateLocalTextView(String text){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,TEXT_MARGINE,0,TEXT_MARGINE);
        TextView textView = new TextView(this.context);
        textView.setText(text);
        textView.setTextSize(TEXT_SIZE);
        textView.setBackgroundResource(R.color.textField);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private SimpleDraweeView generateLocalImage(){
        Uri imageUri = Uri.parse(this.contact.photo);
        SimpleDraweeView imageProfile = new SimpleDraweeView(context);
        imageProfile.setMinimumWidth(IMAGE_WIDTH);
        imageProfile.setMinimumHeight(IMAGE_HEIGHT);
        imageProfile.setMaxWidth(IMAGE_WIDTH);
        imageProfile.setMaxHeight(IMAGE_HEIGHT);
        if (this.contact.photo.equals(""))
            imageProfile.setImageResource(R.mipmap.ic_launcher_round);
        else
            imageProfile.setImageURI(imageUri);
        return imageProfile;
    }

    private AlertDialog generateLocalDeleteDialog(String text){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(text);
        builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new AsyncContactAction(db, null, contact, "%",
                        DataBaseComands.CONTACT_DELETE, contact.getLogin(), null).execute();
                context.refreshListOfContacts("%");
            }
        });
        builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    private Button generateLocalDeleteButton(String text){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button delete = new Button(this.context);
        delete.setText(text);
        delete.setTextSize(BUTTON_TEXT_SIZE);
        delete.setBackgroundResource(R.color.delete);
        delete.setLayoutParams(layoutParams);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = generateLocalDeleteDialog("Вы действительно хотите удалить контакт?");
                dialog.show();
            }
        });
        return delete;
    }

    private Button generateLocalCongratulateButton(String text) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button congratulate = new Button(this.context);
        congratulate.setText(text);
        congratulate.setTextSize(BUTTON_TEXT_SIZE);
        congratulate.setLayoutParams(layoutParams);
        congratulate.setBackgroundResource(R.color.congratulation);
        congratulate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //Проверка на разрешение звонков
                if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.CALL_PHONE},
                            REQUEST_CODE_PERMISSION_CALL_PHONE);
                    return;
                }
                String toDial = "tel:" + contact.phone;
                context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(toDial)));
            }
        });
        return congratulate;
    }

    private HorizontalScrollView generateLocalTextFields(){

        LinearLayout nameAndDate = new LinearLayout(context);
        nameAndDate.setOrientation(LinearLayout.VERTICAL);

        nameAndDate.addView(generateLocalTextView(this.contact.name));
        nameAndDate.addView(generateLocalTextView(DateConverter.dateToString(this.contact.date)));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 0.4f;
        layoutParams.setMargins(VIEW_MARGINE,0,0,0);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(layoutParams);
        horizontalScrollView.addView(nameAndDate);

        nameAndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddChangeInformation.class);
                intent.putExtra("Name", contact.name);
                intent.putExtra("Tellephone", contact.phone);
                intent.putExtra("date", DateConverter.dateToString(contact.date));
                intent.putExtra("photo", contact.photo);
                intent.putExtra("status", "update");
                intent.putExtra("id", contact.uid);
                intent.putExtra("login", contact.login);
                context.startActivity(intent);
            }
        });

        return horizontalScrollView;
    }

    private View generateLocalButtons(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(VIEW_MARGINE, 0, 0, 0);
        layoutParams.weight = 0.5f;
        LinearLayout buttons = new LinearLayout(context);
        buttons.setOrientation(LinearLayout.VERTICAL);
        buttons.addView(generateLocalDeleteButton("Удалить"));
        buttons.addView(generateLocalCongratulateButton("Поздравить"));
        buttons.setLayoutParams(layoutParams);

        return buttons;
    }

    public View getView(){

        // Компонент, который содержит фото и предыдущий компонент
        LinearLayout full = new LinearLayout(context);
        RelativeLayout.LayoutParams fullLayoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, IMAGE_HEIGHT);
        fullLayoutParams.setMargins(VIEW_MARGINE,0,VIEW_MARGINE, VIEW_MARGINE);
        full.setOrientation(LinearLayout.HORIZONTAL);
        full.setLayoutParams(fullLayoutParams);

        full.addView(generateLocalImage());
        full.addView(generateLocalTextFields());
        full.addView(generateLocalButtons());

        return full;
    }
}
