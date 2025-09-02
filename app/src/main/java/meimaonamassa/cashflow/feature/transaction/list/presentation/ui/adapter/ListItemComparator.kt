package meimaonamassa.cashflow.feature.transaction.list.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import meimaonamassa.cashflow.feature.transaction.list.presentation.ui.ListItem

class ListItemComparator : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem == newItem
    }
}