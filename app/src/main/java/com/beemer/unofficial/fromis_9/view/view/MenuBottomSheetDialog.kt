package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beemer.unofficial.fromis_9.databinding.BottomsheetMenuBinding
import com.beemer.unofficial.fromis_9.view.adapter.BottomSheetListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetDialog(private val list: List<String>, private val onItemClick: (item: String, position: Int) -> Unit) : BottomSheetDialogFragment() {
    private var _binding: BottomsheetMenuBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetListAdapter = BottomSheetListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomsheetMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = bottomSheetListAdapter
        bottomSheetListAdapter.apply {
            setItemList(list)

            setOnItemClickListener { item, position ->
                if (position == 0)
                    dismiss()
                else
                    onItemClick(item, position)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}