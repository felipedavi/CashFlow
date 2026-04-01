package meimaonamassa.cashflow.feature.transaction.chart.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import meimaonamassa.cashflow.databinding.FragmentChartBinding
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModel
import com.github.mikephil.charting.components.Legend
import meimaonamassa.cashflow.MainApplication
import meimaonamassa.cashflow.data.entity.TransactionEntity
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModelFactory
import meimaonamassa.cashflow.util.PreferenceManager
import meimaonamassa.cashflow.util.extension.toCurrency
import org.threeten.bp.YearMonth

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefManager: PreferenceManager

    private val args: ChartFragmentArgs by navArgs()
    private val viewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory((requireActivity().application as MainApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        prefManager = PreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pieChart.clear()
        try {
            val selectedMonth = YearMonth.parse(args.selectedMonth)
            if (viewModel.currentMonth.value != selectedMonth) {
                viewModel.currentMonth.value = selectedMonth
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setupChartConfiguration()
        observeFinancialData()
    }

    private fun setupChartConfiguration() {
        binding.pieChart.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            legend.isEnabled = true
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            setNoDataText("Nenhuma movimentação neste mês")
        }
    }

    private fun observeFinancialData() {
        val currentTotals = viewModel.monthlyTotals.value
        if (currentTotals != null) {
            updateChartData(currentTotals.first, currentTotals.second)
        }
        viewModel.monthlyTotals.observe(viewLifecycleOwner) { totals ->
            updateChartData(totals.first, totals.second)
        }
        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            val budget = prefManager.getMonthlyBudget()
            updateBudgetStatus(transactions, budget)
        }
    }

    private fun updateChartData(income: Double, expense: Double) {
        val entries = ArrayList<PieEntry>()
        val dynamicColors = ArrayList<Int>()

        if (income > 0) {
            entries.add(PieEntry(income.toFloat(), "Receitas"))
            dynamicColors.add("#4CAF50".toColorInt())
        }

        if (expense > 0) {
            entries.add(PieEntry(expense.toFloat(), "Despesas"))
            dynamicColors.add("#F44336".toColorInt())
        }

        if (entries.isEmpty()) {
            binding.pieChart.clear()
            binding.pieChart.setNoDataText("Nenhuma movimentação neste mês")
            binding.pieChart.invalidate()
            return
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = dynamicColors
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.WHITE
        dataSet.sliceSpace = 2f

        val pieData = PieData(dataSet)
        binding.pieChart.apply {
            data = pieData
            centerText = "Saldo Mensal\n${(income - expense).toCurrency()}"
            setCenterTextSize(16f)
            animateY(1000, Easing.EaseInOutQuad)
            invalidate()
        }
    }

    private fun updateBudgetStatus(transactions: List<TransactionEntity>, totalBudget: Float) {
        if (totalBudget <= 0) {
            binding.layoutBudgetStatus.visibility = View.GONE
            return
        }

        binding.layoutBudgetStatus.visibility = View.VISIBLE

        val spent = mutableMapOf("Necessidades" to 0.0, "Desejos" to 0.0, "Investimentos" to 0.0)

        transactions.filter { !it.transactionType }.forEach { trans ->
            val cat = trans.category ?: ""
            if (spent.containsKey(cat)) {
                spent[cat] = spent[cat]!! + trans.monetaryValue
            }
        }

        updateCategoryUI(
            binding.textNeedsStatus, binding.progressNeeds,
            spent["Necessidades"] ?: 0.0, totalBudget * 0.5, "Necessidades (50%)"
        )

        updateCategoryUI(
            binding.textWantsStatus, binding.progressWants,
            spent["Desejos"] ?: 0.0, totalBudget * 0.3, "Desejos (30%)"
        )

        updateCategoryUI(
            binding.textInvestmentsStatus, binding.progressInvestments,
            spent["Investimentos"] ?: 0.0, totalBudget * 0.2, "Investimentos (20%)"
        )
    }

    private fun updateCategoryUI(label: TextView, bar: ProgressBar, spent: Double, limit: Double, title: String) {
        val remaining = limit - spent
        label.text = String.format("%s: %s / %s (Resta %s)",
            title, spent.toCurrency(), limit.toCurrency(), remaining.toCurrency())

        bar.max = 100

        val progressPercent = if (limit > 0) ((spent / limit) * 100).toInt() else 0
        bar.progress = progressPercent

        val color = if (spent > limit) Color.RED else "#4CAF50".toColorInt()

        bar.progressTintList = android.content.res.ColorStateList.valueOf(color)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}