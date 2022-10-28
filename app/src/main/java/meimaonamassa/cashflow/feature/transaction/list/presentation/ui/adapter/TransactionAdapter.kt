package meimaonamassa.cashflow.feature.transaction.list.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import meimaonamassa.cashflow.data.entity.TransactionEntity
import meimaonamassa.cashflow.databinding.ItemTransactionBinding
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModel
import meimaonamassa.cashflow.feature.transaction.list.presentation.ui.viewholder.TransactionViewHolder

class TransactionAdapter internal constructor(private val listener: (Int) -> Unit) :
    ListAdapter<TransactionEntity, TransactionViewHolder>(TransactionComparator()) {

    private lateinit var viewModel: TransactionViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemBinding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(itemBinding, listener, viewModel)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    fun setViewModel(viewModel: TransactionViewModel) {
        this.viewModel = viewModel
    }

}