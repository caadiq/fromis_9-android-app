package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumListBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumListAdapter
import com.beemer.unofficial.fromis_9.view.utils.ItemDecoratorDivider
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import com.beemer.unofficial.fromis_9.viewmodel.Type
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumListBinding.inflate(layoutInflater) }

    private val albumViewModel: AlbumViewModel by viewModels()

    private val albumListAdapter = AlbumListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupView() {
        binding.btnShop.setOnClickListener {
            startActivity(Intent(this, WeverseShopActivity::class.java))
        }

        binding.txtSearch.setOnClickListener {
            startActivity(Intent(this, AlbumSongSearchActivity::class.java))
        }

        binding.btnType.setOnClickListener {
            MenuBottomSheetDialog(listOf("분류", "전체", "싱글", "미니", "정규"), onItemClick = { item, _ ->
                when (item) {
                    "전체" -> albumViewModel.setType(Type.ALL)
                    "싱글" -> albumViewModel.setType(Type.SINGLE)
                    "미니" -> albumViewModel.setType(Type.MINI)
                    "정규" -> albumViewModel.setType(Type.ALBUM)
                }
            }).show(supportFragmentManager, "MenuBottomSheetDialog")
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = albumListAdapter
            setHasFixedSize(true)
            addItemDecoration(ItemDecoratorDivider(this@AlbumListActivity, 0, 16, 0, 0, 0, 0, null))
        }

        albumListAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, AlbumDetailsActivity::class.java)
            intent.putExtra("albumName", item.albumName)
            intent.putExtra("cover", item.cover)
            intent.putExtra("colorMain", item.colorMain)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        albumViewModel.apply {
            getAlbumList()
            binding.shimmerFrameLayout.startShimmer()

            type.observe(this@AlbumListActivity) { type ->
                albumListAdapter.filter.filter(
                    type?.let {
                        when (it) {
                            Type.ALL -> ""
                            Type.SINGLE -> "싱글"
                            Type.MINI -> "미니"
                            Type.ALBUM -> "정규"
                        }
                    }
                )

                binding.btnType.text = type?.let {
                    when (it) {
                        Type.ALL -> "전체"
                        Type.SINGLE -> "싱글"
                        Type.MINI -> "미니"
                        Type.ALBUM -> "정규"
                    }
                }

                if (albumListAdapter.getItemSize() > 0)
                    binding.recyclerView.scrollToPosition(0)
            }

            albumList.observe(this@AlbumListActivity) { list ->
                albumListAdapter.setItemList(list)
                binding.shimmerFrameLayout.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
            }
        }
    }
}