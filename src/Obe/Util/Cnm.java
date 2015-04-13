package Obe.Util;

public class Cnm {
    public int jiecheng(int p)
    {
    	int a = 1;
        for(int i = 1; i < p+1; i++)
        {
            a = i * a;
        }
        return a;
    }
    
    public int chengfa(int b,int c)
    {
    	int d = 1;
        for(int i = b; i > b-c; i--)
        {
            d = i * d;
        }
        return d; 		
    }
	
    public double c(int n, int m)
    {
    	double q;
    	Cnm nm = new Cnm();
    	return q = (double)nm.chengfa(n, m)/(double)nm.jiecheng(m);
    }    
    
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
    	Cnm nm = new Cnm();
		System.out.println("c(10,3)的结果为：" 
		        + nm.chengfa(8,2) + "/" + nm.jiecheng(2) 
		        + "=" + nm.c(8,2)); 
		System.out.println("c(10,7)的结果为：" 
			+ nm.chengfa(10,7) + "/" + nm.jiecheng(7) 
			+ "=" + nm.c(10,7)); 
	}
}
