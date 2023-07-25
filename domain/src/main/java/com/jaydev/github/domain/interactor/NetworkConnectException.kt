package com.jaydev.github.domain.interactor

import java.io.IOException

class NetworkConnectException : IOException("Network is Not Connected.")

class InternalServerException(val errorCode: Int, val errorMessage: String) :
    IOException("Internal Server Error.")
