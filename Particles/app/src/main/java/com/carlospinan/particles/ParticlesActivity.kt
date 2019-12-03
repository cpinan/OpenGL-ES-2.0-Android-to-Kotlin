package com.carlospinan.particles

import com.carlospinan.particles.common.GLBaseActivity
import com.carlospinan.particles.common.GLBaseRenderer

class ParticlesActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return ParticlesRenderer(this)
    }

    override fun onReady() {
    }
}
