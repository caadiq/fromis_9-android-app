package com.beemer.unofficial.fromis_9.view.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.beemer.unofficial.fromis_9.databinding.DialogYearmonthPickerBinding
import com.beemer.unofficial.fromis_9.view.adapter.MonthPicker
import com.beemer.unofficial.fromis_9.view.adapter.YearMonthPickerAdapter
import com.beemer.unofficial.fromis_9.view.utils.ItemDecoratorDivider
import java.time.YearMonth

class YearMonthPickerDialog(private val yearMonth: YearMonth, private val onConfirm: (yearMonth: YearMonth) -> Unit) : DialogFragment() {
    private var _binding: DialogYearmonthPickerBinding? = null
    private val binding get() = _binding!!

    private val yearMonthPickerAdapter = YearMonthPickerAdapter()

    private val monthList = listOf(
        "1월", "2월", "3월", "4월", "5월", "6월",
        "7월", "8월", "9월", "10월", "11월", "12월"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogYearmonthPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialog()
        setupView()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDialog() {
        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.width = (context.resources.displayMetrics.widthPixels.times(0.8)).toInt()
        }
    }

    private fun setupView() {
        binding.txtYear.text = yearMonth.year.toString()

        binding.imgPrev.setOnClickListener {
            val year = binding.txtYear.text.toString().toInt()
            if (year == 2018)
                return@setOnClickListener
            binding.txtYear.text = (year - 1).toString()
        }

        binding.imgNext.setOnClickListener {
            val year = binding.txtYear.text.toString().toInt()
            if (year == 2050)
                return@setOnClickListener
            binding.txtYear.text = (year + 1).toString()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConrifm.setOnClickListener {
            val year = binding.txtYear.text.toString().toInt()

            onConfirm(YearMonth.of(year, yearMonthPickerAdapter.getSelectedItem().plus(1)))
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = yearMonthPickerAdapter
            addItemDecoration(ItemDecoratorDivider(requireContext(), 0, 12, 0, 0, 0, 0, null))
        }

        yearMonthPickerAdapter.apply {
            setItemList(monthList.mapIndexed { index, month -> MonthPicker(month, yearMonth.monthValue - 1 == index) })

            setOnItemClickListener { _, position ->
                yearMonthPickerAdapter.setItemSelected(position)
            }
        }
    }
}