package br.edu.ifrj.portal.cashflow.feature.transaction.add.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.edu.ifrj.portal.cashflow.MainApplication
import br.edu.ifrj.portal.cashflow.R
import br.edu.ifrj.portal.cashflow.base.DateConverters
import br.edu.ifrj.portal.cashflow.data.entity.TransactionEntity
import br.edu.ifrj.portal.cashflow.databinding.FragmentTransactionAddBinding
import br.edu.ifrj.portal.cashflow.feature.transaction.add.TransactionAddViewModel
import br.edu.ifrj.portal.cashflow.feature.transaction.add.TransactionAddViewModelFactory
import br.edu.ifrj.portal.cashflow.util.CurrencyTextWatcher
import br.edu.ifrj.portal.cashflow.util.DatePickerFragment
import br.edu.ifrj.portal.cashflow.util.extension.fromCurrency
import br.edu.ifrj.portal.cashflow.util.extension.hideKeyboard
import br.edu.ifrj.portal.cashflow.util.extension.isValid
import br.edu.ifrj.portal.cashflow.util.extension.toFormattedDate

class TransactionAddFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: TransactionAddViewModel
    private var _binding: FragmentTransactionAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionAddBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, TransactionAddViewModelFactory((requireActivity().application
                as MainApplication).repository)
        )[TransactionAddViewModel::class.java]
        setListeners()
        return binding.root
    }

    override fun onClick(v: View?) {
        val id: Int? = v?.id
        if (id == R.id.button_save) {
            if (!binding.editDescription.isValid() or  !binding.editMoney.isValid() or
                !binding.editDate.isValid())
                Log.i("Validation", null.toString())
            else if(binding.groupRadioTransactionType.checkedRadioButtonId == -1)
                Toast.makeText(context, getText(R.string.group_radio_error), Toast.LENGTH_SHORT).show()
            else {
                val description = binding.editDescription.text.toString().trim()
                val date = DateConverters.toOffsetDateTime(binding.editDate.text.toString().toFormattedDate())
                val monetaryValue = binding.editMoney.text.toString().fromCurrency()
                val transactionType = binding.radioIncome.isChecked
                val transaction = TransactionEntity(0, description, date, monetaryValue, transactionType)
                viewModel.insert(transaction)
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.editMoney.run {
            addTextChangedListener(CurrencyTextWatcher(this))
        }

        binding.editDate.keyListener = null

        binding.editDate.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            hideKeyboard(view)
            if (hasFocus) {
                DatePickerFragment(binding.editDate) { binding.editDate.setText(it) }
                    .show(requireActivity().supportFragmentManager, "datePicker")
                binding.editDate.clearFocus()
            }
        }

        binding.buttonSave.setOnClickListener(this)
    }

}