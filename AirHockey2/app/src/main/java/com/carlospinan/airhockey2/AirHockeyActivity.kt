package com.carlospinan.airhockey2

import com.carlospinan.airhockey2.common.GLBaseActivity
import com.carlospinan.airhockey2.common.GLBaseRenderer

class AirHockeyActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return AirHockeyRenderer(this)
    }

    override fun onReady() {
    }

}