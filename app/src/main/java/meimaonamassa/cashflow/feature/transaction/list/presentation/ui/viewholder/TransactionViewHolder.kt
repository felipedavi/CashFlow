package meimaonamassa.cashflow.feature.transaction.list.presentation.ui.viewholder

import android.app.AlertDialog
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import meimaonamassa.cashflow.R
import meimaonamassa.cashflow.base.DateConverters
import meimaonamassa.cashflow.data.entity.TransactionEntity
import meimaonamassa.cashflow.databinding.ItemTransactionBinding
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModel
import meimaonamassa.cashflow.util.extension.fromFormattedDate
import meimaonamassa.cashflow.util.extension.toCurrency

class TransactionViewHolder(
    private val itemBinding: ItemTransactionBinding,
    private val listener: (TransactionEntity) -> Unit,
    private val viewModel: TransactionViewModel
) :
    RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

    private lateinit var transaction: TransactionEntity

    fun bind(data: TransactionEntity) {
        transaction = data

        itemBinding.textItemTransaction.text = transaction.description
        itemBinding.textItemTransactionDate.text =
            DateConverters.fromOffsetDateTime(transaction.date).toString().fromFormattedDate()
        itemBinding.textItemTransactionMonetaryValue.text = transaction.monetaryValue.toCurrency()

        if (transaction.transactionType) {
            itemBinding.itemView.setBackgroundColor(0xFF00FF00.toInt())
        } else {
            itemBinding.itemView.setBackgroundColor("#FF0000".toColorInt())
        }

        itemView.setOnClickListener {
            listener.invoke(transaction)
        }

        itemView.setOnLongClickListener {
            if (transaction.isInstallment && transaction.installmentGroupId != null) {
                AlertDialog.Builder(itemView.context)
                    .setTitle(R.string.transaction_removal)
                    .setMessage("Deseja remover apenas esta parcela ou todas do grupo?")
                    .setPositiveButton("Todas do Grupo") { _, _ ->
                        viewModel.deleteGroup(transaction.installmentGroupId!!)
                    }
                    .setNegativeButton("Apenas Esta") { _, _ ->
                        viewModel.delete(transaction)
                    }
                    .setNeutralButton(R.string.cancel, null)
                    .show()
            } else {
                AlertDialog.Builder(itemView.context)
                    .setTitle(R.string.transaction_removal)
                    .setMessage(R.string.want_remove)
                    .setPositiveButton(R.string.remove) { _, _ ->
                        viewModel.delete(transaction)
                    }
                    .setNeutralButton(R.string.cancel, null)
                    .show()
            }
            true
        }

    }

    override fun onClick(v: View?) {
        listener.invoke(transaction)
    }
}