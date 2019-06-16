package com.san4o.just4fun

import java.io.File
import java.io.FilenameFilter
import java.lang.RuntimeException

fun readNonNullLine() : String = readLine() ?: ""

fun readLineOrDefault(defaultValue: String, strict: Boolean = false) : String {
    val text = readNonNullLine()
    if (text.isEmpty()){
        if (defaultValue.isNotEmpty()) {
            return defaultValue
        }else{
            if (strict) {
                throw RuntimeException("Error. Input empty string")
            }
        }
    }
    return text
}
fun readLineOrThrow() : String {
    val line = readNonNullLine()
    if (line.isEmpty()){
        throw RuntimeException("Error input empty string")
    }
    return line
}

fun <T> List<T>.filterOnlyNew(anotherList: List<T>, isMatch: (T, T) -> Boolean): List<T> {
    return this.filter { thisListItem -> anotherList.none { anotherListItem -> isMatch.invoke(thisListItem, anotherListItem) } }
}
fun File.createFile(){
    if (!this.exists()){
        val createNewFile = if (this.createNewFile()) {""}else{"DOESN'T"}

        println("File ${this.absolutePath} $createNewFile created")
    }
}
fun File.checkExist() {
    if (!this.exists()) {
        throw RuntimeException("File ${this.absolutePath} DOESN'T EXIST")
    }
}

fun File.findFilesRecursively(filter: (String) -> Boolean = { true }): List<File> {
    val listFiles = this.listFiles { f -> f.isDirectory || filter.invoke(f.name) }

    val files = ArrayList<File>()
    files.addAll(listFiles.filter { !it.isDirectory })
    files.addAll(listFiles.filter { it.isDirectory }.flatMap { it.findFilesRecursively(filter) })
    return files
}

fun File.prettyLength(): String {
    val length = this.length()
    val kb = length / 1024
    val mb = kb / 1024
    val gb = mb / 1024


    if (gb != 0L) {
        val ostMb = mb - gb * 1024
        return "$gb.$ostMb GB"
    }
    if (mb != 0L) {
        val ostKB = kb - mb * 1024
        return "$mb.$ostKB MB"
    }

    return "$kb KB"
}

private class EmptyFilenameFilter() : FilenameFilter {
    override fun accept(dir: File?, name: String?): Boolean = true

}