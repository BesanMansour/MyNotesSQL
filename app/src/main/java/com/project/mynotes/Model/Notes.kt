package com.project.mynotes.Model

class Notes {

    var id:Int?=null
    var title:String?=null
    var des:String?=null

    constructor(id: Int?, title: String?, des: String?) {
        this.id = id
        this.title = title
        this.des = des
    }

    override fun toString(): String {
        return "Notes(id=$id, title=$title, des=$des)"
    }

}