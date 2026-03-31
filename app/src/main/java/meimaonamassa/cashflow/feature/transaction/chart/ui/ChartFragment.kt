package meimaonamassa.cashflow.feature.transaction.chart.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import meimaonamassa.cashflow.databinding.FragmentChartBinding
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModel
import com.github.mikephil.charting.components.Legend
import meimaonamassa.cashflow.MainApplication
import meimaonamassa.cashflow.feature.transaction.list.presentation.TransactionViewModelFactory
import meimaonamassa.cashflow.util.extension.toCurrency

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory((requireActivity().application as MainApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.monthlyTotals.observe(viewLifecycleOwner) { totals ->
            val income = totals.first
            val expense = totals.second
            updateChartData(income, expense)
        }
    }

    private fun updateChartData(income: Double, expense: Double) {
        val entries = ArrayList<PieEntry>()

        if (income > 0) entries.add(PieEntry(income.toFloat(), "Receitas"))
        if (expense > 0) entries.add(PieEntry(expense.toFloat(), "Despesas"))

        if (entries.isEmpty()) {
            binding.pieChart.clear()
            binding.pieChart.setNoDataText("Nenhuma movimentação neste mês")
            binding.pieChart.invalidate()
            return
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            "#4CAF50".toColorInt(),
            "#F44336".toColorInt()
        )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}