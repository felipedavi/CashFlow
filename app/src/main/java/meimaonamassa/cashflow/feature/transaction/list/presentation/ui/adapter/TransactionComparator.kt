package meimaonamassa.cashflow.feature.transaction.list.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import meimaonamassa.cashflow.data.entity.TransactionEntity

class TransactionComparator : DiffUtil.ItemCallback<TransactionEntity>() {
    override fun areItemsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: TransactionEntity,
        newItem: TransactionEntity
    ): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.description == newItem.description &&
                oldItem.date == newItem.date &&
                oldItem.monetaryValue == newItem.monetaryValue &&
                oldItem.transactionType == newItem.transactionType
    }
}