package com.taskappictest.Model


class ApiResponse(status: String, message: String) {

    var status: String? = null

    var message: String? = null

    var errorCode: String? = null

    var filterDatalist: List<FilterDatalist>? = null
}