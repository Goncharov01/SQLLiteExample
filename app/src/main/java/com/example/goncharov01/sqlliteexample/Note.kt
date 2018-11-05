package com.example.goncharov01.sqlliteexample

class Note {

    var id: Int? = null
    var title: String? = null
    var content: String? = null

//    Note(10, "заголовок", "контент")
    constructor(id: Int, title: String, content: String) {
        this.id = id
        this.title = title
        this.content = content
    }

}
