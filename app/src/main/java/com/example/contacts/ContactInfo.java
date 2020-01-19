package com.example.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.contacts.async.AsyncContactAction;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.entity.Contact;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

public class ContactInfo{

    private Profile context;
    private Contact contact;

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

    public View getView(){
        HorizontalScrollView scroll = new HorizontalScrollView(context);
        // Компонент, который содержит ФИО и дату
        LinearLayout nameAndDate = new LinearLayout(context);
        nameAndDate.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        nameAndDate.setLayoutParams(layoutParams);
        nameAndDate.setBackgroundColor(Color.WHITE);

        nameAndDate.addView(generateLocalTextView(this.contact.surname+" "+this.contact.name+" "+this.contact.patronymic));
        nameAndDate.addView(generateLocalTextView(this.contact.date));

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
                        new AsyncContactAction(MainActivity.db, null, contact, "%",
                                DataBaseComands.CONTACT_DELETE).execute();
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

        full.addView(generateLocalImage());
        full.addView(nameAndDate);
        full.addView(delete);
        scroll.setLayoutParams(layoutParams);
        scroll.addView(full);

        return scroll;
    }
}
