package com.item.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.item.model.ItemServlet;

@Path("/Items")
public class ItemService {

	ItemServlet itemObjt = new ItemServlet();

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String readItems() {
		
		return itemObjt.readitems();

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertitem(@FormParam("item_category") String item_category, @FormParam("item_name") String item_name,
			@FormParam("item_des") String item_des, @FormParam("price") String price, @FormParam("date") String date) {

		String output = itemObjt.insertitem(item_category, item_name, item_des, price, date);
		return output;

	}

	@PUT
	@Path("/")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)

	public String updateitem(String itemData) {

		JsonObject itemObject = new JsonParser().parse(itemData).getAsJsonObject();

		String itemCode = itemObject.get("itemCode").getAsString();
		String item_category = itemObject.get("item_category").getAsString();
		String item_name = itemObject.get("item_name").getAsString();
		String item_des = itemObject.get("item_des").getAsString();
		String price = itemObject.get("price").getAsString();
		String date = itemObject.get("date").getAsString();

		String output = itemObjt.updateitem(itemCode, item_category, item_name, item_des, price, date);
		return output;

	}

	@DELETE
	@Path("/")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_XML)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String deleteitem(String itemData) {
//		System.out.println("IN Delete");

		Document ducument = Jsoup.parse(itemData, "", Parser.xmlParser());
//		System.out.println("IN doc");

		String itemID = ducument.select("itemCode").text();
		String output = itemObjt.deleteitem(itemID);
		System.out.println(output);
		return output;

	}

}
