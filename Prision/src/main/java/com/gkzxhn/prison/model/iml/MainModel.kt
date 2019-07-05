package com.gkzxhn.prison.model.iml


import android.util.Log
import com.android.volley.AuthFailureError
import com.gkzxhn.prison.model.IMainModel
import com.gkzxhn.prison.common.Constants
import com.gkzxhn.prison.utils.XtHttpUtil
import com.gkzxhn.prison.async.VolleyUtils

import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

/**
 * Created by Raleigh.Luo on 17/4/12.
 */

class MainModel :CallZijingModel(), IMainModel {


    /**
     * 获取免费呼叫次数
     * @param onFinishedListener
     */
    override fun requestFreeTime(onFinishedListener: VolleyUtils.OnFinishedListener<JSONObject>) {
        try {
            val url = String.format("%s?terminalNumber=%s", Constants.REQUEST_FREE_MEETING_TIME,
                    sharedPreferences.getString(Constants.USER_ACCOUNT, ""))
            volleyUtils[JSONObject::class.java, url, REQUEST_TAG, onFinishedListener]
        } catch (authFailureError: AuthFailureError) {
            authFailureError.printStackTrace()
        }

    }

    /**
     * 获取版本信息
     */
    override fun requestVersion(onFinishedListener: VolleyUtils.OnFinishedListener<JSONObject>) {
        try {
            val url=String.format("%s?page=1&rows=10",Constants.REQUEST_VERSION_URL)
            volleyUtils[JSONObject::class.java, url, REQUEST_TAG, onFinishedListener]
        } catch (authFailureError: AuthFailureError) {
            authFailureError.printStackTrace()
        }

    }


    /**
     * 根据日期获取会见列表
     */
    override fun request(date: String,currentPage:Int,pageNumber:Int,  onFinishedListener: VolleyUtils.OnFinishedListener<JSONObject>) {
        val account = sharedPreferences.getString(Constants.USER_ACCOUNT, "")
        System.out.println("新的地址"+Constants.DOMAIN_NAME)
        System.out.println("新的地址1"+Constants.REQUEST_MEETING_LIST_URL)
        val url = String.format("%s?terminalNumber=%s&meetingDate=%s&page=%s&rows=%s", Constants.REQUEST_MEETING_LIST_URL, account, date,
                currentPage,pageNumber)
        try {
            volleyUtils[JSONObject::class.java, url, REQUEST_TAG, onFinishedListener]
        } catch (authFailureError: AuthFailureError) {
            authFailureError.printStackTrace()
        }

    }
}
