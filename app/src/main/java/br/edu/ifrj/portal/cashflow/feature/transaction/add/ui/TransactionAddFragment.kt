package br.edu.ifrj.portal.cashflow.feature.transaction.add.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.ifrj.portal.cashflow.databinding.FragmentTransactionAddBinding
import br.edu.ifrj.portal.cashflow.util.DatePickerFragment

class TransactionAddFragment : Fragment() {
    private var _binding: FragmentTransactionAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionAddBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.editDate.keyListener = null

        binding.editDate.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                DatePickerFragment(binding.editDate.text.toString()) { binding.editDate.setText(it) }
                    .show(requireActivity().supportFragmentManager, "datePicker")
            }
        }
    }

}