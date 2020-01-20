package com.example.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
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
import com.example.contacts.database.converter.DateConverter;
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,50,0,0);
        TextView textView = new TextView(this.context);
        textView.setText(text);
        textView.setTextSize(24);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private SimpleDraweeView generateLocalImage(){
        Uri imageUri = Uri.parse(this.contact.photo);
        SimpleDraweeView imageProfile = new SimpleDraweeView(context);
        imageProfile.setMinimumWidth(200);
        imageProfile.setMinimumHeight(300);
        imageProfile.setMaxWidth(200);
        imageProfile.setMaxHeight(300);
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

    private Button generateLocalCongratulateButton(String text) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button congratulate = new Button(this.context);
        congratulate.setText(text);
        congratulate.setTextSize(8);
        congratulate.setLayoutParams(layoutParams);
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
        nameAndDate.addView(generateLocalTextView(new DateConverter().dateToString(this.contact.date)));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 0.4f;
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(layoutParams);
        horizontalScrollView.addView(nameAndDate);

        return horizontalScrollView;
    }

    private View generateLocalButtons(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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
                LinearLayout.LayoutParams.MATCH_PARENT, 300);
        fullLayoutParams.setMargins(50,0,50, 50);
        full.setOrientation(LinearLayout.HORIZONTAL);
        full.setLayoutParams(fullLayoutParams);

        full.addView(generateLocalImage());
        full.addView(generateLocalTextFields());
        full.addView(generateLocalButtons());

        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddChangeInformation.class);
                intent.putExtra("Name", contact.name);
                intent.putExtra("Tellephone", contact.phone);
                intent.putExtra("date", new DateConverter().dateToString(contact.date));
                intent.putExtra("photo", contact.photo);
                intent.putExtra("status", "update");
                intent.putExtra("id", contact.uid);
                intent.putExtra("login", contact.login);
                context.startActivity(intent);
            }
        });

        return full;
    }
}
