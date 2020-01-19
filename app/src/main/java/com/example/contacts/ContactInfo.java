package com.example.contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.contacts.async.AsyncDelContact;
import com.example.contacts.async.AsyncGetAllContact;
import com.example.contacts.database.entity.Contact;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

public class ContactInfo{

    private Profile context;
    private Contact contact;

    public ContactInfo(Profile context, Contact contact) {
        this.context = context;
        this.contact = contact;
    }

    public View getView(){
        HorizontalScrollView scroll = new HorizontalScrollView(context);
        // Компонент, который содержит ФИО и дату
        LinearLayout nameAndDate = new LinearLayout(context);
        nameAndDate.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        nameAndDate.setLayoutParams(layoutParams);
        nameAndDate.setBackgroundColor(Color.WHITE);
        TextView name = new TextView(context);
        name.setTextSize(24);
        name.setText(this.contact.surname+" "+this.contact.name+" "+this.contact.patronymic);
        TextView date = new TextView(context);
        date.setTextSize(24);
        date.setText(this.contact.date);
        nameAndDate.addView(name);
        nameAndDate.addView(date);

        Uri imageUri = Uri.parse(this.contact.photo);
        SimpleDraweeView imageProfile = new SimpleDraweeView(context);
        imageProfile.setMinimumWidth(200);
        imageProfile.setMinimumHeight(300);
        imageProfile.setImageURI(imageUri);

        Button delete = new Button(context);
        delete.setText("Удалить");
        delete.setLayoutParams(layoutParams);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
// Add the buttons
                builder.setTitle("Вы действительно хотите удалить контакт?");
                builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        new AsyncDelContact(MainActivity.db, contact).execute();
                        context.refreshListOfContacts("%");
                    }
                });
                builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Компонент, который содержит фото и предыдущий компонент
        LinearLayout full = new LinearLayout(context);
        RelativeLayout.LayoutParams fullLayoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 300);
        fullLayoutParams.setMargins(100,0,50, 50);
        full.setOrientation(LinearLayout.HORIZONTAL);
        full.setLayoutParams(fullLayoutParams);

        full.addView(imageProfile);
        full.addView(nameAndDate);
        full.addView(delete);
        scroll.setLayoutParams(layoutParams);
        scroll.addView(full);

        return scroll;
    }
}
