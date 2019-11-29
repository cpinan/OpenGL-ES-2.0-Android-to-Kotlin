package com.carlospinan.airhockeywithbettermallets

import com.carlospinan.airhockeywithbettermallets.common.GLBaseActivity
import com.carlospinan.airhockeywithbettermallets.common.GLBaseRenderer

class AirHockeyActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return AirHockeyRenderer(this)
    }

    override fun onReady() {
    }

}