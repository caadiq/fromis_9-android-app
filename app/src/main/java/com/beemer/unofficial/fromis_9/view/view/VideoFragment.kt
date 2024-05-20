package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.FragmentVideoBinding
import com.beemer.unofficial.fromis_9.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private val videoViewModel: VideoViewModel by viewModels()

    private lateinit var searchView: SearchView

    private val title by lazy { getString(R.string.str_video_title) }
    private var searchQuery: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupToggleGroup()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        binding.txtTitle.text = title
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        requireActivity().addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar_search, menu)

                val searchItem = menu.findItem(R.id.search)
                searchView = searchItem.actionView as SearchView

                searchView.apply {
                    queryHint = getString(R.string.str_video_search)
                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            searchQuery = query
                            binding.txtTitle.text = query
                            binding.toolbar.collapseActionView()
                            searchView.clearFocus()
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            return false
                        }
                    })
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupToggleGroup() {
        binding.btnToggleGroup.apply {
            selectButton(binding.btnAll)

            setOnSelectListener {
                videoViewModel.setToggleGroup(it.id)
            }
        }
    }

    private fun setupViewModel() {
        videoViewModel.apply {
            toggleGroup.observe(viewLifecycleOwner) {
                binding.btnToggleGroup.selectButton(it)
                binding.txtTitle.text = when(it) {
                    binding.btnAll.id -> title
                    binding.btnMv.id -> getString(R.string.str_video_mv)
                    binding.btnChannel9.id -> getString(R.string.str_video_channel9)
                    binding.btnFm124.id -> getString(R.string.str_video_fm124)
                    binding.btnVlog.id -> getString(R.string.str_video_vlog)
                    binding.btnFromisoda.id -> getString(R.string.str_video_fromisoda)
                    else -> title
                }
            }
        }
    }
}