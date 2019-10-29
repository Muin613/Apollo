package com.munin.music.ui

import android.os.Bundle
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.munin.library.log.Logger
import com.munin.library.thread.GlobalExecutor
import com.munin.library.view.widget.refreshlayout.interfaces.OnRefreshLoadMoreListener
import com.munin.library.view.widget.refreshlayout.interfaces.RefreshLayout
import com.munin.library.view.widget.refreshlayout.state.RefreshState
import com.munin.music.BR
import com.munin.music.R
import com.munin.music.model.pictureplaza.PicturePlazaViewModel
import com.munin.music.net.ApiClient
import com.munin.music.ui.adapter.PicPlazaAdapter
import com.munin.music.ui.common.RefreshLayoutManger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_piture_plaza.*
import java.util.concurrent.atomic.AtomicBoolean
import androidx.databinding.DataBindingUtil.setContentView as bindContentView

class PicturePlazaActivity : BaseActivity(), OnRefreshLoadMoreListener {
    val tag = "PicturePlazaActivity"
    var page = 2
    var lock: AtomicBoolean = AtomicBoolean(false)
    var picturePlazaViewModel: PicturePlazaViewModel? = null
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
    var binding: ViewDataBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var adapter = PicPlazaAdapter(this)
        picturePlazaViewModel = ViewModelProviders.of(this).get(PicturePlazaViewModel::class.java)
        binding = bindContentView(this, R.layout.activity_piture_plaza)
        binding?.setVariable(BR.adapter, adapter)
        binding?.setVariable(BR.layoutManager, LinearLayoutManager( this))
        refresh_layout.setRefreshLoadLayoutManger(RefreshLayoutManger(this))
        initData()
        refresh_layout.setListener(this)
        picturePlazaViewModel?.recomends?.observe(this, Observer {
            adapter.binData(it)
            adapter.notifyDataSetChanged()
        })
//        val user = User()
//        user.userName = "munin"
//        user.password = "123456"
//        user.userId = "123456"
//        DataBaseUtils.getDatabase().userDao().insertItem(user)
    }

    var dispose: Disposable? = null
    fun initData() {
        val data = ApiClient.instance.service.getPictures(page)
        dispose?.dispose()
        dispose = data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    Looper.myQueue().addIdleHandler {
                        lock.getAndSet(false)
                        picturePlazaViewModel?.recomends?.postValue(it.recommends)
                        false
                    }
                    if (refresh_layout.currentState == RefreshState.REFRESHING) {
                        refresh_layout.finishRefresh()
                    }
                    if (refresh_layout.currentState == RefreshState.LOADING) {
                        refresh_layout.finishLoad()
                    }
                },  {
                    lock.getAndSet(false)
                    if (refresh_layout.currentState == RefreshState.REFRESHING) {
                        refresh_layout.finishRefresh()

                    }
                    if (refresh_layout.currentState == RefreshState.LOADING) {
                        refresh_layout.finishLoad()
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose?.dispose()
    }
}
