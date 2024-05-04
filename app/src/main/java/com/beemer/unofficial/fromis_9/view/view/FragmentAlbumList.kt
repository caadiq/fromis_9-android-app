package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumListBinding
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import com.beemer.unofficial.fromis_9.viewmodel.SortBy

class FragmentAlbumList : Fragment() {
    private var _binding: FragmentAlbumListBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel: AlbumViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.btnToggleGroup.apply {
            check(binding.btnRelease.id)
            addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    val sortBy = when (checkedId) {
                        binding.btnRelease.id -> SortBy.RELEASE
                        binding.btnTitle.id -> SortBy.TITLE
                        binding.btnType.id -> SortBy.TYPE
                        else -> SortBy.RELEASE
                    }
                    albumViewModel.setSortBy(sortBy)
                }
            }
        }

        binding.btnDesc.setOnCheckedChangeListener { _, isChecked ->
            albumViewModel.setDescending(!isChecked)
        }
    }

    private fun observeViewModel() {
        albumViewModel.apply {
            sortBy.observe(viewLifecycleOwner) {
                when (it) {
                    SortBy.RELEASE -> binding.btnToggleGroup.check(binding.btnRelease.id)
                    SortBy.TITLE -> binding.btnToggleGroup.check(binding.btnTitle.id)
                    SortBy.TYPE -> binding.btnToggleGroup.check(binding.btnType.id)
                    else -> binding.btnToggleGroup.check(binding.btnRelease.id)
                }
            }

            isDescending.observe(viewLifecycleOwner) {
                binding.btnDesc.isChecked = !it
            }
        }
    }
}