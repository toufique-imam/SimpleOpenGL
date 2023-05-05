package com.retroapp.airhockey.airhockey.util

import kotlin.math.sqrt


class Geometry {
    companion object {
        class Point(val x: Float, val y: Float, val z: Float) {
            fun translateY(distance: Float): Point {
                return Point(x, y + distance, z)
            }

            fun translate(vector: Vector): Point {
                return Point(x + vector.x, y + vector.y, z + vector.z)
            }
        }

        class Vector(val x: Float, val y: Float, val z: Float) {
            fun crossProduct(other: Vector): Vector {
                return Vector(
                    (y * other.z) - (z * other.y),
                    (z * other.x) - (x * other.z),
                    (x * other.y) - (y * other.x)
                )
            }

            fun dotProduct(other: Vector): Float {
                return x * other.x + y * other.y + z * other.z
            }

            fun length(): Float {
                return sqrt(x * x + y * y + z * z)
            }

            fun scale(scale: Float): Vector {
                return Vector(x * scale, y * scale, z * scale)
            }
        }

        class Circle(val center: Point, val radius: Float) {
            fun scale(scale: Float): Circle {
                return Circle(center, radius * scale)
            }
        }

        class Plane(val point: Point, val normal: Vector)

        class Cylinder(val center: Point, val radius: Float, val height: Float)


        class Ray(
            val point: Point,
            val vector: Vector
        )

        class Sphere(val center: Point, val radius: Float)


        fun vectorBetween(from: Point, to: Point): Vector {
            return Vector(to.x - from.x, to.y - from.y, to.z - from.z)
        }

        private fun distanceBetween(point: Point, ray: Ray): Float {
            val p1ToPoint = vectorBetween(ray.point, point)
            val p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point)

            val areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length()
            val lengthOfBase = ray.vector.length()
            return areaOfTriangleTimesTwo / lengthOfBase
        }

        fun intersectionPoint(ray: Ray, plane: Plane): Point {
            val rayToPlaneVector = vectorBetween(ray.point, plane.point)
            val scaleFactor =
                rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)
            return ray.point.translate(ray.vector.scale(scaleFactor))
        }

        fun intersects(sphere: Sphere, ray: Ray): Boolean {
            return distanceBetween(sphere.center, ray) < sphere.radius
        }
    }
}