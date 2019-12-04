package com.carlospinan.livewallpaper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.opengl.GLES20.*
import android.opengl.Matrix.*
import androidx.core.math.MathUtils
import com.carlospinan.livewallpaper.common.GLBaseRenderer
import com.carlospinan.livewallpaper.extensions.loadCubeMap
import com.carlospinan.livewallpaper.extensions.loadTexture
import com.carlospinan.livewallpaper.objects.Heightmap
import com.carlospinan.livewallpaper.objects.ParticleShooter
import com.carlospinan.livewallpaper.objects.ParticleSystem
import com.carlospinan.livewallpaper.objects.Skybox
import com.carlospinan.livewallpaper.programs.HeightmapShaderProgram
import com.carlospinan.livewallpaper.programs.ParticleShaderProgram
import com.carlospinan.livewallpaper.programs.SkyboxShaderProgram
import com.carlospinan.livewallpaper.utilities.Point
import com.carlospinan.livewallpaper.utilities.Vector
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


const val ANGLE_VARIANCE = 5f
const val SPEED_VARIANCE = 1f

class WallpaperRenderer(
    private val context: Context
) : GLBaseRenderer() {

    private val vectorToLight = floatArrayOf(0.30f, 0.35f, -0.89f, 0f)

    private val pointLightPositions = floatArrayOf(
        -1f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,
        1f, 1f, 0f, 1f
    )

    private val pointLightColors = floatArrayOf(
        1.00f, 0.20f, 0.02f,
        0.02f, 0.25f, 0.02f,
        0.02f, 0.20f, 1.00f
    )

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewMatrixForSkybox = FloatArray(16)
    private val projectionMatrix = FloatArray(16)

    private val tempMatrix = FloatArray(16)
    private val modelViewMatrix = FloatArray(16)
    private val itModelViewMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private lateinit var redParticleShooter: ParticleShooter
    private lateinit var greenParticleShooter: ParticleShooter
    private lateinit var blueParticleShooter: ParticleShooter

    private var globalStartTime = -1L
    private var particleTexture = -1
    private var skyboxTexture = -1

    private lateinit var skybox: Skybox
    private lateinit var skyboxProgram: SkyboxShaderProgram

    private lateinit var heightmap: Heightmap
    private lateinit var heightmapProgram: HeightmapShaderProgram

    private lateinit var particleProgram: ParticleShaderProgram
    private lateinit var particleSystem: ParticleSystem

    private var xRotation = 0f
    private var yRotation = 0f

    private var xOffset = 0f
    private var yOffset = 0f

    override fun onSurfaceCreated(unused: GL10?, eglConfig: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)

        heightmapProgram = HeightmapShaderProgram(context)
        heightmap = Heightmap(
            (context.resources.getDrawable(R.drawable.heightmap) as BitmapDrawable).bitmap
        )

        skyboxProgram = SkyboxShaderProgram(context)
        skybox = Skybox()

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

        particleTexture = context.loadTexture(R.drawable.particle_texture)

        skyboxTexture = context.loadCubeMap(
            intArrayOf(
                R.drawable.night_left, R.drawable.night_right,
                R.drawable.night_bottom, R.drawable.night_top,
                R.drawable.night_front, R.drawable.night_back
            )
        )

    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        perspectiveM(
            projectionMatrix,
            0,
            45f,
            width.toFloat() / height.toFloat(),
            1f,
            100f // 10f
        )

        updateViewMatrices()
    }

    override fun onDrawFrame(unused: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        drawHeightmap()
        drawSkyBox()
        drawParticles()
    }

    override fun handleTouchDrag(x: Float, y: Float) {
        xRotation += x / 50f
        yRotation += y / 50f

        xRotation = MathUtils.clamp(xRotation, -90f, 90f)
        yRotation = MathUtils.clamp(yRotation, -90f, 90f)

        // Setup view matrix
        updateViewMatrices()
    }

    private fun updateViewMatrices() {
        setIdentityM(viewMatrix, 0)

        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f)
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f)

        System.arraycopy(viewMatrix, 0, viewMatrixForSkybox, 0, viewMatrix.size)

        // We want the translation to apply to the regular view matrix, and not
        // the skybox.
        translateM(viewMatrix, 0, 0f - xOffset, -1.5f - yOffset, -5f)
    }

    private fun updateMvpMatrix() {
        multiplyMM(
            modelViewMatrix,
            0,
            viewMatrix,
            0,
            modelMatrix,
            0
        )

        invertM(
            tempMatrix,
            0,
            modelViewMatrix,
            0
        )

        transposeM(
            itModelViewMatrix,
            0,
            tempMatrix,
            0
        )

        multiplyMM(
            modelViewProjectionMatrix,
            0,
            projectionMatrix,
            0,
            modelViewMatrix,
            0
        )
    }

    private fun updateMvpMatrixForSkybox() {
        multiplyMM(tempMatrix, 0, viewMatrixForSkybox, 0, modelMatrix, 0)
        multiplyMM(
            modelViewProjectionMatrix,
            0,
            projectionMatrix,
            0,
            tempMatrix,
            0
        )
    }

    private fun drawHeightmap() {
        setIdentityM(modelMatrix, 0)

        // Expand the heightmap's dimensions, but don't expand the height as
        // much so that we don't get insanely tall mountains.
        // Expand the heightmap's dimensions, but don't expand the height as
        // much so that we don't get insanely tall mountains.
        scaleM(modelMatrix, 0, 100f, 10f, 100f)
        updateMvpMatrix()

        heightmapProgram.useProgram()
        // Put the light positions into eye space.
        val vectorToLightInEyeSpace = FloatArray(4)
        val pointPositionsInEyeSpace = FloatArray(12)
        multiplyMV(
            vectorToLightInEyeSpace,
            0,
            viewMatrix,
            0,
            vectorToLight,
            0
        )
        multiplyMV(
            pointPositionsInEyeSpace,
            0,
            viewMatrix,
            0,
            pointLightPositions,
            0
        )
        multiplyMV(
            pointPositionsInEyeSpace,
            4,
            viewMatrix,
            0,
            pointLightPositions,
            4
        )
        multiplyMV(
            pointPositionsInEyeSpace,
            8,
            viewMatrix,
            0,
            pointLightPositions,
            8
        )

        heightmapProgram.setUniforms(
            modelViewMatrix,
            itModelViewMatrix,
            modelViewProjectionMatrix,
            vectorToLightInEyeSpace,
            pointPositionsInEyeSpace,
            pointLightColors
        )
        heightmap.bindData(heightmapProgram)
        heightmap.draw()
    }

    private fun drawSkyBox() {
        setIdentityM(
            modelMatrix,
            0
        )
        updateMvpMatrixForSkybox()

        glDepthFunc(GL_LEQUAL) // This avoids problems with the skybox itself getting clipped.

        skyboxProgram.useProgram()
        skyboxProgram.setUniforms(modelViewProjectionMatrix, skyboxTexture)
        skybox.bindData(skyboxProgram)
        skybox.draw()

        glDepthFunc(GL_LESS)
    }

    private fun drawParticles() {
        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f

        redParticleShooter.addParticles(particleSystem, currentTime, 1)
        greenParticleShooter.addParticles(particleSystem, currentTime, 1)
        blueParticleShooter.addParticles(particleSystem, currentTime, 1)

        setIdentityM(modelMatrix, 0)
        updateMvpMatrix()

        glDepthMask(false)
        glEnable(GL_BLEND)
        glBlendFunc(GL_ONE, GL_ONE)

        particleProgram.useProgram()
        particleProgram.setUniforms(modelViewProjectionMatrix, currentTime, particleTexture)
        particleSystem.bindData(particleProgram)
        particleSystem.draw()

        glDisable(GL_BLEND)
        glDepthMask(true)
    }

    fun handleOffsetsChanged(xOffset: Float, yOffset: Float) {
        // Offsets range from 0 to 1.
        this.xOffset = (xOffset - 0.5f) * 2.5f
        this.yOffset = (yOffset - 0.5f) * 2.5f
        updateViewMatrices()
    }

}