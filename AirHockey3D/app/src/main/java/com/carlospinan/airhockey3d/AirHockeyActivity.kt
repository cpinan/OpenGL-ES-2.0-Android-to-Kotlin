package com.carlospinan.airhockey3d

import com.carlospinan.airhockey3d.common.GLBaseActivity
import com.carlospinan.airhockey3d.common.GLBaseRenderer

class AirHockeyActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return AirHockeyRenderer(this)
    }

    override fun onReady() {
    }

}