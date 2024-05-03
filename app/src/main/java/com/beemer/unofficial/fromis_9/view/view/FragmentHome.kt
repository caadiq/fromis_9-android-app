package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.FragmentHomeBinding
import com.beemer.unofficial.fromis_9.model.dto.HomeDto
import com.beemer.unofficial.fromis_9.view.adapter.HomeAdapter

class FragmentHome : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter()
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupToolbar()
        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        binding.toolbar.title = ""
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) 2 else 1
            }
        }

        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = homeAdapter
            itemAnimator = null
        }

        homeAdapter.apply {
            setItemList(
                listOf(
                    HomeDto(image = R.drawable.image_fromis9, text = "프로미스나인"),
                    HomeDto(image = R.drawable.image_album, text = "앨범"),
                    HomeDto(image = R.drawable.image_flover, text = "응원법")
                )
            )

            setOnItemClickListener { item, _ ->
                when (item.text) {
                    "프로미스나인" -> {}
                    "앨범" -> {
                        val action = FragmentHomeDirections.actionFragmentHomeToFragmentAlbumList()
                        findNavController().navigate(action)
                    }
                    "응원법" -> {}
                }
            }
        }
    }
}