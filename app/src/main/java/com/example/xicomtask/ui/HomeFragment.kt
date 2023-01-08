package com.example.xicomtask.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.innobuzztask.utils.Resource
import com.example.innobuzztask.viewModel.DataViewModel
import com.example.xicomtask.MainActivity
import com.example.xicomtask.R
import com.example.xicomtask.adapter.ImageRecyAdapter
import com.example.xicomtask.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ImageRecyAdapter
    private lateinit var viewModel: DataViewModel
    private lateinit var list: MutableList<String>

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        list = mutableListOf()

        viewModel.getDataResponse("108",  "popular")
        viewModel.getData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { it ->

                        it.images.forEach {
                            list.add(it.xt_image.toString())
                        }
                        adapter = ImageRecyAdapter(list, requireContext())


                        binding.imageRecy.adapter = adapter
                        binding.imageRecy.layoutManager = LinearLayoutManager(requireContext())
                        binding.imageRecy.addOnScrollListener(this@HomeFragment.scrollListener)

                    }
                }
                is Resource.Error -> {
                    response.message.let { message ->
                        Toast.makeText(
                            requireContext(),
                            "Error occured $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        })

        binding.loadMoreBtn.setOnClickListener {
            viewModel.getDataResponse("108","Popular")
        }
    }

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager =recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPos =layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndLastPage = !isLoading &&  !isLastPage
            val isAtLastItem = firstVisibleItemPos + visibleItemCount >=totalItemCount
            val isNotAtBegining  = firstVisibleItemPos >= 0
            val isTotalMoreThanVisible =totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndLastPage && isLastPage && isNotAtBegining && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){
                viewModel.getDataResponse("108","Popular")
                isScrolling = false
            }else{
                recyclerView.setPadding(0,0,0,0)
            }
        }
    }

}
