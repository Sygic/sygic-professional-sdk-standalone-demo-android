package com.sygic.example.ipcdemo3d.utils

fun Int.formatTime(): String {
    val day = this / 60 / 60 / 24
    val hour = this / 60 / 60 - 24 * day
    val min = this / 60 - 60 * hour;

    return if (day == 0) {
        String.format("%d h %d min", hour, min);
    } else {
        String.format("%d days, %d h %d min", day, hour, min);
    }

}
