package meimaonamassa.cashflow.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "transaction_id") val id: Int = 0,
    var payerPayee: String,
    var description: String,
    var date: OffsetDateTime?= null,
    @ColumnInfo(name = "monetary_value") var monetaryValue: Double,
    @ColumnInfo(name = "transaction_type") var transactionType: Boolean,
    @ColumnInfo(name = "is_installment") var isInstallment: Boolean = false,
    @ColumnInfo(name = "installment_current") var installmentCurrent: Int? = null,
    @ColumnInfo(name = "installment_total") var installmentTotal: Int? = null,
)
