public class Ray {

    // Variables
    private Vector position;
    /**
     * Angle in radians (cartesian coordinates)
     */
    private double angle;
    private double strength;


    // Constructors
    public Ray(Vector position, double angle, double strength) {
        this.position = position;
        this.angle = angle;
        this.strength = strength;
    }

    public Ray(double x, double y, double angle, double strength) {
        this(new Vector(x,y), angle, strength);
    }

    public Ray() {
        this(0,0,0,0);
    }



    // Setters
    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setStrength(double strength) {
        if (strength < 0 || strength > 1) throw new RuntimeException("Strength needs to be between 0 and 1");
        this.strength = strength;
    }



    // Getters
    public Vector getPosition() {
        return position;
    }

    public double getAngle() {
        return angle;
    }

    public double getStrength() {
        return strength;
    }



    // Methods
    public void lookAt(Vector v) {
        this.angle = Vector.sub(v, this.position).heading();
    }
}
