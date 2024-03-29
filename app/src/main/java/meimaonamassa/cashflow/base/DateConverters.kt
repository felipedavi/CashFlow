package meimaonamassa.cashflow.base

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

object DateConverters {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return LocalDate.parse(it, formatter)
                .atStartOfDay(ZoneOffset.UTC)
                .toOffsetDateTime()
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
}