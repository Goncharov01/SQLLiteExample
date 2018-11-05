package com.example.goncharov01.sqlliteexample

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        //переопределяем функцию ( бандл будет содержать предыдущую информацию об активити)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        //создаем разметку

        try {
            //перепишем в объект бандл все данные которые у нас, которые мы записали в активимэйн
            var bundle: Bundle = intent.extras
            //достаем из объекста бандл айдишник, если его нет присываемываем стандартное значение 0
            id = bundle.getInt("MainActId", 0)
            //
            if (id != 0) {
                //получаем из бандла строку по ключю маин акт титле и записываем эту строку в поле для редактирование с айдишником эдит титле
                edtTitle.setText(bundle.getString("MainActTitle"))
                //получаем из бандла строку по ключу маин акт контент, и записываем эту строку в поле для редактирование с айдишников эдит контент
                var a=bundle.getString("MainActContent")
                edtContent.setText(a)
            }
        } catch (ex: Exception) {
        }

        btAdd.setOnClickListener {
            var dbManager = NoteDbManager(this)

            var values = ContentValues()
            values.put("Title", edtTitle.text.toString())
            values.put("Content", edtContent.text.toString())
                    // если айдишник пустой это значит что такого объекта нет в базе данных и его нужно вставить
            if (id == 0) {
                val mID = dbManager.insert(values)

                if (mID > 0) {
                    Toast.makeText(this, "Add note successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add note!", Toast.LENGTH_LONG).show()
                }

            }
            //если айдишник имеет значение значит мы обновляем объект
            else {
                var selectionArs = arrayOf(id.toString())
                val mID = dbManager.update(values, "Id=?", selectionArs)

                if (mID > 0) {
                    Toast.makeText(this, "Add note successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add note!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
