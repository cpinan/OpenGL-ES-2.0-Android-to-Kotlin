package com.carlospinan.particles

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20.*
import android.opengl.Matrix
import com.carlospinan.particles.common.GLBaseRenderer
import com.carlospinan.particles.extensions.loadTexture
import com.carlospinan.particles.objects.ParticleFireworksExplosion
import com.carlospinan.particles.objects.ParticleShooter
import com.carlospinan.particles.objects.ParticleSystem
import com.carlospinan.particles.programs.ParticleShaderProgram
import com.carlospinan.particles.utilities.Point
import com.carlospinan.particles.utilities.Vector
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

const val ANGLE_VARIANCE = 5f
const val SPEED_VARIANCE = 1f

class ParticlesRenderer(
    private val context: Context
) : GLBaseRenderer() {

    private val random = Random()

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    private lateinit var particleProgram: ParticleShaderProgram
    private lateinit var particleSystem: ParticleSystem

    private lateinit var redParticleShooter: ParticleShooter
    private lateinit var greenParticleShooter: ParticleShooter
    private lateinit var blueParticleShooter: ParticleShooter

    private lateinit var particleFireworksExplosion: ParticleFireworksExplosion

    private val hsv = floatArrayOf(0f, 1f, 1f)
    private var globalStartTime = -1L
    private var texture = -1

    override fun onSurfaceCreated(unused: GL10?, eglConfig: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)

        particleProgram = ParticleShaderProgram(context)
        particleSystem = ParticleSystem(10000)
        globalStartTime = System.nanoTime()

        val particleDirection = Vector(0f, 0.5f, 0f)

        redParticleShooter = ParticleShooter(
            Point(-1f, 0f, 0f),
            particleDirection,
            Color.rgb(255, 50, 5),
            ANGLE_VARIANCE,
            SPEED_VARIANCE
        )

        greenParticleShooter = ParticleShooter(
            Point.zero(),
            particleDirection,
            Color.rgb(25, 255, 25),
            ANGLE_VARIANCE,
            SPEED_VARIANCE
        )

        blueParticleShooter = ParticleShooter(
            Point(1f, 0f, 0f),
            particleDirection,
            Color.rgb(5, 50, 255),
            ANGLE_VARIANCE,
            SPEED_VARIANCE
        )

        particleFireworksExplosion = ParticleFireworksExplosion()

        texture = context.loadTexture(R.drawable.particle_texture)

    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        Matrix.perspectiveM(
            projectionMatrix,
            0,
            45f,
            width.toFloat() / height.toFloat(),
            1f,
            10f
        )

        Matrix.setIdentityM(
            viewMatrix,
            0
        )

        Matrix.translateM(
            viewMatrix,
            0,
            0f,
            -1.5f,
            -5f
        )

        Matrix.multiplyMM(
            viewProjectionMatrix,
            0,
            projectionMatrix,
            0,
            viewMatrix,
            0
        )
    }

    override fun onDrawFrame(unused: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f

        redParticleShooter.addParticles(particleSystem, currentTime, 5)
        greenParticleShooter.addParticles(particleSystem, currentTime, 5)
        blueParticleShooter.addParticles(particleSystem, currentTime, 5)

        if (random.nextFloat() < 0.02f) {
            hsv[0] = random.nextInt(360).toFloat()

            particleFireworksExplosion.addExplosion(
                particleSystem,
                Point(
                    -1f + random.nextFloat() * 2f,
                    3f + random.nextFloat() / 2f,
                    -1f + random.nextFloat() * 2f
                ),
                Color.HSVToColor(hsv),
                globalStartTime
            )
        }

        particleProgram.useProgram()

        particleProgram.setUniforms(viewProjectionMatrix, currentTime, texture)
        particleSystem.bindData(particleProgram)
        particleSystem.draw()

    }

}