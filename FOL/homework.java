
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.lang.Character;

public class homework {
	public static int numQ;
	public static int numKB;
	public static String[] orign_queries;
	public static String[] orign_sentences;
	public static ArrayList<Predicate> queries = new ArrayList<Predicate>();
	//public static ArrayList<ArrayList<String>> sentences;
	//private static Map<Integer, Set<Predicate>> KB = new HashMap<Integer, Set<Predicate>>();
	private static Map<Integer, Clause> KB = new HashMap<Integer, Clause>();
	private static Map<Predicate, Set<Integer>> preDict = new HashMap<Predicate, Set<Integer>>();
	public static int[] numVar = new int[26];
	
	static class Predicate {
		
        String name;
        ArrayList<String> vars;
        boolean negate;
        
        public Predicate(Predicate org) {
        	name = org.name;
        	vars = (ArrayList<String>) org.vars.clone();
        	negate = org.negate;
        }
        
        public Predicate(String name, ArrayList<String> var, boolean negate) {
            this.name = name;
            this.vars = var;
            this.negate = negate;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Predicate))  return false;
            else{
            	Predicate another = (Predicate) obj;
            	if(!this.name.equals(another.name)) return false;
            	else if(this.negate != another.negate) return false;
            	else if(!this.vars.equals(another.vars)) return false;              
            }
            return true;
        }
        @Override
        public String toString() {
        	
        	if (vars == null) return null;
        	int len = vars.size();
        	if (len == 0) return "( )";
        	
        	StringBuffer sb = new StringBuffer();
        	if (negate) {
        		sb.append("~");
        	}
        	sb.append(name);
        	sb.append("(");
        	sb.append(vars.get(0));
        	for (int i = 1; i < len; i++) {
        		sb.append(", ");
        		sb.append(vars.get(i));
        	}
        	sb.append(")");
        	return sb.toString();
        }
        
        public Predicate Complement() {
        	Predicate another = new Predicate(this.name, this.vars, !this.negate);
        	return another;
        }
        
    }
	static class Clause{
		Set<Predicate> predSet;
		Map<String, Predicate> clauseNameMap;
		
		public Clause(Set<Predicate> clause) {
			Set<Predicate> copiedClause = new HashSet<>();
			copiedClause.addAll(clause);
			this.predSet = new HashSet<Predicate>(copiedClause);
			this.clauseNameMap = new HashMap<String, Predicate>();
			for(Predicate pred: predSet) {
				this.clauseNameMap.put(pred.name, pred);
			}			 
		}
		
		public Clause() {
			this.predSet = new HashSet<Predicate>();
			this.clauseNameMap = new HashMap<String, Predicate>();
		}
		public Clause eliminate(Predicate pred) {
			Clause copiedClause = new Clause();
			copiedClause.predSet.addAll(this.predSet);
			copiedClause.clauseNameMap.putAll(this.clauseNameMap);
			copiedClause.predSet.remove(pred);
			copiedClause.clauseNameMap.remove(pred.name);
			return copiedClause;
		}
		public void add(Clause c1) {
			this.predSet.addAll(c1.predSet);
			this.clauseNameMap.putAll(c1.clauseNameMap);
		}
		public boolean isEmpty() {
			if(this.predSet.isEmpty()) return true;
			return false;
		}
		
		@Override
		public int hashCode() {
			int result = 31;
			result = result * 17 + predSet.hashCode();
			result = result * 17 + clauseNameMap.hashCode();
			return result;
		}
		
	    public boolean equals(Clause another) {
			if(this.predSet.equals(another.predSet)) return true;
			return false;
		}
	}

	public static void main(String[] args) {
		
		//read input
		readfile();
		/*
		 * System.out.println(numQ); System.out.println(numKB);
		 * System.out.println(queries[0]); System.out.println(sentences);
		 */
		// initialize queries and KB
		for(String str: orign_queries) { 
			queries.add(ConvertToPre(str, 0));
			System.out.println("query:" + str);
		}
		
        for(int i =0; i<orign_sentences.length; i++) {
        	addToKB(orign_sentences[i], i);
        	System.out.println(KB.get(i).predSet.toString());
        }
        //use resolution 
		 //test resolution
        String[] result = new String[numQ];
        //System.out.println(queries.size());
        int i = 0;
        for(Predicate q: queries) {
        	System.out.println();
        	System.out.println(q.toString());
        	if(q.vars.get(0).equals( "Alice")) {
        		System.out.print("xxx");
        	}
        	System.out.println("kbsize:" + KB.get(1).predSet.size());
        	boolean res = resolution(KB, q);
        	
        	result[i] = Boolean.toString(res);
        	System.out.println("result:" + result[i]);
        	i++;
        }
        
        
        
		//test CNF	
		/*
		 * for(ArrayList<String> s : sentences) { for(String s2 : s) {
		 * System.out.print(s2); } System.out.println(); }
		 */
        
		//test ConvertToPre
		
		/*
		 * String s = "~Alert(Alice,y)"; Predicate p = ConvertToPre(s);
		 * System.out.println("p.name:"+p.name); System.out.println(p.negate);
		 * System.out.println(p.vars);
		 */
		 
        // test predicate equal and complement method
		
		
		/*
		 * ArrayList<String> arr = new ArrayList<String>(); arr.add("Bob");
		 * arr.add("Warfarin"); Predicate p1 = new Predicate("Take", arr, false);
		 * Predicate p2 = new Predicate("Take", arr, true);
		 * System.out.println(p1.equals(p2));
		 * System.out.println(p1.Complement().negate);
		 */
		 
		 
        //test unify (x1 : Bob)
		/*
		 * ArrayList<String> arr = new ArrayList<String>(); arr.add("Bob");
		 * arr.add("Warfarin"); ArrayList<String> arr2 = new ArrayList<String>();
		 * arr2.add("x1"); arr2.add("Warfarin"); ArrayList<String> arr3 = new
		 * ArrayList<String>(); arr3.add("x1"); arr3.add("Bob"); Predicate p1 = new
		 * Predicate("Take", arr, false); Predicate p2 = new Predicate("Take", arr2,
		 * false); //Predicate p2 = new Predicate("Loves", arr, false); Map<String,
		 * String> theta = unifyCompound(p1, p2, new HashMap<String, String>());
		 * //Iterator iter = theta.entrySet().iterator(); if(theta==null)
		 * {System.out.print(theta==null);} else { for (Map.Entry<String, String>
		 * mapElement : theta.entrySet()) { System.out.println(mapElement.getKey() +
		 * " : " + mapElement.getValue()); } }
		 */
		
		//test int[]
		/*
		 * int[] like = new int[26]; System.out.print('a'-'a');
		 * System.out.print('z'-'a');
		 */
        // test resolve
		
		/*
		 * ArrayList<String> arr = new ArrayList<String>(); arr.add("x");
		 * ArrayList<String> arr2 = new ArrayList<String>(); arr2.add("Like");
		 * arr2.add("y"); ArrayList<String> arr3 = new ArrayList<String>();
		 * arr3.add("x"); arr3.add("z1"); Predicate p1 = new Predicate("Animal", arr,
		 * false); Predicate p2 = new Predicate("Loves", arr2, false); Predicate p3 =
		 * new Predicate("Loves", arr3, true); HashSet<Predicate> set1 = new
		 * HashSet<Predicate>(); set1.add(p1); set1.add(p2); HashSet<Predicate> set2 =
		 * new HashSet<Predicate>(); set2.add(p3); Clause c1 = new Clause(set1); Clause
		 * c2 = new Clause(set2); Clause newClause = resolve(c1, c2);
		 * System.out.println(newClause.predSet.size()); for(Predicate pre:
		 * newClause.predSet ) { System.out.println(pre.name);
		 * System.out.println(pre.vars); System.out.println(pre.negate); }
		 */


		
		// putput
		outputfile(result);

	}
	/**
     * Read input file and refresh global variables: queries, sentences, numQ, numKB
     * input file name: ***
     * @param 
     */
	public static void readfile() {
		// read input file
		try {
		      File inputObj = new File("input.txt");
		      Scanner inputReader = new Scanner(inputObj);
		      int i = 0;
		      while (inputReader.hasNextLine()) {
		        String str = inputReader.nextLine();
		        str = str.trim();
		        if(i == 0) {
		        	numQ = Integer.parseInt(str);
		        	orign_queries = new String[numQ];
		        	//queries = new ArrayList<ArrayList<String>>();
		        }
		        else if(i == numQ + 1) {
		        	numKB = Integer.parseInt(str);
		        	orign_sentences = new String[numKB];
		        	//sentences = new ArrayList<ArrayList<String>>();
		        }
		        else if(0 < i && i< numQ + 1) {	
		        	orign_queries[i-1] = str;
		        	//queries.add(toCNF(str));
		        }
		        else {
		        	orign_sentences[i-2-numQ] = str;
		        	//sentences.add(toCNF(str));
		        }		        
		        i++;
		      }
		      inputReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred when opening the input file.");
		    }
	}
	/**
     * Output file
     * @param str
     */
	public static void outputfile(String[] str) {
		try {
		      FileWriter myWriter = new FileWriter("output.txt");
		      for(String s: str) {
			      myWriter.write(s.toUpperCase() + "\r\n");
			      System.out.println(s);
		      }
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred when writing a file.");
		      e.printStackTrace();
		    }
	}
	/**
     * resolution: check KB^~Q
     * @param KB query
     * @return true if resolvents of KB^~Q contains empty clause
     */
	public static boolean resolution(Map<Integer, Clause> orgKb, Predicate query) {
		Map<Integer, Clause> kb = new HashMap<>(orgKb);
		//copiedkb = kb.clone();
		Deque<Clause> queue = new LinkedList<Clause>();
		Set<Clause> store = new HashSet<Clause>();
		//boolean unify = false;
		Predicate nquery = query.Complement(); 		
 		Set<Predicate> predSet = new HashSet<Predicate>();
 		predSet.add(nquery);
 		Clause nqueryClause = new Clause(predSet);
 		queue.addLast(nqueryClause);
 		long start = System.currentTimeMillis();
 		while(!queue.isEmpty()) {
 			Clause C1 = queue.pollFirst();
			for(int key: kb.keySet()) {
 	 			Clause C2 = kb.get(key);
 				Clause clauseOrign = new Clause();
 				clauseOrign.add(C1);
 				clauseOrign.add(C2);
 				Clause resolvents = resolve(C1, C2);
 				if(resolvents.isEmpty()) return true;
 				//  two sentences' unify has no change
 				if(!clauseOrign.equals(resolvents)) {
 					if(!store.contains(resolvents)) {
 						queue.addLast(resolvents);
 	 					store.add(resolvents);
 					} 					
 					//unify = true;
 				}
 	 		}
			long time = System.currentTimeMillis() - start;
			if(time > 36000) {
				return false;
			}
 		}	
 		
 		 //if(!unify) return false;
 		 return false;
 		}
	
	/*
	 * public static boolean resolution(Map<Integer, Clause> clauses, Predicate
	 * query) { Predicate nquery = query.Complement(); Set<Predicate> predSet = new
	 * HashSet<Predicate>(); predSet.add(nquery); clauses.put(clauses.size(), new
	 * Clause(predSet)); Clause newClause = new Clause(); for(int key:
	 * clauses.keySet()) { Clause C1 = clauses.get(key); for(int key2:
	 * clauses.keySet()) { Clause C2 = clauses.get(key2); Clause resolvents =
	 * resolve(C1, C2); if(resolvents.isEmpty()) return true;
	 * newClause.add(resolvents); } } if(clauses.containsValue(newClause)) return
	 * false; clauses.put(clauses.size(), newClause);
	 * 
	 * }
	 */
	/**
     * resolve: use resolution inference rule to resolve two clauses
     * @param two clauses Ci, Cj
     * @return resolvent clause
     */
	public static Clause resolve(Clause orgC1, Clause orgC2) {
		Clause C1 = new Clause(orgC1.predSet);
		Clause C2 = new Clause(orgC2.predSet);
		Clause newClause = new Clause(new HashSet<>());	
		Clause tempC1 = new Clause(C1.predSet);
		//Clause copiesC1 = new Clause(new HashSet<>());
		//Clause copiesC2 = new Clause(new HashSet<>());
		Iterator<Predicate> iterator = tempC1.predSet.iterator();
		while(iterator.hasNext()) {
			Predicate pred1 = iterator.next();
			String name1 = pred1.name;
			if(C2.clauseNameMap.keySet().contains(name1)) {
				Predicate temp = pred1.Complement();
				Map<String, String> theta = unifyCompound(temp, C2.clauseNameMap.get(name1), new HashMap<String, String>());
				if(theta != null) {
					C1 = C1.eliminate(pred1);
					C2 = C2.eliminate(C2.clauseNameMap.get(name1));
					// After eliminate C1, C2 become new obj
					C1 = updateVar(C1, theta);
					C2 = updateVar(C2, theta);
				}
			}
	    }
		/*
		 * for(Predicate pred1 : C1.predSet) {
		 * }
		 */		
		newClause.add(C1);
		newClause.add(C2);
		return newClause;
	}
	// change variables using theta
	public static Clause updateVar(Clause orgClause, Map<String, String> theta) {
		
		int len = orgClause.predSet.size();
		HashSet<Predicate> set = new HashSet<>(len);
		for (Predicate p: orgClause.predSet) {
			set.add(new Predicate(p));
		}
		Clause clause = new Clause(set);
		
		Iterator<Predicate> iterator = clause.predSet.iterator();
		while(iterator.hasNext()) {
			Predicate pred = iterator.next();
			int size = pred.vars.size();
			for(int i =0; i < size; i++) {
				if(isVariable(pred.vars.get(i))) {
					if(theta.containsKey(pred.vars.get(i))) {
						pred.vars.set(i, theta.get(pred.vars.get(i)));
					}
				}
			}
		}
		return clause;
	}
	/**
     * unify two compounds to get a substitution(hashmap) to make them identical
     * @param predicate pred1, pred2 ; hashmap substitution
     * @return 
     */
	public static Map<String, String> unifyCompound(Predicate pred1, Predicate pred2, Map<String, String> curSubs) {
		//Map<String, String> subsDict = new HashMap<String, String>();
		if(pred1.negate == pred2.negate) {
			if(!pred1.name.equals(pred2.name)) return null;
			else if(pred1.equals(pred2)) return curSubs;
			return unifyArgs(pred1.vars, pred2.vars, curSubs);
		}
		return null;
	}
// unify list of arguments
	public static Map<String, String> unifyArgs(ArrayList<String> orgVars1, ArrayList<String> orgVars2, Map<String, String> curSubs) {
		//Map<String, String> subsDict = new HashMap<String, String>();
		if (curSubs == null) return null;
		ArrayList<String> vars1 = (ArrayList<String>) orgVars1.clone();
		ArrayList<String> vars2 = (ArrayList<String>) orgVars2.clone();
		if(vars1.equals(vars2)) return curSubs;
		else if(vars1.size() != vars2.size()) return null;
		String varsFirst1 = vars1.remove(0);
		String varsFirst2 = vars2.remove(0);
		return unifyArgs(vars1, vars2, unifyArg(varsFirst1, varsFirst2, curSubs));
	}
//unify argument
	public static Map<String, String> unifyArg(String var1, String var2, Map<String, String> curSubs) {
		//Map<String, String> subsDict = new HashMap<String, String>();
		
		if(var1.equals(var2)) return curSubs;
		else if(isVariable(var1)) return unify_var(var1, var2, curSubs);
		else if(isVariable(var2)) return unify_var(var2, var1, curSubs);
		return null;
	}
	/**
     * unify variable
     * @param String var1, var2, substitution map: theta
     * @return substitution map
     */
	public static Map<String, String> unify_var(String var1, String var2, Map<String, String> curSubs){
		
		if(curSubs.containsKey(var1)) return unifyArg(curSubs.get(var1), var2, curSubs);
		else if(curSubs.containsKey(var2)) return unifyArg(var1, curSubs.get(var2), curSubs);
		else {
			curSubs.put(var1, var2);
			return curSubs;
		}
	}
		
	/**
     * Justify whether var is variable or not
     * @param String var
     * @return true is var is a variable
     */
 	public static boolean isVariable(String var) {
		if(var.length() >= 1) {
			if(Character.isLowerCase(var.charAt(0))) return true;
		}
		return false;
	}
	/**
     * Get transfer a string to CNF structure
     * @param str
     * @return an ArrayList of string
     */
  	public static ArrayList<String> toCNF(String str) {
		ArrayList<String> front = new ArrayList<String>();
		ArrayList<String> back = new ArrayList<String>();
		ArrayList<String> result = new ArrayList<String>();
		String[] orign = str.split(" ");		
		boolean isfront = true;
		for(int i =0; i < orign.length; i++) {
			if(!orign[i].equals("=>") && isfront && !orign[i].equals("&")  && !orign[i].equals("=>") ) {
				front.add(orign[i]);
			}else if(orign[i].equals("=>")) {
				isfront = false;
			}
			else if(!orign[i].equals("&") && !orign[i].equals("=>")){
				back.add(orign[i]);
			}
		}
	    for(String s : front) {
	    	if(isfront == false) {
	    		s = negation(s);
	    		result.add(s);
	    	}else {
	    		result.add(s);
	    	}			
		}
		for(String s : back) {
			result.add(s);
		}
		return result;
	}
	/**
     * Get the negation of a given string
     * @param s
     */
    public static String negation(String s) {
    	if(s.charAt(0) == '~') s = s.substring(1);
		else s = "~"+ s;
    	return s;
    }
    /**
     * Add a sentence string to KB and refresh the predicate dictionary 
     * @param str
     * @return an ArrayList of string
     */
    public static void addToKB(String str, int index) {
    	ArrayList<String> front = new ArrayList<String>();
		ArrayList<String> back = new ArrayList<String>();		
		Set<Predicate> pred = new HashSet<Predicate>();
		String[] orign = str.split(" ");		
		boolean isfront = true;
		for(int i =0; i < orign.length; i++) {
			if(!orign[i].equals("=>") && isfront && !orign[i].equals("&")  && !orign[i].equals("=>") ) {
				front.add(orign[i]);
			}else if(orign[i].equals("=>")) {
				isfront = false;
			}
			else if(!orign[i].equals("&") && !orign[i].equals("=>")){
				back.add(orign[i]);
			}
		}
	    for(String s : front) {
	    	if(isfront == false) {
	    		s = negation(s);
	    		Predicate predTemp = ConvertToPre(s, index);
	    		pred.add(predTemp);
	    		
	    		Set<Integer> setTemp = preDict.getOrDefault(predTemp, new HashSet<Integer>());
	    		setTemp.add(index);
	    		preDict.put(predTemp, setTemp);
	    	}else {
	    		Predicate predTemp = ConvertToPre(s, index);
	    		pred.add(predTemp);
	    		Set<Integer> setTemp = preDict.getOrDefault(predTemp, new HashSet<Integer>());
	    		setTemp.add(index);
	    		preDict.put(predTemp, setTemp);
	    	}			
		}
		for(String s : back) {
			Predicate predTemp = ConvertToPre(s, index);
    		pred.add(predTemp);
    		Set<Integer> setTemp = preDict.getOrDefault(predTemp, new HashSet<Integer>());
    		setTemp.add(index);
    		preDict.put(predTemp, setTemp);
		}
		Clause clause = new Clause(pred);
		KB.put(index, clause);
		//KB.put(index, pred);
		//test
		/*
		 * System.out.println(index); System.out.println();
		 * System.out.println(pred.size());
		 */
    }
    /**
     * Convert a string to Predicate
     * @param str
     * @return a predicate
     */
	public static Predicate ConvertToPre(String str, int index) {
		Predicate pred;
		String name = "";
		ArrayList<String> variables = new ArrayList<>();
		boolean negate = false;
		int j;
		if(str.charAt(0) == '~') {
			negate = true;
			j = 1;			
		} 
		else j=0;
		
		while(str.charAt(j) != '(' && j < str.length()) {
			name += str.charAt(j);
			j++;
		}
		int varStart = j + 1;
		while(str.charAt(j) != ')') {
			j++;
		}
		int varEnd = j;
		String[] vars = str.substring(varStart, varEnd).split("\\s*,\\s*");
		for(String var: vars) {
			if(isVariable(var)) {
				//int num = numVar[var.charAt(0)-'a']++;
				var += index+1;
			}
			variables.add(var);
		}
		pred = new Predicate(name, variables, negate);
		return pred;
	}
	

}
    

