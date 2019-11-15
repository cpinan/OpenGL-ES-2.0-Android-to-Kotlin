package com.carlospinan.airhockeyortho

import com.carlospinan.airhockeyortho.common.GLBaseActivity
import com.carlospinan.airhockeyortho.common.GLBaseRenderer

class AirHockeyActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return AirHockeyRenderer(this)
    }

    override fun onReady() {
    }

}