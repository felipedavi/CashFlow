package meimaonamassa.cashflow.feature.transaction.add.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import meimaonamassa.cashflow.MainApplication
import meimaonamassa.cashflow.R
import meimaonamassa.cashflow.base.DateConverters
import meimaonamassa.cashflow.data.entity.TransactionEntity
import meimaonamassa.cashflow.databinding.FragmentTransactionAddBinding
import meimaonamassa.cashflow.feature.transaction.add.TransactionAddViewModel
import meimaonamassa.cashflow.feature.transaction.add.TransactionAddViewModelFactory
import meimaonamassa.cashflow.util.CurrencyTextWatcher
import meimaonamassa.cashflow.util.DatePickerFragment
import meimaonamassa.cashflow.util.extension.fromCurrency
import meimaonamassa.cashflow.util.extension.hideKeyboard
import meimaonamassa.cashflow.util.extension.isValid
import meimaonamassa.cashflow.util.extension.toFormattedDate

class TransactionAddFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: TransactionAddViewModel
    private var _binding: FragmentTransactionAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionAddBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this, TransactionAddViewModelFactory(
                (requireActivity().application as MainApplication).repository
            )
        )[TransactionAddViewModel::class.java]
        setListeners()
        return binding.root
    }

    override fun onClick(v: View?) {
        val id: Int? = v?.id
        if (id == R.id.button_save) {
            if (!binding.editPayerPayee.isValid() || !binding.editDescription.isValid() || !binding.editDate.isValid() || !binding.editMoney.isValid() ) {
                Log.i("Validation", "Field validation failed.")
            }
            else if (binding.groupRadioTransactionType.checkedRadioButtonId == -1) {
                Toast.makeText(context, getText(R.string.group_radio_error), Toast.LENGTH_SHORT).show()
            }
            else {
                val payerPayer = binding.editPayerPayee.text.toString().trim()
                val description = binding.editDescription.text.toString().trim()
                val date = DateConverters.toOffsetDateTime(
                    binding.editDate.text.toString().toFormattedDate()
                )
                val monetaryValue = binding.editMoney.text.toString().fromCurrency()
                val transactionType = binding.radioIncome.isChecked

                val isInstallment = binding.checkInstallment.isChecked
                val installmentsCountString = binding.editInstallmentsCount.text.toString()

                if (isInstallment && installmentsCountString.isNotEmpty()) {
                    val installments = installmentsCountString.toInt()
                    val installmentValue = monetaryValue / installments

                    for (i in 1..installments) {
                        val installmentDescription = "$description - Par. $i/$installments"
                        val installmentDate = date?.plusMonths((i - 1).toLong())

                        val transactions = TransactionEntity(
                            id = 0,
                            payerPayee = payerPayer,
                            description = installmentDescription,
                            date = installmentDate,
                            monetaryValue = installmentValue,
                            transactionType = transactionType
                        )

                        viewModel.insert(transactions)
                    }
                } else {
                    val transaction = TransactionEntity(
                        id = id,
                        payerPayee = payerPayer,
                        description = description,
                        date = date,
                        monetaryValue = monetaryValue,
                        transactionType = transactionType
                    )
                    viewModel.insert(transaction)
                }

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
                DatePickerFragment(binding.editDate) { binding.editDate.setText(it) }.show(
                    requireActivity().supportFragmentManager, "datePicker"
                )
                binding.editDate.clearFocus()
            }
        }

        binding.groupRadioTransactionType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_income -> {
                    binding.textPayerPayee.text = getString(R.string.text_payer)
                }

                R.id.radio_expense -> {
                    binding.textPayerPayee.text = getString(R.string.text_payee)
                }
            }
        }

        binding.checkInstallment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.editInstallmentsCount.visibility = View.VISIBLE
            } else {
                binding.editInstallmentsCount.visibility = View.GONE
                binding.editInstallmentsCount.text.clear()
            }
        }

        binding.buttonSave.setOnClickListener(this)

    }

}