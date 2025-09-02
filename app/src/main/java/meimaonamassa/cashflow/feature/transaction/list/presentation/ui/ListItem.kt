package meimaonamassa.cashflow.feature.transaction.list.presentation.ui

import meimaonamassa.cashflow.data.entity.TransactionEntity

sealed class ListItem {
    data class Header(val date: String) : ListItem()
    data class TransactionItem(val transaction: TransactionEntity) : ListItem()
}