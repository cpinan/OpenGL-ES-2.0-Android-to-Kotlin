package com.carlospinan.livewallpaper.objects

import android.graphics.Color
import android.opengl.GLES20
import com.carlospinan.livewallpaper.common.BYTES_PER_FLOAT
import com.carlospinan.livewallpaper.data.VertexArray
import com.carlospinan.livewallpaper.programs.ParticleShaderProgram
import com.carlospinan.livewallpaper.utilities.Point
import com.carlospinan.livewallpaper.utilities.Vector

private const val POSITION_COMPONENT_COUNT = 3
private const val COLOR_COMPONENT_COUNT = 3
private const val VECTOR_COMPONENT_COUNT = 3
private const val PARTICLE_START_TIME_COMPONENT_COUNT = 1

private const val TOTAL_COMPONENT_COUNT = (POSITION_COMPONENT_COUNT
        + COLOR_COMPONENT_COUNT
        + VECTOR_COMPONENT_COUNT
        + PARTICLE_START_TIME_COMPONENT_COUNT)

private const val STRIDE: Int = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT

class ParticleSystem(
    private val maxParticleCount: Int
) {
    private val particles = FloatArray(maxParticleCount * TOTAL_COMPONENT_COUNT)
    private val vertexArray = VertexArray(particles)

    private var currentParticleCount = 0
    private var nextParticle = 0

    fun addParticle(
        position: Point,
        color: Int,
        direction: Vector,
        particleStartTime: Float
    ) {
        val particleOffset = nextParticle * TOTAL_COMPONENT_COUNT
        var currentOffset = particleOffset
        nextParticle++

        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++
        }

        if (nextParticle == maxParticleCount) {
            // Start over at the beginning, but keep currentParticleCount so
            // that all the other particles still get drawn.
            nextParticle = 0
        }

        particles[currentOffset++] = position.x
        particles[currentOffset++] = position.y
        particles[currentOffset++] = position.z

        particles[currentOffset++] = Color.red(color) / 255f
        particles[currentOffset++] = Color.green(color) / 255f
        particles[currentOffset++] = Color.blue(color) / 255f

        particles[currentOffset++] = direction.x
        particles[currentOffset++] = direction.y
        particles[currentOffset++] = direction.z

        particles[currentOffset++] = particleStartTime

        vertexArray.updateBuffer(
            particles,
            particleOffset,
            TOTAL_COMPONENT_COUNT
        )

    }

    fun bindData(program: ParticleShaderProgram) {
        var dataOffset = 0
        vertexArray.setVertexAttribPointer(
            dataOffset,
            program.positionAttributeLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += POSITION_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffset,
            program.colorAttributeLocation,
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += COLOR_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffset,
            program.directionVectorAttributeLocation,
            VECTOR_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += VECTOR_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffset,
            program.particleStartTimeAttributeLocation,
            PARTICLE_START_TIME_COMPONENT_COUNT,
            STRIDE
        )

    }

    fun draw() {
        GLES20.glDrawArrays(
            GLES20.GL_POINTS,
            0,
            currentParticleCount
        )
    }
}