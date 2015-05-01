
final class Mmult{
	public Matrix mult(int rec, Matrix a, Matrix b, Matrix c) {
            c.recMatMul(rec, a, b);
            return c;
	}

	
    public static int power(int base, int exponent) {
        return (int) Math.pow(base, exponent);
    }

    public static void main(String args[]) {
       
    	
    	int rec = 2, loop = power(2,2);
       
    	long start, end;
        double time;
        
        Mmult m = new Mmult();
        if (args.length == 2) {
            rec = Integer.parseInt(args[0]);
            loop = Integer.parseInt(args[1]);
        } else if (args.length != 0) {
        	System.out.println("usage: mmult [rec loop]");
            System.exit(66);
        }

        int cells = power(2,rec) * loop;
        System.out.println("Running Matrix multiply, on a matrix of size "
                + cells + " x " + cells + ", threads = " + power(8, 0/*task*/));

        Matrix a = new Matrix( rec, loop, 1.0f, false); // a is row-wise flipped, to make product zero
        Matrix b = new Matrix(rec, loop, 1.0f, false);
        Matrix c = new Matrix(rec, loop, 0.0f, false);
        start = System.currentTimeMillis();
        c =m.mult(rec, a, b, c);
        
        end = System.currentTimeMillis();
        time = (double) end - start;
        time /= 1000.0; // seconds.

        System.out.println("checking result, should be " + ((float) cells));
        if (c.check(rec, (float) cells)) {
            System.out.println("application time Mmult (" + 1+ "," + rec
                    + "," + loop + "," + cells + ") took " + time + " s");
            System.out.println("application result Mmult (" + 1 + "," + rec
                    + "," + loop + "," + cells + ") = OK");
            System.out.println("Test succeeded!");
        } else {
            System.out.println("application time Mmult (" + 1 + "," + rec
                    + "," + loop + "," + cells + ") GAVE WRONG RESULT!");
            System.out.println("application result Mmult (" + 1+ "," + rec
                    + "," + loop + "," + cells + ") GAVE WRONG RESULT!");
            System.out.println("Test failed!");
            System.exit(1);
        }
    }
}