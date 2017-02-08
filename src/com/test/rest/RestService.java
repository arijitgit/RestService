package com.test.rest;

import java.util.*;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;


@Path("/message")
public class RestService {
	
	
	// http://localhost:8080/TestRest/rest/message/getItems
	
	@GET
	@Path("/getItems")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getItems() throws JSONException {	
		JSONObject result = new JSONObject(); 
		try {	           
        Map<String, String> objLst = new HashMap<String, String>();
        objLst.put("ItemId","001");
        objLst.put("price","20012");
        objLst.put("message","sucess");
        result.put("result ", objLst);        
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(result.toString()).build();
	}
	
	
	// http://localhost:8080/TestRest/rest/message/getUsers
	@POST
	@Path("/getUsers1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public RestDto getUsers1(RestDto obj) throws JSONException {	
		System.out.println("ID "+obj.getId());
		RestDto objRestDto = new RestDto();		
		objRestDto.setId(12);
		objRestDto.setName("Arijit");
		System.out.println("objRestDto @@@==>  "+objRestDto);
		return objRestDto;
	}
	
	

	// http://localhost:8080/TestRest/rest/message/arijit
	@GET
	@Path("/{param}")
	@Produces("application/json")	
	public Response xmlParseAndDBInsert(@PathParam("param") String xmlString) throws JSONException {
		JSONObject result = new JSONObject();  // XML should be sent by POST can be JSON payload
		try {	           
        Map objLst = new HashMap();
        objLst.put("ItemId","001");
        objLst.put("price","20012");
        objLst.put("message","sucess");
        result.put("result"+xmlString, objLst);        
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(result.toString()).build();
	
	}
	
	
	// http://localhost:8080/TestRest/rest/message/users
	
	
	


}