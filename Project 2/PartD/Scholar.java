import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.String;
import java.net.*;


public class Scholar extends HttpServlet {
	private static PrintWriter out;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		//
		// Register MySQL JDBC driver
		//

		try {
			// register the MySQL JDBC driver with DriverManager
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		// get the output stream for result page
		//

		response.setContentType("text/html");
		out = response.getWriter();

		try {
			//
			// Connect to the database
			//

			Connection con = null;

			//
			// URL is jdbc:mysql:dbname
			// Change CS143 to the right database that you use
			//
			String url = "jdbc:mysql://localhost/cs143vky";
			String userName = "cs143vky";
			String password = "qs1m_by5";

			// connect to the database, user name and password can be specified
			// through this method
			con = DriverManager.getConnection(url, userName, password);

			//
			// Execute a SQL statement and print out the result
			//

			// Execute a SQL statement
			Statement stmt = con.createStatement();
			

			String page = request.getParameter("page");

			// print out the result

			//switch statement for operations
			switch(page) {
				case "paper":
							printHeader(out, "Paper");
							showPaperInfo(stmt, con, request, out);
							break;
				case "author":
							printHeader(out, "Author");
							showAuthorInfo(stmt, con, request, out);
							break;
				case "search_author":
							printHeader(out, "Search Author");
							ResultSet rs1 = searchAuthorInfo(stmt, con, request);
							if(rs1==null)
								break;
							printAuthorSearch(rs1, out);	
							break;
				case "search_paper":
							printHeader(out, "Search Paper");	
							ResultSet rs2 = searchPaperInfo(stmt, con, request);
							if(rs2==null)
								break;
							printPaperSearch(rs2, out);
							break;
				case "paper_form":
							printHeader(out, "Paper Form");
							printForm(out, "add_paper", "Paper", getRoot()+"servlet/Scholar", "", "Add Paper", "");
							break;
				case "author_form":
							printHeader(out, "Author Form");
							printForm(out, "add_author", "Author", getRoot()+"servlet/Scholar", "CoAuthored", "Add Author", "Add Coauthor");
							break;
				case "question_form":
							printHeader(out, "Question Form");
							printForm(out, "add_question", "Question", getRoot()+"servlet/Scholar", "", "Add Question or Comment", "");
							break;
				case "citation_form":
							printHeader(out, "Citation Form");
							printForm(out, "add_citation", "Cite", getRoot()+"servlet/Scholar", "", "Add Citation", "");
							break;
				case "add_author":
							printHeader(out, "Add Author");
							addAuthorInfo(stmt, con, request);
							addCoAuthoredInfo(stmt, con, request, out);
							break;
				case "add_paper":
							printHeader(out, "Add Paper");
							addPaperInfo(stmt, con, request, out);
							break;
				case "add_citation":
							printHeader(out, "Add Citation");
							addCitationInfo(stmt, con, request, out);
							break;
				case "add_question":
							printHeader(out, "Ask Question or Post Comment");
							addCommentsToPaper(stmt, con, request, out);
							break;
				default:
							printHeader(out, "Home");
							break;


			}
			
			printFooter(out);


			stmt.close();
			con.close();
		} catch (SQLException ex) {
			printException(ex);
		}
	}
	public void addAuthorInfo(Statement stmt, Connection con, HttpServletRequest request) {
		//prepare to add a new Author record
		PreparedStatement prepareAddAuthor;
		try{
			prepareAddAuthor = con.prepareStatement
			(
				"INSERT INTO Author VALUES(?, ?, ?, ?, ?)"
			);

			//set the values of each Author attribute
			try{
				int author_ID = Integer.parseInt(request.getParameter("author_ID"));
				String author_name = decodeParameter(request.getParameter("author_name"));
				String preferred_name = decodeParameter(request.getParameter("preferred_name"));
				String first_name = decodeParameter(request.getParameter("first_name"));
				String last_name = decodeParameter(request.getParameter("last_name"));

				prepareAddAuthor.setInt(1, author_ID);
				prepareAddAuthor.setString(2, author_name);
				prepareAddAuthor.setString(3, preferred_name);
				prepareAddAuthor.setString(4, first_name);
				prepareAddAuthor.setString(5, last_name);
				prepareAddAuthor.executeUpdate();
			}catch(NumberFormatException e){
				printMessage("Please enter a valid ID.", false, out);
				return;
			}			
		}catch(SQLException e)
		{
			printException(e);
		}
		catch(UnsupportedEncodingException e){
			printMessage("Decoding Error", false, out);
		}
		/* TODO: check if parameters are valid */
		//execute statement
		
	}

	public void addCoAuthoredInfo(Statement stmt, Connection con, HttpServletRequest request, PrintWriter out) {
		PreparedStatement prepareAddCoAuthor;
		try{
			//find the max coauthor ID and add 1 to it to create a new ID
			ResultSet rs0 = stmt.executeQuery("SELECT MAX(ID) FROM CoAuthored");
			if(rs0.next()){
				int coauthored_ID = rs0.getInt(1) + 1;
				//prepare to add a new CoAuthored record
				prepareAddCoAuthor = con.prepareStatement
				(
					"INSERT INTO CoAuthored VALUES(?, ?, ?, ?)"
				);
				//set the values of each CoAuthored attribute
				String s_author2ID = decodeParameter(request.getParameter("author2ID"));
				String s_paper_ID = decodeParameter(request.getParameter("paper_ID"));
				if(!s_author2ID.equals("") && !s_paper_ID.equals("")){
					int author_ID = Integer.parseInt(request.getParameter("author_ID"));
					int author2ID = Integer.parseInt(request.getParameter("author2ID"));
					int paper_ID = Integer.parseInt(request.getParameter("paper_ID"));
					prepareAddCoAuthor.setInt(1, coauthored_ID);
					prepareAddCoAuthor.setInt(2, author_ID);
					prepareAddCoAuthor.setInt(3, author2ID);
					prepareAddCoAuthor.setInt(4, paper_ID);
					ResultSet rs1 = stmt.executeQuery("SELECT COUNT(ID) FROM Author WHERE ID="+request.getParameter("author2ID"));
					if(rs1.next() && rs1.getInt(1)==0)
					{
						printMessage("Invalid Coauthor ID", false, out);
						return;
					}
					prepareAddCoAuthor.executeUpdate();
					printMessage("You have successfully added an author!", true, out);
				}
			}
			printMessage("You have successfully added an author!", true, out);
		}catch(SQLException e)
		{
			printException(e);
		}
		catch(NumberFormatException e){
			printMessage("Invalid Coauthor or Paper ID", false, out);
		}
		catch(UnsupportedEncodingException e){
			printMessage("Decoding Error", false, out);
		}	
	}

	public void addPaperInfo(Statement stmt, Connection con, HttpServletRequest request, PrintWriter out) {
		PreparedStatement prepareAddPaper;
		try{
			//find the max  ID and add 1 to it to create a new ID
			ResultSet rs0 = stmt.executeQuery("SELECT MAX(ID) FROM Paper");
			if(rs0.next()){
				int ID = rs0.getInt(1) + 1;
				//prepare to add a new Paper record
				prepareAddPaper = con.prepareStatement
				(
					"INSERT INTO Paper VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
				);
				//set the values of each Paper attribute
				int paper_ID = 0;
				int num_authors = 0;
				try{
					paper_ID = Integer.parseInt(request.getParameter("paper_ID"));
					num_authors = Integer.parseInt(request.getParameter("num_authors"));
				}catch(NumberFormatException e){
					printMessage("Please enter a valid paper ID.", false, out);
					return;
				}	
				int num_abstract_wds = Integer.parseInt(request.getParameter("num_abstract_wds"));
				int num_kb = Integer.parseInt(request.getParameter("num_kb"));
				int num_pages = Integer.parseInt(request.getParameter("num_pages"));
				int num_revisions = Integer.parseInt(request.getParameter("num_revisions"));
				int num_title_wds = Integer.parseInt(request.getParameter("num_title_wds"));

	 			java.sql.Date date; 
	  			date = java.sql.Date.valueOf(request.getParameter("submit_date"));

	  			//must enter an authors str
	  			String authors_str = request.getParameter("authors_str");
	  			if(authors_str.equals("")){
	  				printMessage("Must enter authors", false, out);
	  				return;
	  			}
	 				
				prepareAddPaper.setInt(1, ID);
				prepareAddPaper.setInt(2, paper_ID);
				prepareAddPaper.setString(3, decodeParameter(request.getParameter("title_str")));
				prepareAddPaper.setString(4, decodeParameter(request.getParameter("authors_str")));
				prepareAddPaper.setString(5, decodeParameter(request.getParameter("area")));
				prepareAddPaper.setInt(6, num_abstract_wds);
				prepareAddPaper.setInt(7, num_authors);
				prepareAddPaper.setInt(8, num_kb);
				prepareAddPaper.setInt(9, num_pages);
				prepareAddPaper.setInt(10, num_revisions);
				prepareAddPaper.setInt(11, num_title_wds);
				prepareAddPaper.setString(12, decodeParameter(request.getParameter("comments_str")));
				prepareAddPaper.setDate(13, date);
				prepareAddPaper.setString(14, decodeParameter(request.getParameter("submitter_email")));
				prepareAddPaper.setString(15, decodeParameter(request.getParameter("submitter_name")));

				prepareAddPaper.executeUpdate();
				printMessage("You have successfully added a Paper!", true, out);
			}
		}catch(SQLException e)
		{
			printException(e);
		}
		catch(UnsupportedEncodingException e){
			printMessage("Decoding Error", false, out);
		}
		catch(IllegalArgumentException e)
	  	{
	  		printMessage("Invalid Date Format: Use format yyyy-mm-dd", false, out);
	  	}

	}

	public void showPaperInfo(Statement stmt, Connection con, HttpServletRequest request, PrintWriter out) {
		try{
			//get the paper_id and display summary
			int ID = Integer.parseInt(request.getParameter("paper_ID"));
			ResultSet showSummary = stmt.executeQuery("SELECT title_str, authors_str, num_pages, num_revisions, comments_str FROM Paper WHERE paper_id="+ ID);
			if(!showSummary.next())
			{
				printMessage("Invalid Paper ID", false, out);
				return;
			}
			//store summary into 2 arrays
			String [] summary = {showSummary.getString(1), showSummary.getString(2), showSummary.getString(5)};
			int [] nums = {showSummary.getInt(3), showSummary.getInt(4)};
			//search for authors of that particular paper
			
			ResultSet authorInfo = stmt.executeQuery("SELECT author1ID FROM CoAuthored WHERE paper_id="+ ID);
			int [] id_arr;
			if(authorInfo.next())
			{
				int author_id = authorInfo.getInt(1);
				Statement s = con.createStatement();
				ResultSet papers = s.executeQuery("SELECT DISTINCT paper_ID FROM CoAuthored WHERE paper_ID<>"+ID+" AND author1ID="+ author_id);
				papers.last();
				int p_size = papers.getRow();
				id_arr = new int[p_size]; 
				papers.beforeFirst();
				int i = 0;
				while(papers.next()){
					id_arr[i] = papers.getInt(1);
					i++;
				}		
			
			}
			else
				id_arr = new int[0];
			

			
			//get citation information
			Statement c_s = con.createStatement();
			ResultSet citationInfo = c_s.executeQuery("SELECT * FROM Cites WHERE paper1ID = "+ID);
			citationInfo.last();
			int c_size = citationInfo.getRow();
			int [][] citation = new int[c_size][2];
			citationInfo.beforeFirst();
			int j = 0;
			while(citationInfo.next())
			{

				citation[j][0] = citationInfo.getInt(3);
				citation[j][1] = citationInfo.getInt(4);
				j++;
			}
			citationInfo.close();
			//call write function: showSummary, papersFromAuthor, citationInfo
			printPaper(summary, nums, id_arr, citation, out);
			
		}catch(SQLException e)
		{
			printException(e);
		}
		
	}

	public void showAuthorInfo(Statement stmt, Connection con, HttpServletRequest request, PrintWriter out) {
		try{

			//search for coauthor
			int author_ID = 0;
			try{
				author_ID = Integer.parseInt(request.getParameter("author_ID"));
			}catch(NumberFormatException e){
				printMessage("Invalid Author ID", false, out);
				return;
			}	


			//get record of author
			ResultSet authorInfo = stmt.executeQuery("SELECT * FROM Author WHERE ID="+ author_ID);
			if(!authorInfo.next())
			{
				printMessage("Invalid Author ID", false, out);
				return;
			}
			String [] attributes = {authorInfo.getString(2), authorInfo.getString(3), 
									authorInfo.getString(4), authorInfo.getString(5)}; 

			//get coauthors
			ResultSet coauthorInfo = stmt.executeQuery("SELECT DISTINCT author2ID FROM CoAuthored WHERE author1ID="+ author_ID);
			coauthorInfo.last();
			int c_size = coauthorInfo.getRow();
			int [] coauthors = new int[c_size];
			coauthorInfo.beforeFirst();
			int i = 0;
			while(coauthorInfo.next()){
				coauthors[i] = coauthorInfo.getInt(1);
				i++;
			} 
			coauthorInfo.close();
			//get all papers written by that author
			ResultSet papersFromAuthor = stmt.executeQuery("SELECT DISTINCT paper_ID FROM CoAuthored WHERE author1ID="+ author_ID);
			papersFromAuthor.last();
			int p_size = papersFromAuthor.getRow();
			int [] papers = new int[p_size];
			papersFromAuthor.beforeFirst();
			int j = 0;
			while(papersFromAuthor.next()){
				papers[j] = papersFromAuthor.getInt(1);
				j++;
			} 
			papersFromAuthor.close();
				
			//call write function: passes in papersFromAuthors
			printAuthor(attributes, coauthors, papers, out);
			
		}catch(SQLException e)
		{
			printException(e);
		}
		
	}
	public void addCitationInfo(Statement stmt, Connection con, HttpServletRequest request, PrintWriter out) {
		PreparedStatement prepareAddCitation;
		try{
			//find the max coauthor ID and add 1 to it to create a new ID
			ResultSet rs0 = stmt.executeQuery("SELECT MAX(ID) FROM Cites");
			if(rs0.next()){
				int cite_ID = rs0.getInt(1) + 1;
				//prepare to add a new CoAuthored record
				prepareAddCitation = con.prepareStatement
				(
					"INSERT INTO Cites VALUES(?, ?, ?, ?)"
				);
				int p1ID =0, p2ID=0;
				try{
					p1ID = Integer.parseInt(request.getParameter("paper1ID"));
					p2ID = Integer.parseInt(request.getParameter("paper2ID"));
				}catch(NumberFormatException e){
					printMessage("Please enter a valid Paper ID.", false, out);
					return;
				}
			
				//check validity of ID's
				ResultSet rs1 = stmt.executeQuery("SELECT * FROM Author WHERE ID="+p1ID+" LIMIT 1");
				rs1.last();
				if(rs1.getRow()==0)
				{
					printMessage("Invalid Paper IDs", false, out);
					return;
				}
				ResultSet rs2 = stmt.executeQuery("SELECT * FROM Author WHERE ID="+p2ID+" LIMIT 1");
				rs2.last();
				if(rs2.getRow()==0)
				{
					printMessage("Invalid Paper IDs", false, out);
					return;
				}
			

				//set the values of each CoAuthored attribute
				prepareAddCitation.setInt(1, cite_ID);
				prepareAddCitation.setInt(2, p1ID);
				prepareAddCitation.setInt(3, p2ID);
				String s = request.getParameter("is_self_citation");
				if(s.equals(""))
					prepareAddCitation.setNull(4, java.sql.Types.INTEGER);
				else
					prepareAddCitation.setInt(4, Integer.parseInt(request.getParameter("is_self_citation")));
				

				/* TODO: check if parameters are valid */
				prepareAddCitation.executeUpdate();
				printMessage("You have successfully added a citation!", true, out);
				rs1.close();
				rs2.close();
			}
			rs0.close();

		}catch(SQLException e)
		{
			printException(e);
		}
	}

	public void addCommentsToPaper(Statement stmt, Connection con, HttpServletRequest request, PrintWriter out) {
		PreparedStatement prepareAddComments;
		try{
			//get paper_ID of paper in which comments are to be made
			int pID = 0;
			try{
				pID = Integer.parseInt(request.getParameter("paper_ID"));
			}catch(NumberFormatException e){
				printMessage("Invalid Paper ID", false, out);
				return;
			}	
			ResultSet rs0 = stmt.executeQuery("SELECT comments_str FROM Paper WHERE paper_ID="+pID);
			if(rs0.next()){
				String comment = rs0.getString(1);
				//update comment
				comment+= "\n"+decodeParameter(request.getParameter("comments_str"));

				//prepare to add a new CoAuthored record
				stmt.executeUpdate("UPDATE Paper SET comments_str=\""+comment+"\" WHERE paper_ID="+pID);
				printMessage("You have successfully posted a question or comment!", true, out);
			}
			else
			{
				printMessage("Invalid Paper ID", false, out);
			}

		}catch(SQLException e)
		{
			printException(e);
		}
		catch(UnsupportedEncodingException e){
			printMessage("Decoding Error", false, out);
		}

	}

	public ResultSet searchPaperInfo(Statement stmt, Connection con, HttpServletRequest request) {
		//store the search key
		ResultSet rs;
		String search_key = "";
		try{
			search_key = decodeParameter(request.getParameter("keywords"));
		}
		catch(UnsupportedEncodingException e){
			printMessage("Decoding Error", false, out);
			return null;
		}
		try{
			try{
				//search integer
				int search_num = Integer.parseInt(search_key);
				rs = stmt.executeQuery("SELECT * FROM Paper WHERE ID="+search_num+" OR paper_ID="+search_num+
																			" OR num_abstract_wds="+search_num+
																			" OR num_authors="+search_num+ 
																			" OR num_kb="+search_num+
																			" OR num_pages="+search_num+
																			" OR num_revisions="+search_num+
																			" OR num_title_wds="+search_num );
				return rs;

			}catch(NumberFormatException e)
			{
				//search for string
				rs = stmt.executeQuery("SELECT * FROM Paper WHERE title_str LIKE \"%"+search_key+"%\" OR authors_str LIKE \"%"+search_key+
																			"%\" OR area LIKE \"%"+search_key+
																			"%\" OR comments_str LIKE \"%"+search_key+ 
																			"%\" OR submitter_email LIKE \"%"+search_key+
																			"%\" OR submitter_name LIKE \"%"+search_key+
																			"%\"");
				return rs;
			}
		}catch(SQLException e)
		{
			printException(e);
		}
		return null;
	}

	public ResultSet searchAuthorInfo(Statement stmt, Connection con, HttpServletRequest request) {
		//store the search key
		ResultSet rs;
		String search_key = "";
		try{
			search_key = decodeParameter(request.getParameter("keywords"));
		}
		catch(UnsupportedEncodingException e){
			printMessage("Decoding Error", false, out);
			return null;
		}
		try{
			try{
				//search integer
				int search_num = Integer.parseInt(search_key);
				rs = stmt.executeQuery("SELECT * FROM Author WHERE ID="+search_num);
				return rs;

			}catch(NumberFormatException e)
			{
				//search for string
				rs = stmt.executeQuery("SELECT * FROM Author WHERE author_name LIKE \"%"+search_key+"%\" OR preferred_name LIKE \"%"+search_key+
																			"%\" OR first_name LIKE \"%"+search_key+ 
																			"%\" OR last_name LIKE \"%"+search_key+
																			"%\"");
				return rs;
			}
		}catch(SQLException e)
		{
			printException(e);
		}
		return null;
	}
	void printException(SQLException ex)
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
    public void printHeader(PrintWriter out , String title) {
/*
  Prints out Header and Navigation that is used across all pages
*/
    
        out.println(
              "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            +       "<link  href=\"" + getRoot() + "css/reset.css\" type=\"text/css\" rel=\"stylesheet\" />"
            +       "<link  href=\"" + getRoot() + "css/fontello.css\" type=\"text/css\" rel=\"stylesheet\" />"
            +       "<link  href=\"" + getRoot() + "css/style.css\" type=\"text/css\" rel=\"stylesheet\" />"
            + "</head>"
            + "<body>"

            + "<header>"
            + "<div id=\"searchbar\">"
            +       "<label>Search </label>"
            +       "<form method=\"get\" action=\""+ getRoot() + "servlet/Scholar\">" 
            +              "<input checked=\"yes\" type=\"radio\" name=\"page\" value=\"search_author\"> Author"
            +              "<input type=\"radio\" name=\"page\" value=\"search_paper\"> Paper"
            +              "<input type=\"text\" name=\"keywords\" placeholder=\"Keywords\">"
            +              "<input id=\"search\" type=\"submit\" value=\"Go\">"
            +       "</form>"
            + "</div>"

            + "<nav>"
            + "<ul id=\"icons\">"
            +        generateLink(getRoot() + "servlet/Scholar?page=paper_form","<li><i class=\"icon-doc-text\"></i>Add Paper</li>") 
            +        generateLink(getRoot() + "servlet/Scholar?page=author_form","<li><i class=\"icon-user-add\"></i>Add Author</li>")
            +        generateLink(getRoot() + "servlet/Scholar?page=citation_form","<li><i class=\"icon-pencil\"></i>Add Citation</li>")
            +        generateLink(getRoot() + "servlet/Scholar?page=question_form","<li><i class=\"icon-help\"></i>Ask Question</li>")
            + "</ul>"
            + "</nav>"


            + "</header>"

            + "<h1> Scholar</h1>" + ((title !="") ? ("<h3>" + title + "</h3>") : "") 

            + "<div id=\"container\">"
    );

}

public String generateListItem(String attr, String val) {

  //attr - attribute
        //val - value
  return "<li><span>" + attr +"</span>"+ val + "</li>";

}

public String getRoot() {
  return "http://cs143.seas.ucla.edu:8080/cs143vky/";
}

public String generateURL(String pageName,String[]parameters,String[] values) throws UnsupportedEncodingException {
  //@package java.net.URLEncoder
  //http://docs.oracle.com/javase/1.5.0/docs/api/java/net/URLEncoder.html


    String url = getRoot() + "servlet/Scholar";
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

public void printPaperSearch(ResultSet papers, PrintWriter out) {
/*
 PRINTS
 1. Summary of Page
  a. Paper.title_str
  b. Paper.authors_str
  c. Paper.num_pages
  d. Paper.num_revisions
        e. Paper.comments_str
 2. LINK to Full Paper information using title which calls print Paper
*/
try {
    out.println("<ul class=\"results\">"); //begin printing list of papers

    while (papers.next()) {

    out.println("<li><ul>");//begin list item

    int paper_ID = papers.getInt("paper_ID");
    String title_str = papers.getString("title_str");
    String authors_str = papers.getString("authors_str");
    int num_pages = papers.getInt("num_pages");
    int num_revisions = papers.getInt("num_revisions");
    String comments_str = papers.getString("comments_str");

    out.println(generateListItem("Title:",generateLink(getRoot() + "servlet/Scholar?page=paper&paper_ID=" + Integer.toString(paper_ID), title_str)));
    out.println(generateListItem("Authors:",authors_str));
    out.println(generateListItem("Pages:",Integer.toString(num_pages)));
    out.println(generateListItem("Revisions:",Integer.toString(num_revisions)));
    out.println(generateListItem("Comments",comments_str));
    


  

    out.println("</li></ul>");//end list item
    }
    papers.close();
    out.println("</ul>");//end list

  } catch(SQLException e) {
      printException(e);
  }
}

public void printAuthorSearch(ResultSet authors, PrintWriter out) {
/*
  PRINTS
  1. Author Name Author.author_name
  2. Preferred Name Author.preferred_name
  3. First Name , Last Name Author.first_name, Author.last_name
  4. LINK to Full Author Information servlet/Scholar 
*/
try {
    out.println("<ul class=\"results\">"); //begin printing list of authors
    while (authors.next()) {

    out.println("<li><ul>");//begin list item 

    int author_ID = authors.getInt("ID");
    String author_name = authors.getString("author_name");
    String preferred_name = authors.getString("preferred_name");
    String first_name = authors.getString("first_name");
    String last_name = authors.getString("last_name");
    
    out.println(generateListItem("Author:",generateLink(getRoot() + "servlet/Scholar?page=author&author_ID=" + Integer.toString(author_ID), author_name)));
    out.println(generateListItem("Preferred Name:",preferred_name));
    out.println(generateListItem("First Name:",first_name));
    out.println(generateListItem("Last Name",last_name));

    out.println("</li></ul>");//end list item
    }

    authors.close();
    
    out.println("</ul>");//end list

  } catch(SQLException e) {
      printException(e);
  }
}
public void printPaper(String [] paper_str, int [] paper_int, int [] paper_ID, int [][] citation,PrintWriter out) {
/*
  PRINTS
  1. Summary of Page
      a. Paper.title_str
      b. Paper.authors_str
      c. Paper.num_pages
      d. Paper.num_revisions
  2. Links to other papers by the same author use Paper.paper_id for link
  3. Some comments left by users Paper.comments_str
  4. Citation Information: Cites.Paper2ID //also generate links to cited papers
*/
/* SAMPLE MOCKUP
<ul class="page">
      <li><span>Title:</span>Combinatorics of the Modular Group II: the Kontsevich integrals</li>
      <li><span>Authors:</span>C. Itzykson and J.-B. Zuber </li>
      <li><span>Pages:</span>46</li>
      <li><span>Revisions:</span>0</li>

      <li>
        <ol id="links">
          <li>Links:</li>
          <li><a href="#">Page 1</a></li>
          <li><a href="#">Page 2</a></li>
          <li><a href="#">Page 3</a></li>
          <li><a href="#">Page 4</a></li>
          <li><a href="#">Page 5</a></li>
        </ol>
      </li>

      <!-- Comments -->
      <li id="comments"><span>Comments:</span>46 pages</li>

      <li>
        <ul id="citation">
          <li>Citation:</li>
          <li><span>paper1ID:</span> 29863 </li>
          <li><span>paper2ID:</span> 29052 </li>
          <li><span>is_self_citation:</span> 0 </li>
        </ul>
      </li>

</ul>
*/


    out.println("<ul class=\"page\">"); //begin printing paper attributes
   

    out.println("<li><ul>");//begin list item 

      String title_str = paper_str[0]; //paper.getString("title_str");
      String authors_str = paper_str[1]; //paper.getString("authors_str");
      String comments_str = paper_str[2]; //paper.getString("comments_str");
      int num_pages = paper_int[0]; //paper.getInt("num_pages");
      int num_revisions = paper_int[1]; //paper.getInt("num_revisions");
     

      out.println(generateListItem("Title:",title_str));
      out.println(generateListItem("Authors:",authors_str));
      out.println(generateListItem("Pages:",Integer.toString(num_pages)));
      out.println(generateListItem("Revisions:",Integer.toString(num_revisions)));
    /*==== BEGIN LINKS TO OTHER PAGES ==== */
    /*
      <li>
        <ol id="links">
          <li>Links:</li>
          <li><a href="#">Page 1</a></li>
          <li><a href="#">Page 2</a></li>
          <li><a href="#">Page 3</a></li>
          <li><a href="#">Page 4</a></li>
          <li><a href="#">Page 5</a></li>
        </ol>
      </li>
    */

    out.println(
        "<li><ol id=\"links\">" //begin links to other pages
      + "<li>Links:</li>"
    );

    for(int i = 0; i < paper_ID.length; i++) { //print other links


        int other_ID = paper_ID[i];
       

        // <li><a href="#">Page 1</a></li>
        out.println(
         "<li>" + generateLink(getRoot() + "servlet/Scholar?page=paper&paper_ID=" + Integer.toString(other_ID), Integer.toString(other_ID)) + "</li>"
        );

    }

   out.println("</ol></li>");
   /*====  END LINKS TO OTHER PAGES ==== */

   /*==== BEGIN COMMENTS ==== */
   /*
    <!-- Comments -->
      <li id="comments"><span>Comments:</span>46 pages</li>
   */
    out.println("<li id=\"comments\"><span>Comments: </span>" + comments_str + "</li>");

   /*==== END COMMENTS ====*/

  /*==== BEGIN CITATIONS ====*/
 /*
  <li>
        <ul id="citation">
          <li>Citation:</li>
          <li><span>paper1ID:</span> 29863 </li>
          <li><span>paper2ID:</span> 29052 </li>
          <li><span>is_self_citation:</span> 0 </li>
        </ul>
  </li>
 */
    out.println("<li><ul id=\"citation\">");
    out.println("<li>Citations:</li>");

    for(int i = 0; i < citation.length; i++ ) {

      int paper2ID = citation[i][0];
      int is_self_citation = citation[i][1];
       
      out.println(generateListItem("paper2ID:",Integer.toString(paper2ID)));
      out.println(generateListItem("is_self_citation:",Integer.toString(is_self_citation)));

    }

    out.println("</li></ul>");

  /*==== END CITATIONS ===*/
    
    out.println("</ul>");//end paper


}

public void printAuthor(String[] author_attributes,int [] coauthors, int [] papers, PrintWriter out) {
/*
  PRINTS
  1. Author Name
  2. Preferred Name
  3. First Name , Last Name
  4. coauthors if exists : max is 337 , avg is 10.5
  5. Links to papers author has written use Paper.paper_id

*/
/*
<ul class="page">
      <li><span>Author:</span>Izawa K.-I</li>
      <li><span>Preferred Name:</span>K.-I.Izawa</li> 
      <li><span>First Name:</span>Izawa K.</li> 
      <li><span>Last Name:</span>-I</li>

      <li>
        <ol id="coauthors"> <!-- different from -->
          <li>CoAuthors:</li>
           <li><a href="#">42290</a></li>
             <li><a href="#">33898</a></li>  
             <li><a href="#">33898</a></li>    
             <li><a href="#">34340</a></li>
        </ol>
      </li>

      <li>
      <ol id="links">
          <li>Links:</li>
          <li><a href="#">Page 1</a></li>
          <li><a href="#">Page 2</a></li>
          <li><a href="#">Page 3</a></li>
          <li><a href="#">Page 4</a></li>
          <li><a href="#">Page 5</a></li>
      </ol>
      </li>


</ul>
*/

 // try {
    out.println("<ul class=\"page\">"); //begin printing paper attributes
   
    String author_name = author_attributes[0];//author.getString("author_name");
    String preferred_name = author_attributes[1];//author.getString("preferred_name");
    String first_name = author_attributes[2];//author.getString("first_name");
    String last_name = author_attributes[3];//author.getString("last_name");
    
    out.println(generateListItem("Author:",author_name));
    out.println(generateListItem("Preferred Name:",preferred_name));
    out.println(generateListItem("First Name:",first_name));
    out.println(generateListItem("Last Name",last_name));

  //}



  /*==== BEGIN COAUTHORS ====*/
  /*
  <li>
        <ol id="coauthors"> 
          <li>CoAuthors:</li>
            <li><a href="#">42290</a></li>
             <li><a href="#">33898</a></li>  
             <li><a href="#">33898</a></li>    
             <li><a href="#">34340</a></li>
        </ol>
      </li>

  */
  out.println(
    "<li>"+
    "<ol id=\"coauthors\">"
  );

  out.println("<li>CoAuthors:</li>");



  for(int i = 0; i< coauthors.length; i++) {

    int coauthor = coauthors[i];//"author2ID"

     out.println(
         "<li>" + generateLink(getRoot() + "servlet/Scholar?page=author&author_ID=" + Integer.toString(coauthor),Integer.toString(coauthor)) + "</li>"
      );
   
  }

  out.println("</ol></li>");

  

  /*==== END COAUTHORS ====*/
  /*==== BEGIN LINKS ====*/
  /*
      <li><ol id="links">
            <li>Links:</li>
            <li><a href="#">Page 1</a></li>
            <li><a href="#">Page 2</a></li>
            <li><a href="#">Page 3</a></li>
            <li><a href="#">Page 4</a></li>
            <li><a href="#">Page 5</a></li>
        </ol>
      </li>
  */
  out.println(
    "<li>"+
    "<ol id=\"coauthors\">"
  );

  out.println("<li>Links:</li>");


  for(int i = 0; i < papers.length; i++) {

    int paper_ID = papers[i];//paper_ID


     out.println(
         "<li>" + generateLink(getRoot() + "servlet/Scholar?page=paper&paper_ID=" + Integer.toString(paper_ID),Integer.toString(paper_ID)) + "</li>"
     );
   
  }

  out.println("</ol></li>");


  out.println("</ul>");//end author page

  /*==== END LINKS ====*/
    

 // } catch(SQLException e) {
 //     printException(e);
//  }



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

public void printForm(PrintWriter out,String pageName,String option,String action,String option2, String title ,String title2) {
/*
  PRINTS form for specific page
  Add Paper 
  Add Author/CoAuthor
  Add Citation 
  Ask Question

  in  the case we're we have to print Author and CoAuthor, we need to arrays of options
*/

String[] fields_arr = getFormFields(option);
String[] fields_arrTwo = (option2 != "") ? getFormFields(option2) : null;

  out.println(
      "<form id=\"inputInformation\" method=\"get\" action=\"" + action + "\">" //begin form
    + "<h2>"+ title +"</h2>"
  );

  for( int i = 0; i < fields_arr.length; i++) { //begin fields
   /* Sample input fields
      <label>author_ID</label>
      <input type="text" name="author_ID">
   */

    out.println(
        "<label>" + fields_arr[i] +"</label>"
      + "<input type=\"text\" name=\"" + fields_arr[i] + "\">" 
    );
  }

  if(fields_arrTwo != null) {//print out second array if relevant
      out.println(
       "<h2>"+ title2 +"</h2>"
    );

    for( int i = 0; i < fields_arrTwo.length; i++) { //begin fields
     /* Sample input fields
        <label>author_ID</label>
        <input type="text" name="author_ID">
     */

      out.println(
          "<label>" + fields_arrTwo[i] +"</label>"
        + "<input type=\"text\" name=\"" + fields_arrTwo[i] + "\">" 
      );
    }


  }//end second array

 
  
  out.println(
    "<input type=\"hidden\" name=\"page\" value=\""+ pageName +"\">"
  + "<input type=\"submit\" value=\"Submit\">"
  + "</form>" //end form
  );




}

public String[] getFormFields(String option) {

  String[] authorFields = {"author_ID","author_name","preferred_name","first_name","last_name"};
  String[] coAuthoredFields = {"author2ID","paper_ID"};
  String[] paperFields = {"paper_ID","title_str","authors_str","area","num_abstract_wds","num_authors","num_kb","num_pages","num_revisions","num_title_wds","comments_str","submit_date","submitter_email","submitter_name"};
  String[] citeFields = {"paper1ID","paper2ID","is_self_citation"};
  String[] questionFields = {"paper_ID","comments_str"};

  switch(option) {
    case "Author":
      return authorFields;
    case "CoAuthored":
      return coAuthoredFields;
    case "Paper":
      return paperFields;
    case "Cite":
      return citeFields;
    case "Question":
      return questionFields; 
    default:
      return null;
  }

}

public void printMessage (String msg, boolean isTrue,PrintWriter out) {
//for error handling

  out.println( "<p class=\"" + ( (isTrue) ?  "green" : "red") + "\">" + msg + "</p>");

}

}
