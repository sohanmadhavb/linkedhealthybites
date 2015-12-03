package org.semweb.assign6;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;



public class LinkedHealthyBites {

	static String defaultNameSpace = "http://org.semweb/assign6/people#";
	static String newNameSpace = "http://www.semanticweb.org/sohanbangaru/ontologies/2015/10/linked-food-inspection-ontology.owl";
	
    Model _friends = null;
	Model schema = null;
	InfModel inferredFriends = null;
	OntModel restaurants = null;
	//String directory = "MyDb/Dataset1";
	//Dataset dataset = TDBFactory.createDataset(directory);
	
	public static void main(String[] args) throws IOException {
//		LinkedHealthyBites lhb = new LinkedHealthyBites();
		LinkedHealthyBites lhb1 = new LinkedHealthyBites();
//		System.out.println("ha bhai");
//		lhb.populateFOAFFriends();
//		System.out.println("\nSay Hello to Myself");
//		lhb.mySelf(lhb._friends);
		lhb1.populateFOAFFriends1();
		List<String> res = lhb1.mySelf1(lhb1._friends);
		//lhb1.populateNewFriends();
		lhb1.populateFOAFSchema();
		System.out.println("\n FoafFriends");
		lhb1.populateNewFriendsSchema();
		System.out.println("\n NewFriends");
		lhb1.addalignment(res);
		System.out.println("\n Ontologies Aligned");
		lhb1.bindReasoner();
		System.out.println("\n Reasoner bound");
		lhb1.writeRDF();
		System.out.println("\n Model Written");
	}//schemaorg:proprietaryName
	
	public void populateFOAFFriends() throws IOException
	{
			_friends = ModelFactory.createOntologyModel();
	InputStream inFoafInstance = 
	             FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/Inspections.rdf");
			_friends.read(inFoafInstance,newNameSpace);
			inFoafInstance.close();
		}
	
	public void populateFOAFFriends1() throws IOException
	{
			_friends = ModelFactory.createOntologyModel();
	InputStream inFoafInstance = 
	             FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/restauran.rdf");
			_friends.read(inFoafInstance,newNameSpace);
			inFoafInstance.close();
		}
//	public String mySelf(Model model)
//	{
//	//Hello to Me - focused search
//	String res = runQuery(" select ?dateCreated where { ?Restaurant schemaorg:dateCreated ?dateCreated }", model);  //add the query string
//	return res;
//
//	}
	
	public List<String> mySelf1(Model model)
	{
	//Hello to Me - focused search
	List<String> res = runQuery(" select DISTINCT ?id where { ?x rdfs:label ?id . ?x rest:hasName ?y . ?y rdfs:label ?name }", model);  //add the query string
	return res;

	}
	public List<String> runQuery(String queryRequest, Model model)
	{
			
	  StringBuffer queryStr = new StringBuffer();
		
	  // Establish Prefixes
	  //Set default Name space first
	  queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
	  queryStr.append("PREFIX linked-food-inspection-ontology" + ": <" + newNameSpace + "> ");
	  queryStr.append("PREFIX rest" + ": <" + "http://www.semanticweb.org/asu/health-inspection-data#" + "> ");
	  queryStr.append("PREFIX rdfs" + ": <" +  
	                  "http://www.w3.org/2000/01/rdf-schema#" + "> ");
	  queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
	  queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" 
	                   + "> ");
	  queryStr.append("PREFIX schemaorg" + ": <" + "http://schema.org/" + "> ");
			
	  //Now add query
	  queryStr.append(queryRequest);
	  Query query = QueryFactory.create(queryStr.toString());
	  QueryExecution qexec = QueryExecutionFactory.create(query, model);
			
	  try 
	  {
		ResultSet response = qexec.execSelect();
		//String outres = " "
	
		//String[] outres = null; 
		List<String> outres = new ArrayList<String>();
		while(response.hasNext())
		{
			QuerySolution soln = response.nextSolution();
			RDFNode name = soln.get("?id");
			//String[] outres = null;
			if( name != null )
			{
				//System.out.println( "Hello to " + name.toString().substring(0, 8) );//date is till 8 chars  
				//return name.toString().substring(0, 8);
				//outres[i] = name.toString();
			//	System.out.println(name.toString()); 
				outres.add(name.toString());
			
				//return name.toString();
			//	System.out.println(i);
			}
			else
				System.out.println("No Friends found!");
			
		} 
		
		System.out.println(outres); 
		return outres;
		//return "";
				
	  }
	  finally { qexec.close();}		
	}
	public void populateNewFriends() throws IOException 
	{		
	InputStream inFoafInstance = 
	FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/violation.rdf");
		_friends.read(inFoafInstance,defaultNameSpace);
		inFoafInstance.close();
	}
	public void populateFOAFSchema() throws IOException
	{
	  InputStream inFoaf = FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/restauran.rdf");
	  schema = ModelFactory.createOntologyModel();
	  
	  //schema.read("http://xmlns.com/foaf/spec/index.rdf");
	  
	// Use local copy for demos without network connection
	  schema.read(inFoaf, defaultNameSpace);
	  inFoaf.close();
	}
		
	public void populateNewFriendsSchema() throws IOException 
	{
	  InputStream inFoafInstance = 
	      FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/violation.rdf");
	  schema.read(inFoafInstance,defaultNameSpace);
	  inFoafInstance.close();
	}
	public void addalignment(List<String> res)
	{
		// State that :individual is equivalentClass of foaf:Person
		Resource resource = schema.createResource 
		                    (defaultNameSpace + "Restaurant");
		Property prop = schema.createProperty 
		                ("http://www.w3.org/2002/07/owl#equivalentClass");
		Resource obj = schema.createResource 
		               ("http://www.semanticweb.org/asu/health-inspection-data#Restaurant");
		schema.add(resource,prop,obj);
		//State that sem web is the same person as Semantic Web
		String result[] = res.toArray(new String[res.size()]);
		for(int i=0;i<7601;i++)
		{
			resource = schema.createResource 
					("http://www.semanticweb.org/asu/health-inspection-data/business/" + result[i] + "/identifier");
			prop = schema.createProperty ("http://www.w3.org/2002/07/owl#sameAs");
			obj = schema.createResource("http://www.semanticweb.org/asu/health-inspection-data/violation/" + result[i] + "/identifier");
			schema.add(resource,prop,obj);
		}

	}
	public void bindReasoner()
	{
	  Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
	  reasoner = reasoner.bindSchema(schema);
	  inferredFriends = ModelFactory.createInfModel(reasoner, _friends); 
	  
	}
	
	public void writeRDF() throws IOException{
		String OUTPUT_FILE = "/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/inferred.rdf";
		
		OutputStream out = new FileOutputStream(OUTPUT_FILE);
		inferredFriends.write(out, "RDF/XML");
		out.close();
		
	}
	

}


	

