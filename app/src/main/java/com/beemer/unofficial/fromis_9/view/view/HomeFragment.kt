package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.beemer.unofficial.fromis_9.databinding.FragmentHomeBinding
import com.beemer.unofficial.fromis_9.view.adapter.HomeAdapter
import com.beemer.unofficial.fromis_9.view.adapter.HomeItem
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.stringToDate
import com.beemer.unofficial.fromis_9.viewmodel.Fromis9ViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalDateTime
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val fromis9ViewModel: Fromis9ViewModel by viewModels()

    private val homeAdapter = HomeAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun setupView() {
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = homeAdapter
            itemAnimator = null
        }

        homeAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is HomeItem.HomeTitle -> return@setOnItemClickListener
                is HomeItem.HomeDebut -> return@setOnItemClickListener
                is HomeItem.HomeMember -> {
                    val intent = Intent(requireContext(), MemberActivity::class.java)
                    intent.putExtra("name", item.memberList[0].name)
                    startActivity(intent)
                }
                is HomeItem.HomeAlbum -> {
                    if (item.albumList.isEmpty()) {
                        val intent = Intent(requireContext(), AlbumListActivity::class.java)
                        startActivity(intent)
                    } else {
                        val album = item.albumList[0]
                        val intent = Intent(requireContext(), AlbumDetailsActivity::class.java)
                        intent.putExtra("albumName", album.albumName)
                        intent.putExtra("cover", album.cover)
                        intent.putExtra("colorMain", album.colorMain)
                        intent.putExtra("colorPrimary", album.colorPrimary)
                        intent.putExtra("colorSecondary", album.colorSecondary)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        fromis9ViewModel.apply {
            getFromis9()

            fromis9.observe(viewLifecycleOwner) {
                binding.progressIndicator.hide()

                val items = mutableListOf<HomeItem>()

                items.add(HomeItem.HomeDebut("플로버와 함께한 지 ${dday(it.debut, it.end)}일", "${it.debut} ~ ${it.end}"))

                items.add(HomeItem.HomeTitle("멤버"))
                items.add(HomeItem.HomeMember(it.members))

                items.add(HomeItem.HomeTitle("앨범"))
                items.add(HomeItem.HomeAlbum(it.albums))

                homeAdapter.setItemList(items)

                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun dday(debutDate: String, endDate: String) : Long {
        val debutDateTime = stringToDate(debutDate, "yyyy.MM.dd", Locale.KOREA).atStartOfDay()
        val endDateTime = stringToDate(endDate, "yyyy.MM.dd", Locale.KOREA).atStartOfDay()

        val now = LocalDateTime.now()
        val referenceDateTime = if (now.isAfter(endDateTime)) endDateTime else now
        val diff = Duration.between(debutDateTime, referenceDateTime)

        return diff.toDays()
    }
}