package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumListBinding
import com.beemer.unofficial.fromis_9.model.data.AlbumData
import com.beemer.unofficial.fromis_9.view.adapter.AlbumListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import com.beemer.unofficial.fromis_9.viewmodel.SortBy

class AlbumListFragment : Fragment() {
    private var _binding: FragmentAlbumListBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel: AlbumViewModel by activityViewModels()

    private val albumListAdapter = AlbumListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupRecyclerView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        if (albumViewModel.albumList.value.isNullOrEmpty())
            albumViewModel.getAlbumList()

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

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = albumListAdapter
            setHasFixedSize(true)
        }

        albumListAdapter.setOnItemClickListener { item, _ ->
            val action = AlbumListFragmentDirections.actionAlbumListFragmentToAlbumDetailsFragment(
                AlbumData(
                    albumName = item.albumName,
                    cover = item.cover,
                    colorMain = item.colorMain
                )
            )
            findNavController().navigate(action)
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
                sortAlbumList(albumList.value ?: emptyList())
            }

            isDescending.observe(viewLifecycleOwner) { descending ->
                binding.btnDesc.isChecked = !descending
                sortAlbumList(albumList.value ?: emptyList())
            }

            albumList.observe(viewLifecycleOwner) { list ->
                albumListAdapter.setItemList(list)
            }
        }
    }
}