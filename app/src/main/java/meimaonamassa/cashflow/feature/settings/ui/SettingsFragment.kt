package meimaonamassa.cashflow.feature.settings.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import meimaonamassa.cashflow.MainApplication
import meimaonamassa.cashflow.R
import meimaonamassa.cashflow.databinding.FragmentSettingsBinding
import meimaonamassa.cashflow.feature.settings.SettingsViewModel
import meimaonamassa.cashflow.feature.settings.SettingsViewModelFactory

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        val repository = (requireActivity().application as MainApplication).repository
        SettingsViewModelFactory(repository)
    }

    private val exportCsvLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
            uri?.let {
                val outputStream = requireContext().contentResolver.openOutputStream(it)
                outputStream?.let { stream ->
                    viewModel.exportData(stream) {
                        val result = Bundle()
                        result.putString("action", "export")
                        setFragmentResult("settingsAction", result)
                        findNavController().popBackStack()
                    }
                }
            }
        }

    private val importCsvLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                val inputStream = requireContext().contentResolver.openInputStream(it)
                inputStream?.let { stream ->
                    viewModel.importData(
                        inputStream = stream,
                        onSuccess = {
                            val result = Bundle()
                            result.putString("action", "import")
                            setFragmentResult("settingsAction", result)
                            findNavController().popBackStack()
                        },
                        onError = {
                            android.widget.Toast.makeText(
                                requireContext(),
                                getString(R.string.error_import_failed),
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonExport.setOnClickListener {
            val dateFormat =
                java.text.SimpleDateFormat("yyyyMMdd-HHmmss", java.util.Locale.getDefault())
            val currentTime = dateFormat.format(java.util.Date())
            exportCsvLauncher.launch("cashflow_backup_$currentTime.csv")
        }

        binding.buttonImport.setOnClickListener {
            importCsvLauncher.launch(arrayOf("text/comma-separated-values", "text/csv"))
        }

        binding.buttonDelete.setOnClickListener {
            showClearDataDialog()
        }
    }

    private fun showClearDataDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_reset_data))
            .setMessage(getString(R.string.alert_reset_data_details))
            .setPositiveButton(getString(R.string.alert_reset_data_positive)) { _, _ ->
                viewModel.clearAllData {
                    val result = Bundle()
                    result.putString("action", "delete")
                    setFragmentResult("settingsAction", result)
                    findNavController().popBackStack()
                }
            }
            .setNegativeButton(getString(R.string.alert_reset_data_cancel), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}