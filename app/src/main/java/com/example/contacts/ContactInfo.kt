package com.example.contacts

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.example.contacts.async.AsyncContactAction
import com.example.contacts.database.AppDatabase
import com.example.contacts.database.DataBaseComands
import com.example.contacts.database.converter.DateConverter.dateToString
import com.example.contacts.database.entity.Contact
import com.facebook.drawee.view.SimpleDraweeView

class ContactInfo(private val context: Profile, private val contact: Contact) {
    private val TEXT_SIZE = 24f
    private val BUTTON_TEXT_SIZE = 8f
    private val IMAGE_WIDTH = 200
    private val IMAGE_HEIGHT = 300
    private val TEXT_MARGINE = 20
    private val VIEW_MARGINE = 50
    private val db: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "contacts").build()
    private fun generateLocalTextView(text: String?): TextView {
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, TEXT_MARGINE, 0, TEXT_MARGINE)
        val textView = TextView(context)
        textView.text = text
        textView.textSize = TEXT_SIZE
        textView.layoutParams = layoutParams
        return textView
    }

    private fun generateLocalImage(): SimpleDraweeView {
        val imageUri = Uri.parse(contact.photo)
        val imageProfile = SimpleDraweeView(context)
        imageProfile.minimumWidth = IMAGE_WIDTH
        imageProfile.minimumHeight = IMAGE_HEIGHT
        imageProfile.maxWidth = IMAGE_WIDTH
        imageProfile.maxHeight = IMAGE_HEIGHT
        if (contact.photo == "") imageProfile.setImageResource(R.mipmap.ic_launcher_round) else imageProfile.setImageURI(imageUri)
        return imageProfile
    }

    private fun generateLocalDeleteDialog(text: String): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(text)
        builder.setPositiveButton("ДА") { dialog, id ->
            AsyncContactAction(db, null, contact, "%",
                    DataBaseComands.CONTACT_DELETE, null).execute()
            context.refreshListOfContacts("%")
        }
        builder.setNegativeButton("НЕТ") { dialog, id -> dialog.cancel() }
        return builder.create()
    }

    private fun generateLocalDeleteButton(text: String): Button {
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val delete = Button(context)
        delete.text = text
        delete.textSize = BUTTON_TEXT_SIZE
        delete.layoutParams = layoutParams
        delete.setOnClickListener {
            val dialog = generateLocalDeleteDialog("Вы действительно хотите удалить контакт?")
            dialog.show()
        }
        return delete
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateLocalCongratulateButton(text: String): Button {
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val congratulate = Button(context)
        congratulate.text = text
        congratulate.textSize = BUTTON_TEXT_SIZE
        congratulate.layoutParams = layoutParams
        congratulate.setOnClickListener(View.OnClickListener {
            //Проверка на разрешение звонков
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_CODE_PERMISSION_CALL_PHONE)
                return@OnClickListener
            }
            val toDial = "tel:" + contact.phone
            context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(toDial)))
        })
        return congratulate
    }

    private fun generateLocalTextFields(): HorizontalScrollView {
        val nameAndDate = LinearLayout(context)
        nameAndDate.orientation = LinearLayout.VERTICAL
        nameAndDate.addView(generateLocalTextView(contact.name))
        nameAndDate.addView(generateLocalTextView(dateToString(contact.date)))
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.weight = 0.4f
        layoutParams.setMargins(VIEW_MARGINE, 0, 0, 0)
        val horizontalScrollView = HorizontalScrollView(context)
        horizontalScrollView.layoutParams = layoutParams
        horizontalScrollView.addView(nameAndDate)
        nameAndDate.setOnClickListener {
            val intent = Intent(context, AddChangeInformation::class.java)
            intent.putExtra("Name", contact.name)
            intent.putExtra("Tellephone", contact.phone)
            intent.putExtra("date", dateToString(contact.date))
            intent.putExtra("photo", contact.photo)
            intent.putExtra("status", "update")
            intent.putExtra("id", contact.uid)
            context.startActivity(intent)
        }
        return horizontalScrollView
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateLocalButtons(): View {
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.setMargins(VIEW_MARGINE, 0, 0, 0)
        layoutParams.weight = 0.5f
        val buttons = LinearLayout(context)
        buttons.orientation = LinearLayout.VERTICAL
        buttons.addView(generateLocalDeleteButton("Удалить"))
        buttons.addView(generateLocalCongratulateButton("Поздравить"))
        buttons.layoutParams = layoutParams
        return buttons
    }

    // Компонент, который содержит фото и предыдущий компонент
    val view: View
        @RequiresApi(Build.VERSION_CODES.M)
        get() { // Компонент, который содержит фото и предыдущий компонент
            val full = LinearLayout(context)
            val fullLayoutParams = RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, IMAGE_HEIGHT)
            fullLayoutParams.setMargins(VIEW_MARGINE, 0, VIEW_MARGINE, VIEW_MARGINE)
            full.orientation = LinearLayout.HORIZONTAL
            full.layoutParams = fullLayoutParams
            full.addView(generateLocalImage())
            full.addView(generateLocalTextFields())
            full.addView(generateLocalButtons())
            return full
        }

    companion object {
        private const val REQUEST_CODE_PERMISSION_CALL_PHONE = 1
    }

}