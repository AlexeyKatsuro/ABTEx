package com.e.btex.data

import java.util.Date


internal class MyExImpl : MyEx {

    override val date: Long = Date().time

}