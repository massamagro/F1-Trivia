package com.vozmediano.f1trivia.domain.model.quiz

class Question(
    var title : String,
    var options : MutableList<Option>,
    var image: String ? = null
){
    override fun toString(): String {
        var str = "$title\n"
        options.forEach{
            str += "${it.shortText}\n"
        }
        str += "$image"
        return str
    }
}