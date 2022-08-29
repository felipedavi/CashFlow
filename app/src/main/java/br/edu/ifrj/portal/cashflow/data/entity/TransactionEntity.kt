package br.edu.ifrj.portal.cashflow.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "transaction_id") val id: Int = 0,
    var description: String,
    var date: String,
    @ColumnInfo(name = "monetary_value") var monetaryValue: Double,
    @ColumnInfo(name = "transaction_type") var transactionType: Boolean
)
