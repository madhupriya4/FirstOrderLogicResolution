package homework3;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

//custom pair class
class Pair<T, U> {
    public final T key;
    public final U value;

    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }
}

public class homework {
	
	
	static int nq,ns;
	static String[] queries= {};
	static ArrayList <ArrayList<Pair<String,String>>> kbPerm;
	static int LoopCheck=0;
	
	//print function useful for testing
	/*public static void printKb(ArrayList<ArrayList<Pair<String, String>>> kb)
	{
		for(int i=0;i<kb.size();i++)
			{
				for(int j=0;j<kb.get(i).size();j++)
					System.out.print(kb.get(i).get(j).key+":"+kb.get(i).get(j).value+",  ");
				System.out.println();	
			}
	}*/
	
	//get predicate inverse
	public static String getPredInv(String q)
	{
		String pred;
		if(q.startsWith("~"))
			pred=q.substring(1);
		else
			pred="~".concat(q.substring(0));
		
		if(pred.contains("("))
			pred=pred.substring(0, pred.indexOf("("));
		return pred;
	}
	
	//get input
	public static void getFormattedInput(ArrayList<ArrayList<Pair<String, String>>> kb)
	{
		try {
			
			FileReader fr=new FileReader("input.txt");
			BufferedReader br=new BufferedReader(fr);
		

			nq=Integer.parseInt(br.readLine());
			queries=new String[nq];
			
			for(int i=0;i<nq;i++)
				queries[i]=br.readLine();
		
			ns=Integer.parseInt(br.readLine());
						
			for(int i=0;i<ns;i++)
			{
				String temp1=br.readLine();
				temp1 = temp1.replaceAll("\\s","");
				String [] line=temp1.split("[\\|\\s]+");
				ArrayList<Pair<String,String>> temp= new ArrayList<Pair<String,String>>();
				for(int j=0;j<line.length;j++)
				{
					
					int brackStart=line[j].indexOf('(');
					int brackEnd=line[j].lastIndexOf(')');
					
					String [] pred=line[j].substring(0, brackStart).split(",");
					String [] args=line[j].substring(brackStart+1, brackEnd).split(",");		
			
					temp.add(new Pair<String,String>(String.join(",", pred), String.join(",", args)));
				}
				kb.add(temp);
			}
			br.close();
		}
		catch(FileNotFoundException e){}
		catch(IOException e){}
		
	}
	
	//get value given a key and position to not be equal
	public static int get(ArrayList<Pair<String,String>> mp,String query,int w)
	{
		for(int k=0;k<mp.size();k++)
			if(mp.get(k).key.equals(query)&&k==w)
				return k;
		
		return -1;
	}
	
	//get value given a key
	public static int get(ArrayList<Pair<String,String>> mp,String query)
	{
		for(int k=0;k<mp.size();k++)
			if(mp.get(k).key.equals(query))
				return k;
		
		return -1;		
	}
	
	//substitutes constants in place of variables
	public static ArrayList<Pair<String, String>> substitute(ArrayList<Pair<String,String>> mp,String Const, String var)
	{
		ArrayList<Pair<String,String>> newMap=new ArrayList<Pair<String,String>> ();
		for(int k=0;k<mp.size();k++)
		{
			String [] args=(mp.get(k).value).split(",");	
			for(int w=0;w<args.length;w++)
				if(args[w].equals((var)))
					args[w]=Const;
			
			newMap.add(new Pair<String,String>(mp.get(k).key, String.join(",", args)));
		}
		
		return newMap;
	}
	
	//unifies two predicates and checks for violations
	public static boolean unify(ArrayList <ArrayList<Pair<String,String>>> kb, String query1, String query2,int i,int j,int w)
	{
		try {
		if(get(kb.get(i),query1)==-1 || get(kb.get(j),query2,w)==-1)
			return false;
		
		String [] q1Args=kb.get(i).get(get(kb.get(i),query1)).value.split(",");
		String [] q2Args=kb.get(j).get(get(kb.get(j),query2,w)).value.split(",");
		
		for(int k=0;k<q1Args.length;k++)
			if(Character.isUpperCase(q1Args[k].charAt(0)) && Character.isUpperCase(q2Args[k].charAt(0)) && !q1Args[k].equals(q2Args[k]))
					return false;
		
		//map to prevent same predicate from being bound twice
		Map<String,String> vars=new TreeMap<String,String>();
		for(int k=0;k<q1Args.length;k++)
		{
			if(Character.isUpperCase(q1Args[k].charAt(0)) && Character.isUpperCase(q2Args[k].charAt(0)) && !q1Args[k].equals(q2Args[k]))
				return false;
			
			if(Character.isUpperCase(q1Args[k].charAt(0)) && !Character.isUpperCase(q2Args[k].charAt(0)))
			{
				String var=q2Args[k];
				kb.set(j, substitute(kb.get(j), q1Args[k], var));
				if(j<kbPerm.size())
					kbPerm.set(j, substitute(kbPerm.get(j), q1Args[k], var));
				//change org array as well
				q2Args=kb.get(j).get(get(kb.get(j),query2,w)).value.split(",");
			}
			if(Character.isUpperCase(q2Args[k].charAt(0)) && !Character.isUpperCase(q1Args[k].charAt(0)))
			{
				String var=q1Args[k];
				kb.set(i, substitute(kb.get(i), q2Args[k], var));
				if(i<kbPerm.size())
					kbPerm.set(i, substitute(kbPerm.get(i), q2Args[k], var));
				q1Args=kb.get(i).get(get(kb.get(i),query1)).value.split(",");
			}
			String var1=q1Args[k];
			String var2=q2Args[k];
			if(vars.containsKey(var1) && !vars.get(var1).equals(var2))
				return false;
			else
				vars.put(var1, var2);
		}
		kb.get(i).remove(kb.get(i).get(get(kb.get(i),query1)));		
		kb.get(j).remove(kb.get(j).get(get(kb.get(j),query2,w)));
		
		return true;
		}
		catch(StackOverflowError e)
		{
			return false;
		}
	}
	
	//looks for another query to check	
	public static boolean resolveAnotherQuery(ArrayList <ArrayList<Pair<String,String>>> kb, String query,int i,int j)
	{
		try
		{
			if(kb.get(i).size()==0)
				return true;
		
			for(int k=0;k<kb.get(i).size();k++)
			{			
				boolean flag= resn(kb, kb.get(i).get(k).key, i,j);
				if(flag==true)
					return true;
			}
		
			return false;
		}
		catch(StackOverflowError e)
		{
			return false;
		}
	}
	
	//resolves two predicates
	public static boolean resolve(ArrayList <ArrayList<Pair<String,String>>> kb, String query1, String query2,int i,int j,int w)
	{
		try
		{
			LoopCheck++;	
			boolean res=unify(kb, query1, query2, i, j,w);
			
			if(!res)
				return false;
			
			if(kb.get(i).size()==0 && kb.get(j).size()==0)
				return true;
			
			//send a copy of the kb every time
			ArrayList <ArrayList<Pair<String,String>>> temp=new ArrayList<ArrayList<Pair<String,String>>>();
			for(int k=0;k<kb.size();k++)
				temp.add(copyList(kb.get(k)));
			
			return resolveAnotherQuery(kb, query1, i,j) && resolveAnotherQuery(temp, query2, j,i);
		}
		catch(StackOverflowError e)
		{
			return false;
		}	
	}
	
	//useful for deep copying one list to another
	public static ArrayList<Pair<String, String>> copyList(ArrayList<Pair<String, String>> map)
	{
		ArrayList<Pair<String, String>> mp =new ArrayList<Pair<String, String>>();
		for(int i=0;i<map.size();i++)
			mp.add(map.get(i));
		return mp;
	}
	
	//resolves a query by finding the first true match
	public static boolean resn(ArrayList <ArrayList<Pair<String,String>>> kb,String query, int queryIndex,int dontTouch)
	{
		try {
			if(LoopCheck>(50000))
				return false;
					
			String queryInv=getPredInv(query);
			
			//look for all negations
			int index=0;String kbValue=null;int flag=0;
			
			for(int i=0;i<kb.size();i++)
			{
				kbValue=null;
				if(i==dontTouch)
					continue;
				
				for(int w=0;w<kb.get(i).size();w++)
				{					
					if(kb.get(i).get(w).key.equals(queryInv))
					{
						kbValue=kb.get(i).get(w).value;
						index=i;
						
						//send a copy each time
						ArrayList <ArrayList<Pair<String,String>>> temp=new ArrayList<ArrayList<Pair<String,String>>>();
						for(int j=0;j<kb.size();j++)
							temp.add(copyList(kb.get(j)));
						
						//create a copy of the permanent KB
						ArrayList <ArrayList<Pair<String,String>>> permtemp=new ArrayList<ArrayList<Pair<String,String>>>();
						for(int j=0;j<kbPerm.size();j++)
							permtemp.add(copyList(kbPerm.get(j)));
						
						if(resolve(temp, query, queryInv,queryIndex,index,w)==true)
							return true;
						
						//revert back permanent KB
						kbPerm.clear();
						for(int j=0;j<permtemp.size();j++)
							kbPerm.add(copyList(permtemp.get(j)));
					}
				}
			}
			
			//reuse sentences in the org KB
			if(kbValue==null)
			{				
				for(int p=0;p<kbPerm.size();p++)
				{
					if(p==dontTouch)
						continue;
					for(int w=0;w<kbPerm.get(p).size();w++)
					{
						if(kbPerm.get(p).get(w).key.equals(queryInv))
						{
							flag=1;
							for(int r=0;r<kbPerm.get(p).size();r++)
							{
								//only resuse sentences with constants
								String [] args=kbPerm.get(p).get(r).value.split(",");
								for(int k=0;k<args.length;k++)
									if(!Character.isUpperCase(args[k].charAt(0)))
									{
										flag=2;
										break;
									}
								
								if(flag==2)
									break;
							}
							if(flag==2)
								continue;		
						}
						
						if(kbPerm.get(p).get(w).key.equals(queryInv))
						{
								
								index=kb.size();
								kb.add(kbPerm.get(p));
								
								//send a copy of the current KB
								ArrayList <ArrayList<Pair<String,String>>> temp=new ArrayList<ArrayList<Pair<String,String>>>();
								for(int j=0;j<kb.size();j++)
									temp.add(copyList(kb.get(j)));
								
								//create a copy of KB perm
								ArrayList <ArrayList<Pair<String,String>>> permtemp=new ArrayList<ArrayList<Pair<String,String>>>();
								for(int j=0;j<kbPerm.size();j++)
									permtemp.add(copyList(kbPerm.get(j)));

								if(resolve(temp, query, queryInv,queryIndex,index,w)==true)
									return true;
								
								//revert back permanent KB
								kbPerm.clear();
								for(int j=0;j<permtemp.size();j++)
									kbPerm.add(copyList(permtemp.get(j)));

						}
					}
				}
			}
			return false;
		}
		catch(StackOverflowError e)
		{
			return false;
		}
	}
	
	//process queries one by one
	public static void processQueries(ArrayList <ArrayList<Pair<String,String>>> kb)
	{
		
		FileWriter fw;
		try 
		{
			fw = new FileWriter("output.txt");
			BufferedWriter bw = new BufferedWriter(fw);
		
			for(int i=0;i<nq;i++)
			{				
				ArrayList <ArrayList<Pair<String,String>>> temp=new ArrayList<ArrayList<Pair<String,String>>>();
				
				for(int j=0;j<kb.size();j++)
					temp.add(copyList(kb.get(j)));
						
				//add negation of query as well
				ArrayList <Pair<String,String>> mp =new ArrayList <Pair<String,String>>();
				
				String qInv=getPredInv(queries[i]);
				
				mp.add(new Pair<String,String>(qInv,queries[i].substring(queries[i].indexOf('(')+1, queries[i].lastIndexOf(')'))));
				
				temp.add(mp);
				
				kbPerm=new ArrayList<ArrayList<Pair<String,String>>>();
				for(int j=0;j<temp.size();j++)
					kbPerm.add(copyList(temp.get(j)));
				
				LoopCheck=0;
				try
				{
					String res=(""+resn(temp, qInv,temp.size()-1,-1)).toUpperCase();
					bw.write(""+res+"\n");
				}
				catch(StackOverflowError e)
				{
					bw.write("FALSE\n");
				}
			}
			
			bw.close();
			fw.close();
		}
		 catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{
		ArrayList <ArrayList<Pair<String,String>>> kb=new ArrayList<ArrayList<Pair<String,String>>>();
		getFormattedInput(kb);
		processQueries(kb);
				
	}
}