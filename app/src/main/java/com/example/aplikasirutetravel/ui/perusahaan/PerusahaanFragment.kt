package com.example.aplikasirutetravel.ui.perusahaan

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.databinding.FragmentPerusahaanBinding
import com.example.aplikasirutetravel.utils.invisible
import com.example.aplikasirutetravel.utils.visible
import com.example.aplikasirutetravel.viewmodel.PerusahaanViewModel
import com.example.aplikasirutetravel.viewmodel.ViewModelFactory
import com.example.aplikasirutetravel.vo.Status

class PerusahaanFragment : Fragment(), PerusahaanCallback {
    private var _binding: FragmentPerusahaanBinding? = null
    private val binding get() = _binding
    private var isShowSearch = false
    private var searchMenu: Menu? = null
    private var itemSearch: MenuItem? = null
    private lateinit var viewModelProviders: ViewModelProvider
    private lateinit var viewModels: PerusahaanViewModel
    private lateinit var perusahaanAdapter: PerusahaanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPerusahaanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding!!.toolbar?.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding?.toolbar?.toolbarTitle?.text = "Daftar Perusahaan"
        binding?.toolbar?.toolbarBack?.let {
            Glide.with(this)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(R.drawable.ic_back_toolbar)
                .into(it)
        }
        setHasOptionsMenu(true)

        initToolbarSearch()

        binding?.toolbar?.toolbarBack?.setOnClickListener {
            if (isShowSearch) {
                binding?.searchToolbar?.searchToolbar?.id?.let {
                    circleReveal(
                        it,
                        1,
                        containsOverflow = true,
                        isShow = false
                    )
                }
            } else {
                findNavController().popBackStack()
            }
        }

        val factory = ViewModelFactory.getInstance(requireActivity())

        viewModels = ViewModelProvider(this, factory)[PerusahaanViewModel::class.java]

        perusahaanAdapter = PerusahaanAdapter(this@PerusahaanFragment)
        viewModels.getAllPerusahaan().observe(this, { perusahaan ->
            when (perusahaan.status) {
                Status.LOADING -> binding?.progressBar?.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    binding?.progressBar?.visibility = View.GONE
                    perusahaanAdapter.setPerusahaan(perusahaan.data)
                    perusahaanAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        })

        with(binding?.rvPerusahaan) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = perusahaanAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                binding?.searchToolbar?.searchToolbar?.id?.let { circleReveal(it, 1,
                    containsOverflow = true,
                    isShow = true
                ) }
                itemSearch?.expandActionView()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initToolbarSearch() {
        binding!!.searchToolbar?.searchToolbar?.inflateMenu(R.menu.menu_search)
        searchMenu = binding!!.searchToolbar?.searchToolbar?.menu

        binding!!.searchToolbar?.searchToolbar?.setNavigationOnClickListener {
            binding!!.searchToolbar?.searchToolbar?.id?.let { it1 -> circleReveal(it1, 1,
                containsOverflow = true,
                isShow = false
            ) }
        }

        itemSearch = searchMenu!!.findItem(R.id.action_search)
        itemSearch?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding?.searchToolbar?.searchToolbar?.id?.let { circleReveal(it, 1,
                    containsOverflow = true,
                    isShow = false
                ) }
                return true
            }
        })

        initSearchView()
    }

    private fun initSearchView() {
        val searchView = searchMenu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.isSubmitButtonEnabled = false

        val txtSearch = searchView.findViewById<EditText>(R.id.search_src_text)
        txtSearch.hint = "Ketik untuk mencari"
//        txtSearch.setBackgroundColor(Color.TRANSPARENT)
        txtSearch.background = null
        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)
        closeButton.setImageResource(R.drawable.ic_close)
        closeButton.setOnClickListener {
            txtSearch.setText("")
//            vmPlace?.sendCloseToolbar("close")
        }

        val searchTextView =
            searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)
        try {
            val mCursorDrawableRes =
                TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes.set(searchTextView, R.drawable.view_cursor_search)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                vmPlace?.sendData(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModels.getPerusahaanSearch(newText).observe(viewLifecycleOwner, { perusahaan ->
                    when (perusahaan.status) {
                        Status.LOADING -> binding?.progressBar?.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding?.progressBar?.visibility = View.GONE
                            perusahaanAdapter.setPerusahaan(perusahaan.data)
                            perusahaanAdapter.notifyDataSetChanged()
//                            Toast.makeText(context, "cek hum ${newText} dan ${perusahaan.data}", Toast.LENGTH_SHORT).show()
                        }
                        Status.ERROR -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                return true
            }
        })
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun circleReveal(
        viewID: Int,
        posFromRight: Int,
        containsOverflow: Boolean,
        isShow: Boolean
    ) {
        val myView = activity?.findViewById<View>(viewID)
        var width = myView?.width

        if (posFromRight > 0) width = width?.minus(
            posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources.getDimensionPixelSize(
                R.dimen.abc_action_button_min_width_material
            ) / 2
        )

        if (containsOverflow) width =
            width?.minus(resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material))

        val cx = width
        val cy = myView?.height?.div(2)

        isShowSearch = isShow
        val anim = when (isShow) {
            true -> {
                cx?.let { cxs ->
                    cy?.let { cy ->
                        width?.toFloat()?.let {
                            ViewAnimationUtils.createCircularReveal(
                                myView,
                                cxs, cy, 0f, it
                            )
                        }
                    }
                }
            }
            false -> {
//                vmPlace?.sendCloseToolbar("close")
                cx?.let { cxs ->
                    cy?.let { cy ->
                        width?.toFloat()?.let {
                            ViewAnimationUtils.createCircularReveal(
                                myView,
                                cxs, cy, it, 0f
                            )
                        }
                    }
                }
            }
        }

        anim?.duration = 220.toLong()
        anim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    myView?.invisible()
                }
            }
        })

        if (isShow) myView?.visible()
        anim?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(id_perusahaan: String) {
        val arg = Bundle()
        arg.putString(
            DetailPerusahaanFragment.ID_PERUSAHAAN,
            id_perusahaan
        )
        findNavController().navigate(R.id.detailPerusahaanFragment, arg)
    }
}