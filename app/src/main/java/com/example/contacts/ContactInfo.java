package com.example.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.example.contacts.async.AsyncContactAction;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.entity.Contact;
import com.facebook.drawee.view.SimpleDraweeView;

public class ContactInfo {

    private Profile context;
    private Contact contact;

    private static final int REQUEST_CODE_PERMISSION_CALL_PHONE= 1;

    public ContactInfo(Profile context, Contact contact) {
        this.context = context;
        this.contact = contact;
    }

    private TextView generateLocalTextView(String text){
        TextView textView = new TextView(this.context);
        textView.setText(text);
        textView.setTextSize(24);
        return textView;
    }

    private SimpleDraweeView generateLocalImage(){
        Uri imageUri = Uri.parse(this.contact.photo);
        SimpleDraweeView imageProfile = new SimpleDraweeView(context);
        imageProfile.setMinimumWidth(200);
        imageProfile.setMinimumHeight(300);
        imageProfile.setImageURI(imageUri);
        return imageProfile;
    }

    private AlertDialog generateLocalDeleteDialog(String text){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(text);
        builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                new AsyncContactAction(MainActivity.db, null, contact, "%",
                        DataBaseComands.CONTACT_DELETE, contact.getLogin()).execute();
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
        delete.setTextSize(8);
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

    public View getView(){
        // Компонент, который содержит ФИО и дату
        LinearLayout nameAndDate = new LinearLayout(context);
        nameAndDate.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        nameAndDate.setLayoutParams(layoutParams);
        nameAndDate.setBackgroundColor(Color.WHITE);

        nameAndDate.addView(generateLocalTextView(this.contact.name));
        nameAndDate.addView(generateLocalTextView(this.contact.date));

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams1.weight = 0.1f;
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(layoutParams1);
        horizontalScrollView.addView(nameAndDate);

        Button call = new Button(context);
        call.setText("Поздравить");
        call.setLayoutParams(layoutParams);
        call.setOnClickListener(new View.OnClickListener() {
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

        // Компонент, который содержит фото и предыдущий компонент
        LinearLayout full = new LinearLayout(context);
        RelativeLayout.LayoutParams fullLayoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 300);
        fullLayoutParams.setMargins(100,0,50, 50);
        full.setOrientation(LinearLayout.HORIZONTAL);
        full.setLayoutParams(fullLayoutParams);

        full.addView(generateLocalImage());
        full.addView(horizontalScrollView);
        full.addView(generateLocalDeleteButton("Удалить"));
        //full.addView(call);

        nameAndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddChangeInformation.class);
                intent.putExtra("Name", contact.name);
                intent.putExtra("Tellephone", contact.phone);
                intent.putExtra("date", contact.date);
                intent.putExtra("photo", contact.photo);
                intent.putExtra("status", "update");
                intent.putExtra("id", contact.uid);
                context.startActivity(intent);
            }
        });

        //scroll.setLayoutParams(layoutParams);
        //scroll.addView(full);

        return full;
    }
}
