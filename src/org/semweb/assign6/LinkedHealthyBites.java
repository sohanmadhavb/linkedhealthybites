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
	static String newNameSpace2 = "http://www.semanticweb.org/asu/health-inspection-data#";
	static String dealsNameSpace = "http://www.semanticweb.org/asu/8coupons-deals-data#";
	static String ratingsNameSpace = "http://www.semanticweb.org/asu/yelp-review-data#";
	
	public Model restaurantModel = null;
	Model schema = null;
	Model inspectionsModel = null;
	Model violationsModel = null;
	Model dealsModel = null;
	Model ratingsModel = null;
	InfModel inferredFriends = null;
	OntModel restaurants = null;
	public void restaurantRdftomodel() 
	{
		restaurantModel = ModelFactory.createOntologyModel();
		InputStream inFoafInstance = 
				FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/Restaurants.rdf");
		restaurantModel.read(inFoafInstance,newNameSpace2);
		//inFoafInstance.close();
	}

	public List<String> retrieveRestaurantName(Model model)
	{

		List<String> outputRnames = runQueryForRnames(" select DISTINCT ?y where { ?x a rest:Restaurant . ?x rest:hasName ?y . }LIMIT 10", model);  //add the query string
		return outputRnames;

	}

	public String retrieveIDagainstRName(Model model, String rname)
	{

		String outputIDs = runQueryforID((" select DISTINCT ?id where {?x a rest:Restaurant . ?x rdfs:label ?id . ?x rest:hasName \"" + rname + "\". }"), model);  //add the query string
		return outputIDs;

	}

	public List<String> retrieveViolations(Model model, String restaurantID) throws IOException
	{

		List<String> outputViolations = runQueryforViolations(" select  ?date ?type ?category ?desc where { ?x a viol:Violation. ?x viol:OnRestaurantViolation \""+ restaurantID + "\" . OPTIONAL {?x viol:hasViolationDated ?date .}OPTIONAL { ?x viol:hasViolatedTypeOf ?type .}  OPTIONAL { ?x viol:hasViolationRiskCategoriedUnder ?category .}  OPTIONAL { ?x viol:hasViolationDescribedAs ?desc .}} ", model);  //add the query string
		return outputViolations;
	}

	public List<String> retrieveInspections(Model model, String restaurantID)
	{

		List<String> outputInspections = runQueryforInspections(" SELECT  ?score ?type ?date where { ?x a insp:Inspection. ?x insp:OnRestaurantInspection \"" + restaurantID + "\" . OPTIONAL { ?x insp:hasInspectionDated ?date. } OPTIONAL {?x insp:hasInspectionTypeOf ?type.} OPTIONAL {?x insp:hasInspectionScore ?score.}}", model);  //add the query string
		return outputInspections;

	}

	public List<String> retrieveDeals(Model model, String rname) throws IOException
	{

		List<String> outputDeals = runQueryforDeals((" select ?title ?expiry where {?x a deals:Deal. ?x deals:hasRName \"" + rname + "\". OPTIONAL { ?x deals:hasDTitle ?dealtitle .} OPTIONAL { ?x deals:hasExpiry ?exp .}}"), model);  //add the query string
		return outputDeals;

	}

	public List<String> retrieveRatings(Model model, String rname) throws IOException
	{

		List<String> outputRatings = runQueryforRatings((" select ?cuisine ?rating where {?x a rat:Restaurant . ?x rat:hasName \"" + rname + "\" . ?x rat:hasCuisine ?cuisine . ?x rat:hasRating ?rating .}"), model);  //add the query string
		return outputRatings;

	}
	public List<String> runQueryForRnames(String queryRequest, Model model)
	{

		StringBuffer queryStr = new StringBuffer();

		// Establish Prefixes
		//Set default Name space first
		queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
		queryStr.append("PREFIX health-inspection-data" + ": <" + newNameSpace2 + "> ");
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

			List<String> rnames = new ArrayList<String>();

			while(response.hasNext())
			{
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?y");

				if( name != null )
					rnames.add(name.toString());
				else
					System.out.println("No results found!");

			} 

			return rnames;

		}
		finally { qexec.close();}		
	}

	public String runQueryforID(String queryRequest, Model model)
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
			String outputIDs = new String();
			while(response.hasNext())
			{
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?id");

				if( name != null )
					outputIDs = name.toString();
				else
					System.out.println("No results found!");

			} 
			return outputIDs;


		}
		finally { qexec.close();}		
	}
	public List<String> runQueryforViolations(String queryRequest, Model model) throws IOException
	{

		StringBuffer queryStr = new StringBuffer();

		// Establish Prefixes
		//Set default Name space first
		queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
		queryStr.append("PREFIX health-inspection-data" + ": <" + newNameSpace2 + "> ");
		queryStr.append("PREFIX viol" + ": <" + "http://www.semanticweb.org/asu/health-inspection-data#" + "> ");
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

			List<String> descs = new ArrayList<String>();
			List<String> dates = new ArrayList<String>();
			List<String> scores = new ArrayList<String>();
			List<String> violation_types = new ArrayList<String>();
			dates.add("Dates \n");
			descs.add("Violation Description \n"); 
			scores.add("Score \n");
			violation_types.add("Violation Type \n");
			while(response.hasNext())
			{
				QuerySolution soln = response.nextSolution();
				RDFNode desc = soln.get("?desc");
				RDFNode date = soln.get("?date");
				RDFNode score = soln.get("?type");
				RDFNode violation_type = soln.get("?category");

				if(scores!= null)
				{
					scores.add(score.toString());
					scores.add("\n");
				}
				else
					System.out.println("No Friends found!");
				if(violation_types!= null)
				{
					violation_types.add(violation_type.toString());
					violation_types.add("\n");
				}
				else
					System.out.println("No Friends found!");
				if( date != null )
				{
					dates.add(date.toString()); 
					dates.add("\n");
				}
				else
					System.out.println("No Friends found!");
				if( desc != null )
				{
					descs.add(desc.toString());
					descs.add("\n");
				}
				else
					System.out.println("No Friends found!");

			} 
			dates.addAll(descs);
			dates.addAll(scores);
			dates.addAll(violation_types);
			return dates;


		}
		finally { qexec.close();}		
	}
	public List<String> runQueryforInspections(String queryRequest, Model model)
	{

		StringBuffer queryStr = new StringBuffer();

		// Establish Prefixes
		//Set default Name space first
		queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
		queryStr.append("PREFIX health-inspection-data" + ": <" + newNameSpace2 + "> ");
		queryStr.append("PREFIX insp" + ": <" + "http://www.semanticweb.org/asu/health-inspection-data#" + "> ");
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

			List<String> dates = new ArrayList<String>();
			List<String> scores = new ArrayList<String>();
			List<String> inspection_types = new ArrayList<String>();
			dates.add("Dates \n");
			scores.add("Scores \n");
			inspection_types.add("Inspection Type \n");
			while(response.hasNext())
			{
				QuerySolution soln = response.nextSolution();
				RDFNode date = soln.get("?date");
				RDFNode score = soln.get("?score");
				RDFNode inspection_type = soln.get("?type");
				if(score!= null)
				{
					scores.add(score.toString());
					scores.add("\n");
				}
				else
					System.out.println("No results found!");

				if(inspection_type!= null)
				{
					inspection_types.add(inspection_type.toString());
					inspection_types.add("\n");
				}
				else
					System.out.println("No results found!");

				if( date != null )
				{
					dates.add(date.toString());
					dates.add("\n");
				}
				else
					System.out.println("No results found!");

			} 
			dates.addAll(scores);
			dates.addAll(inspection_types);

			return dates;

		}
		finally { qexec.close();}		
	}

	public List<String> runQueryforDeals(String queryRequest, Model model) throws IOException
	{

		StringBuffer queryStr = new StringBuffer();

		// Establish Prefixes
		//Set default Name space first
		queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
		queryStr.append("PREFIX health-inspection-data" + ": <" + dealsNameSpace + "> ");
		queryStr.append("PREFIX deals" + ": <" + "http://www.semanticweb.org/asu/8coupons-deals-data#" + "> ");
		queryStr.append("PREFIX insp" + ": <" + "http://www.semanticweb.org/asu/health-inspection-data#" + "> ");
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

			List<String> titles = new ArrayList<String>();
			List<String> expiries = new ArrayList<String>();
			while(response.hasNext())
			{
				QuerySolution soln = response.nextSolution();
		
				RDFNode title = soln.get("?title");
				RDFNode expiry = soln.get("?expiry");

				if(title!= null)
					titles.add(title.toString());
				else
					System.out.println("No title of deals found!");

				if( expiry != null )
					expiries.add(expiry.toString());
				else
					System.out.println("No expiry of deals found!");

			} 

			System.out.println(titles); 
			System.out.println(expiries); 

			//LhbUserInterface obj = new LhbUserInterface();
			
			return titles;

		}
		finally { qexec.close();}		
	}

	public List<String> runQueryforRatings(String queryRequest, Model model)
	{

		StringBuffer queryStr = new StringBuffer();

		// Establish Prefixes
		//Set default Name space first
		queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
		queryStr.append("PREFIX rat" + ": <" + ratingsNameSpace + "> ");
		//queryStr.append("PREFIX rat" + ": <" + "http://www.semanticweb.org/asu/yelp-review-data#" + ">");
		queryStr.append("PREFIX insp" + ": <" + "http://www.semanticweb.org/asu/health-inspection-data#" + "> ");
		queryStr.append("PREFIX rdfs" + ": <" +  "http://www.w3.org/2000/01/rdf-schema#" + "> ");
		queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
		queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + "> ");
		//queryStr.append("PREFIX schemaorg" + ": <" + "http://schema.org/" + "> ");

		//Now add query
		queryStr.append(queryRequest);
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try 
		{
			ResultSet response = qexec.execSelect();

			List<String> cuisines = new ArrayList<String>();
			List<String> ratings = new ArrayList<String>();
		//	List<String> inspection_types = new ArrayList<String>();
			cuisines.add("Cuisines \n");
			ratings.add("Ratings \n");
		//	inspection_types.add("Inspection Type \n");
			while(response.hasNext())
			{
				QuerySolution soln = response.nextSolution();
				RDFNode cuisine = soln.get("?cuisine");
				RDFNode rating = soln.get("?rating");
			//	RDFNode inspection_type = soln.get("?type");
				if(cuisine!= null)
				{
					cuisines.add(cuisine.toString());
					cuisines.add("\n");
				}
				else
					System.out.println("No results found!");

//				if(inspection_type!= null)
//				{
//					inspection_types.add(inspection_type.toString());
//					inspection_types.add("\n");
//				}
//				else
//					System.out.println("No results found!");

				if( rating != null )
				{
					ratings.add(rating.toString());
					ratings.add("\n");
				}
				else
					System.out.println("No results found!");

			} 
			cuisines.addAll(ratings);

			return cuisines;

		}
		finally { qexec.close();}		
	}
	public void violationsRdftoModel() throws IOException 
	{
		violationsModel = ModelFactory.createOntologyModel();
		InputStream inFoafInstance = 
				FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/violations.rdf");
		violationsModel.read(inFoafInstance, newNameSpace2);
		inFoafInstance.close();
	}

	public void inspectionsRdftoModel() throws IOException 
	{
		inspectionsModel = ModelFactory.createOntologyModel();
		InputStream inFoafInstance = 
				FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/inspections.rdf");
		inspectionsModel.read(inFoafInstance, newNameSpace2);
		inFoafInstance.close();
	}

	public void dealsRdftoModel() throws IOException 
	{
		dealsModel = ModelFactory.createOntologyModel();
		InputStream inFoafInstance = 
				FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/Deals.rdf");
		dealsModel.read(inFoafInstance, dealsNameSpace);
		inFoafInstance.close();
	}

	public void ratingsRdftoModel() throws IOException 
	{
		ratingsModel = ModelFactory.createOntologyModel();
		InputStream inFoafInstance = 
				FileManager.get().open("/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/Ratings.rdf");
		ratingsModel.read(inFoafInstance, ratingsNameSpace);
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
		for(int i=0;i<res.size();i++)
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
		inferredFriends = ModelFactory.createInfModel(reasoner, restaurantModel); 

	}

	public void writeRDF() throws IOException{
		String OUTPUT_FILE = "/Users/agmip/Documents/workspace/LinkedHealthyBites/src/Ontologies/inferred.rdf";

		OutputStream out = new FileOutputStream(OUTPUT_FILE);
		inferredFriends.write(out, "RDF/XML");
		out.close();

	}


}




