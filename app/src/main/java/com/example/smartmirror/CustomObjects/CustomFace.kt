package com.example.smartmirror.CustomObjects

import android.content.Context
import android.graphics.PointF
import java.io.FileWriter
import java.io.IOException
import kotlin.math.pow
import kotlin.math.sqrt

class CustomFace {
    // Constructor
    constructor(faceParams: String) {
        this.faceParams = faceParams
        this.dictFaceParams = toMap(this.faceParams)
        this.listLandmarks = getLandmarks(this.dictFaceParams)
        this.name = this.dictFaceParams["name"]!!
    }


    // -- Parameters --
    private var dictFaceParams: Map<String, String>
    private var listLandmarks: List<MutableList<String>>
    private var faceParams: String
    var name: String

    // -- Functions --
    fun saveToFile(context: Context) {
        val filePath = "${context.getFilesDir().getPath()}/saved_face.txt"

        try {
            val fileWriter = FileWriter(filePath)
            fileWriter.append(this.faceParams)

            fileWriter.flush()
            fileWriter.close()

        } catch (e: IOException) {
            println("Error saving face: $e")
        }
    }

    // Compare the points of face based on a proximity parameter (proximity)
    fun compare(face2: CustomFace, proximity: Float): Boolean {
        var isClose = true
        val numRows = this.listLandmarks.size

        for (i in 0 until numRows) {
            val landmarkPoint1 = this.listLandmarks[i][1]
            val landmarkPoint2 = face2.listLandmarks[i][1]

            val regex = Regex("[PointF\\(\\)\\s]")

            val stringPoint1 = landmarkPoint1.replace(regex, "").split(",")
            val stringPoint2 = landmarkPoint2.replace(regex, "").split(",")

            val point1 = PointF(stringPoint1[0].toFloat(), stringPoint1[1].toFloat())
            val point2 = PointF(stringPoint2[0].toFloat(), stringPoint2[1].toFloat())

            if (euclideanDistance(point1, point2) > proximity) {
                isClose = false
            }
        }
        return isClose
    }

    override fun toString(): String {
        var result = ""
        val rows = this.faceParams.split(";")
        for (row in rows) {
            result += "$row\n"
        }
        return result
    }

    // -- Private functions --

    private fun euclideanDistance(p1: PointF, p2: PointF): Double {

        var part1 = (p2.x - p1.x).toDouble().pow(2.0)
        var part2 = (p2.y - p1.y).toDouble().pow(2.0)

        return sqrt(part1 + part2)
    }

    private fun toMap(faceParams: String): Map<String, String> {
        return faceParams.split(";").filter { it.isNotBlank() }.map { it.split("_") }
            .associate { (key, value) -> key to value }
    }

    private fun getLandmarks(dictFaceParams: Map<String, String>): List<MutableList<String>> {
        var finalList = mutableListOf<MutableList<String>>()
        val landmarksVal = listOf("1 ", "3 ", "4 ", "0 ", "5 ", "11 ", "6 ", "7 ", "9 ", "10 ")

        for (key in dictFaceParams.keys) {
            if (key in landmarksVal) {
                finalList.add(listOf("$key", "${dictFaceParams[key]}").toMutableList())
            }
        }

        return finalList.toList()
    }

}



