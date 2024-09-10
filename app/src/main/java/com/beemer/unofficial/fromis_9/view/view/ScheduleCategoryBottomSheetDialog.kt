package com.beemer.unofficial.fromis_9.view.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beemer.unofficial.fromis_9.databinding.BottomsheetScheduleCategoryBinding
import com.beemer.unofficial.fromis_9.view.adapter.Category
import com.beemer.unofficial.fromis_9.view.adapter.ScheduleCategoryListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ScheduleCategoryBottomSheetDialog(private val categories: List<Category>, private val onItemSelected: (selectedItems: List<Category>) -> Unit) : BottomSheetDialogFragment() {
    private var _binding: BottomsheetScheduleCategoryBinding? = null
    private val binding get() = _binding!!

    private val scheduleCategoryListAdapter = ScheduleCategoryListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomsheetScheduleCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onItemSelected(scheduleCategoryListAdapter.getItemList())
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = scheduleCategoryListAdapter
            layoutManager = com.google.android.flexbox.FlexboxLayoutManager(context).apply {
                flexDirection = com.google.android.flexbox.FlexDirection.ROW
                flexWrap = com.google.android.flexbox.FlexWrap.WRAP
            }
        }

        scheduleCategoryListAdapter.apply {
            setItemList(categories)

            setOnItemClickListener { _, position ->
                scheduleCategoryListAdapter.setItemSelected(position)
            }
        }
    }
}