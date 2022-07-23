package fr.hugotiem.citymapper.model

import java.util.*

open class Result(var time: Int)

class TransitResult(time: Int, var price: Double, var arrivedAt: Date, var steps: List<String>) : Result(time) {}

class DrivingResult(time: Int): Result(time) {}