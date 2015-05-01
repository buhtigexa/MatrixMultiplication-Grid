import java.io.Serializable;

/**
 *  This class is useful to help to processing grid job results
 */

public class Result implements Serializable{

	
	
	private static final long serialVersionUID = -1442974399042414904L;
	public Matrix data;
	public int id,task,rec;
	public int size;
	
	public Result(Matrix d,int i,int t,int r){
		data=d;
		id=i;
		task=t;
		rec=r;
	

		
		
	}
	
}
