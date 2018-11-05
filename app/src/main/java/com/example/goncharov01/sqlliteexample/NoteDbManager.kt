package com.example.goncharov01.sqlliteexample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class NoteDbManager {

    private val dbName = "JSANotes"
    private val dbTable = "Notes"
    private val colId = "Id"
    private val colTitle = "Title"
    private val colContent = "Content"
    private val dbVersion = 1

    private val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colId + " INTEGER PRIMARY KEY," + colTitle + " TEXT, " + colContent + " TEXT);"
// создание таблицы креате табле скюл
    private var db: SQLiteDatabase? = null

    constructor(context: Context) {
        var dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }
// используется для добавления новых строк данных в таблицу в базе данных ПРИМЕР:(INSERT INTO COMPANY VALUES (7, 'James', 24, 'Houston', 10000.00 );)
    fun insert(values: ContentValues): Long {
        //функция инсерт( вставить) переменная:принимает contentValue):  будет длинный
//INSERT INTO dbTable VALUES (7, 'James', 24, 'Houston', 10000.00 )
        val ID = db!!.insert(dbTable, "", values) //контент валью хранит данные в формате ключ значение: ключ имя столбца, значение, значение столбца
        //переменная id=db=sqliteDatabe!! вставить (notes, и написать переменную)
        return ID
                //вывести id
    }
//используется для извлечения данных из таблицы базы данных SQLite, которая возвращает данные в форме таблицы результатов.
    fun queryAll(): Cursor {
//("select * from " + dbTable)  - выбрать всё, вром и имя таблицы   select * from dbTable
         return db!!.rawQuery("select * from " + dbTable, null)
                //вывести db!! запрос на выбрать все + notes = по началу равен нулю)
    }
//Запрос используется для удаления существующих записей из таблицы ПРИМЕР:(DELETE FROM COMPANY WHERE ID = 7;)
    fun delete(selection: String, selectionArgs: Array<String>): Int {
//функция делейт (удалить) выбраное типа стринг и изменить на ( на что изменяем) это все типа инт
        val count = db!!.delete(dbTable, selection, selectionArgs)
                //переменная каунт=SQLiteDatabase удалить (имя таблицы, что удаляем)
        return count
    }
//Запрос используется для изменения существующих записей в таблице. ПРИМЕР:(UPDATE COMPANY SET ADDRESS = 'Texas' WHERE ID = 6;)
    fun update(values: ContentValues, selection: String, selectionargs: Array<String>): Int {
//функция апдате (переменная валуе принимает контент валуе, выбираем что хотим обновить в аррай стринг вводим на что обновляем
        val count = db!!.update(dbTable, values, selection, selectionargs)
                //переменная коунт SQLiteDatabase = обновить(Table переменная,
        return count
        //вывести коунт
    }
//внутренний класс дата бэйс хелпер способный создать или обновить базу данных
    inner class DatabaseHelper : SQLiteOpenHelper {


        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            //создаем конструктор ( контекст принимает контекст ) супер класс(контекст:JSANotes
            this.context = context
            //контекс выводит контекст
        }

        override fun onCreate(db: SQLiteDatabase?) {
            //переопределяем функцию он креате(notes принимает в себя sqllitedatabase может ровнятся нулю ничего не выводит)
            db!!.execSQL(CREATE_TABLE_SQL)
            //execSQL выполнить sql запрос и создать таблицу креате табле sql
            Toast.makeText(this.context, " database is created", Toast.LENGTH_LONG).show()
            //принимает три параметра первый парментр в каком контексте какого активити это будеть, что напишет database is create, и как долго будет весеть сообщение long (долго)  show показать всплывающие сообщ,

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            //переопределяем функцию абгрейт(обновить) где db=SQLiteDatabase  олд версион типа интеджер
            db!!.execSQL("Drop table IF EXISTS " + dbTable)
            onCreate(db)
            //выполнить sql запрос который сделает удалить таблицу если она есть + dbTable=Notes
        }
    }
}