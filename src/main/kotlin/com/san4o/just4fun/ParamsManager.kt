package com.san4o.just4fun

import java.io.File
import java.lang.Exception
import java.lang.RuntimeException


class ParamsManager {
    private val fileName = "params.txt"


    fun print(params: Params) {
        println("   sourceFolder: '${params.sourceFolder}'\n" +
                "   scanFolder: '${params.scanFolder}'\n" +
                "   copyNew: '${params.copyNewFilesFolder}'\n" +
                "   compareMode: ${params.compareMode}\n" +
                "   extensions: ${params.targetExtensions}\n"
        )
    }


    private fun print(name : String, defaultValue: String){
        val appendix = if (defaultValue.isEmpty()) {
            ""
        } else {
            "(last: $defaultValue)"
        }
        print("$name $appendix: ")
    }

    fun input(defaultParams: Params = Params.empty()) : Params{
        print("source folder", defaultParams.sourceFolder)
        val source = readLineOrDefault(defaultParams.sourceFolder, true)

        print("scan folder", defaultParams.scanFolder)
        val scan = readLineOrDefault(defaultParams.scanFolder, true)

        print("copy new files folder[default:<source>\\new]", defaultParams.copyNewFilesFolder)
        var copyNew = readLineOrDefault(defaultParams.copyNewFilesFolder, false)
        if (copyNew.isEmpty()) {
            copyNew = source + File.separator + "new"
        }

        print("compare mode[default: view]", defaultParams.compareMode.name)
        val mode = toCompareMode(readLineOrDefault(defaultParams.compareMode.name, false))

        val defaultExtensions = CompareManager.DEFAULT_EXTENSIONS.joinToString(",")
        print("target extensions[default: $defaultExtensions]", defaultParams.targetExtensions)
        var extensions = readLineOrDefault(defaultParams.targetExtensions, false)
        if (extensions.isEmpty()){
            extensions = defaultExtensions
        }


        return Params(
                scanFolder = scan,
                sourceFolder = source,
                copyNewFilesFolder = copyNew,
                compareMode = mode,
                targetExtensions = extensions
        )
    }

    private fun toCompareMode(value: String) = when (value.toLowerCase()) {
        "view" -> CompareMode.VIEW
        "copy" -> CompareMode.COPY
        else -> CompareMode.VIEW
    }


    fun save(params: Params){
        val file = File(fileName).apply { createFile() }

        val textBuilder = StringBuilder()
        textBuilder.append(ParamsKey.source).append("=").append(params.sourceFolder).append("\n")
        textBuilder.append(ParamsKey.scan).append("=").append(params.scanFolder).append("\n")
        textBuilder.append(ParamsKey.copyNew).append("=").append(params.copyNewFilesFolder).append("\n")
        textBuilder.append(ParamsKey.mode).append("=").append(params.compareMode.name).append("\n")
        textBuilder.append(ParamsKey.extensions).append("=").append(params.targetExtensions).append("\n")

        file.writeText(textBuilder.toString())

        println("params saved")
    }

    fun read() : Params{
        val file = File(fileName)
        if (!file.exists()){
            println("no saved params")
            return Params.empty()
        }

        println("Find saved params : ${file.absolutePath}")

        val map = HashMap<ParamsKey, Any>()

        for (line in file.readLines()) {
            if (line.isEmpty()) continue

            val name = line.substringBefore("=")
            val value = line.substringAfter("=")

            val key = try { ParamsKey.valueOf(name) } catch (e: Exception){ throw RuntimeException("valueOf($name)", e)}
            map[key] = if (key == ParamsKey.mode) {toCompareMode(value)}else{value}
        }

        return Params(
                scanFolder = map.getOrDefault(ParamsKey.scan, "")as String,
                sourceFolder = map.getOrDefault(ParamsKey.source, "")as String,
                copyNewFilesFolder = map.getOrDefault(ParamsKey.copyNew, "") as String,
                compareMode = map.getOrDefault(ParamsKey.mode, CompareManager.DEFAULT_MODE) as CompareMode,
                targetExtensions = map.getOrDefault(ParamsKey.extensions, CompareManager.DEFAULT_EXTENSIONS.joinToString(",")) as String
        )
    }
}