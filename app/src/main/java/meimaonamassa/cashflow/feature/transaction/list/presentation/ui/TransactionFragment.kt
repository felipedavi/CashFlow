package meimaonamassa.cashflow.feature.transaction.list.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import meimaonamassa.cashflow.MainApplication
import meimaonamassa.cashflow.R
import meimaonamassa.cashflow.base.DateConverters
import meimaonamassa.cashflow.databinding.FragmentTransactionBinding
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModel
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModelFactory
import meimaonamassa.cashflow.feature.transaction.list.presentation.ui.adapter.GroupedTransactionAdapter
import meimaonamassa.cashflow.util.extension.fromFormattedDate
import meimaonamassa.cashflow.util.extension.toCurrency

class TransactionFragment : Fragment() {
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: GroupedTransactionAdapter
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, TransactionViewModelFactory((requireActivity().application
                as MainApplication).repository)
        )[TransactionViewModel::class.java]

        adapter = GroupedTransactionAdapter(::transactionListClickListener, viewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTransactions.layoutManager = LinearLayoutManager(context)
        binding.recyclerTransactions.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, systemBars.bottom)
            insets
        }

        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            _binding?.let { safeBinding ->
                if (transactions.isNullOrEmpty()) {
                    safeBinding.recyclerTransactions.visibility = View.GONE
                    safeBinding.emptyContainer.root.visibility = View.VISIBLE
                    safeBinding.balanceContainer.root.visibility = View.GONE
                } else {
                    safeBinding.recyclerTransactions.visibility = View.VISIBLE
                    safeBinding.emptyContainer.root.visibility = View.GONE
                    safeBinding.balanceContainer.root.visibility = View.VISIBLE

                    val groupedList = mutableListOf<ListItem>()
                    val groupedByDate = transactions.groupBy {
                        it.date?.let { date ->
                            DateConverters.fromOffsetDateTime(date)
                        }
                    }
                    groupedByDate.keys.sortedByDescending { it }.forEach { date ->
                        date?.let { d ->
                            groupedList.add(ListItem.Header(d.fromFormattedDate()))
                            val sortedItems = groupedByDate[d]?.sortedByDescending { it.monetaryValue }
                            sortedItems?.forEach { transaction ->
                                groupedList.add(ListItem.TransactionItem(transaction))
                            }
                        }
                    }
                    adapter.submitList(groupedList)
                }
            }
        }

        viewModel.totalIncome.observe(viewLifecycleOwner) { totalIncome ->
            _binding?.balanceContainer?.textIncome?.text = totalIncome.toCurrency()
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) { totalExpense ->
            _binding?.balanceContainer?.textExpense?.text = totalExpense.toCurrency()
        }

        viewModel.balance.observe(viewLifecycleOwner) { balance ->
            _binding?.balanceContainer?.textBalance?.text = balance.toCurrency()
        }

        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.navigateToTransactionAddFragment)
        }
    }

    private fun transactionListClickListener(id: Int) {
        val action = TransactionFragmentDirections
            .navigateToTransactionDetailFragment(id)
        findNavController().navigate(action)
    }
}