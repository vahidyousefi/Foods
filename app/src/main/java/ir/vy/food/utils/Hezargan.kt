package ir.vy.food.utils

fun String.withThousandSeparator(): String {
    return try {
        val number = this.toLong()
        "%,d".format(number)
    } catch (e: Exception) {
        this
    }
}
