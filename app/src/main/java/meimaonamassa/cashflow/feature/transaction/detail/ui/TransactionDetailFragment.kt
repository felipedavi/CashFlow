package meimaonamassa.cashflow.feature.transaction.detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import meimaonamassa.cashflow.MainApplication
import meimaonamassa.cashflow.R
import meimaonamassa.cashflow.base.DateConverters
import meimaonamassa.cashflow.data.entity.TransactionEntity
import meimaonamassa.cashflow.databinding.FragmentTransactionDetailBinding
import meimaonamassa.cashflow.feature.transaction.detail.TransactionDetailViewModel
import meimaonamassa.cashflow.feature.transaction.detail.TransactionDetailViewModelFactory
import meimaonamassa.cashflow.util.CurrencyTextWatcher
import meimaonamassa.cashflow.util.DatePickerFragment
import meimaonamassa.cashflow.util.extension.*

class TransactionDetailFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentTransactionDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TransactionDetailFragmentArgs by navArgs()
    private lateinit var viewModel: TransactionDetailViewModel
    private lateinit var selectedTransaction: TransactionEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            this, TransactionDetailViewModelFactory(
                (requireActivity().application as MainApplication).repository
            )
        )[TransactionDetailViewModel::class.java]

        val id = args.transactionID
        viewModel.start(id)

        observe()
        setListeners()

        return binding.root
    }

    override fun onClick(v: View?) {
        val id: Int? = v?.id
        if (id == R.id.button_update) {
            if (!binding.editDescription.isValid() or !binding.editMoney.isValid() or !binding.editDate.isValid()) Log.i(
                "Validation",
                null.toString()
            )
            else if (binding.groupRadioTransactionType.checkedRadioButtonId == -1) Toast.makeText(
                context,
                getText(R.string.group_radio_error),
                Toast.LENGTH_SHORT
            ).show()
            else {
                val description = binding.editDescription.text.toString().trim()
                val date = DateConverters.toOffsetDateTime(
                    binding.editDate.text.toString().toFormattedDate()
                )
                val monetaryValue = binding.editMoney.text.toString().fromCurrency()
                val transactionType = binding.radioIncome.isChecked

                val updatedTransaction = selectedTransaction.copy(
                    description = description,
                    date = date,
                    monetaryValue = monetaryValue,
                    transactionType = transactionType
                )

                viewModel.update(updatedTransaction)
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewModel.transaction.observe(viewLifecycleOwner) {
            selectedTransaction = it
            binding.editDescription.setText(selectedTransaction.description)
            binding.editDate.setText(
                DateConverters.fromOffsetDateTime(selectedTransaction.date).toString()
                    .fromFormattedDate()
            )
            binding.editMoney.setText(selectedTransaction.monetaryValue.toCurrency())
            if (selectedTransaction.transactionType) {
                binding.radioIncome.isChecked = true
            } else {
                binding.radioExpense.isChecked = true
            }
        }
    }

    private fun setListeners() {
        binding.editMoney.run {
            addTextChangedListener(CurrencyTextWatcher(this))
        }

        binding.editDate.keyListener = null

        binding.editDate.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            hideKeyboard(view)
            if (hasFocus) {
                DatePickerFragment(binding.editDate) { binding.editDate.setText(it) }.show(
                        requireActivity().supportFragmentManager,
                        "datePicker"
                    )
                binding.editDate.clearFocus()
            }
        }

        binding.buttonUpdate.setOnClickListener(this)
    }

}