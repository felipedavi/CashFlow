package br.edu.ifrj.portal.cashflow.feature.transaction.list.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ifrj.portal.cashflow.R
import br.edu.ifrj.portal.cashflow.databinding.FragmentTransactionBinding

class TransactionFragment : Fragment() {
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
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

}