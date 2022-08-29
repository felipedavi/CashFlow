package br.edu.ifrj.portal.cashflow.feature.transaction.list.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import br.edu.ifrj.portal.cashflow.databinding.ItemTransactionBinding
import br.edu.ifrj.portal.cashflow.feature.transaction.list.presentation.TransactionViewModel
import br.edu.ifrj.portal.cashflow.feature.transaction.list.presentation.ui.viewholder.TransactionViewHolder

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