package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumDetailsPhotoBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumPhotoListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel

class AlbumDetailsPhotoFragment : Fragment() {
    private var _binding: FragmentAlbumDetailsPhotoBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel: AlbumViewModel by activityViewModels()

    private val albumPhotoListAdapter = AlbumPhotoListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumDetailsPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = albumPhotoListAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        albumViewModel.albumDetails.observe(viewLifecycleOwner) {
            albumPhotoListAdapter.setItemList(it.photoList)
        }
    }
}