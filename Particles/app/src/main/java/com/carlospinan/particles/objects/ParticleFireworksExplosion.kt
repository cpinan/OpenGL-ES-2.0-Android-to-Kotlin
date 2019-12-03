package com.carlospinan.particles.objects

import android.graphics.Color
import android.opengl.Matrix
import com.carlospinan.particles.utilities.Point
import com.carlospinan.particles.utilities.Vector
import java.util.*

class ParticleFireworksExplosion {

    private val random = Random()

    private val rotationMatrix = FloatArray(16)
    private val directionVector = floatArrayOf(0f, 0f, 1f, 1f)
    private val resultVector = FloatArray(4)
    private val hsv = FloatArray(3)

    fun addExplosion(particleSystem: ParticleSystem, position: Point, color: Int, startTime: Long) {
        val currentTime = (System.nanoTime() - startTime) / 1000000000f
        for (trail in 1..50) {
            Matrix.setRotateEulerM(
                rotationMatrix,
                0,
                random.nextFloat() * 360f,
                random.nextFloat() * 360f,
                random.nextFloat() * 360f
            )

            Matrix.multiplyMV(
                resultVector,
                0,
                rotationMatrix,
                0,
                directionVector,
                0
            )

            val magnitude = 0.5f + (random.nextFloat() / 2f)
            var timeForThisStream = currentTime
            Color.colorToHSV(color, hsv)

            for (particle in 1..10) {
                particleSystem.addParticle(
                    position,
                    Color.HSVToColor(hsv),
                    Vector(
                        resultVector[0] * magnitude,
                        resultVector[1] * magnitude + 0.5f,
                        resultVector[2] * magnitude
                    ),
                    timeForThisStream
                )
                timeForThisStream += 0.025f
                hsv[2] *= 0.9f
            }

        }
    }

}