package com.san4o.just4fun

data class Params(
        val scanFolder: String,
        val sourceFolder: String,
        val copyNewFilesFolder: String,
        val compareMode: CompareMode,
        val targetExtensions: String
){
    companion object {
        fun empty() : Params = Params("", "", "", CompareMode.VIEW, "")
    }
}