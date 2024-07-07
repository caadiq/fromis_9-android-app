package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumDetailsIntroductionBinding
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel

class AlbumDetailsIntroductionFragment : Fragment() {
    private var _binding: FragmentAlbumDetailsIntroductionBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel: AlbumViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumDetailsIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        albumViewModel.albumDetails.observe(viewLifecycleOwner) { albumDetails ->
            binding.txtDescription.text = albumDetails.description
        }
    }
}