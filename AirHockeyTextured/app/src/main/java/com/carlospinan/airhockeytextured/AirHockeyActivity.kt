package com.carlospinan.airhockeytextured

import com.carlospinan.airhockeytextured.common.GLBaseActivity
import com.carlospinan.airhockeytextured.common.GLBaseRenderer

class AirHockeyActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return AirHockeyRenderer(this)
    }

    override fun onReady() {
    }

}