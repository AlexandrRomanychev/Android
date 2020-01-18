package com.example.contacts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ContactInfo{

    private String surname;
    private Context context;
    private String name;
    private String patronymic;
    private String date;
    private String phone;

    public ContactInfo(Context context, String surname, String name,
                       String patronymic, String date, String phone) {
        this.context = context;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.date = date;
        this.phone = phone;
    }

    public View getView(){
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0, 50);
        layout.setLayoutParams(layoutParams);
        layout.setBackgroundColor(Color.WHITE);
        TextView name = new TextView(context);
        name.setTextSize(24);
        name.setText(this.surname+" "+this.name+" "+this.patronymic);
        TextView date = new TextView(context);
        date.setTextSize(24);
        date.setText(this.date);
        layout.addView(name);
        layout.addView(date);
        return layout;
    }
}
