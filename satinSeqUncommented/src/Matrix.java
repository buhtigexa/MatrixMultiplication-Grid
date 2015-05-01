
final class Matrix extends Leaf implements java.io.Serializable {
    Matrix _00, _01, _10, _11;

    
    public Matrix(int rec, int loop, float dbl, boolean flipped) {
        super(rec, loop, dbl, flipped); // construct Leaf

        if (rec <= 0)
            return;
            rec--;

        _00 = new Matrix(rec, loop, dbl, flipped);
        _01 = new Matrix(rec, loop, (flipped ? -dbl : dbl), flipped);
        _10 = new Matrix(rec, loop, dbl, flipped);
        _11 = new Matrix(rec, loop, (flipped ? -dbl : dbl), flipped);
    }

    public float sum( int rec) {
        float s = 0.0f;
        if (rec > 0) {
                rec--;
            

            s += _00.sum(rec);
            s += _01.sum(rec);
            s += _10.sum(rec);
            s += _11.sum( rec);
        } else {
            s = super.sum();
        }
        return s;
    }

    public void print(/*int task,*/ int rec) {
        if (rec > 0) {
                rec--;
            

            _00.print(rec);
            _01.print(rec);
            _10.print(rec);
            _11.print(rec);
        } else {
            super.print();
        }
    }

    public boolean check(int rec, float result) {
        boolean ok = true;
        
        if (rec > 0) {
                rec--;
           
            ok &= _00.check( rec, result);
            ok &= _01.check(rec, result);
            ok &= _10.check( rec, result);
            ok &= _11.check( rec, result);
        } else {
            ok &= super.check(result);
        }

        return ok;
    }

    public void recMatMul(int depth, Matrix a, Matrix b) {
        if (depth == 0) {
            // pass Matrices as local variables 
            // loopMatMul(a, b);
            multiplyStride2(a, b);
        } else {
            // depth-1 stuff should be faster than creating a new Size thing
            // each time
            _00.recMatMul(depth - 1, a._00, b._00);
            _01.recMatMul(depth - 1, a._00, b._01);
            _10.recMatMul(depth - 1, a._10, b._00);
            _11.recMatMul(depth - 1, a._10, b._01);

            _00.recMatMul(depth - 1, a._01, b._10);
            _01.recMatMul(depth - 1, a._01, b._11);
            _10.recMatMul(depth - 1, a._11, b._10);
            _11.recMatMul(depth - 1, a._11, b._11);
        }
    }
}