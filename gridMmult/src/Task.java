import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gridgain.grid.GridException;
import org.gridgain.grid.GridJob;
import org.gridgain.grid.GridJobResult;
import org.gridgain.grid.GridJobResultPolicy;
import org.gridgain.grid.GridTaskNoResultCache;
import org.gridgain.grid.GridTaskSplitAdapter;
import org.gridgain.grid.marshaller.GridMarshaller;
import org.gridgain.grid.resources.GridMarshallerResource;


@GridTaskNoResultCache
public class Task extends GridTaskSplitAdapter<Matrix[],Result>{

	
	private static final long serialVersionUID = -1241472472665338956L;

	@GridMarshallerResource
	private GridMarshaller marshaller;
	
	protected int task,rec,rtask,rrec;
	
	protected Matrix c;
	protected int idTask;
	protected Result result;
	
	public Task(int t,int r,int id) {
		
		task=t;
		rec=r;
		rtask=task;
		rrec=rec;
	
		idTask=id;
	}
	
	@Override
	protected Collection<? extends GridJob> split(int arg0, Matrix[] arg1)
			throws GridException {
		
		
		List<Job> jobs = new ArrayList<Job>(arg0);
		
		
		 if (task + rec <= 0)
	            return null;

		
        if (task > 0) {
            task--;
        } 
        else {
            rec--;
        }
		
		c=arg1[2];
		
		
		
		jobs.add(new Job(arg1[0]._00,arg1[1]._00,arg1[2]._00,task,rec,0));
        jobs.add(new Job(arg1[0]._00,arg1[1]._01,arg1[2]._01,task,rec,1));
        jobs.add(new Job(arg1[0]._10,arg1[1]._00,arg1[2]._10,task,rec,2));
        jobs.add(new Job(arg1[0]._10,arg1[1]._01,arg1[2]._11,task,rec,3));
    
    	jobs.add(new Job(arg1[0]._01,arg1[1]._10,arg1[2]._00, task,rec,0));
        jobs.add(new Job(arg1[0]._01,arg1[1]._11,arg1[2]._01, task,rec,1));
        jobs.add(new Job(arg1[0]._11,arg1[1]._10,arg1[2]._10, task,rec,2));
        jobs.add(new Job(arg1[0]._11,arg1[1]._11,arg1[2]._11, task,rec,3));
         
		
        return jobs;
	}

	
		@Override 
	public GridJobResultPolicy result(GridJobResult res, List<GridJobResult> rcvd) {
        
			// Calculated each partial result without keeping in memory until the end of the execution.

			if ((res.getException()!=null)){
				
				return GridJobResultPolicy.FAILOVER;
			}
			
			result = (Result)res.getData();
			switch(result.id){
				
					case 0: 
						c._00.loopMatSum(c._00, result.data);
						break;
					case 1:
						c._01.loopMatSum(c._01, result.data);
						break;
					case 2: 
						c._10.loopMatSum(c._10, result.data);

						break;
					case 3:
						c._11.loopMatSum(c._11, result.data);
						break;	
					default:
						System.out.println("[TASK-REDUCE] >> Error en el valor recibido de matriz hija : " + result.id);
						break;
				}
				
			return GridJobResultPolicy.WAIT;
		}
		
		
		
		@Override
		public Result reduce(List<GridJobResult> arg0) throws GridException {
			
			// This method executes at final computation
			
			result=null;
			result=new Result(c, idTask,rtask,rrec);
			return result;
		}

		
	
		
		

	
}
