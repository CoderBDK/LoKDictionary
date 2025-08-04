package com.coderbdk.lokdictionary.data.model

enum class WordType(val typeName: String) {
    UNKNOWN("Unknown Word Type"),
    NOUN("Noun"),
    PRONOUN("Pronoun"),
    VERB("Verb"),
    ADJECTIVE("Adjective"),
    ADVERB("Adverb"),
    PREPOSITION("Preposition"),
    CONJUNCTION("Conjunction"),
    ARTICLE("Article")
}