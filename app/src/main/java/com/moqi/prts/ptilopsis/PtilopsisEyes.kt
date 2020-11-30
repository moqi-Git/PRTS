package com.moqi.prts.ptilopsis

import java.lang.annotation.Native

class PtilopsisEyes{

    companion object{
        init {
            System.loadLibrary("libptilopsis")
        }
    }

    public external fun getOpenCVVersion(): String
}