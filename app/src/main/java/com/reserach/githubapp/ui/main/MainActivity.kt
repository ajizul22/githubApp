package com.reserach.githubapp.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.reserach.githubapp.R
import com.reserach.githubapp.data.model.User
import com.reserach.githubapp.databinding.ActivityMainBinding
import com.reserach.githubapp.databinding.DialogErrorMessageBinding

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var bind: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    private var totalPage: Int = 1

    private var isLoading = false
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        layoutManager = LinearLayoutManager(this)

        initComponent()
        initData()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private fun initComponent() {
        bind.apply {
            swipeRefresh.setOnRefreshListener(this@MainActivity)
            swipeRefresh.isRefreshing = false
            etQuery.text = null

            btnSearch.setOnClickListener {
                searchUser(true)
            }

            // RECYCLERVIEW
            rvSearchUser.layoutManager = layoutManager
            rvSearchUser.setHasFixedSize(true)
            rvSearchUser.adapter = adapter

            rvSearchUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    val total  = adapter.itemCount

                    if (!isLoading){
                        if (visibleItemCount + pastVisibleItem>= total){
                            page++
                            searchUser(false)
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)

                }
            })

            adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: User) {
                    Toast.makeText(this@MainActivity, data.userName, Toast.LENGTH_SHORT).show()
                }
            })

            // set enter key on search
            etQuery.setOnKeyListener { view, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    searchUser(true)
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

        }
    }

    private fun initData() {
        viewModel.getSearchUser().observe(this, {
            if (it != null) {
                for (i in 0 until it.size) {
                    viewModel.setUserDetail(it.get(i).login)
                }
            }


        })

        viewModel.getUserDetail().observe(this, {
            if (it!= null) {
                val list = ArrayList<User>()
                list.add(it)
                adapter.setList(list)
                showLoading(false)
                bind.onScroll.visibility = View.GONE
                isLoading = false
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            if (it != null) {
                showLoading(false)
                showDialogThree(it.toString())
            }
        })
    }

    private fun searchUser(isOnScroll: Boolean) {
        bind.apply {

            val query = etQuery.text.toString()

            if (isOnScroll) {
                if (query.isEmpty()) return
                showLoading(true)
            } else {
                if (!isOnScroll) onScroll.visibility = View.VISIBLE
                isLoading = true
            }
            viewModel.setSearchUser(query,page)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            bind.progressBar.visibility = View.VISIBLE
        } else {
            bind.progressBar.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        initComponent()
    }

    fun showDialogThree(text: String) {
        val rootView = DataBindingUtil.inflate<DialogErrorMessageBinding>(layoutInflater, R.layout.dialog_error_message, null, false)

        val dialog = AlertDialog.Builder(this)
                .setView(rootView.root)
                .setCancelable(false)
                .create()
        dialog.show()

        rootView.tvErrorMessage.text = text
        dialog.setCanceledOnTouchOutside(true)

    }
}