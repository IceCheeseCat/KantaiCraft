package com.github.icecheesecat.kantaicraft.common;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BoundingBoxAlgorithm {

    public static boolean rayIntersection(AABB box, Vec3 p0, Vec3 ray) {

        double dirfra_x =  1.0f / ray.x();
        double dirfra_y =  1.0f / ray.y();
        double dirfra_z =  1.0f / ray.z();


        // lb is the corner of AABB with minimal coordinates - left bottom, rt is maximal corner
        // r.org is origin of ray
        double t1 = (box.minX - p0.x()) * dirfra_x;
        double t2 = (box.maxX - p0.x()) * dirfra_x;
        double t3 = (box.minY - p0.y()) * dirfra_y;
        double t4 = (box.maxY - p0.y()) * dirfra_y;
        double t5 = (box.minZ - p0.z()) * dirfra_z;
        double t6 = (box.maxZ - p0.z()) * dirfra_z;


        double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        // if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0)
        {
            return false;
        }
        // if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax)
        {
            return false;
        }
        return true;

    }

}
