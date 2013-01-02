

import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.String;
import java.net.*;


public class PrintView  {

    public void printHeader(PrintWriter out, String title, HttpSession session){
   /*
      Prints header of CMT
   */

   String role = "";

   if(session != null) {
   		 role = (String) session.getAttribute("role");
   	}

   boolean isLoggedIn = role != "";

   out.println(
	   	"<!DOCTYPE html>"
		+ "<html>"
		+ "<head>"
		+	"<link rel=\"stylesheet\" type=\"text/css\" href=\"" + getRoot() + "http://reset5.googlecode.com/hg/reset.min.css\">"
		+	"<link  href=\"" + getRoot() + "cmt_assets/css/bootstrap.min.css\" type=\"text/css\" rel=\"stylesheet\" />"
		+	"<link  href=\"" + getRoot() + "cmt_assets/css/bootstrap-responsive.min.css\" type=\"text/css\" rel=\"stylesheet\" />"
		+	"<link  href=\"" + getRoot() + "cmt_assets/css/style.css\" type=\"text/css\" rel=\"stylesheet\" />"
		+ "</head>"

		+ "<div id=\"masthead\">"

		+ "<header>"
		+	"<span>UCLA</span><em>CMT</em>" 
		+"</header>"
		+ "<h2>"+ title +"</h2>" //title of page so user knows where he is navigating
   );
		 
   if(!isLoggedIn) {
		printLogin(out);//prints login form
	} else {
		out.println(
			 "<div id=\"header_panel\">"
			+	"<span>" + session.getAttribute("email") + " : " + role + "    </span> | " 
			+ generateLink(getRoot() + getPrefix() + "?page=account_settings" , "Account Settings")
			+ "|"  + generateLink(getRoot() + getPrefix() + "?page=log_out", "Logout")
			+ "</div>"
		);
	}
    
   

	out.println("</div>" //end masthead
		       + getRoleNavigation(role) 
		       + "<div id=\"container\">"
	);
	 if(!isLoggedIn) {

	 	out.println(
		 		"<form id=\"inputInformation\" class=\"paper\">"
		 	   + "<h2>Sign Up</h2>"
			   + "<label> email </label>"
			   +   generateInput("email","")
		   	   + "<label> last_name </label>"
		   	   +   generateInput("last_name","")
		  	   + "<label>middle_name</label>"
		  	   +   generateInput("middle_name","")
		   	   + "<label>	first_name </label>"
		   	   +   generateInput("first_name","")
		  	   + "<label>	affiliation </label>"
		  	   +   generateInput("affiliation","")

		  	   + generateHiddenInput("page","sign_up") 
		  	   + "<input type=\"submit\" value=\"Sign Up\">"
		  	   + "</form>"
	  	   );
	}

   	



    }

    public void printLogin(PrintWriter out){
	    out.println(
	    	 "<form id=\"login\" method=\"POST\" action=\"" + getRoot() + getPrefix()  + "\">"
				
			+	"<input type=\"text\" name=\"username\" placeholder=\"Username\"/>"
				
			+	"<input type=\"password\" name=\"password\" placeholder=\"Password\" />"

			+	"<input type=\"submit\" value=\"Sign In\" class=\"btn btn-info\" />"
			+   "<input type=\"hidden\" name=\"page\" value=\"log_in\" />"
			+ "</form>"
	    );
    }

    public String getRoleNavigation(String role){

    //returns navigation string // if role is not define simply returns empty string

    	String navigation = "<nav><ul id=\"icons\">";

    	switch(role) {
    		case "CHAIR":

		     navigation +=  "<li>"+ generateLink( getRoot() + getPrefix() + "?page=chair","<i class=\"icon-search icon-white\"></i> View Conferences") + "</li>"
						+		"<li>"+ generateLink( getRoot() + getPrefix() + "?page=new_conference_form","<i class=\"icon-plus icon-white\"></i> Add Conference") + "</li>";
						break;

    		case "AUTHOR": 

			 navigation +=      "<li>" + generateLink( getRoot() + getPrefix() + "?page=author", "<i class=\"icon-plus icon-white\"></i> View Papers") +"</li>"
						+		"<li>"  + generateLink( getRoot() + getPrefix() + "?page=add_paper_form" , " <i class=\"icon-plus icon-white\"></i> Add Paper") + "</li>";
						break;

    		case "REVIEWER":

		     navigation +=  "<li>" +  generateLink( getRoot() + getPrefix() + "?page=reviewer","<i class=\"icon-search icon-white\"></i> View Reviews") + "</li>"
						+		"<li>" + generateLink( getRoot() + getPrefix() + "?page=author", "<i class=\"icon-search  icon-white\"></i> View Papers") +"</li>"
						+		"<li>"  + generateLink( getRoot() + getPrefix() + "?page=add_paper_form" , " <i class=\"icon-plus icon-white\"></i> Add Paper") + "</li>";
						break;

    		default:
    			navigation = "";
    	}

    	if(navigation != "") { //implies there is no role
    		navigation += "</ul></nav>";
    	}

    	return navigation;
    }

	public void printFooter(PrintWriter out) {
	/*
	  Prints end of container, footer and closing body and html  tags
	*/
	    out.println(

	      "</div> <!-- end container -->"


	    + "<div class=\"clearfix\"></div>"
	    + "<footer>"
	    +    "By <span>Anthony Su</span> and <span>Vincent Kyi</span>"
	    + "</footer>"

	    + "</body>"
	    + "</html>"

	    );
	}


	public void printException(SQLException ex, PrintWriter out)
	{
		out.println("SQLException caught<br>");
		out.println("---<br>");
		while (ex != null) {
			out.println("Message   : " + ex.getMessage() + "<br>");
			out.println("SQLState  : " + ex.getSQLState() + "<br>");
			out.println("ErrorCode : " + ex.getErrorCode() + "<br>");
			out.println("---<br>");
			ex = ex.getNextException();
		}
	}

    public String generateInput(String name , String value) {
    	return "<input type=\"text\" name=\"" + name + "\" value=\"" +  value +"\">";
    }

	public String generateListItem(String attr, String val) {

	  //attr - attribute
	        //val - value
	  return "<li><span>" + attr +"</span>"+ val + "</li>";

	}


	public String getRoot() {
	   return "http://cs143.seas.ucla.edu:8080/cs143vky/";
	}

	public String getPrefix() { //prefix for CMT
		return "servlet/CMT";
	}

	public String generateURL(String pageName,String[]parameters,String[] values) throws UnsupportedEncodingException {
	  //@package java.net.URLEncoder
	  //http://docs.oracle.com/javase/1.5.0/docs/api/java/net/URLEncoder.html


	    String url = getRoot();
	    String query = "?page=" + pageName ;

	    for(int i = 0; i < parameters.length; i++) {
	       
	       query += "&" + parameters[i] + "=" + URLEncoder.encode(values[i],"ISO-8859-1");

	    }

	    return url + query;

	}

	public String decodeParameter(String parameter) throws UnsupportedEncodingException  {
    //@package java.net.URLEncoder
   
   return URLDecoder.decode(parameter, "UTF-8");

   }

	public String generateLink(String src, String title) {
	  //generates an anchor element 
	  String prefix = "/servlet/Scholar";

	  return "<a href=\""+ src +"\">" + title + "</a>";
	}

	public String generateHiddenInput(String name , String value) {
		return "<input type=\"hidden\" name=\"" + name  + "\" value=\"" +  value + "\">";
	}

	public void printMessage (String msg, boolean isTrue,PrintWriter out) {
	//for error handling

	  out.println( "<p class=\"" + ( (isTrue) ?  "green" : "red") +  " paper \">" + msg + "</p>");

	}


	/* */

	public void printChairInfo(ResultSet CIDs, Connection con, HttpServletRequest request, PrintWriter out){
			try{
				//search for conferences of that chair user
				Integer count = 0;

				while(CIDs.next()){
					Statement s1 = con.createStatement();
					ResultSet conferences = s1.executeQuery("SELECT * FROM Conference WHERE cid="+ CIDs.getInt("cid"));

					String cid = Integer.toString(CIDs.getInt("cid")); //passed to query string

					//print out conferences

					if(conferences.next()) {

					    count ++;

						String cname = conferences.getString("cname");
						java.sql.Timestamp start_time = conferences.getTimestamp("submission_start_time"); //use start_time.toString() to convert to string
						java.sql.Timestamp end_time = conferences.getTimestamp("submission_end_time");
						int reviews_per_paper = conferences.getInt("num_reviews_per_paper");
						int max_reviews_per_reviewer = conferences.getInt("max_reviews_per_reviewer");

						out.println("<ul class=\"paper list\"> "
							         + generateListItem("Conference Name: ",cname)
							         + generateListItem("Start Time: ",start_time.toString())
							         + generateListItem("End Time: ",end_time.toString())
							         + generateListItem("Required Reviews Per Paper: ",Integer.toString(reviews_per_paper))
							         + generateListItem("Maximum Reviews Per Reviewer: ",Integer.toString(max_reviews_per_reviewer))
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=add_reviewer_form&cid=" + cid,"Add Reviewer") + "</li>"
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=assign_reviewer_form&cid=" + cid, "Assign Page to Reviewer") + "</li>"
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=trigger_final_decision&cid=" + cid, "Make Final Decision") + "</li>"
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=modify_conference_form&cid=" + cid, "Modify Conference Info") + "</li>"
							         + "</ul>");

						s1.close();
						conferences.close();
				    }
				}//end while loop

				out.println( "<h3 id=\"display_count\">Displaying " + Integer.toString(count) + " Conferences</h3>");
			
			}catch(SQLException e)
			{
				printException(e, out);
			}

		}

		public void printAuthorInfo(ResultSet PIDs, Connection con, HttpServletRequest request, PrintWriter out){
			try{

				Integer count = 0;
				//search for papers written by author
				while(PIDs.next()){

					int paper_id = PIDs.getInt("paper_id");
					Statement s0 = con.createStatement();
					ResultSet papers = s0.executeQuery("SELECT * FROM Paper WHERE paper_id="+ paper_id);

					//print out papers
					String title = "";
					String abstr = "";
					if(papers.next())
					{
						count ++;
						title = papers.getString("title");
						abstr = papers.getString("abstract");
					}

					//show the status of the paper
					Statement s1 = con.createStatement();
					ResultSet decisions = s1.executeQuery("SELECT decision FROM Status WHERE paper_id="+ paper_id);
					String decision = "";
					if(decisions.next()) {
						int d = decisions.getInt("decision");
						if(d == 0)
							decision = "In Review";
						else if(d > 4)
							decision = "Accepted";
						else
							decision = "Rejected";
					}
					out.println("<ul class=\"paper list float-left\"> "
							         + generateListItem("Title: ",title)
							         + generateListItem("Abstract: ",abstr)
							         + generateListItem("Decision: ",decision)
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=add_coauthor_form&paper_id=" + paper_id,"Add Co-author") + "</li>"
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=modify_paper_form&paper_id=" + paper_id, "Modify Paper") + "</li>"
							         + "</ul>");



					s0.close();
					papers.close();
					s1.close();
					decisions.close();
				}//end while

			    out.println( "<h3 id=\"display_count\">Displaying " + Integer.toString(count) + " papers</h3>");
			}catch(SQLException e)
			{
				printException(e, out);
			}

		}

		public void printReviewerInfo(ResultSet PIDs, Connection con, HttpServletRequest request, PrintWriter out){
			try{
				//search for all papers to be reviewed
				Integer count = 0;
				while(PIDs.next()){

					int paper_id = PIDs.getInt("paper_id");
					Statement s0 = con.createStatement();
					ResultSet papers = s0.executeQuery("SELECT * FROM Paper WHERE paper_id="+ paper_id);

					//print out papers

					//print out papers
					String title = "";
					String abstr = "";
					if(papers.next())
					{
						count ++;
						title = papers.getString("title");
						abstr = papers.getString("abstract");
					}

					//show the status of the paper
					Statement s1 = con.createStatement();
					ResultSet decisions = s1.executeQuery("SELECT decision FROM Status WHERE paper_id="+ paper_id);
					String decision = "";
					if(decisions.next()) {
						int d = decisions.getInt("decision");
						if(d == 0)
							decision = "In Review";
						else if(d > 4)
							decision = "Accepted";
						else
							decision = "Rejected";
					}

					out.println("<ul class=\"paper list float-left\"> "
							         + generateListItem("Title: ",title)
							         + generateListItem("Abstract: ",abstr)
							         + generateListItem("Decision: ",decision)
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=review_form&paper_id=" + paper_id,"Modify Review") + "</li>"
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=read_paper&paper_id=" + paper_id, "Read Paper") + "</li>"
							         + "</ul>");
					s0.close();
					papers.close();
					s1.close();
					decisions.close();
				}
				out.println( "<h3 id=\"display_count\">Displaying " + Integer.toString(count) + " paper to review</h3>");
			}catch(SQLException e)
			{
				printException(e, out);
			}

		}
		public void viewPaper(ResultSet paper, Connection con, HttpServletRequest request, PrintWriter out){
			try{
					if(paper.next()){

					int paper_id = paper.getInt("paper_id");

					//print out paper

					String title = paper.getString("title");
					String abstr = paper.getString("abstract");
					String content = paper.getString("content");
					out.println("<ul class=\"paper list float left\"> "
							         + generateListItem("Title: ",title)
							         + generateListItem("Abstract: ",abstr)
							         + generateListItem("Content: ",content)
							         + "<li>" + generateLink(getRoot() + getPrefix() + "?page=review_form&paper_id=" + paper_id,"Modify Review") + "</li>"
							         + "</ul>");
					}	
			}catch(SQLException e)
			{
				printException(e, out);
			}

		}

	

		public void printForm(PrintWriter out,ResultSet values , String pageName,String form_type, String title) throws SQLException {
		/*
		  PRINTS form for specific page
		  Add Paper 
		  Add Author/CoAuthor
		  Add Citation 
		  Ask Question

		  in  the case we're we have to print Author and CoAuthor, we need to arrays of options
		*/

		  out.println(
		      "<form id=\"inputInformation\" class=\"paper\" method=\"get\" action=\"" + getRoot() + getPrefix() + "\">" //begin form
		    + "<h2>"+ title +"</h2>"
		  );

	    String type_string = "string" 
		       , type_int = "integer"
		       , type_timestamp = "timestamp";

		  Map map = getFormFields(form_type);


		  if(values != null) {
		  	values.next();
		  }

		  Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		  while (it.hasNext()) {

		   /* Sample input fields
		      <label>author_ID</label>
		      <input type="text" name="author_ID">
		   */
			  Map.Entry<String, String> entry = it.next();

			  String key = entry.getKey();
			  String value = "";
			  String data_type = (String)map.get(key);

			  

				  if(data_type == type_string && values !=null) {

				  	value =  values.getString(key);

				  } else if (data_type == type_int  && values !=null) {

				  	value = Integer.toString(values.getInt(key));

				  } else if (data_type == type_timestamp  && values !=null) {

				  	value = values.getTimestamp(key).toString();

				  }

			

			   switch(form_type) {

			   	case "modify_conference_form":
			   	if(values != null) {
			   	 		  out.println(generateHiddenInput("cid",Integer.toString(values.getInt("cid"))));
			   	 }
			   	 		  break;
			   	case "modify_paper_form":
			   	case "modify_review_form":
			   	 	if(values != null) {
			   	 		  out.println(generateHiddenInput("paper_id",Integer.toString(values.getInt("paper_id"))));
			   	 	}
			   	break;
			 
			   	 default:
			   	      ; // do nothing
			  }

					    out.println(
					        "<label>" + entry.getKey() +"</label>"
					        );

					      if( (String)key == "comments") {
					        out.println("<textarea rows=\"8\" cols=\"50\" name=\"" + (String)key +"\">"+ value +"</textarea>");
					      } else {
					      	 out.println("<input type=\"text\" name=\"" + (String)key + "\"" + " value=\"" + value  + "\">");
					      }
					    
					   
			}//end it.hasNext
		

		  
		  out.println(
		    "<input type=\"hidden\" name=\"page\" value=\""+ pageName +"\">"
		  + "<input type=\"submit\" value=\"Submit\">"
		  + "</form>" //end form
		  );




		}

		public void printAddCoAuthorForm(PrintWriter out,Integer paper_id) {
			  out.println(
			      "<form id=\"inputInformation\" class=\"paper\" method=\"get\" action=\"" + getRoot() + getPrefix() + "\">" //begin form
			    + "<h2> Add CoAuthor to Paper " + paper_id+ "</h2>"
			  );


			  out.println(
			  "<label>email :</label>" +	generateInput("email","")
			  + "<input type=\"hidden\" name=\"page\" value=\"add_coauthor\">"
			  + generateHiddenInput("paper_id",Integer.toString(paper_id))
			  + "<input type=\"submit\" value=\"Add CoAuthor\">"
			  + "</form>" //end form
			  );
		}

		public void printAddPaperForm (PrintWriter out, Connection con) throws SQLException {
			java.util.Date cur_date = new java.util.Date();	
			Timestamp now = new Timestamp(cur_date.getTime());
			Statement s1 = con.createStatement();
			ResultSet conferences = s1.executeQuery("SELECT cid, cname FROM Conference WHERE submission_end_time > \'"+now+"\'");


			  out.println(
			      "<form id=\"inputInformation\" class=\"paper\" method=\"get\" action=\"" + getRoot() + getPrefix() + "\">" //begin form
			    + "<h2> Add Paper </h2>"
			  );


			  Map map = getFormFields("paper_form");


			  Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			  while (it.hasNext()) {

				  Map.Entry<String, String> entry = it.next();

				  String key = entry.getKey();
				  String data_type = (String)map.get(key);

				

				    out.println(
				        "<label>" + entry.getKey() +"</label>"
				      

				   );


			      if( (String)key == "abstract" || (String)key=="content") {
				        out.println("<textarea rows=\"8\" cols=\"50\" name=\"" + (String)key +"\"></textarea>");
			      } else {
			      	 out.println("<input type=\"text\" name=\"" + (String)key + "\">");
			      }
				}//end while loop

			  out.println(
			    "<label>Select Conference</label>"
			  + "<select name=\"cid\" >"
			  );

			while(conferences.next()) {
				String cid = conferences.getString("cid")
				     , cname = conferences.getString("cname");
				out.println("<option value=\"" + cid +"\">" + cname + "</option>");

			}


		  out.println(
		  	"</select>"
		  + generateHiddenInput("page","submit_new_paper")
		  + "<input type=\"submit\" value=\"Submit Paper\">"
		  + "</form>" //end form
		  );

		}
		public void printAddReviewerForm(PrintWriter out, ResultSet authors, int cid ) throws SQLException {
			 
			authors.beforeFirst();
			 out.println(
			      "<form id=\"inputInformation\" class=\"paper\" method=\"get\" action=\"" + getRoot() + getPrefix() + "\">" //begin form
			    + "<h2> Add Reviewer</h2>"
			    + "<label>Select Reviewer</label>"
			    + "<select name=\"email\" >"
			  );
			while(authors.next()) {
				String email = authors.getString("email");
				out.println("<option value=\"" + email +"\">" + email + "</option>");

			}

			 out.println(
		   "</select>"
		  +  "<input type=\"hidden\" name=\"page\" value=\"add_reviewer\">"
		  + generateHiddenInput("cid", Integer.toString(cid))
		  + "<input type=\"submit\" value=\"Add Reviewer\">"
		  + "</form>" //end form
		  );
		}

		public void printAssignReviewerForm(PrintWriter out, ResultSet authors, ResultSet papers, int cid ) throws SQLException {
			 out.println(
			      "<form id=\"inputInformation\" class=\"paper\" method=\"get\" action=\"" + getRoot() + getPrefix() + "\">" //begin form
			    + "<h2> Add Reviewer</h2>"
			    + "<label>Select Reviewer</label>"
			    + "<select  name=\"uid\">"
			  );
			while(authors.next()) {
				String email = authors.getString("email");
				String uid = authors.getString("uid");
				out.println("<option value=\"" + uid +"\">" + email + "</option>");

			}
			out.println(
			 "</select>"
			+ "<label>Select Paper</label>"
			+ "<select  name=\"paper_id\">"
			);

			while(papers.next()) {
				String paper_id = papers.getString("paper_id")
				      , title = papers.getString("title");
				out.println("<option value=\"" + paper_id +"\">" + title + "</option>");

			}

			 out.println(
		   "</select>"
		  + generateHiddenInput("page", "assign_reviewer")
		  + generateHiddenInput("cid", Integer.toString(cid))
		  + "<input type=\"submit\" value=\"Add Reviewer\">"
		  + "</form>" //end form
		  );
		}

		public Map<String,String> getFormFields(String option) {

		  final String type_string = "string" 
		       , type_int = "integer"
		       , type_timestamp = "timestamp";

		  Hashtable account_settings = new Hashtable<String, String>() {{ 
		  															  put("middle_name", type_string);
		  															  put("last_name", type_string); 
		  															  put("first_name", type_string);
		  															  put("affiliation", type_string);
		  															}};
		   Hashtable conference_fields = new Hashtable<String, String>() {{
		   																	put("cname", type_string);
		   																	put("submission_start_time", type_timestamp);
		   																	put("submission_end_time", type_timestamp);
		   																	//put("num_reviews_per_paper", type_int);

		   																 }};
	   	   Hashtable new_conference_fields = new Hashtable<String, String>() {{
	   																	put("cname", type_string);
	   																	put("submission_start_time", type_timestamp);
	   																	put("submission_end_time", type_timestamp);
	   																	put("num_reviews_per_paper", type_int);
	   																	put("max_reviews_per_reviewer", type_int);

	   																 }};
		   	Hashtable paper_fields = new Hashtable<String, String>() {{ 
		  															  put("content", type_string);
		  															  put("abstract", type_string); 
		  															  put("title", type_string);
		  															}};

		  	Hashtable review_fields = new Hashtable<String, String>() {{ 
		  															  put("comments", type_string);
		  															  put("rating", type_int); 
		  															}};
		  	Hashtable new_coauthor_fields = new Hashtable<String, String>(){{ 
		  																		put("email",type_string);
		  																   }};
	/*	   				 
| cname                    | varchar(60) | YES  |     | NULL                |                             |
| submission_start_time    | timestamp   | NO   |     | CURRENT_TIMESTAMP   | on update CURRENT_TIMESTAMP |
| submission_end_time      | timestamp   | NO   |     | 0000-00-00 00:00:00 |                             |
| num_reviews_per_paper    | int(5)      | YES  |     | NULL                |                             |
*/

		  switch(option) {
		    case "account_settings":
		      return account_settings;
		    case "add_coauthor_form":
		    	return new_coauthor_fields;
		    case "new_conference_form":
		      return new_conference_fields;
		    case "modify_conference_form":
		      return conference_fields;
		    case "paper_form":
		    case "modify_paper_form":
		      return paper_fields;
		    case "modify_review_form":
		      return review_fields;
		  

		    default:
		      return null;
		  }

		}

			

		}
