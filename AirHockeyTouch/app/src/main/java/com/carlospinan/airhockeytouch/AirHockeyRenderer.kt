package com.carlospinan.airhockeytouch

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.Matrix
import androidx.core.math.MathUtils
import com.carlospinan.airhockeytouch.common.GLBaseRenderer
import com.carlospinan.airhockeytouch.extensions.loadTexture
import com.carlospinan.airhockeytouch.objects.Mallet
import com.carlospinan.airhockeytouch.objects.Puck
import com.carlospinan.airhockeytouch.objects.Table
import com.carlospinan.airhockeytouch.programs.ColorShaderProgram
import com.carlospinan.airhockeytouch.programs.TextureShaderProgram
import com.carlospinan.airhockeytouch.utilities.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val LEFT_BOUND = -0.5f
private const val RIGHT_BOUND = 0.5f
private const val FAR_BOUND = -0.8f
private const val NEAR_BOUND = 0.8f

class AirHockeyRenderer(private val context: Context) : GLBaseRenderer() {

    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram

    private lateinit var puck: Puck
    private lateinit var table: Table
    private lateinit var mallet: Mallet

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private val invertedViewProjectionMatrix = FloatArray(16)

    private var texture = -1
    private var malletPressed = false

    private lateinit var previousBlueMalletPosition: Point
    private lateinit var blueMalletPosition: Point

    private lateinit var puckPosition: Point
    private lateinit var puckVector: Vector

    override fun onSurfaceCreated(unused: GL10?, eglConfig: EGLConfig?) {
        // Set the background clear color to red. The first component is
        // red, the second is green, the third is blue, and the last
        // component is alpha, which we don't use in this lesson.
        OpenGLES20.gl2ClearColor(
            red = 0.0f,
            green = 0.0f,
            blue = 0.0f,
            alpha = 0.0f
        )

        table = Table()
        mallet = Mallet(0.08f, 0.15f, 32)
        puck = Puck(0.06f, 0.02f, 32)

        blueMalletPosition = Point(0f, mallet.height / 2f, 0.4f)
        previousBlueMalletPosition = blueMalletPosition

        puckPosition = Point(0f, puck.height / 2f, 0f)
        puckVector = Vector.zero()

        textureProgram =
            TextureShaderProgram(
                context
            )
        colorProgram =
            ColorShaderProgram(
                context
            )

        texture = context.loadTexture(R.drawable.air_hockey_surface)

    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        OpenGLES20.gl2ViewPort(0, 0, width, height)

        Matrix.perspectiveM(
            projectionMatrix,
            0,
            45.0f,
            width.toFloat() / height.toFloat(),
            1.0f,
            10.0f
        )

        Matrix.setLookAtM(
            viewMatrix,
            0,
            0.0f,
            1.2f,
            2.2f,
            0f,
            0f,
            0f,
            0f,
            1f,
            0f
        )
    }

    override fun onDrawFrame(unused: GL10?) {
        puckPosition = puckPosition.translate(puckVector)
        if (puckPosition.x < LEFT_BOUND + puck.radius || puckPosition.x > RIGHT_BOUND - puck.radius) {
            puckVector = Vector(-puckVector.x, puckVector.y, puckVector.z)
            puckVector = puckVector.scale(0.9f)
        }
        if (puckPosition.z < FAR_BOUND + puck.radius || puckPosition.z > NEAR_BOUND - puck.radius) {
            puckVector = Vector(puckVector.x, puckVector.y, -puckVector.z)
            puckVector = puckVector.scale(0.9f)
        }
        // Clamp the puck position.
        puckPosition = Point(
            MathUtils.clamp(
                puckPosition.x,
                LEFT_BOUND + puck.radius,
                RIGHT_BOUND - puck.radius
            ),
            puckPosition.y,
            MathUtils.clamp(
                puckPosition.z,
                FAR_BOUND + puck.radius,
                NEAR_BOUND - puck.radius
            )
        )
        puckVector = puckVector.scale(0.99f)

        // Clear the rendering surface.
        OpenGLES20.gl2Clear(GL_COLOR_BUFFER_BIT)

        Matrix.multiplyMM(
            viewProjectionMatrix,
            0,
            projectionMatrix,
            0,
            viewMatrix,
            0
        )

        Matrix.invertM(
            invertedViewProjectionMatrix,
            0,
            viewProjectionMatrix,
            0
        )

        // Draw the table.
        positionTableInScene()
        textureProgram.useProgram()
        textureProgram.setUniforms(modelViewProjectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        // Draw the mallets.
        positionObjectInScene(0f, mallet.height / 2f, -0.4f)
        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorProgram)
        mallet.draw()

        positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)

        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mallet.draw()

        // Draw the puck.
        positionObjectInScene(puckPosition.x, puckPosition.y, puckPosition.z)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorProgram)
        puck.draw()

    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(
            modelMatrix,
            0,
            x,
            y,
            z
        )
        Matrix.multiplyMM(
            modelViewProjectionMatrix,
            0,
            viewProjectionMatrix,
            0,
            modelMatrix,
            0
        )
    }

    private fun positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(
            modelMatrix,
            0,
            -90f,
            1f,
            0f,
            0f
        )
        Matrix.multiplyMM(
            modelViewProjectionMatrix,
            0,
            viewProjectionMatrix,
            0,
            modelMatrix,
            0
        )
    }

    override fun handleTouchPress(touchX: Float, touchY: Float) {
        log("Touch Press -> $touchX ; $touchY")
        val ray = convertNormalized2DPointToRay(touchX, touchY)

        // Now test if this ray intersects with the mallet by creating a
        // bounding sphere that wraps the mallet.
        val malletBoundingSphere = Sphere(
            Point.from(blueMalletPosition),
            mallet.height / 2f
        )

        // If the ray intersects (if the user touched a part of the screen that
        // intersects the mallet's bounding sphere), then set malletPressed =
        // true.

        malletPressed = Geometry.intersects(malletBoundingSphere, ray)
    }

    override fun handleTouchDrag(touchX: Float, touchY: Float) {
        // log("Touch Drag -> $touchX ; $touchY")
        if (malletPressed) {
            previousBlueMalletPosition = blueMalletPosition

            val ray = convertNormalized2DPointToRay(touchX, touchY)
            // Define a plane representing our air hockey table.
            val plane = Plane(
                Point.zero(),
                Vector(0f, 1f, 0f)
            )
            // Find out where the touched point intersects the plane
            // representing our table. We'll move the mallet along this plane.
            val touchedPoint = Geometry.intersectionPoint(ray, plane)
            blueMalletPosition = Point(
                MathUtils.clamp(
                    touchedPoint.x,
                    LEFT_BOUND + mallet.radius,
                    RIGHT_BOUND - mallet.radius
                ),
                mallet.height / 2f,
                MathUtils.clamp(
                    touchedPoint.z,
                    mallet.radius,
                    NEAR_BOUND - mallet.radius
                )
            )

            val distance = Geometry.vectorBetween(blueMalletPosition, puckPosition).length()

            if (distance < (puck.radius + mallet.radius)) {
                // The mallet has struck the puck. Now send the puck flying
                // based on the mallet velocity.
                puckVector = Geometry.vectorBetween(
                    previousBlueMalletPosition,
                    blueMalletPosition
                )
            }

        }

    }

    private fun convertNormalized2DPointToRay(touchX: Float, touchY: Float): Ray {
        // We'll convert these normalized device coordinates into world-space
        // coordinates. We'll pick a point on the near and far planes, and draw a
        // line between them. To do this transform, we need to first multiply by
        // the inverse matrix, and then we need to undo the perspective divide.
        val nearPointNdc = floatArrayOf(touchX, touchY, -1f, 1f)
        val farPointNdc = floatArrayOf(touchX, touchY, 1f, 1f)

        val nearPointWorld = FloatArray(4)
        val farPointWorld = FloatArray(4)

        Matrix.multiplyMV(
            nearPointWorld,
            0,
            invertedViewProjectionMatrix,
            0,
            nearPointNdc,
            0
        )

        Matrix.multiplyMV(
            farPointWorld,
            0,
            invertedViewProjectionMatrix,
            0,
            farPointNdc,
            0
        )

        divideByW(nearPointWorld)
        divideByW(farPointWorld)

        val nearPointRay = Point(
            nearPointWorld[0],
            nearPointWorld[1],
            nearPointWorld[2]
        )

        val farPointRay = Point(
            farPointWorld[0],
            farPointWorld[1],
            farPointWorld[2]
        )

        return Ray(
            nearPointRay,
            Geometry.vectorBetween(nearPointRay, farPointRay)
        )

    }

    private fun divideByW(vector: FloatArray) {
        vector[0] /= vector[3]
        vector[1] /= vector[3]
        vector[2] /= vector[3]
    }

}