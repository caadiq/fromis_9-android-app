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
import com.beemer.unofficial.fromis_9.view.adapter.YearMonthPickerYearAdapter
import com.beemer.unofficial.fromis_9.view.utils.ItemDecoratorDivider
import java.time.YearMonth

class YearMonthPickerDialog(private val yearMonth: YearMonth, private val onConfirm: (yearMonth: YearMonth) -> Unit) : DialogFragment() {
    private var _binding: DialogYearmonthPickerBinding? = null
    private val binding get() = _binding!!

    private val yearMonthPickerAdapter = YearMonthPickerAdapter()
    private val yearMonthPickerYearAdapter = YearMonthPickerYearAdapter()

    private val monthList = listOf(
        "1월", "2월", "3월", "4월", "5월", "6월",
        "7월", "8월", "9월", "10월", "11월", "12월"
    )

    private val yearList = (2017..2024).toList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogYearmonthPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialog()
        setupView()
        setupViewPager()
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
        binding.imgPrev.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
        }

        binding.imgNext.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
        }

        binding.txtCancel.setOnClickListener {
            dismiss()
        }

        binding.txtConrifm.setOnClickListener {
            onConfirm(YearMonth.of(yearList[binding.viewPager.currentItem], yearMonthPickerAdapter.getSelectedItem().plus(1)))
            dismiss()
        }
    }

    private fun setupViewPager() {
        yearMonthPickerYearAdapter.setItemList(yearList)
        binding.viewPager.apply {
            adapter = yearMonthPickerYearAdapter
            setCurrentItem(yearList.indexOf(yearMonth.year), false)
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