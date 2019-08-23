public class Vector {

    // Variables
    public double x, y;



    // Constructors
    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector v) {
        this(v.x, v.y);
    }

    public Vector() {
        this(0,0);
    }



    // Methods
    public Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector set(Vector v) {
        this.set(v.x, v.y);
        return this;
    }

    public Vector add(Vector v) {
        this.x += v.x;
        this.y += v.y;

        return this;
    }

    public Vector sub(Vector v) {
        this.x -= v.x;
        this.y -= v.y;

        return this;
    }

    public Vector mult(double val) {
        this.x *= val;
        this.y *= val;

        return this;
    }

    public Vector div(double val) {
        this.x /= val;
        this.y /= val;

        return this;
    }

    public double mag() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector normalize() {
        this.div(this.mag());
        return this;
    }

    public double heading() {
        return Math.atan2(-y,x);
    }

    public Vector rotate(double angle) {
        this.set(Vector.fromHeading(this.heading() + angle).mult(this.mag()));
        return this;
    }



    // Static methods
    public static Vector add(Vector v, Vector u) {
        return new Vector(v).add(u);
    }

    public static Vector sub(Vector v, Vector u) {
        return new Vector(v).sub(u);
    }

    public static Vector fromHeading(double heading) {
        return new Vector(Math.cos(heading), -Math.sin(heading));
    }
}
