package com.gkzxhn.prison.model.iml

import com.gkzxhn.prison.common.Constants
import com.gkzxhn.prison.model.ILoginModel
import com.gkzxhn.wisdom.async.VolleyUtils
import org.json.JSONObject

/**
 * Created by Raleigh.Luo on 18/3/28.
 */

class LoginModel : BaseModel(), ILoginModel {
    /**
     * 获取终端信息
     */
    override fun getMeetingRoom(account: String, password: String, onFinishedListener: VolleyUtils.OnFinishedListener<JSONObject>) {
        val url = String.format("%s?terminalNumber=%s",Constants.REQUEST_MEETING_ROOM , account )
        volleyUtils.get(JSONObject::class.java,url, REQUEST_TAG, onFinishedListener)
    }
}
