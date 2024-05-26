package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.FragmentVideoBinding
import com.beemer.unofficial.fromis_9.view.adapter.VideoListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private val videoViewModel: VideoViewModel by viewModels()

    private val videoListAdapter = VideoListAdapter()

    private lateinit var searchView: SearchView

    private var searchQuery: String? = null
    private var playlist: String? = null
    private var isLoading = false
    private var isRefreshed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupToggleGroup()
        setupRecyclerView()
        setupView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        binding.txtTitle.text = getString(R.string.str_video_title)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_toolbar_search, menu)

                    val searchItem = menu.findItem(R.id.search)
                    searchView = searchItem.actionView as SearchView

                    searchView.apply {
                        queryHint = getString(R.string.str_video_search)
                        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                binding.swipeRefreshLayout.isRefreshing = true
                                searchQuery = query
                                searchView.clearFocus()
                                binding.txtTitle.text = query
                                videoViewModel.getVideoList(0, 20, searchQuery, playlist, true)
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                return false
                            }
                        })
                    }

                    searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                        override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                            return true
                        }

                        override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                            binding.swipeRefreshLayout.isRefreshing = true
                            searchQuery = null
                            setTitleAndGetVideoList()
                            return true
                        }
                    })
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return false
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun setupToggleGroup() {
        binding.btnToggleGroup.apply {
            selectButton(binding.btnAll)

            setOnSelectListener {
                videoViewModel.setToggleGroup(it.id)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = videoListAdapter
            itemAnimator = null

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)?.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                    // 스크롤을 끝까지 내렸을 때
                    if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && !isLoading) {
                        videoViewModel.page.value?.let { page ->
                            page.nextPage?.let {
                                videoViewModel.getVideoList(it, 20, searchQuery, playlist, false)
                            }
                        }
                    }
                }
            })
        }

        videoListAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(requireActivity(), VideoDetailsActivity::class.java).apply {
                putExtra("videoId", item.videoId)
                putExtra("title", item.title)
                putExtra("views", item.views)
                putExtra("publishedAt", item.publishedAt)
                putExtra("description", item.description)
            }
            startActivity(intent)
        }
    }

    private fun setupView() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            videoViewModel.getVideoList(0, 20, searchQuery, playlist, true)
        }
    }

    private fun setupViewModel() {
        videoViewModel.apply {
            getVideoList(0, 20, searchQuery, playlist, true)

            toggleGroup.observe(viewLifecycleOwner) {
                binding.btnToggleGroup.selectButton(it)
                playlist = when (it) {
                    binding.btnAll.id -> null
                    binding.btnMv.id -> "mv"
                    binding.btnChannel9.id -> "channel9"
                    binding.btnFm124.id -> "fm124"
                    binding.btnVlog.id -> "vlog"
                    binding.btnFromisoda.id -> "fromisoda"
                    else -> null
                }
                setTitleAndGetVideoList()
            }

            videoList.observe(viewLifecycleOwner) { list ->
                binding.swipeRefreshLayout.isRefreshing = false
                setLoading(false)

                videoListAdapter.setItemList(list)
                if (this@VideoFragment.isRefreshed)
                    binding.recyclerView.scrollToPosition(0)
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                this@VideoFragment.isLoading = isLoading
                if (isLoading)
                    videoListAdapter.showProgress()
                else
                    videoListAdapter.hideProgress()
            }

            isRefreshed.observe(viewLifecycleOwner) { isRefreshed ->
                this@VideoFragment.isRefreshed = isRefreshed
            }
        }
    }

    private fun setTitleAndGetVideoList() {
        binding.txtTitle.text = when (playlist) {
            "mv" -> getString(R.string.str_video_mv)
            "channel9" -> getString(R.string.str_video_channel9)
            "fm124" -> getString(R.string.str_video_fm124)
            "vlog" -> getString(R.string.str_video_vlog)
            "fromisoda" -> getString(R.string.str_video_fromisoda)
            else -> getString(R.string.str_video_title)
        }
        videoViewModel.getVideoList(0, 20, searchQuery, playlist, true)
    }
}