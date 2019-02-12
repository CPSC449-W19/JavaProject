package Structure;

public class Triple<T1, T2, T3> {
	
	    private T1 x;
	    private T2 y;
	    private T3 z;
	    
	    public Triple(T1 x, T2 y, T3 z) {
	        this.x = x;
	        this.y = y;
	        this.z = z;
	    }

	    @Override
	    public String toString() {
	        return String.format("(%s,%s,%s)",getX(),getY(),getZ());
	    }

	    public T1 getX() {
	        return this.x;
	    }

	    public T2 getY() {
	        return this.y;
	    }
	    
	    public T3 getZ() {
	    	return this.z;

	}

}
