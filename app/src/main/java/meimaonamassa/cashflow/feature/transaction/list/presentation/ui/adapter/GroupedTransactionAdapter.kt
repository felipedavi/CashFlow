package meimaonamassa.cashflow.feature.transaction.list.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import meimaonamassa.cashflow.databinding.ItemTransactionBinding
import meimaonamassa.cashflow.databinding.ListItemHeaderBinding
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModel
import meimaonamassa.cashflow.feature.transaction.list.presentation.ui.ListItem
import meimaonamassa.cashflow.feature.transaction.list.presentation.ui.viewholder.TransactionViewHolder

class GroupedTransactionAdapter internal constructor(
    private val listener: (Int) -> Unit,
    private val viewModel: TransactionViewModel
) :
    ListAdapter<ListItem, RecyclerView.ViewHolder>(ListItemComparator()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItem.Header -> VIEW_TYPE_HEADER
            is ListItem.TransactionItem -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val headerBinding = ListItemHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(headerBinding)
            }
            VIEW_TYPE_ITEM -> {
                val itemBinding = ItemTransactionBinding.inflate(inflater, parent, false)
                TransactionViewHolder(itemBinding, listener, viewModel)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ListItem.TransactionItem -> (holder as TransactionViewHolder).bind(item.transaction)
        }
    }

    class HeaderViewHolder(private val binding: ListItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: ListItem.Header) {
            binding.dateTextView.text = header.date
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}