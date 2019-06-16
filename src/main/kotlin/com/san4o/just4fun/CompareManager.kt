package com.san4o.just4fun

import java.io.File
import java.lang.RuntimeException

class CompareManager(
        private val extensions: Array<String> = CompareManager.DEFAULT_EXTENSIONS,
        private val compareMode: CompareMode = CompareManager.DEFAULT_MODE,
        private val copyNewFilesFolderPath: String,
        private val overrideNewFiles: Boolean = true
) {
    companion object {
        val DEFAULT_EXTENSIONS = arrayOf("jpg", "jpeg", "bmp", "png")
        val DEFAULT_MODE = CompareMode.VIEW
    }

    fun compare(scanFolder: String, sourceFolder: String) {
        if (scanFolder.isEmpty() || sourceFolder.isEmpty()) {
            throw RuntimeException("Error params")
        }
        val scanFolderRootFile = File(scanFolder).also { it.checkExist() }
        val sourceFolderRootFile = File(sourceFolder).also { it.checkExist() }

        val scanFolderFiles = scanFolderRootFile.findFilesRecursively { fileExtensionsFilter(it) }
        val sourceFolderFiles = sourceFolderRootFile.findFilesRecursively { fileExtensionsFilter(it) }

//        scanFolderFiles.forEach { println("scan: $it ${it.prettyLength()}") }
//        sourceFolderFiles.forEach { println("source: $it ${it.prettyLength()}") }

        println("SOURCE ${sourceFolderRootFile.path} : Find ${sourceFolderFiles.size} files ")
        println("SCAN ${scanFolderRootFile.path} : Find ${scanFolderFiles.size} files ")
        println()

        print("Scanning... ")
        val newFiles = scanFolderFiles.filterOnlyNew(sourceFolderFiles) { scanFile, sourceFile -> isMatch(scanFile, sourceFile) }
        println("OK")

        if (newFiles.isEmpty()) {
            println("No new files in $scanFolder")
            return
        }

        println("Find ${newFiles.size} new files in ${scanFolderRootFile.absolutePath}")

        if (compareMode == CompareMode.COPY) {
            val copyNewFilesFolderFile = File(copyNewFilesFolderPath).also {
                if (it.mkdirs()) {
                    println("Create folder $copyNewFilesFolderPath")
                }
            }

            var i = 1
            var size = newFiles.size
            for (it in newFiles) {

                val targetFile = File(copyNewFilesFolderFile, it.absolutePath.replaceFirst(scanFolderRootFile.absolutePath, ""))

                val copyToFile = it.copyTo(targetFile, overrideNewFiles)
                if (copyToFile.exists()) {
                    print("\r${i++}/$size : ${targetFile.absolutePath} copied")
                }
            }
            println("\rComplete copy $size files                                                                                                                ")
        }

    }

    private fun isMatch(scanFile: File, sourceFile: File): Boolean {
        return scanFile.name == sourceFile.name
                && scanFile.length() == sourceFile.length()
    }

    private fun fileExtensionsFilter(file: String): Boolean = extensions.any { file.endsWith(".$it") }
}