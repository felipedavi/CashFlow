package meimaonamassa.cashflow.feature.transaction.list.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import meimaonamassa.cashflow.MainApplication
import meimaonamassa.cashflow.R
import meimaonamassa.cashflow.databinding.FragmentTransactionBinding
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModel
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModelFactory
import meimaonamassa.cashflow.feature.transaction.list.presentation.ui.adapter.TransactionAdapter

class TransactionFragment : Fragment() {
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter
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

        adapter = TransactionAdapter(::transactionListClickListener)
        adapter.setViewModel(viewModel)

        val recycler = binding.recyclerTransactions
        recycler.layoutManager = LinearLayoutManager(context)

        recycler.adapter = adapter

        viewModel.allTransactions.observe(requireActivity()) { transactions ->
            if (transactions.isNullOrEmpty()) {
                recycler.visibility = View.GONE
                binding.emptyContainer.root.visibility = View.VISIBLE
            } else {
                recycler.visibility = View.VISIBLE
                binding.emptyContainer.root.visibility = View.GONE
                transactions.let {
                    adapter.submitList(it)
                }
            }
        }
        setListeners()

        return binding.root
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
        val directions = TransactionFragmentDirections.navigateToTransactionDetailFragment()
        directions.transactionID = id
        findNavController().navigate(directions)
    }

}