package io.github.bi0qaw.biosphere.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class VectorLib {

	public static final double SQRT2 = Math.sqrt(2);

	public static Vector[] getLine(double length, double density) {
		int points = (int) (length * density) + 1;
		double delta = length / (double) points;
		Vector[] vectors = new Vector[points];
		for (int i = 0; i < points; i++) {
			vectors[i] = new Vector(i*delta, 0, 0);
		}
		return vectors;
	}

	public static Vector[] getLine(Vector vector1, Vector vector2, double density) {
		double length = vector1.distance(vector2);
		int points = (int) (length * density) + 1;
		Vector delta = vector2.clone().subtract(vector1).multiply(1.0 / (double) points);
		Vector[] vectors = new Vector[points];
		for (int i=0; i < points; i++) {
			vectors[i] = delta.clone().multiply(i).add(vector1);
		}
		return vectors;
	}

	public static Vector[] linkLine(Vector v1, Vector v2, double density) {
		int points = (int) (v1.distance(v2) * density) + 1;
		Vector delta = v2.clone().subtract(v1).multiply(1D / (double) points);
		Vector[] vectors = new Vector[points];
		vectors[0] = v1.clone();
		for (int i=1; i<points; i++) {
			vectors[i] = delta.clone().multiply(i).add(v1);
		}
		return vectors;
	}

	public static Vector[] getCircle(double radius, double density) {
		int n = (int) (radius * 2 * Math.PI * density);
		return getPolygon(n, radius);
	}

	public static Vector[] getPolygon(int points, double radius) {
		float deltaAngle = 360 / (float) points;
		Vector[] vectors = new Vector[points];
		for (int i = 0; i < points; i++) {
			vectors[i] = VectorMath.fromCylindricalCoordinates(radius, i * deltaAngle, 0);
		}
		return vectors;
	}

	public static Vector[] getPolygonOutline(int points, double radius, double density) {
		Vector[] polygon = getPolygon(points, radius);
		List<Vector> vectors = new ArrayList<Vector>();
		for (int i=0; i < points - 1; i++) {
			vectors.addAll(Arrays.asList(linkLine(polygon[i], polygon[i+1], density)));
		}
		vectors.addAll(Arrays.asList(linkLine(polygon[polygon.length - 1], polygon[0], density)));
		return vectors.toArray(new Vector[vectors.size()]);
	}

	public static Vector[] getCube(double radius) {
		Vector[] vectors = new Vector[8];
		double d = radius / SQRT2;
		vectors[0] = new Vector(-d, -d, -d);
		vectors[1] = new Vector(-d, -d, d);
		vectors[2] = new Vector(-d, d, -d);
		vectors[3] = new Vector(-d, d, d);
		vectors[4] = new Vector(d, -d, -d);
		vectors[5] = new Vector(d, -d, d);
		vectors[6] = new Vector(d, d, -d);
		vectors[7] = new Vector(d, d, d);
		return vectors;
	}

	public static Vector[] getCubeOutline(double radius, double density) {
		double off = radius/SQRT2;
		int points = (int) (radius * density) + 1;
		double d = 2*off/points;
		Vector[] vectors = new Vector[points * 12];
		for (int i=0; i < points; i++) {
			vectors[i + 0*points] = new Vector(i*d-off, -off, -off);
			vectors[i + 1*points] = new Vector(i*d-off, off, -off);
			vectors[i + 2*points] = new Vector(i*d-off, -off, off);
			vectors[i + 3*points] = new Vector(i*d-off, off, off);
			vectors[i + 4*points] = new Vector(-off, i*d-off, -off);
			vectors[i + 5*points] = new Vector(off, i*d-off, -off);
			vectors[i + 6*points] = new Vector(-off, i*d-off, off);
			vectors[i + 7*points] = new Vector(off, i*d-off, off);
			vectors[i + 8*points] = new Vector(-off, -off, i*d-off);
			vectors[i + 9*points] = new Vector(off, -off, i*d-off);
			vectors[i + 10*points] = new Vector(-off, off, i*d-off);
			vectors[i + 11*points] = new Vector(off, off, i*d-off);
		}
		return vectors;
	}

	public static Vector[] getHelix(double radius, double height, double step, double density) {
		int points = (int) ( height * 2 * Math.PI * radius * density);
		double deltaAngle = 360 * height / (step * points);
		double deltaHeight = height / points;
		Vector[] vectors = new Vector[points];
		for(int i=0; i < points; i++){
			vectors[i] = VectorMath.fromCylindricalCoordinates(radius, deltaAngle * i, deltaHeight * i);
		}
		return vectors;
	}

	public static Vector[] linkAll(Vector[] vectors, double density) {
		List<Vector> vecs = new ArrayList<Vector>();
		for (Vector v1 : vectors) {
			for (Vector v2 : vectors) {
				if (!v1.equals(v2)) {
					vecs.addAll(Arrays.asList(linkLine(v1, v2, density)));
				}
			}
		}
		return vecs.toArray(new Vector[vecs.size()]);
	}

	public static Vector midpoint(Vector[] vectors) {
		Vector midpoint = new Vector();
		double factor = 1D/vectors.length;
		for (Vector vec: vectors) {
			VectorMath.addMul(midpoint, vec, factor);
		}
		return midpoint;
	}

	public static Vector[] offset(Vector[] vectors, Vector offset) {
		for (Vector v: vectors) {
			v.add(offset);
		}
		return vectors;
	}

	public static Vector[] pointReflection(Vector[] vectors, Vector center) {
		for (Vector v: vectors) {
			v.subtract(center).multiply(-1).add(center);
		}
		return vectors;
	}

	public static Vector[] reflection(Vector[] vectors, Vector center, Vector direction) {
		for(Vector v: vectors){
			v.subtract(center).multiply(-1).multiply(direction).add(center);
		}
		return vectors;
	}

	public static Vector[] rotate(Vector[] vectors, Vector axis, double angle) {
		for (Vector v: vectors) {
			v = VectorMath.rot(v, axis, angle);
		}
		return  vectors;
	}

	public static Vector[] rotateX(Vector[] vectors, double angle) {
		for (Vector v: vectors) {
			v = VectorMath.rotX(v, angle);
		}
		return vectors;
	}

	public static Vector[] rotateY(Vector[] vectors, double angle) {
		for (Vector v: vectors) {
			v = VectorMath.rotY(v, angle);
		}
		return vectors;
	}

	public static Vector[] rotateZ(Vector[] vectors, double angle) {
		for (Vector v: vectors) {
			v = VectorMath.rotZ(v, angle);
		}
		return vectors;
	}

	public static Vector[] scale(Vector[] vectors, double factor) {
		for (Vector v: vectors) {
			v.multiply(factor);
		}
		return vectors;
	}

	public static Vector[] scaleDirectional(Vector[] vectors, Vector direction, double factor) {
		for (Vector v: vectors) {
			v.multiply(direction);
			v.multiply(factor);
		}
		return vectors;
	}

	public static Vector[] getSphere(double radius, double density){
		int points = (int) (Math.PI * radius * density);
		Vector[] vectors = new Vector[points * points];
		double deltaYaw = 360D / points;
		double deltaPitch = 180D / points;
		double yaw = 0;
		double pitch = 0;
		for (int i=0; i<points; i++) {
			for (int j=0; j<points; j++) {
				vectors[j+i*points] = VectorMath.fromSphericalCoordinates(radius, yaw, pitch);
				pitch += deltaPitch;
			}
			yaw += deltaYaw;
			pitch = 0;
		}
		return vectors;
	}

	public static Vector[] getRandomSphere(double radius, double density) {
		int points = (int) (Math.PI * radius * density);
		Vector[] vectors = new Vector[points * points];
		Random rand = new Random();
		double yaw, pitch;
		for (int i=0; i < points * points; i++) {
			yaw = rand.nextDouble() * 360;
			pitch = rand.nextDouble() * 180;
			vectors[i] = VectorMath.fromSphericalCoordinates(radius, yaw, pitch);
		}
		return vectors;
	}

	public static Vector[] toVector(Location[] locations) {
		Vector[] vectors = new Vector[locations.length];
		int i = 0;
		for (Location l: locations) {
			vectors[i] = l.toVector();
			i++;
		}
		return vectors;
	}
}