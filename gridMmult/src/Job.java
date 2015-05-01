import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridFuture;
import org.gridgain.grid.GridJobAdapter;
import org.gridgain.grid.GridJobContext;
import org.gridgain.grid.GridTaskFuture;
import org.gridgain.grid.lang.GridInClosure;
import org.gridgain.grid.resources.GridInstanceResource;
import org.gridgain.grid.resources.GridJobContextResource;



	

public class Job extends GridJobAdapter{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5459045476546692678L;

	@GridInstanceResource
    protected Grid grid;

	@GridJobContextResource
    private GridJobContext jobCtx;
	
	protected GridTaskFuture<Result> future;
	protected Matrix a,b,c;
	protected int task,rec,idJob;
	protected Result result;
	protected Matrix[] matrices;
	int cells;
	public Job(Matrix ma,Matrix mb,Matrix mc,int t, int r,int idBlock) {
		
		task=t;
		rec=r;
		a = ma;
		b = mb;
		c = mc;
		future=null;
		idJob=idBlock;
		
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute() throws GridException {
	
		
		if (task == 0){
			
			c.recMatMul(rec, a, b);
			result=new Result(c,idJob,task,rec);
			return result;
		}
			
		if (future==null){
			
			matrices=new Matrix[3];
			matrices[0]=a;
			matrices[1]=b;
			matrices[2]=c;
			
			future=grid.execute(new Task(task,rec,idJob),matrices);
			
			GridInClosure<GridFuture<Result>> listener= new GridInClosure<GridFuture<Result>>() {
				private static final long serialVersionUID = -6362975320088241040L;
				@Override
				public void apply(GridFuture<Result> arg0) {
					 //Resume a suspended job execution
					
					jobCtx.callcc();
				}
				
			};
			
			future.listenAsync(listener);
			// Suspend job execution to be continued later and release the executing thread.
            
			return jobCtx.holdcc();
			
			
					
			
		}
		else {
			assert future.isDone();
			
			result = future.get();
			return result;
		}
		
	}
	
		
	

}
