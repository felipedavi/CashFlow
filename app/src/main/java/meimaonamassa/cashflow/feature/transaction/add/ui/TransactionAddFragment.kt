package meimaonamassa.cashflow.feature.transaction.add.ui

import android.app.AlertDialog
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
            if (!binding.editPayerPayee.isValid() || !binding.editDescription.isValid() || !binding.editDate.isValid() || !binding.editMoney.isValid()) {
                Log.i("Validation", "Field validation failed.")
            } else if (binding.groupRadioTransactionType.checkedRadioButtonId == -1) {
                Toast.makeText(context, getText(R.string.group_radio_error), Toast.LENGTH_SHORT).show()
            } else {
                val payerPayer = binding.editPayerPayee.text.toString().trim()
                val description = binding.editDescription.text.toString().trim()
                val date = DateConverters.toOffsetDateTime(
                    binding.editDate.text.toString().toFormattedDate()
                )
                val monetaryValue = binding.editMoney.text.toString().fromCurrency()
                val transactionType = binding.radioIncome.isChecked

                val isInstallment = binding.checkInstallment.isChecked

                if (isInstallment) {
                    val currentStr = binding.editInstallmentCurrent.text.toString()
                    val finalStr = binding.editInstallmentFinal.text.toString()

                    if (currentStr.isNotEmpty() && finalStr.isNotEmpty()) {
                        val current = currentStr.toInt()
                        val final = finalStr.toInt()

                        AlertDialog.Builder(requireContext())
                            .setTitle("Salvar parcelas")
                            .setMessage("Deseja salvar as demais parcelas automaticamente?")
                            .setPositiveButton("Sim") { _, _ ->
                                saveInstallments(payerPayer, description, date, monetaryValue, transactionType, current, final, true)
                            }
                            .setNegativeButton("Não") { _, _ ->
                                saveInstallments(payerPayer, description, date, monetaryValue, transactionType, current, final, false)
                            }
                            .show()
                    } else {
                        Toast.makeText(context, "Preencha as parcelas.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val transaction = TransactionEntity(
                        id = 0,
                        payerPayee = payerPayer,
                        description = description,
                        date = date,
                        monetaryValue = monetaryValue,
                        transactionType = transactionType,
                        isInstallment = false,
                        installmentCurrent = null,
                        installmentTotal = null
                    )
                    viewModel.insert(transaction)
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun saveInstallments(
        payerPayee: String,
        description: String,
        date: org.threeten.bp.OffsetDateTime?,
        totalValue: Double,
        transactionType: Boolean,
        current: Int,
        final: Int,
        saveAll: Boolean
    ) {
        val totalInstallmentsToSave = final - current + 1
        val installmentValue = if (totalInstallmentsToSave > 0) totalValue / totalInstallmentsToSave else totalValue

        if (saveAll) {
            for (i in current..final) {
                val currentMonthOffset = (i - current).toLong()
                val installmentDate = date?.plusMonths(currentMonthOffset)
                val installmentDesc = "$description - Par. $i/$final"

                val transaction = TransactionEntity(
                    id = 0,
                    payerPayee = payerPayee,
                    description = installmentDesc,
                    date = installmentDate,
                    monetaryValue = installmentValue,
                    transactionType = transactionType,
                    isInstallment = true,
                    installmentCurrent = i,
                    installmentTotal = final
                )
                viewModel.insert(transaction)
            }
        } else {
            val installmentDesc = "$description - Par. $current/$final"
            val transaction = TransactionEntity(
                id = 0,
                payerPayee = payerPayee,
                description = installmentDesc,
                date = date,
                monetaryValue = totalValue,
                transactionType = transactionType,
                isInstallment = true,
                installmentCurrent = current,
                installmentTotal = final
            )
            viewModel.insert(transaction)
        }
        findNavController().navigateUp()
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
                binding.layoutInstallments.visibility = View.VISIBLE
            } else {
                binding.layoutInstallments.visibility = View.GONE
                binding.editInstallmentCurrent.text.clear()
                binding.editInstallmentFinal.text.clear()
            }
        }

        binding.buttonSave.setOnClickListener(this)
    }
}