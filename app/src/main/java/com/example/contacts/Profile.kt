package com.example.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.contacts.async.AsyncContactAction
import com.example.contacts.async.AsyncUserAction
import com.example.contacts.database.AppDatabase
import com.example.contacts.database.DataBaseComands
import com.example.contacts.database.entity.Contact
import com.example.contacts.database.entity.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Profile : AppCompatActivity() {
    private var btn_add: FloatingActionButton? = null
    private var btn_import: FloatingActionButton? = null
    private var btn_new: FloatingActionButton? = null
    private var exit: FloatingActionButton? = null
    private var flag = false
    private var profiles: LinearLayout? = null
    private var search: SearchView? = null
    private var sort: Spinner? = null
    private var db: AppDatabase? = null
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun showListOfProfiles(contacts: List<Contact>?) {
        profiles!!.removeAllViews()
        for (contact in contacts!!) {
            val contactInfo = ContactInfo(this@Profile, contact)
            profiles!!.addView(contactInfo.view)
        }
    }

    fun refreshListOfContacts(rule: String?) {
        when (sort!!.selectedItemPosition) {
            0 -> {
                AsyncContactAction(db!!, this@Profile, null, rule!!,
                        DataBaseComands.CONTACT_GET_ALL, login!!, null).execute()
            }
            1 -> {
                AsyncContactAction(db!!, this@Profile, null, "%" + search!!.query.toString() + "%", DataBaseComands.CONTACT_SORT_NAME_UP, login!!, null).execute()
            }
            2 -> {
                AsyncContactAction(db!!, this@Profile, null, "%" + search!!.query.toString() + "%", DataBaseComands.CONTACT_SORT_NAME_DOWN, login!!, null).execute()
            }
            3 -> {
                AsyncContactAction(db!!, this@Profile, null, "%" + search!!.query.toString() + "%", DataBaseComands.CONTACT_SORT_DATE_UP, login!!, null).execute()
            }
            4 -> {
                AsyncContactAction(db!!, this@Profile, null, "%" + search!!.query.toString() + "%", DataBaseComands.CONTACT_SORT_DATE_DOWN, login!!, null).execute()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshListOfContacts("%" + search!!.query.toString() + "%")
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_profiles)
        db = Room.databaseBuilder(this@Profile, AppDatabase::class.java, "contacts").build()
        val arguments = intent.extras
        login = arguments!!.getString("login")
        this.title = "Вы авторизованы как: $login"
        profiles = findViewById(R.id.profiles)
        sort = findViewById(R.id.spinner_sort)
        search = findViewById(R.id.search_profile)
        sort!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                refreshListOfContacts("%" + search!!.query.toString() + "%")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                refreshListOfContacts("%" + search!!.query.toString() + "%")
            }
        }
        //Поиск по ФИО
        search!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                refreshListOfContacts("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                refreshListOfContacts("%$newText%")
                return true
            }
        })
        // Создание кнопок добавления контактов и их обработчиков
        btn_import = findViewById(R.id.import_profile)
        btn_import!!.setOnClickListener {
            // переход на страницу импорта контакта
            val permissionStatus = ContextCompat.checkSelfPermission(this@Profile, Manifest.permission.READ_CONTACTS)
            //Проверка на разрешение чтения контактов
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                Intent()
                val contactPickerIntent = Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI)
                startActivityForResult(contactPickerIntent, CONTACT_PICK_RESULT)
            } else {
                ActivityCompat.requestPermissions(this@Profile, arrayOf(Manifest.permission.READ_CONTACTS),
                        REQUEST_CODE_PERMISSION_READ_CONTACTS)
            }
        }
        btn_new = findViewById(R.id.new_profile)
        btn_new!!.setOnClickListener { v ->
            // Переход на страницу добавления профиля
            val intent = Intent(v.context, AddChangeInformation::class.java)
            // передача информации о выбранном контакте
            intent.putExtra("ID", -1)
            intent.putExtra("Name", "")
            intent.putExtra("Tellephone", "")
            intent.putExtra("login", login)
            startActivity(intent)
        }
        btn_add = findViewById(R.id.add_profile)
        btn_add!!.setOnClickListener(View.OnClickListener {
            // Если была нажата только один раз, то варианты добавления контакта появяться.
// При повторном - исчезают
            if (!flag) {
                btn_new!!.visibility = View.VISIBLE
                btn_import!!.visibility = View.VISIBLE
                exit!!.visibility = View.VISIBLE
                btn_add!!.setImageResource(R.drawable.cancel)
            } else {
                btn_new!!.visibility = View.INVISIBLE
                btn_import!!.visibility = View.INVISIBLE
                exit!!.visibility = View.INVISIBLE
                btn_add!!.setImageResource(R.drawable.plus)
            }
            flag = !flag
        })
        exit = findViewById(R.id.exit)
        exit!!.setOnClickListener {
            AsyncUserAction(this@Profile, db!!, User(login!!, ""), MainActivity::class.java, DataBaseComands.USER_DELETE).execute()
            finish()
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var PhoneNumber = ""
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CONTACT_PICK_RESULT -> {
                    val contactData = data!!.data
                    val c = contentResolver.query(contactData!!, null, null, null, null)
                    if (c!!.moveToNext()) {
                        val Id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                        val Name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val CountPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()
                        if (CountPhone > 0) {
                            val phones = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(Id),
                                    null)
                            while (phones!!.moveToNext()) { // Берем по умолчанию первый телефон
                                PhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                break
                            }
                            phones.close()
                        }
                        // создание объекта Intent для запуска add_change_information.xml
                        val intent = Intent(this, AddChangeInformation::class.java)
                        // передача информации о выбранном контакте
                        intent.putExtra("ID", Id.toInt())
                        intent.putExtra("Name", Name)
                        intent.putExtra("Tellephone", PhoneNumber)
                        intent.putExtra("login", login)
                        startActivity(intent)
                    }
                }
                else -> {
                }
            }
        }
    }

    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchSave", search!!.query.toString())
        outState.putInt("sortIndexSave", sort!!.selectedItemPosition)
        super.onSaveInstanceState(outState)
    }

    // получение сохраненного состояния
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        search!!.setQuery(savedInstanceState.getString("searchSave"), true)
        sort!!.setSelection(savedInstanceState.getInt("sortIndexSave"))
    }

    override fun onBackPressed() { // super.onBackPressed();
        openQuitDialog()
    }

    private fun openQuitDialog() {
        val quitDialog = AlertDialog.Builder(
                this@Profile)
        quitDialog.setTitle("Вы хотите выйти из приложения?")
        quitDialog.setPositiveButton("Да") { dialog, which ->
            AsyncUserAction(this@Profile, db!!, User(login!!, ""), MainActivity::class.java, DataBaseComands.USER_DELETE).execute()
            finish()
        }
        quitDialog.setNegativeButton("Нет") { dialog, which -> dialog.cancel() }
        quitDialog.show()
    }

    companion object {
        @JvmStatic
        var login: String? = ""
            private set
        private const val CONTACT_PICK_RESULT = 1
        private const val REQUEST_CODE_PERMISSION_READ_CONTACTS = 1
    }
}