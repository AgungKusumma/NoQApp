package com.capstoneproject.noqapp.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {
    val networkIO: Executor = Executors.newFixedThreadPool(3)
}