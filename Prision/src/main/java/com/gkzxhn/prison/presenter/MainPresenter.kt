package com.gkzxhn.prison.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

import com.android.volley.VolleyError
import com.gkzxhn.prison.R
import com.gkzxhn.prison.activity.VideoMettingActivity
import com.gkzxhn.prison.async.AsynHelper
import com.gkzxhn.prison.common.Constants
import com.gkzxhn.prison.common.GKApplication
import com.gkzxhn.prison.entity.MeetingEntity
import com.gkzxhn.prison.entity.VersionEntity
import com.gkzxhn.prison.model.IMainModel
import com.gkzxhn.prison.model.iml.MainModel
import com.gkzxhn.prison.view.IMainView
import com.gkzxhn.wisdom.async.VolleyUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.starlight.mobile.android.lib.util.ConvertUtil
import com.starlight.mobile.android.lib.util.HttpStatus
import com.starlight.mobile.android.lib.util.JSONUtil

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Raleigh.Luo on 17/4/12.
 */

class MainPresenter(context: Context, view: IMainView) : BasePresenter<IMainModel, IMainView>(context, MainModel(), view) {
    private var requestZijingTime = 0

    private val TAG = MainPresenter::class.java.simpleName
    fun resetTime() {
        requestZijingTime = 0
    }
    fun turnOff(){
        mModel.turnOff(null)
    }

    /**
     * 发请求，检测设备视频会议是否已经准备好
     */
    fun requestZijing() {
        requestZijingTime++
        mModel.getCallHistory(object : VolleyUtils.OnFinishedListener<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val code = ConvertUtil.strToInt(JSONUtil.getJSONObjectStringValue(response, "code"))
                if (code == 0) {
                    mView?.startZijingService()
                    checkCallStatus()
                } else {
                    mView?.zijingServiceFailed()
                }
            }

            override fun onFailed(error: VolleyError) {
                if (requestZijingTime < 5) {//最多请求5次
                    requestZijing()
                } else {
                    mView?.zijingServiceFailed()
                }
            }
        })
    }

    /**
     * 检查是否正在呼叫 是则挂断
     */
    fun checkCallStatus(){
        mModel.getCallInfor(object :VolleyUtils.OnFinishedListener<JSONObject>{
            override fun onSuccess(response: JSONObject) {
                val code = response.getInt("code")
                if (code == 0 ) {//正在拨打电话
                    //挂断
                    mModel.hangUp(null)
                }
            }

            override fun onFailed(error: VolleyError) {
            }

        })

    }

    /**
     * 请求免费会见次数
     */
    fun requestFreeTime() {
        mModel.requestFreeTime(object : VolleyUtils.OnFinishedListener<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val code = ConvertUtil.strToInt(JSONUtil.getJSONObjectStringValue(response, "code"))
                if (code == HttpStatus.SC_OK) {
                    val time = ConvertUtil.strToInt(JSONUtil.getJSONObjectStringValue(
                            JSONUtil.getJSONObject(response,"data"), "access_times"))
                    //保存到本地
                    getSharedPreferences().edit().putInt(Constants.CALL_FREE_TIME, time).apply()
                }
            }

            override fun onFailed(error: VolleyError) {
            }
        })
    }

    /**
     * 请求会见列表
     */
    fun request(isRefresh: Boolean,date: String) {
//        checkGUI()
        if (isRefresh) {
            currentPage = FIRST_PAGE
            mView?.startRefreshAnim()
        }
        mModel.request(date,currentPage,PAGE_SIZE, object : VolleyUtils.OnFinishedListener<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val view = if (mWeakView == null) null else mWeakView!!.get()
                view?.stopRefreshAnim()
                try {
                    val code = ConvertUtil.strToInt(JSONUtil.getJSONObjectStringValue(response, "code"))
                    if (code == HttpStatus.SC_OK) {
                        val resultJson = JSONUtil.getJSONObjectStringValue(JSONUtil.getJSONObject(response,"data"), "meetings")
                        startAsynTask(Constants.MAIN_TAB, object : AsynHelper.TaskFinishedListener {
                            override fun back(`object`: Any?) {
                                val mData =`object` as List<MeetingEntity>
                                if (mData != null && mData.size > 0) currentPage = currentPage + 1
                                mView?.updateItems(mData)
                                mView?.stopRefreshAnim()
                            }
                        }, resultJson)

                    } else {
                        mView?.updateItems(null)
                    }
                } catch (e: Exception) {
                }

            }

            override fun onFailed(error: VolleyError) {
                showErrors(error)
            }
        })
    }

    /**
     *  取消会见
     */
    fun requestCancel(id: String, reason: String) {
        mView?.showProgress()
        mModel.requestCancel(id, reason, object : VolleyUtils.OnFinishedListener<String> {
            override fun onSuccess(response: String) {
                mView?.dismissProgress()
                val code = ConvertUtil.strToInt(JSONUtil.getJSONObjectStringValue(JSONUtil.getJSONObject(response), "code"))
                if (code == HttpStatus.SC_OK) {
                    mView?.showToast(R.string.canceled_meeting)
                    mView?.onCanceled()
                } else {
                    mView?.showToast(R.string.operate_failed)
                }
            }

            override fun onFailed(error: VolleyError) {
                showErrors(error)
            }
        })

    }

    /**
     *  请求版本信息
     */
    fun requestVersion() {
        mModel.requestVersion(object : VolleyUtils.OnFinishedListener<JSONObject> {
            override fun onSuccess(response: JSONObject) {
                val code = ConvertUtil.strToInt(JSONUtil.getJSONObjectStringValue(response, "code"))
                if (code == HttpStatus.SC_OK) {
                    val versionsJson=JSONUtil.getJSONObjectStringValue(JSONUtil.getJSONObject(response,"data"),"versions")
                    val versions= Gson().fromJson<List<VersionEntity>>(versionsJson,
                            object : TypeToken<List<VersionEntity>>() {
                            }.type)
                    for(version in versions){
                        if(version.id==2){
                            mView?.updateVersion(version)
                            break
                        }
                    }
                }
            }

            override fun onFailed(error: VolleyError) {}
        })
    }

    /**
     * 判断当前云信id状态
     */
    fun checkStatusCode(): StatusCode {
        val code = NIMClient.getStatus()
        when(code){
            StatusCode.KICKOUT-> {// 被其他端挤掉
                Toast.makeText(GKApplication.instance, R.string.kickout, Toast.LENGTH_SHORT).show()
                GKApplication.instance.loginOff()
            }
            StatusCode.CONNECTING ->{// 正在连接
                mView?.showToast(R.string.yunxin_offline)
            }
            StatusCode.LOGINING-> {// 正在登录
                mView?.showToast(R.string.yunxin_offline)
            }
            StatusCode.NET_BROKEN -> { // 网络连接已断开
                mView?.showToast(R.string.network_error)
            }
            StatusCode.UNLOGIN-> {// 未登录
                //系统自动登录云信
                val username = getSharedPreferences().getString(Constants.USER_ACCOUNT, "")
                val password = getSharedPreferences().getString(Constants.USER_PASSWORD, "")
                if ((username != null) and (username!!.length > 0)) {
                    val info = LoginInfo(username, password) // config...
                    //登录云信
                    NIMClient.getService(AuthService::class.java).login(info)
                            .setCallback(null)
                } else {//退出到登录界面
                    GKApplication.instance.loginOff()
                }
            }
        }
        return code
    }

    override fun stopAnim() {
        super.stopAnim()
        mView?.dismissProgress()
    }
}
