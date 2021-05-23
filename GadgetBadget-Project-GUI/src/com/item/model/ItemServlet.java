package com.item.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class ItemServlet {

	private static Connection connect() {

		Connection con = null;

		try {

			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/os_fornt_end", "root", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;

	}

	
	public static String readitems() {
		
		//System.out.println("GET");
		
		String output = "";

		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed

			output = "<div class=''><table class='table table-hover table-bordered table-striped table-bordered' style='width:100%' style='text-align:center'><thead class='thead-dark'>"
					+ "<th style='padding:10px; text-align:center;'>Item Code</th>"
					+ "<th style='padding:10px; text-align:center;'>Item Category</th>"
					+ "<th style='padding:10px; text-align:center;'>Item Name</th>"
					+ "<th style='padding:10px; text-align:center;'>Item Description</th>"
					+ "<th style='padding:10px; text-align:center;'>Item Price</th>"
					+ "<th style='padding:10px; text-align:center;'>Item date</th>"
					+ "<th style='padding:10px; text-align:center;'>Status</th>"
					+ "<th style='padding:10px; text-align:center;'>Update</th><th class='text-center'>Remove</th></thead>";

			String query = "SELECT * FROM item";
//			System.out.println(query);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String itemCode = Integer.toString(rs.getInt("item_code"));
				String itemCategory = rs.getString("item_category");
				String itemName = rs.getString("item_name");
				String itemDesc = rs.getString("item_des");
				String itemPrice = rs.getString("price");
				String itemDate = rs.getString("date");
				String itemStatus = rs.getString("status");

//				System.out.println(itemCode);
//				System.out.println(itemName);

				// Add into the html table
				output += "<tbody><td style='padding:10px; text-align:center;'>" + itemCode + "</td>";
				output += "<td style='padding:10px; text-align:center;'>" + itemCategory + "</td>";
				output += "<td style='padding:10px; text-align:center;'>" + itemName + "</td>";
				output += "<td style='padding:10px; text-align:center;'>" + itemDesc + "</td>";
				output += "<td style='padding:10px; text-align:center;'>" + itemPrice + "</td>";
				output += "<td style='padding:10px; text-align:center;'>" + itemDate + "</td>";
				output += "<td style='padding:10px; text-align:center;'>" + itemStatus + "</td>";

				// buttons
				
				output += "<td class='text-center'><input id='update' onclick='click_update();' name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-success'></td>"
						+ "<td class='text-center'><button class='btnRemove btn btn-danger' name='btnRemove' id ='btnRemove' value='"+ itemCode +"' >Remove</button></td></tbody>";
				
			}
			con.close();
			// Complete the html table
			output += "</table></div>";
		} catch (Exception e) {
			output = "Error while reading the item details...!";
			System.out.println(e.getMessage());
			System.out.println(e);
			e.printStackTrace();
		}
		return output;
	}
	
	// insert
		public String insertitem(String item_category, String item_name, String item_des, String price,
				String date) {

			String output = "";

			try {

				Connection con = connect();
				if (con == null) {
					return "Error Inserting";
				}

				String query = "INSERT INTO item (item_category, item_name, item_des, price, date) VALUES (?, ?, ?, ?, ?)";

				PreparedStatement ps = con.prepareStatement(query);

//						ps.setInt(1, 0);;
				ps.setString(1, item_category);
				ps.setString(2, item_name);
				ps.setString(3, item_des);
				ps.setString(4, price);
				ps.setString(5, date);

				ps.execute();
				con.close();

				String newitem = readitems();
				output = "{\"status\":\"success\", \"data\": \"" + newitem + "\"}";

				//output = "item Details have been Inserted Successfully !";

			} catch (Exception e) {
				//output = "item Details Inserted Failed";
				System.out.println(e.getMessage());
				System.out.println(e);
				e.printStackTrace();
				output = "{\"status\":\"error\", \"data\": \"Error while inserting the order.\"}";
				
			}

			return output;
		}
	
	
		// update
		public String updateitem(String itemCode, String item_category, String item_name, String item_des,
				String price, String date) {

			String output = "";

			try {

				Connection con = connect();
				if (con == null) {
					return "Error while connecting to the database for updating.";
				}

				String query = "UPDATE item SET item_category=?, item_name=?, item_des=?, price=?, date=? where item_code=?";
				PreparedStatement ps = con.prepareStatement(query);

				ps.setString(1, item_category);
				ps.setString(2, item_name);
				ps.setString(3, item_des);
				ps.setString(4, price);
				ps.setString(5, date);
				ps.setString(6, itemCode);

				ps.execute();
				con.close();

				String newitem = readitems();

			output = "{\"status\":\"success\", \"data\": \"" + newitem + "\"}";

				//output = "item details have been updated successfully...!";

			} catch (Exception e) {

				output = "{\"status\":\"error\", \"data\": \"Error while updating the order.\"}";

				//output = "Error while updating item details...!";
				System.err.println(e.getMessage());
				System.out.println(e.getMessage());
				System.out.println(e);
				e.printStackTrace();
			}

			return output;

		}

		// delete
		public String deleteitem(String item_code) {

			String output = "";

			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for updating.";
			}

			try {

				String query = "DELETE FROM item WHERE item_code=?";
				PreparedStatement ps = con.prepareStatement(query);

				//System.out.println(query);
				System.out.println(item_code);

				ps.setInt(1, Integer.parseInt(item_code));

				ps.execute();
				con.close();

				String newitem = readitems();

				output = "{\"status\":\"success\", \"data\": \"" + newitem + "\"}";

				//output = "item has been deleted successfully";

			} catch (SQLException e) {
				
			output = "{\"status\":\"error\", \"data\": \"Error while deleting the order.\"}";

				//output = "Error while deleting the item from the database.";
				System.err.println(e.getMessage());
				System.out.println(e.getMessage());
				System.out.println(e);
				e.printStackTrace();

			}

			return output;

		}

}
