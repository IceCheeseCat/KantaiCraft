package com.github.icecheesecat.kantaicraft.entityship.attribute.planeAttributes;

public class PlaneAttributes {

    private double flySpeed = 0;
    private double turnAcceleration = 0;
    private double antiAir = 0;
    private double torpedo = 0;
    private double los = 0;
    private double antiSubmarine = 0;
    private double bombing = 0;

    public double getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(double flySpeed) {
        this.flySpeed = flySpeed;
    }

    public double getTurnAcceleration() {
        return turnAcceleration;
    }

    public void setTurnAcceleration(double turnAcceleration) {
        this.turnAcceleration = turnAcceleration;
    }

    public double getAntiAir() {
        return antiAir;
    }

    public void setAntiAir(double antiAir) {
        this.antiAir = antiAir;
    }

    public double getTorpedo() {
        return torpedo;
    }

    public void setTorpedo(double torpedo) {
        this.torpedo = torpedo;
    }

    public double getLos() {
        return los;
    }

    public void setLos(double los) {
        this.los = los;
    }

    public double getAntiSubmarine() {
        return antiSubmarine;
    }

    public void setAntiSubmarine(double antiSubmarine) {
        this.antiSubmarine = antiSubmarine;
    }

    public double getBombing() {
        return bombing;
    }

    public void setBombing(double bombing) {
        this.bombing = bombing;
    }
}
