/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package toolbox;

import java.nio.FloatBuffer;

/**
 * A 4 dimensional matrix of floats.
 */
public class Matrix4f
{
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    /**
     * Constructs a new identity matrix.
     */
    public Matrix4f()
    {
        setIdentity();
    }

    public static Vector3f mul(Matrix4f left, Vector4f right) {
        Vector3f dest = new Vector3f();

        dest.x = right.x*left.m00 + right.y*left.m01 + right.z*left.m02 + right.w*left.m03;
        dest.y = right.x*left.m10 + right.y*left.m11 + right.z*left.m12 + right.w*left.m13;
        dest.z = right.x*left.m20 + right.y*left.m21 + right.z*left.m22 + right.w*left.m23;

        return dest;
    }


    /**
     * Stores the matrix in a float buffer.
     *
     * @param buf Where the matrix will be stored.
     */
    public void store(FloatBuffer buf)
    {
        buf.put(m00);
        buf.put(m01);
        buf.put(m02);
        buf.put(m03);
        buf.put(m10);
        buf.put(m11);
        buf.put(m12);
        buf.put(m13);
        buf.put(m20);
        buf.put(m21);
        buf.put(m22);
        buf.put(m23);
        buf.put(m30);
        buf.put(m31);
        buf.put(m32);
        buf.put(m33);
    }

    /**
     * Set the matrix to the identity matrix, ie a default matrix.
     */
    public void setIdentity()
    {
        // diagonal 1s
        m00 = 1f;
        m11 = 1f;
        m22 = 1f;
        m33 = 1f;

        // otherwise 0
        m01 = 0f;
        m02 = 0f;
        m03 = 0f;
        m10 = 0f;
        m12 = 0f;
        m13 = 0f;
        m20 = 0f;
        m21 = 0f;
        m23 = 0f;
        m30 = 0f;
        m31 = 0f;
        m32 = 0f;
    }

    /**
     * Translate a matrix by a direction given by a vector.
     *
     * @param translation Direction of translation.
     * @param src The matrix being translated.
     * @param dest The matrix after translation.
     */
    public static void translate(Vector3f translation, Matrix4f src, Matrix4f dest)
    {
        dest.m30 += src.m00 * translation.x + src.m10 * translation.y + src.m20 * translation.z;
        dest.m31 += src.m01 * translation.x + src.m11 * translation.y + src.m21 * translation.z;
        dest.m32 += src.m02 * translation.x + src.m12 * translation.y + src.m22 * translation.z;
        dest.m33 += src.m03 * translation.x + src.m13 * translation.y + src.m23 * translation.z;
    }

    /**
     * Rotate a matrix along a given axis.
     *
     * @param angle Radian of rotation.
     * @param axis Axis along which the rotation occurs as a unit vector.
     * @param src The matrix being rotated.
     * @param dest The matrix after rotation.
     */
    public static void rotate(float angle, Vector3f axis, Matrix4f src, Matrix4f dest)
    {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float oneminusc = 1.0f - c;
        float xy = axis.x*axis.y;
        float yz = axis.y*axis.z;
        float xz = axis.x*axis.z;
        float xs = axis.x*s;
        float ys = axis.y*s;
        float zs = axis.z*s;

        float f00 = axis.x*axis.x*oneminusc+c;
        float f01 = xy*oneminusc+zs;
        float f02 = xz*oneminusc-ys;
        // n[3] not used
        float f10 = xy*oneminusc-zs;
        float f11 = axis.y*axis.y*oneminusc+c;
        float f12 = yz*oneminusc+xs;
        // n[7] not used
        float f20 = xz*oneminusc+ys;
        float f21 = yz*oneminusc-xs;
        float f22 = axis.z*axis.z*oneminusc+c;

        float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
        float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
        float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
        float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
        float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
        float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
        float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
        float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
        dest.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
        dest.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
        dest.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
        dest.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
        dest.m00 = t00;
        dest.m01 = t01;
        dest.m02 = t02;
        dest.m03 = t03;
        dest.m10 = t10;
        dest.m11 = t11;
        dest.m12 = t12;
        dest.m13 = t13;
    }

    /**
     * Scale a matrix by a given amount.
     *
     * @param vec Scale of size change.
     * @param src The matrix being scaled.
     * @param dest The matrix after being scaled.
     */
    public static void scale(Vector3f vec, Matrix4f src, Matrix4f dest)
    {
        dest.m00 = src.m00 * vec.x;
        dest.m01 = src.m01 * vec.x;
        dest.m02 = src.m02 * vec.x;
        dest.m03 = src.m03 * vec.x;
        dest.m10 = src.m10 * vec.y;
        dest.m11 = src.m11 * vec.y;
        dest.m12 = src.m12 * vec.y;
        dest.m13 = src.m13 * vec.y;
        dest.m20 = src.m20 * vec.z;
        dest.m21 = src.m21 * vec.z;
        dest.m22 = src.m22 * vec.z;
        dest.m23 = src.m23 * vec.z;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up)
    {

        Vector3f f = Vector3f.normalize(Vector3f.subtract(center, eye));
        Vector3f u = Vector3f.normalize(up);
        Vector3f s = Vector3f.normalize(Vector3f.cross(f, u));
        u = Vector3f.cross(s, f);

        Matrix4f result = new Matrix4f();
        result.m00 = s.x;
        result.m10 = s.y;
        result.m20 = s.z;
        result.m01 = u.x;
        result.m11 = u.y;
        result.m21 = u.z;
        result.m02 = -f.x;
        result.m12 = -f.y;
        result.m22 = -f.z;

        translate(new Vector3f(-eye.x,-eye.y,-eye.z), result, result);
        return result;
    }

    /**
     * Invert the source matrix and put the result in the destination
     * @param src The source matrix
     * @param dest The destination matrix, or null if a new matrix is to be created
     * @return The inverted matrix if successful, null otherwise
     */
    public static Matrix4f invert(Matrix4f src, Matrix4f dest) {
        float determinant = src.determinant();

        if (determinant != 0) {
            /*
             * m00 m01 m02 m03
             * m10 m11 m12 m13
             * m20 m21 m22 m23
             * m30 m31 m32 m33
             */
            if (dest == null)
                dest = new Matrix4f();
            float determinant_inv = 1f/determinant;

            // first row
            float t00 =  determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
            float t01 = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
            float t02 =  determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
            float t03 = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
            // second row
            float t10 = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
            float t11 =  determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
            float t12 = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
            float t13 =  determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
            // third row
            float t20 =  determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
            float t21 = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
            float t22 =  determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
            float t23 = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
            // fourth row
            float t30 = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
            float t31 =  determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
            float t32 = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
            float t33 =  determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);

            // transpose and divide by the determinant
            dest.m00 = t00*determinant_inv;
            dest.m11 = t11*determinant_inv;
            dest.m22 = t22*determinant_inv;
            dest.m33 = t33*determinant_inv;
            dest.m01 = t10*determinant_inv;
            dest.m10 = t01*determinant_inv;
            dest.m20 = t02*determinant_inv;
            dest.m02 = t20*determinant_inv;
            dest.m12 = t21*determinant_inv;
            dest.m21 = t12*determinant_inv;
            dest.m03 = t30*determinant_inv;
            dest.m30 = t03*determinant_inv;
            dest.m13 = t31*determinant_inv;
            dest.m31 = t13*determinant_inv;
            dest.m32 = t23*determinant_inv;
            dest.m23 = t32*determinant_inv;
            return dest;
        } else
            return null;
    }


    /**
     * @return the determinant of the matrix
     */
    public float determinant() {
        float f =
                m00
                        * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
                        - m13 * m22 * m31
                        - m11 * m23 * m32
                        - m12 * m21 * m33);
        f -= m01
                * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
                - m13 * m22 * m30
                - m10 * m23 * m32
                - m12 * m20 * m33);
        f += m02
                * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
                - m13 * m21 * m30
                - m10 * m23 * m31
                - m11 * m20 * m33);
        f -= m03
                * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
                - m12 * m21 * m30
                - m10 * m22 * m31
                - m11 * m20 * m32);
        return f;
    }

    /**
     * Calculate the determinant of a 3x3 matrix
     * @return result
     */

    private static float determinant3x3(float t00, float t01, float t02,
                                        float t10, float t11, float t12,
                                        float t20, float t21, float t22)
    {
        return   t00 * (t11 * t22 - t12 * t21)
                + t01 * (t12 * t20 - t10 * t22)
                + t02 * (t10 * t21 - t11 * t20);
    }

    /**
     * Transform a Vector by a matrix and return the result in a destination
     * vector.
     * @param left The left matrix
     * @param right The right vector
     * @param dest The destination vector, or null if a new one is to be created
     * @return the destination vector
     */
    public static Vector4f transform(Matrix4f left, Vector4f right, Vector4f dest) {
        if (dest == null)
            dest = new Vector4f();

        float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w;
        float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w;
        float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w;
        float w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w;

        dest.x = x;
        dest.y = y;
        dest.z = z;
        dest.w = w;

        return dest;
    }
}
