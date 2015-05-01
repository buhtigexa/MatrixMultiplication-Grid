import org.gridgain.grid.GridConfigurationAdapter;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridTaskFuture;
import org.gridgain.grid.marshaller.optimized.GridOptimizedMarshaller;
import org.gridgain.grid.typedef.G;

/**
 * UNICEN
 * Parallel and Distributed Computing
 * Matrix Multiplication.
 *
 * @author Marcelo Rodriguez
 *
 */

public class Main {

	protected Matrix a,b,c;
	protected Matrix[] matrices;
	
	public static int power(int base, int exponent) {
        return (int) Math.pow(base, exponent);
    }

	
	public static void main(String[] args) throws GridException {
	    
		    int task = 2, rec = 5, loop = power(2,5);
	        long start, end;
	        double time;
	        Main m = new Main();
	        
	        Matrix[] matrices=new Matrix[3];
	    		
	        if (args.length == 3) {
	            task = Integer.parseInt(args[0]);
	            rec = Integer.parseInt(args[1]);
	            loop = Integer.parseInt(args[2]);
	        } else if (args.length != 0) {
	            System.out.println("usage: mmult [task rec loop]");
	            System.exit(66);
	        }

	        	// split matrix!
		      
	            m.a = new Matrix(task, rec, loop, 1.0f, false); 
	            m.b = new Matrix(task, rec, loop, 1.0f, false);
		        m.c = new Matrix(task, rec, loop, 0.0f, false);
		    
		        matrices[0]=m.a;
	   			matrices[1]=m.b;
	   			matrices[2]=m.c;
		        
		        int cells =power(2, task + rec) * loop;
		        System.out.println("Running Matrix multiply, on a matrix of size "

		        	+ cells + " x " + cells + ", threads = " + power(8, task));
		        
		    try{
		     	
				GridOptimizedMarshaller marshaller = new GridOptimizedMarshaller();
				marshaller.setRequireSerializable(true);
				GridConfigurationAdapter cfg = new GridConfigurationAdapter();
				cfg.setMarshaller(marshaller);
				G.start(cfg);
				org.gridgain.grid.Grid grid = G.grid();
					
				start = System.currentTimeMillis();
				
				m.a = new Matrix(task, rec, loop, 1.0f, false); // a is row-wise flipped, to make product zero
		        m.b = new Matrix(task, rec, loop, 1.0f, false);
		        m.c = new Matrix(task, rec, loop, 0.0f, false);
			    
		        matrices[0]=m.a;
				matrices[1]=m.b;
				matrices[2]=m.c;
			    
		   		@SuppressWarnings("unchecked")
				GridTaskFuture<Result> future = grid.execute(new Task(task,rec,0),matrices);
		   		Result resultado = future.get();
		   		end = System.currentTimeMillis();
		   		m.c=resultado.data;
		   		time = (double) end - start;
		   		time /= 1000.0; // seconds.
		   		System.out.println("Running Matrix multiply, on a matrix of size "
		   				+ cells + " x " + cells + ", threads = " + power(8, task));
		   	
		   		System.out.println("checking result, should be " + ((float) cells));
		      
		   		if (m.c.check(task, rec, (float) cells)) {
		   			System.out.println("application time Mmult (" + task + "," + rec
		                    + "," + loop + "," + cells + ") took " + time + " s");
		         
				       
		        	System.out.println("application result Mmult (" + task + "," + rec
		                    + "," + loop + "," + cells + ") = OK");
		            System.out.println("Test succeeded!");
		        } 
		   			else 
		   			{
		   				System.out.println("application time Mmult (" + task + "," + rec
		                    + "," + loop + "," + cells + ") GAVE WRONG RESULT!");
		   				System.out.println("application result Mmult (" + task + "," + rec
		                    + "," + loop + "," + cells + ") GAVE WRONG RESULT!");
		   				System.out.println("Test failed!");
		   				System.exit(1);
		   			}
				
			
	
		   			
		        }
				
			finally{
				
				G.stop(true);
			}
		       
	}
	       
	
}
		

		
				
		
		

