package com.diabetic.infrastructure.persistent.file.stub

import java.io.File

internal object TestStorage {
    private val baseDir = File("src/test/res/runtime")

    fun get(subPaths: String): File {
        val file = File(baseDir, subPaths)
        if (file.exists()) {
            file.delete()
        }

        if (!file.createNewFile()) {
            throw RuntimeException("Cannot create new file $subPaths")
        }
        return file
    }
}