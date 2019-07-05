package com.gkzxhn.prison.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.gkzxhn.prison.R
import com.gkzxhn.prison.async.AsynHelper
import com.gkzxhn.prison.common.Constants
import com.gkzxhn.prison.entity.VersionEntity
import com.gkzxhn.prison.presenter.SettingPresenter
import com.gkzxhn.prison.service.EReportService
import com.gkzxhn.prison.view.ISettingView
import com.starlight.mobile.android.lib.util.CommonHelper
import kotlinx.android.synthetic.main.network_layout.network_layout_btn_check_network
as btnCheckNetwork
import kotlinx.android.synthetic.main.network_layout.network_layout_btn_disable_gui as btnDisableGui
import kotlinx.android.synthetic.main.network_layout.network_layout_btn_enable_gui as btnEnableGui
import kotlinx.android.synthetic.main.network_layout.network_layout_tv_check_network_hint as tvCheckNetworkHint
import kotlinx.android.synthetic.main.network_layout.network_layout_tv_disable_gui_hint as tvDisableGuiHint
import kotlinx.android.synthetic.main.network_layout.network_layout_tv_enable_gui_hint as tvEnableGuiHint
import kotlinx.android.synthetic.main.network_layout.network_layout_sp_setweb as ipAddress
import kotlinx.android.synthetic.main.network_layout.network_layout_et_setweb as ipShow
/**检查网络
 * Created by Raleigh.Luo on 18/5/8.
 */

class NetworkActivity : SuperActivity(), ISettingView {
    //ip数组值
    private lateinit var mIPArray: Array<String>
    private lateinit var ip: String
    //请求presenter
    private lateinit var mPresenter: SettingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_layout)
        initSpinner()
        ipShow.setText(Constants.DOMAIN_NAME)
        //初始化Presenter
        mPresenter = SettingPresenter(this, this)
    }

    private fun initSpinner() {
        mIPArray = resources.getStringArray(R.array.set_ip_array)
        val adapter = ArrayAdapter(this,R.layout.spinner_item,mIPArray)
        ipAddress.adapter = adapter
        ipAddress.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                CommonHelper.clapseSoftInputMethod(this@NetworkActivity)
                ip = mIPArray[p2]
                ipShow.setText(ip)
            }
        }
    }

    /**
     * 页面所有点击事件
     */
    fun onClickListener(view: View) {
        when (view.id) {
            R.id.network_layout_btn_webset ->{
                if (!ipShow.text.toString().trim().isEmpty()) {
                    var a = ipShow.text.toString().trim()
                    Constants.DOMAIN_NAME = a
                    changeBaseUrl()
                    System.out.println("新的地址"+Constants.DOMAIN_NAME)
                    showToast("保存成功")

                }else{
                    showToast("请输入地址")
                }

            }
            R.id.common_head_layout_iv_left -> {
                finish()
            }
            R.id.network_layout_btn_enable_gui -> {//启用Gui
                tvEnableGuiHint.setText(R.string.start_gui_ing)
                //单元测试 延迟加载
                setIdleNow(true)
                btnEnableGui.isEnabled = false
                // adb shell pm enable cn.com.rocware.c9gui
                mPresenter.startAsynTask(Constants.OPEN_GUI_TAB, object : AsynHelper.TaskFinishedListener {
                    override fun back(`object`: Any?) {
                        btnEnableGui.isEnabled = true
                        val i = `object` as Int
                        if (i == 0) {//启用成功
                            tvEnableGuiHint.setText(R.string.start_gui_success)
                            tvDisableGuiHint.setText(R.string.stop_gui_hint)
                        } else {
                            tvEnableGuiHint.setText(R.string.start_gui_failed)
                        }
                        //单元测试 释放延迟加载
                        setIdleNow(false)
                    }

                })

            }
            R.id.network_layout_btn_disable_gui -> {//关闭Gui
                btnDisableGui.isEnabled = false
                tvDisableGuiHint.setText(R.string.stop_gui_ing)
                //单元测试 延迟加载
                setIdleNow(true)
                //adb shell pm disable cn.com.rocware.c9gui
                mPresenter.startAsynTask(Constants.CLOSE_GUI_TAB, object : AsynHelper.TaskFinishedListener {
                    override fun back(`object`: Any?) {
                        btnDisableGui.isEnabled = true
                        val i = `object` as Int
                        if (i == 0) {//禁用用成功
                            tvDisableGuiHint.setText(R.string.stop_gui_success)
                            tvEnableGuiHint.setText(R.string.start_gui_hint)
                            stopService(Intent(this@NetworkActivity, EReportService::class.java))
                            val preferences = getSharedPreferences(Constants.USER_TABLE, Activity.MODE_PRIVATE)
                            if (preferences.getString(Constants.USER_ACCOUNT, "").length > 0) {//登录 重启服务
                                val mService = Intent(this@NetworkActivity, EReportService::class.java)
                                startService(mService)
                            }
                        } else {
                            tvDisableGuiHint.setText(R.string.stop_gui_failed)
                        }
                        //单元测试 释放延迟加载
                        setIdleNow(false)
                    }
                })
            }
            R.id.network_layout_btn_check_network -> {//检查网络
                tvCheckNetworkHint.setText(R.string.check_network_ing)
                //按钮不可点击
                btnCheckNetwork.isEnabled = false
                //关闭GUI
                mPresenter.checkNetworkStatus()
            }
        }
    }

    private fun changeBaseUrl() {
        Constants.REQUEST_MEETING_LIST_URL = Constants.DOMAIN_NAME + "/api/meetings/getMeetingsForPrison"//会见列表
        Constants.REQUEST_CANCEL_MEETING_URL = Constants.DOMAIN_NAME + "/api/meetings/update"// 取消会见
        Constants.REQUEST_MEETING_DETAIL_URL = Constants.DOMAIN_NAME + "/api/families/detail"// 会见详情
        Constants.REQUEST_MEETING_MEMBERS_URL = Constants.DOMAIN_NAME + "/api/jails/meetingMembers"// 查询会见家属
        Constants.REQUEST_VERSION_URL = Constants.DOMAIN_NAME + "/api/versions/page"//版本更新
        Constants.REQUEST_CRASH_LOG_URL = Constants.DOMAIN_NAME + "/app_loggers/save"//奔溃日志
        Constants.REQUEST_MEETING_ROOM = Constants.DOMAIN_NAME + "/api/terminals/detail"//会议室信息

        Constants.REQUEST_FAMILY_BY_KEY = Constants.DOMAIN_NAME + "/api/meetings/getMeetingsFree"//免费会见－根据用户名和手机号码查询家属

        Constants.REQUEST_FREE_MEETING_TIME = Constants.DOMAIN_NAME + "/api/jails/access_times"//免费呼叫次数
        Constants.UPDATE_FREE_MEETING_TIME = Constants.DOMAIN_NAME + "/api/jails/access"//减少呼叫次数

        Constants.ADD_FREE_MEETING = Constants.DOMAIN_NAME + "/api/free_meetings/add"//记录免费会见信息
        Constants.UPDATE_FREE_MEETING = Constants.DOMAIN_NAME + "/api/free_meetings/updateDuration"//更新免费会见时长
        Constants.UPDATE_MEETING_DURATION = Constants.DOMAIN_NAME + "/api/meetings/updateDuration"//更新远程会见时长(该接口已关闭)

        Constants.ADD_COMMUNICATE_RECORDS = Constants.DOMAIN_NAME + "/api/meetings/addMeetingCallRecords"//新增会见通话记录
        Constants.UPDATE_COMMUNICATE_RECORDS = Constants.DOMAIN_NAME + "/api/meetings/updateMeetingCallRecords"//更新(结束)会见通话记录

    }


    /**
     * 网络状态返回
     */
    override fun networkStatus(isConnected: Boolean) {
        //按钮可点击
        btnCheckNetwork.isEnabled = true
        if (isConnected) {
            tvCheckNetworkHint.setTextColor(resources.getColor(R.color.connect_success))
            tvCheckNetworkHint.setText(R.string.check_network_normal)
        } else {
            tvCheckNetworkHint.setTextColor(resources.getColor(R.color.red_text))
            tvCheckNetworkHint.setText(R.string.check_network_innormal)
        }
    }

    override fun startRefreshAnim() {
    }

    override fun stopRefreshAnim() {
    }

    override fun updateVersion(version: VersionEntity?) {
    }

}
