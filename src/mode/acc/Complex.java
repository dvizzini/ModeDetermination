package mode.acc;

public class Complex {
	public final double real;   // the real part
	public final double imaginary;   // the imaginary part

	// create a new object with the given real and imaginary parts
	public Complex(double realNum, double imagNum) {
		real = realNum;
		imaginary = imagNum;
	}

	// return a string representation of the invoking Complex object
	public String toString() {
		if (imaginary == 0) return real + "";
		if (real == 0) return imaginary + "i";
		if (imaginary <  0) return real + " - " + (-imaginary) + "i";
		return real + " + " + imaginary + "i";
	}

	// return abs/modulus/magnitude and angle/phase/argument
	public double abs()   { return Math.hypot(real, imaginary); }  // Math.sqrt(re*re + im*im)
	public double phase() { return Math.atan2(imaginary, real); }  // between -pi and pi

	// return a new Complex object whose value is (this + b)
	public Complex plus(Complex b) {
		Complex a = this;             // invoking object
		double real = a.real + b.real;
		double imag = a.imaginary + b.imaginary;
		return new Complex(real, imag);
	}

	// return a new Complex object whose value is (this - b)
	public Complex minus(Complex b) {
		Complex a = this;
		double real = a.real - b.real;
		double imag = a.imaginary - b.imaginary;
		return new Complex(real, imag);
	}

	// return a new Complex object whose value is (this * b)
	public Complex times(Complex b) {
		Complex a = this;
		double real = a.real * b.real - a.imaginary * b.imaginary;
		double imag = a.real * b.imaginary + a.imaginary * b.real;
		return new Complex(real, imag);
	}

	// scalar multiplication
	// return a new object whose value is (this * alpha)
	public Complex times(double alpha) {
		return new Complex(alpha * real, alpha * imaginary);
	}

	// return a new Complex object whose value is the conjugate of this
	public Complex conjugate() {  return new Complex(real, -imaginary); }

	// return a new Complex object whose value is the reciprocal of this
	public Complex reciprocal() {
		double scale = real*real + imaginary*imaginary;
		return new Complex(real / scale, -imaginary / scale);
	}

	// return the real or imaginary part
	public double re() { return real; }
	public double im() { return imaginary; }

	// return a / b
	public Complex divides(Complex b) {
		Complex a = this;
		return a.times(b.reciprocal());
	}

	// return a new Complex object whose value is the complex exponential of this
	public Complex exp() {
		return new Complex(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
	}

	// return a new Complex object whose value is the complex sine of this
	public Complex sin() {
		return new Complex(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
	}

	// return a new Complex object whose value is the complex cosine of this
	public Complex cos() {
		return new Complex(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
	}

	// return a new Complex object whose value is the complex tangent of this
	public Complex tan() {
		return sin().divides(cos());
	}

	// a static version of plus
	public static Complex plus(Complex a, Complex b) {
		double real = a.real + b.real;
		double imag = a.imaginary + b.imaginary;
		Complex sum = new Complex(real, imag);
		return sum;
	}

}
