package com.example.goncharov01.sqlliteexample

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        //переопределяем функцию онкреате которая будет содержать бандл предыдущую информацию об активити
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        listNotes.add(Note(1, "JavaSampleApproach", "Java technology, Spring Framework - approach to Java by Sample."))
//        listNotes.add(Note(2, "Kotlin Android Tutorial", "Create tutorial for people to learn Kotlin Android. Kotlin is now an official language on Android. It's expressive, concise, and powerful. Best of all, it's interoperable with our existing Android languages and runtime."))
//        listNotes.add(Note(3, "Android Studio", "Android Studio 3.0 provides helpful tools to help you start using Kotlin. Convert entire Java files or convert code snippets on the fly when you paste Java code into a Kotlin file."))
//        listNotes.add(Note(4, "Java Android Tutorial", "Create tutorial for people to learn Java Android. Learn Java in a greatly improved learning environment with more lessons, real practice opportunity, and community support."))
//        listNotes.add(Note(5, "Spring Boot Tutorial", "Spring Boot help build stand-alone, production Spring Applications easily, less configuration then rapidly start new projects."))

        loadQueryAll()
                //
        lvNotes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + listNotes[position].title+ " "+listNotes[position].id+ " "+position+ " "+id , Toast.LENGTH_SHORT).show()
        }
    }
//переопределяем метод что бы у нас заработало меню, меню надувается из R.menu.menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    //вывести супер он креате
    }
    // Обрабатываем нажатие на элемент меню
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // если на элемент меню нажали,
        if (item != null) {
            //если нажали на элемент меню с айдишником итем айди,
            when (item.itemId) {
                //если нажали на ЭЛЕМЕНТ МЕНЮ add note создаем интент
                R.id.addNote -> {
                    //нас перебрасывает на второе активити
                    var intent = Intent(this, NoteActivity::class.java)
                            // запустить активити в интент
                    startActivity(intent)

                }

            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        loadQueryAll()
    }

    fun loadQueryAll() {
//Создаем экзепляр класса notedbmenager для работы с базой данных
        var dbManager = NoteDbManager(this)
        val cursor = dbManager.queryAll()
                //очистить лист
        listNotes.clear()
        //если возможно двигаемся к первому элементу курсора
        if (cursor.moveToFirst()) {
//считываем значение для каждой строки построчно из таблицы
            //do делать
           do {
                // курсор содержит результат выборки, из курсора для каждой таблицы достаем значение по именна каждого столбца
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val content = cursor.getString(cursor.getColumnIndex("Content"))
//дабавляем список новый объект класса note в конструктор которого передали значение прочитанного из каждой строки даблицы
                val note=Note(id,title,content)
                        // дабавляем запись в список 
                listNotes.add(note)
                        //двигаемся к следующий строки пока это возможно
            } while (cursor.moveToNext())

        }
                //создаем экземпляр класса ноте адаптер и передаём конструктор ( this - ссылку на тек. активити и заполненый список который мы считали с базы данных )
        var notesAdapter = NotesAdapter(this, listNotes)
        // для лист вию  нужен адаптер что бы отобразить данные
        lvNotes.adapter = notesAdapter
    }
//адаптер необходим что бы стандартный контейнер лист вию смог отобразить не стандартные данные
    inner class NotesAdapter : BaseAdapter {

        private var notesList = ArrayList<Note>()
        private var context: Context? = null
//получаем снаружи контенкст (где работаем) и список данных которых нужно отобразить
        constructor(context: Context, notesList: ArrayList<Note>) : super() {
            this.notesList = notesList
            this.context = context
        }
//функция гетвию принимает (пизицию типа инт, контент вию типа вию(может равнятся нулю) парент типа виюгрупп)
    //метод getView дает отображение для каждого элемента списка и записывает в это отображение конкретное значение
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
    //объявили объект вию холдер
            val vh: ViewHolder
//если конверт вию будет равен нулю ( тогда выполняем действие в скобочках)
            if (convertView == null) {
                // создаем объект вию на основание разметки, для каждого элемента списка
                view = layoutInflater.inflate(R.layout.note, parent, false)
                // создаем экзепляр класса вию холдер, куда передаём наше вию
                vh = ViewHolder(view)
                //вию элемента элемента списка хранит в себе объект вию в поле тег
                view.tag = vh
                //логирование ( куда будет записывать что ....
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                //если  (convertView == null) не будет равен этой строчке тогда выполняем это действие
                view = convertView
                vh = view.tag as ViewHolder
            }
//достаем объект ноте из нотелиста по позиции (1 элемнт списка или 3,4 )
            var mNote = notesList[position]
                    // вию холдер содержит ссылки на все элементы разметки и мы передаем в поле тв тайтл значение которе дастали из объекта ноте-поле тайтл
            vh.tvTitle.text = mNote.title
            vh.tvContent.text = mNote.content

            vh.ivEdit.setOnClickListener {
                updateNote(mNote)
            }
// подключаем слушателя к ivDelete
            vh.ivDelete.setOnClickListener {
                var dbManager = NoteDbManager(this.context!!)
                val selectionArgs = arrayOf(mNote.id.toString())
                dbManager.delete("Id=?", selectionArgs)
                loadQueryAll()
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }
    }

    private fun updateNote(note: Note) {
    // переменная интент ( в текущем (пакедж каннтекст)
        var intent = Intent(this, NoteActivity::class.java)
        intent.putExtra("MainActId", note.id)
        intent.putExtra("MainActTitle", note.title)
        intent.putExtra("MainActContent", note.content)
        startActivity(intent)
    }
//создаем класс в вию холдер ( принимает вию (который может равнятся нулю)
    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tvContent: TextView
        val ivEdit: ImageView
        val ivDelete: ImageView
// инит инициализацию то есть подключаем то что получили вышe
        init {
            this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById(R.id.tvContent) as TextView
            this.ivEdit = view?.findViewById(R.id.ivEdit) as ImageView
            this.ivDelete = view?.findViewById(R.id.ivDelete) as ImageView
        }
    }

}

