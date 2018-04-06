package com.gkzxhn.prison.model

import com.gkzxhn.wisdom.async.VolleyUtils
import org.json.JSONObject

/**
 * Created by Raleigh.Luo on 17/4/13.
 */

interface ICallUserModel : ICallZijingModel {
    fun requestFreeTime(onFinishedListener: VolleyUtils.OnFinishedListener<JSONObject>)
    fun request(id: String, onFinishedListener: VolleyUtils.OnFinishedListener<JSONObject>)
}
