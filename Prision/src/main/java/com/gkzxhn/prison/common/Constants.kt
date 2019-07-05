package com.gkzxhn.prison.common

/**
 * Created by Raleigh.Luo on 17/3/29.
 */

object Constants {
    /*-------------------------------Config-------------------------------------------------*/
    val CACHE_FILE = GKApplication.instance.filesDir.absolutePath

    val APK_NAME="/app.apk"
    val IS_DEBUG_MODEL = false//debug模式打印日志到控制台,发布版本不打印
    val REQUEST_TIMEOUT = 30000//超时时间半分钟
    /*-------------------------------User Tab-------------------------------------------------*/
    val TEMP_TABLE = "temp_table"//不清空表，临时表

    val USER_TABLE = "user_table"
    val USER_IS_UNAUTHORIZED = "isUnauthorized"
    val USER_ACCOUNT = "user_account"//云信帐号
    val USER_PASSWORD = "user_password"//云信密码

    val TERMINAL_JIAL_ID = "terminal_jial_id"//监狱id
    val TERMINAL_JIAL_NAME = "terminal_jial_name"//监狱名称
    val TERMINAL_ROOM_NUMBER = "terminal_room_number"//会议室号码
    val TERMINAL_HOST_PASSWORD = "terminal_host_password"//主持人密码
    val TERMINAL_GUEST_PASSWORD = "terminal_guest_password"//参与密码 客人密码

//    val TERMINAL_ACCOUNT = "terminal_account"//终端帐号
    val TERMINAL_RATE = "terminal_rate"//终端码率
    val LAST_IGNORE_VERSION = "last_ignore_version"//上一个忽略的版本
    val OTHER_CARD = "other_card"//身份证信息

    val CALL_FREE_TIME = "call_free_time"//免费呼叫次数
    val IS_OPEN_USB_RECORD = "is_open_usb_record"//是否开启usb录屏状态 true/false 开启／关闭

    val FREE_MEETING_PRISON_ID= "free_meeting_prison_id"//免费会见囚犯ID


    /*-------------------------------request url-------------------------------------------------*/
    val IS_TEST_MODEL=true //是否为自动化测试模式，发布时需设置为false
    var Select_DOMAIN =""
    var RELEASE_DOMAIN = "https://www.yuwugongkai.com/ywgk-app"//新发布正式环境
    var DEMO_DOMAIN = "http://120.79.251.238:8022/ywgk-app" //"http://120.78.190.101:8084/ywgk-app-demo"//演示环境
    val DEV_DOMAIN = "http://120.78.190.101:8086/ywgk-app-auth"//开发环境
    var DOMAIN_NAME = DEMO_DOMAIN

    var REQUEST_MEETING_LIST_URL = DOMAIN_NAME + "/api/meetings/getMeetingsForPrison"//会见列表
    var REQUEST_CANCEL_MEETING_URL = DOMAIN_NAME + "/api/meetings/update"// 取消会见
    var REQUEST_MEETING_DETAIL_URL = DOMAIN_NAME + "/api/families/detail"// 会见详情
    var REQUEST_MEETING_MEMBERS_URL = DOMAIN_NAME + "/api/jails/meetingMembers"// 查询会见家属
    var REQUEST_VERSION_URL = DOMAIN_NAME + "/api/versions/page"//版本更新
    var REQUEST_CRASH_LOG_URL = DOMAIN_NAME + "/app_loggers/save"//奔溃日志
    var REQUEST_MEETING_ROOM = DOMAIN_NAME + "/api/terminals/detail"//会议室信息

    var REQUEST_FAMILY_BY_KEY = DOMAIN_NAME + "/api/meetings/getMeetingsFree"//免费会见－根据用户名和手机号码查询家属

    var REQUEST_FREE_MEETING_TIME = DOMAIN_NAME + "/api/jails/access_times"//免费呼叫次数
    var UPDATE_FREE_MEETING_TIME = DOMAIN_NAME + "/api/jails/access"//减少呼叫次数

    var ADD_FREE_MEETING = DOMAIN_NAME + "/api/free_meetings/add"//记录免费会见信息
    var UPDATE_FREE_MEETING = DOMAIN_NAME + "/api/free_meetings/updateDuration"//更新免费会见时长
    var UPDATE_MEETING_DURATION = DOMAIN_NAME + "/api/meetings/updateDuration"//更新远程会见时长(该接口已关闭)

    var ADD_COMMUNICATE_RECORDS = DOMAIN_NAME + "/api/meetings/addMeetingCallRecords"//新增会见通话记录
    var UPDATE_COMMUNICATE_RECORDS = DOMAIN_NAME + "/api/meetings/updateMeetingCallRecords"//更新(结束)会见通话记录

    /*-------------------------------msg what-------------------------------------------------*/
    val START_REFRESH_UI = 1
    val STOP_REFRESH_UI = 2//msg what
    /*-------------------------------Request Code-------------------------------------------------*/
    val EXTRA = "extra"
    val EXTRA_TAB = "extra_tab"
    val EXTRAS = "extras"
    val EXTRA_ENTITY = "extra_entity"
    val EXTRA_POSITION = "extra_position"

    //总会见时长
    val TOTAL_CALL_DURATION = "total_call_duration"
    //已通话时长
    val LAST_CALL_DURATION = "last_call_duration"
    val EXTRA_CODE = 0x001
    val PREVIEW_PHOTO_CODE = 0x102
    val SELECT_PHOTO_CODE = 0x103
    val TAKE_PHOTO_CODE = 0x104
    val RESIZE_REQUEST_CODE = 0x105
    val EXTRAS_CODE = 0x106
    /*-------------------------------Request Tab-------------------------------------------------*/
    val MAIN_TAB = 0x206
    val CLOSE_GUI_TAB = 0x207
    val OPEN_GUI_TAB = 0x208
    /*-------------------------------Service服务器状态配置-------------------------------------------------*/
    val MEETTING_CANCELED = "CANCELED"
    val MEETTING_FINISHED = "FINISHED"
    val MEETTING_PASSED = "PASSED"
    //上传图片的auth认证
    val UPLOAD_FILE_AUTHORIZATION = "523b87c4419da5f9186dbe8aa90f37a3876b95e448fe2a"

    /*-------------------------------action-------------------------------------------------*/

    val ONLINE_SUCCESS_ACTION = "com.gkzxhn.prison.ONLINE_SUCCESS_ACTION"//连线成功
    //云信被踢下线
    val NIM_KIT_OUT = "com.gkzxhn.prison.NIM_KIT_OUT"


    //C9包名
    val C9_PACKAGE_NAME="cn.com.rocware.c9gui"
    //免费会见
    val CALL_FREE_ACTION = "com.gkzxhn.prison.CALL_FREE_ACTION"
    val CALL_DEFUALT_ACTION = "com.gkzxhn.prison.CALL_DEFUALT_ACTION"
    //会见时间闹钟提醒
    val ALARM_CLOCK = "com.gkzxhn.prison.ALARM.CLOCK"
    //系统凌晨闹钟提醒
    val SYSTEM_ALARM_CLOCK = "com.gkzxhn.prison.SYSTEM.ALARM.CLOCK"
    //家属加入会议失败
    val FAMILY_FAILED_JOIN_METTING = "com.gkzxhn.prison.CALL_HANG_UP"
    //家属加入会议
    val FAMILY_JOIN_METTING = "com.gkzxhn.prison.FAMILY_JOIN_METTING"
    //监狱加入会议
    val PRISION_JOIN_METTING = "com.gkzxhn.prison.PRISION_JOIN_METTING"
    //紫荆会议主之人密码密码
    val ZIJING_PASSWORD = "zijing_password"
    val ACCID = "accid"
    val PROTOCOL = "protocol"
    //时间限制
    val TIME_LIMIT = "time_limit"
    val CALL_AGAIN = "call_again"
    val END_REASON = "end_reason"
}
