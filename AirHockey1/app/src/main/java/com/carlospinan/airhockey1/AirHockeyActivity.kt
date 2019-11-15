package com.carlospinan.airhockey1

import com.carlospinan.airhockey1.common.GLBaseActivity
import com.carlospinan.airhockey1.common.GLBaseRenderer

class AirHockeyActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return AirHockeyRenderer(this)
    }

    override fun onReady() {
    }

}