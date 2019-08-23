package com.munin.music.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.munin.library.log.Logger
import com.munin.library.thread.GlobalExecutor
import com.munin.library.view.widget.refreshlayout.interfaces.OnRefreshLoadMoreListener
import com.munin.library.view.widget.refreshlayout.interfaces.RefreshLayout
import com.munin.library.view.widget.refreshlayout.state.RefreshState
import com.munin.music.R
import com.munin.music.dao.User
import com.munin.music.database.DataBaseUtils
import com.munin.music.net.ApiClient
import com.munin.music.ui.adapter.PicturePlazaAdapter
import com.munin.music.ui.common.RefreshLayoutManger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_piture_plaza.*
import java.util.concurrent.atomic.AtomicBoolean

class PicturePlazaActivity : BaseActivity(), OnRefreshLoadMoreListener {
    val tag = "PicturePlazaActivity"
    var page = 2
    var lock: AtomicBoolean = AtomicBoolean(false)
    override fun onRefresh(refreshLayout: RefreshLayout) {
        GlobalExecutor.newInstance().submit {
            if (lock.get()) {
                Logger.i(tag, "onRefresh: has requested!")
            } else if (lock.compareAndSet(false, true)) {
                page++
                initData()
            }
        }

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        GlobalExecutor.newInstance().submit {
            if (lock.get()) {
                Logger.i(tag, "onRefresh: has requested!")
            } else
                if (lock.compareAndSet(false, true)) {
                    page++
                    initData()
                }
        }
    }

    var adapter = PicturePlazaAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piture_plaza)
        refresh_layout.setRefreshLoadLayoutManger(RefreshLayoutManger(this))
        picture_recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        picture_recyclerView.adapter = adapter
        initData()
        refresh_layout.setListener(this)
        val user = User()
        user.userName = "munin"
        user.password = "123456"
        user.userId = "123456"
        DataBaseUtils.getDatabase().userDao().insertItem(user)
    }

    fun initData() {
        var data = ApiClient.instance.service.getPictures(page)
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    lock.getAndSet(false)
                    it.recommends?.let { list -> adapter.data.addAll(list) }
                    adapter.notifyDataSetChanged()
                    if (refresh_layout.currentState == RefreshState.REFRESHING) {
                        refresh_layout.finishRefresh()

                    }
                    if (refresh_layout.currentState == RefreshState.LOADING) {
                        refresh_layout.finishLoad()
                    }
                }, Consumer {
                    lock.getAndSet(false)
                    if (refresh_layout.currentState == RefreshState.REFRESHING) {
                        refresh_layout.finishRefresh()

                    }
                    if (refresh_layout.currentState == RefreshState.LOADING) {
                        refresh_layout.finishLoad()
                    }
                })
    }
}
