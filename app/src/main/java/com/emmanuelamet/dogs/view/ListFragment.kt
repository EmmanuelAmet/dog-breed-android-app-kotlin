package com.emmanuelamet.dogs.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.emmanuelamet.dogs.R
import com.emmanuelamet.dogs.adapter.DogListAdapter
import com.emmanuelamet.dogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val dogListAdapter = DogListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        dogList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogListAdapter
        }

        refresh_layout.setOnRefreshListener {
            dogList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            refresh_layout.isRefreshing = false
            viewModel.refreshByPassCache()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(viewLifecycleOwner, Observer { dogs ->
            dogs?.let {
                dogList.visibility = View.VISIBLE
                dogListAdapter.updateDogList(dogs)
            }
        })

        viewModel.dogLoadError.observe(viewLifecycleOwner, Observer{isError ->
            isError?.let {
                listError.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    loadingView.visibility = View.GONE
                    dogList.visibility = View.GONE
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    listError.visibility = View.GONE
                    dogList.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionSettings -> {
                view?.let{
                    Navigation.findNavController(it).navigate(ListFragmentDirections.actionListFragmentToSettingsFragment())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}