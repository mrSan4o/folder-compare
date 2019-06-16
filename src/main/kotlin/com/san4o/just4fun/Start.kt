package com.san4o.just4fun

import java.io.File
import javax.swing.filechooser.FileSystemView

const val source = "D:\\ФОТО"
const val scan = "D:\\test\\на продажу"
private val paramsManager = ParamsManager()

fun main(args: Array<String>) {


    val params = buildParams()

    val manager = CompareManager(
            copyNewFilesFolderPath = params.copyNewFilesFolder,
            compareMode = params.compareMode
    )

    manager.compare(
            scanFolder = params.scanFolder,
            sourceFolder = params.sourceFolder
    )
}

fun buildParams(): Params {

    val savedParams = paramsManager.read()
    if (savedParams.sourceFolder.isNotEmpty()
            && savedParams.scanFolder.isNotEmpty()){

        paramsManager.print(savedParams)
        print("Use it?(no, <enter> - yes): ")
        val useIt = readNonNullLine()
        if (useIt.isEmpty()){
            return savedParams
        }
    }


    val params = paramsManager.input(savedParams)

    println("Build params:")
    paramsManager.print(params)

    print("Is it right?(no, <enter> - yes): ")
    val isRight = readNonNullLine()
    if (isRight.isNotEmpty()){
        System.exit(0)
    }

    paramsManager.save(params)

    return params
}


//fun initialize() {
//    val services = UsbHostManager.getUsbServices()
//    val usbHub = services.rootUsbHub
//
//   usbHub.attachedUsbDevices.forEach{
//       println(it)
//   }
//}

fun printRootsInfo() {
    println("File system roots returned by   FileSystemView.getFileSystemView():")
    val fsv = FileSystemView.getFileSystemView()
    val roots = fsv.roots
    for (i in roots.indices) {
        println("Root: " + roots[i])
    }

    println("Home directory: " + fsv.homeDirectory)

    println("File system roots returned by File.listRoots():")

    val f = File.listRoots()
    for (i in f.indices) {
        println("Drive: " + f[i])
        println("Display name: " + fsv.getSystemDisplayName(f[i]))
        println("Is drive: " + fsv.isDrive(f[i]))
        println("Is floppy: " + fsv.isFloppyDrive(f[i]))
        println("Readable: " + f[i].canRead())
        println("Writable: " + f[i].canWrite())
        println()
    }
}

