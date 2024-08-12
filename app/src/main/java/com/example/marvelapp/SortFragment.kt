package com.example.marvelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.bianchini.vinicius.matheus.core.domain.model.SortingType
import com.example.marvelapp.databinding.FragmentSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SortFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortBinding? = null
    private val binding: FragmentSortBinding get() = _binding!!

    private var orderBy = SortingType.ORDER_BY_NAME.value
    private var order = SortingType.ORDER_ASCENDING.value

    private val viewModel: SortViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSortBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChipGroupListeners()
        observeUiState()
    }

    private fun setChipGroupListeners() {
        binding.chipGroupOrderBy.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            orderBy = getOrderByValue(chip.id)
        }

        binding.chipGroupOrder.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            order = getOrderValue(chip.id)
        }

        binding.buttonApplySort.setOnClickListener {
            viewModel.applySorting(orderBy, order)
        }
    }

    private fun getOrderByValue(
        chipId: Int
    ): String = when (chipId) {
        R.id.chip_name -> SortingType.ORDER_BY_NAME.value

        R.id.chip_modified -> SortingType.ORDER_BY_MODIFIED.value

        else -> SortingType.ORDER_BY_NAME.value
    }

    private fun getOrderValue(
        chipId: Int
    ): String = when (chipId) {
        R.id.chip_ascending -> SortingType.ORDER_ASCENDING.value

        R.id.chip_descending -> SortingType.ORDER_DESCENDING.value


        else -> SortingType.ORDER_ASCENDING.value
    }

    private fun observeUiState() {
        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                SortViewModel.UiState.ApplyState.Error -> {
                    binding.flipperApply.displayedChild = FLIPPER_CHILD_PROGRESS
                }

                SortViewModel.UiState.ApplyState.Loading -> {
                    binding.flipperApply.displayedChild = FLIPPER_CHILD_PROGRESS
                }

                SortViewModel.UiState.ApplyState.Success -> {
                    binding.flipperApply.displayedChild = FLIPPER_CHILD_BUTTON
                }

                is SortViewModel.UiState.SortingResult -> {
                    val orderBy = uiState.storageSort.first
                    val order = uiState.storageSort.second

                    binding.chipGroupOrderBy.forEach {
                        val chip = it as Chip

                        if (getOrderByValue(chip.id) == orderBy) {
                            chip.isChecked = true
                        }
                    }

                    binding.chipGroupOrder.forEach {
                        val chip = it as Chip

                        if (getOrderValue(chip.id) == order) {
                            chip.isChecked = true
                        }
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_BUTTON = 0
        private const val FLIPPER_CHILD_PROGRESS = 0
    }
}