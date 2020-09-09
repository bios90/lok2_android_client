package com.dimfcompany.akcslclient.logic.utils

import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.getStringMy
import org.joda.time.DateTime
import org.joda.time.Minutes
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*

class DateManager
{
    companion object
    {
        val FORMAT_FOR_PHOTO_FILE = getStringMy(R.string.format_for_file)
        val FORMAT_FOR_SERVER_LARAVEL = getStringMy(R.string.format_for_server_laravel)
        val FORMAT_FOR_DISPLAY = getStringMy(R.string.format_for_display)
        val FORMAT_FOR_DISPLAY_WITH_TIME = getStringMy(R.string.format_for_display_with_time)
    }
}


//Date extensions
fun Date.formatToString(format: String): String
{
    return SimpleDateFormat(format).format(this).toString()
}

fun Date.formatToString(): String
{
    return this.formatToString(DateManager.FORMAT_FOR_DISPLAY)
}

fun Date.getHour(): Int
{
    val date_joda = DateTime(this)
    return date_joda.hourOfDay
}

fun Date.getMinute(): Int
{
    val date_joda = DateTime(this)
    return date_joda.minuteOfHour
}

fun Date.addMinutes(minutes: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.plusMinutes(minutes).toDate()
}

fun Date.minusMinutes(minutes: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.minusMinutes(minutes).toDate()
}

fun Date.addHours(hours: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.plusHours(hours).toDate()
}

fun Date.minusHours(hours: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.minusHours(hours).toDate()
}

fun Date.addDays(days: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.plusDays(days).toDate()
}

fun Date.minusDays(days: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.minusDays(days).toDate()
}

fun Date.addMonths(months: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.plusMonths(months).toDate()
}

fun Date.minusMonths(months: Int): Date
{
    val date_joda = DateTime(this)
    return date_joda.minusMonths(months).toDate()
}

fun Date.setMinutesMy(minutes: Int): Date
{
    if (minutes in (0 until 60) == false)
    {
        throw RuntimeException("***** Error wrong minutes to set *****")
    }

    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.MINUTE, minutes)
    return calendar.time
}

fun Date.setHoursMy(hours: Int): Date
{
    if (hours in (0 until 24) == false)
    {
        throw RuntimeException("***** Error wrong hours to set *****")
    }

    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, hours)
    return calendar.time
}

fun getHourMinuteDiff(date_1: Date, date_2: Date): Pair<Int, Int>
{
    val date_joda_1 = DateTime(date_1)
    val date_joda_2 = DateTime(date_2)

    val minutes_obj = Minutes.minutesBetween(date_joda_1, date_joda_2).minutes
    val hours = minutes_obj / 60
    val minutes = minutes_obj % 60
    return Pair(hours, minutes)
}

fun areDatesEqualForDiff(date_1: Date?, date_2: Date?): Boolean
{
    if (date_1 == null && date_2 == null)
    {
        return true
    }

    if (date_1 == null || date_2 == null)
    {
        return false
    }

    return areAtSameDayHourMinuteSecond(date_1, date_2)
}

fun areAtSameDayHourMinuteSecond(date_1: Date, date_2: Date): Boolean
{
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = date_1
    cal2.time = date_2
    val same_day =
            (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR))
                    && (cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY))
                    && (cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE))
                    && (cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND)))

    return same_day
}