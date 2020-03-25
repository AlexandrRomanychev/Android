package com.example.contacts

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.contacts.async.AsyncContactAction
import com.example.contacts.database.AppDatabase
import com.example.contacts.database.DataBaseComands
import com.example.contacts.database.converter.DateConverter
import com.example.contacts.database.converter.DateConverter.dateToTimestamp
import com.example.contacts.database.entity.Contact
import com.example.contacts.validation.Validation.validateContactPage
import com.facebook.drawee.view.SimpleDraweeView
import java.util.*


class AddChangeInformation : AppCompatActivity() {
    private var name: EditText? = null
    private var phone: EditText? = null
    private var date: TextView? = null
    private var ImageProfile: SimpleDraweeView? = null
    private var selectedImage: Uri? = null
    private var status: DataBaseComands? = null
    private var userLogin: String? = ""
    private val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1
    private var db: AppDatabase? = null
    private var dateAndTime: Calendar = Calendar.getInstance()

    // установка начальных даты и времени
    private fun setInitialDateTime() {
        var now: Date = Date()
        if (now.time < dateAndTime.timeInMillis){
            var toast = Toast.makeText(this@AddChangeInformation, "Нельзя выбрать дату из будущего!", Toast.LENGTH_SHORT)
            toast.show()
        }
        else {
            date!!.setText(DateConverter.dateToString(dateAndTime.timeInMillis))
        }
    }

    // отображаем диалоговое окно для выбора даты
    fun setDate(v: View?) {
        DatePickerDialog(this@AddChangeInformation, d,
                dateAndTime[Calendar.YEAR],
                dateAndTime[Calendar.MONTH],
                dateAndTime[Calendar.DAY_OF_MONTH])
                .show()
    }

    // установка обработчика выбора даты
    var d = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        dateAndTime[Calendar.YEAR] = year
        dateAndTime[Calendar.MONTH] = monthOfYear
        dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
        setInitialDateTime()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        status = DataBaseComands.CONTACT_ADD
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_change_information)
        db = Room.databaseBuilder(this@AddChangeInformation, AppDatabase::class.java, "contacts").build()
        // Получаю все аргументы
        val arguments = intent.extras

        name = findViewById(R.id.name)
        phone = findViewById(R.id.phone)
        date = findViewById(R.id.birthday)

        date!!.setOnClickListener{
            //setInitialDateTime()
            setDate(it)
        }

        val save = findViewById<Button>(R.id.save)
        save.setOnClickListener {
            val uri = if (selectedImage == null) (if (arguments!!.getString("photo") == null) "" else arguments.getString("photo")!!) else selectedImage.toString()
            when (status) {
                DataBaseComands.CONTACT_ADD -> {
                    if (validateContactPage(this@AddChangeInformation, name!!.text.toString(),
                                    date!!.text.toString(), phone!!.text.toString())) {
                        AsyncContactAction(db!!, null,
                                Contact(name!!.text.toString(),
                                        dateToTimestamp(date!!.text.toString())!!, phone!!.text.toString(),
                                        uri), "%", DataBaseComands.CONTACT_ADD,  null).execute()
                        finish()
                    }
                }
                DataBaseComands.CONTACT_UPDATE -> {
                    if (validateContactPage(this@AddChangeInformation, name!!.text.toString(),
                                    date!!.text.toString(), phone!!.text.toString())) {
                        AsyncContactAction(db!!, null,
                                Contact(name!!.text.toString(),
                                        dateToTimestamp(date!!.text.toString())!!, phone!!.text.toString(),
                                        uri, arguments!!.getInt("id")), "%", DataBaseComands.CONTACT_UPDATE, null).execute()
                        finish()
                    }
                }
            }
        }
        ImageProfile = findViewById(R.id.image_human)
        ImageProfile!!.setOnClickListener {
            // переход на страницу импорта контакта
            val permissionStatus = ContextCompat.checkSelfPermission(this@AddChangeInformation, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) { // Переход к галпереи фотографий
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
            } else {
                ActivityCompat.requestPermissions(this@AddChangeInformation, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE)
            }
        }
        // Подставляем данные в TextView профиля
        name!!.setText(arguments!!.getString("Name"))
        phone!!.setText(arguments!!.getString("Tellephone"))
        date!!.setText(arguments!!.getString("date"))
        if (arguments!!.getString("photo") != null) ImageProfile!!.setImageURI(Uri.parse(arguments.getString("photo")))
        val contactStatus = arguments!!.getString("status")
        if (contactStatus != null) {
            status = DataBaseComands.CONTACT_UPDATE
        }
        // Востановление аватарки при повороте
        if (savedInstanceState != null) {
            val uriSave = savedInstanceState.getString("uriSave", null)
            if (uriSave != null) {
                selectedImage = Uri.parse(uriSave)
                ImageProfile!!.setImageURI(selectedImage)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                selectedImage = imageReturnedIntent!!.data // получаю uri фотографий
                ImageProfile!!.setImageURI(selectedImage) // установка изображения
            }
        }
    }

    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("nameSave", name!!.text.toString())
        outState.putString("birthdaySave", date!!.text.toString())
        outState.putString("phoneSave", phone!!.text.toString())
        if (selectedImage != null) {
            outState.putString("uriSave", selectedImage.toString())
        }
        super.onSaveInstanceState(outState)
    }

    // получение сохраненного состояния
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name!!.setText(savedInstanceState.getString("nameSave"))
        date!!.setText(savedInstanceState.getString("birthdaySave"))
        phone!!.setText(savedInstanceState.getString("phoneSave"))
    }

    companion object {
        const val GALLERY_REQUEST = 1
    }
}