


import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.String;
import java.net.*;
import java.util.Date;

public class CMT extends HttpServlet {
	private static PrintWriter out;
	private static PrintView v;
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
		v = new PrintView();
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

			// get the http session
			HttpSession session = request.getSession();

			String page = request.getParameter("page");

			// print out the result

			//switch statement for operations
			switch(page) {
				case "home":
							//printHeader(PrintWriter out, String title, String role)
							v.printHeader(out,"",null);
							break;
				case "log_in":
							login(con, request);
							break;
				case "log_out":
							v.printHeader(out,"",null);
							logout(request);
							break;
				case "sign_up":
							v.printHeader(out,"",null);
							signup(request, con);
							break;
				case "account_settings":
							v.printHeader(out, "Modify Account Information", session);
							modifyUserForm(request, con);
							break;
				case "modify_user_info":
							v.printHeader(out, "Modify Account Information", session);
							modifyUserInfo(request, con);
							break;
				case "chair":
							v.printHeader(out, "Chair's Home", session);
							showChairInfo(request, con);
							break;
				case "author":
							v.printHeader(out, "Author's Home", session);
							showAuthorInfo(request, con);
							break;
				case "reviewer":
							v.printHeader(out, "Reviewer's Home", session);
							showReviewerInfo(request, con);
							break;
				case "new_conference_form":
							v.printHeader(out, "Create New Conference", session);
							v.printForm(out, null, "submit_new_conference", "new_conference_form", "Create New Conference");
							break;
				case "submit_new_conference":
							v.printHeader(out, "Create New Conference", session);
							createConference(request, con);
							break;	
				case "modify_conference_form":
							v.printHeader(out, "Modify Conference", session);
							modifyConferenceForm(request, con);
							break;		
				case "submit_modify_conference":
							v.printHeader(out, "Modify Conference", session);
							modifyConference(request, con);
							break;
				case "add_paper_form":
							v.printHeader(out, "Create New Paper", session);
							v.printAddPaperForm(out, con);
							break;
				case "submit_new_paper":
							v.printHeader(out, "Create New Paper", session);
							createPaper(request, con);
							break;
				case "modify_paper_form":
							v.printHeader(out, "Modify Paper", session);
							modifyPaperForm(request, con);
							break;			
				case "submit_modify_paper":
							v.printHeader(out, "Modify Paper", session);
							modifyPaper(request, con);
							break;
				case "read_paper":
							v.printHeader(out, "Read Paper", session);
							readPaper(request, con);
							break;
				case "review_form":
							v.printHeader(out, "Review", session);
							reviewForm(request, con);
							break;
				case "submit_new_review":
							v.printHeader(out, "Create New Review", session);
							createReview(request, con);
							break; 			
				case "submit_modify_review":
							v.printHeader(out, "Modify Review", session);
							modifyReview(request, con);
							break;
				case "add_coauthor_form":
							v.printHeader(out, "Add Co-author", session);
							addCoauthorForm(request, con);	
							break;		
				case "add_coauthor":
							v.printHeader(out, "Add Co-author", session);
							addCoauthor(request, con);
							break;
				case "add_reviewer_form":
							v.printHeader(out, "Add Reviewer", session);
							addReviewerForm(request, con);
							break;
				case "add_reviewer":
							v.printHeader(out, "Add Reviewer", session);
							addReviewer(request, con);
							break;
				case "assign_reviewer_form":
							v.printHeader(out, "Assign Reviewer", session);
							assignReviewerForm(request, con);
							break;			
				case "assign_reviewer":
							v.printHeader(out, "Assign Reviewer", session);
							assignReviewer(request, con);
							break;
				case "trigger_final_decision":
							v.printHeader(out, "Make Final Decision", session);
							triggerFinalDecision(request, con);
							break;			
				default:
							v.printHeader(out, "", session);
							break;


			}
			
			v.printFooter(out);


			con.close();
		} catch (SQLException ex) {
			v.printException(ex, out);
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
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
			v = new PrintView();
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
				

				String page = request.getParameter("page");

				// print out the result
				//switch statement for operations
				switch(page) {
			
					case "log_in":
								login(con, request);
								break;
					default:
								v.printHeader(out,"",null);
								//printHeader(PrintWriter out, String title, String role,HttpSession session)

				}
				
				v.printFooter(out);

				con.close();
			} catch (SQLException ex) {
				v.printException(ex, out);
			}
		}

	public void login(Connection con, HttpServletRequest request) {
		try{
			String email =  request.getParameter("username");
			Statement s1 = con.createStatement();
			ResultSet checkPW = s1.executeQuery("SELECT last_name, uid, role FROM User WHERE email=\""+ email+"\"");
			if(!checkPW.next())
			{
				v.printHeader(out,"",null);
				v.printMessage("Invalid email or password", false, out);
				return;
			}

			//check password
			String entered_PW = request.getParameter("password");
			if(!entered_PW.equals(checkPW.getString("last_name")))
			{
				v.printHeader(out,"",null);
				v.printMessage("Invalid email or password", false, out);
				return;
			}
			String role = checkPW.getString("role");
			int uid = checkPW.getInt("uid");
			//if password is correct
			//creates a new session
			HttpSession session = request.getSession();
			session.setAttribute("email", email);
			session.setAttribute("password", entered_PW);
			session.setAttribute("uid", uid);
			session.setAttribute("role", role);
			//check the user's role
			Statement s2 = con.createStatement();
			ResultSet roles = s2.executeQuery("SELECT role FROM User WHERE uid="+ uid);
			if(!roles.next())
			{
				v.printHeader(out,"",null);
				v.printMessage("Invalid email or password", false, out);
				return;
			}

			v.printHeader(out,"Dashboard",session);
			switch(roles.getString(1)){
				case "CHAIR":
						showChairInfo(request, con);
						break;
				case "REVIEWER":
						showReviewerInfo(request, con);
						break;
				case "AUTHOR":
				default:
						showAuthorInfo(request, con);
						break;
			}

		
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}
	}
	public void logout(HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			session.invalidate();

			//goHome(out);
			
		}catch(IllegalStateException e)
		{
			//v.printException(e, out);
		}
	}

	public void signup(HttpServletRequest request, Connection con) {
		try{
			PreparedStatement prepareAddUser = con.prepareStatement
			(
				"INSERT INTO User VALUES(?, ?, ?, ?, ?, ?, ?)"
			);

			//set the values of each Author attribute

			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT MAX(uid) FROM User");
			int uid = 0;
			if(rs0.next()){
				uid = rs0.getInt(1) + 1;
			}
			//get all the values from the form and store into variables
			String email = v.decodeParameter(request.getParameter("email"));
			String first_name = v.decodeParameter(request.getParameter("first_name"));
			String middle_name = v.decodeParameter(request.getParameter("middle_name"));
			String last_name = v.decodeParameter(request.getParameter("last_name"));
			String affiliation = v.decodeParameter(request.getParameter("affiliation"));
			String role = "AUTHOR";

			prepareAddUser.setInt(1, uid);
			prepareAddUser.setString(2, email);
			prepareAddUser.setString(3, last_name);
			prepareAddUser.setString(4, middle_name);
			prepareAddUser.setString(5, first_name);
			prepareAddUser.setString(6, affiliation);
			prepareAddUser.setString(7, role);

			Statement s1 = con.createStatement();
			ResultSet rs1 = s1.executeQuery("SELECT * FROM User WHERE email=\""+email+"\"");
			if(rs1.next()){
				v.printMessage("Email already exists. Please choose a different email.", false, out);
				s0.close();
				rs0.close();
				s1.close();
				rs1.close();
				return;
			}
			//execute the insertion
			prepareAddUser.executeUpdate();
			s0.close();
			rs0.close();
			s1.close();
			rs1.close();

			v.printMessage("You have successfully signed up! \n To log in, enter your email and password above.", true, out);
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		catch(UnsupportedEncodingException e){
			v.printMessage("Decoding Error", false, out);
		}
		catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}
	}

	public void modifyUserForm(HttpServletRequest request, Connection con) {
		try{
			//get the session's uid
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");
			//search for the conference id
			Statement s1 = con.createStatement();
			ResultSet userInfo = s1.executeQuery("SELECT * FROM User WHERE uid="+ uid);
			if(!userInfo.next())
			{
				v.printMessage("Invalid User", false, out);
				return;
			}
			userInfo.beforeFirst();
			v.printForm(out, userInfo, "modify_user_info", "account_settings", "Modify User Information");
			s1.close();
			userInfo.close();
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
	}

	public void modifyUserInfo(HttpServletRequest request, Connection con) {
		try{
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");
			//get the field parameters
			String first_name = v.decodeParameter(request.getParameter("first_name"));
			String middle_name = v.decodeParameter(request.getParameter("middle_name"));
			String last_name = v.decodeParameter(request.getParameter("last_name"));
			String affiliation = v.decodeParameter(request.getParameter("affiliation"));

			//load the update query
			Statement s0 = con.createStatement();
			s0.executeUpdate("UPDATE User SET first_name=\""+first_name+"\", middle_name=\""+middle_name+
				"\", last_name=\""+last_name+"\", affiliation=\""+affiliation+"\" WHERE uid="+uid);
			v.printMessage("Your account information has been updated.", true, out);
			s0.close();
		}catch(UnsupportedEncodingException e){
			v.printMessage("Decoding Error", false, out);
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
	}
	public void showChairInfo(HttpServletRequest request, Connection con) {
		try{
			//get uid
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");
			//search for the conference id
			Statement s1 = con.createStatement();
			ResultSet getCID = s1.executeQuery("SELECT cid FROM Chair WHERE uid="+ uid);
	
			//if getCID is null that means there will be no conferences printed
			v.printChairInfo(getCID, con, request, out);
			s1.close();
			getCID.close();
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		
	}

	public void showAuthorInfo(HttpServletRequest request, Connection con) {
		try{
			//get uid
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");
			//search for the conference id
			Statement s1 = con.createStatement();
			ResultSet getPID = s1.executeQuery("SELECT paper_id FROM Author WHERE uid="+ uid);
			getPID.beforeFirst();
			v.printAuthorInfo(getPID, con, request, out);
			s1.close();
			getPID.close();
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		
	}
	
	public void showReviewerInfo(HttpServletRequest request, Connection con) {
		try{
			//get uid
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");
			//search for the conference id
			Statement s1 = con.createStatement();
			ResultSet getPID = s1.executeQuery("SELECT paper_id FROM Reviews WHERE uid="+ uid);
			/*if(!getPID.next())
			{
				v.printMessage("Invalid Reviewer user", false, out);
				return;
			}

			getPID.beforeFirst();*/
			
			v.printReviewerInfo(getPID, con, request, out);
			s1.close();
			getPID.close();
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		
	}

	public void createConference(HttpServletRequest request, Connection con) {
		try{
			//get uid
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");

			PreparedStatement prepareAddConference = con.prepareStatement
			(
				"INSERT INTO Conference VALUES(?, ?, ?, ?, ?, ?)"
			);

			//prepare to add new chair entry
			PreparedStatement prepareAddChair;
			prepareAddChair = con.prepareStatement
			(
				"INSERT INTO Chair VALUES(?, ?, ?)"
			);

			//set the values of each conference attribute

			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT MAX(cid) FROM Conference");
			int cid = 0;
			if(rs0.next()){
				cid = rs0.getInt(1) + 1;
			}
			//get all the values from the form and store into variables
			String cname = v.decodeParameter(request.getParameter("cname"));
			int reviews_per_paper = Integer.parseInt(request.getParameter("num_reviews_per_paper"));
			int reviews_per_reviewer = Integer.parseInt(request.getParameter("max_reviews_per_reviewer"));
			//get all the time variables
			java.sql.Timestamp start_time = Timestamp.valueOf(request.getParameter("submission_start_time"));
			java.sql.Timestamp end_time = Timestamp.valueOf(request.getParameter("submission_end_time"));
			
/*
			//get the current date
			java.util.Date cur_date = new java.util.Date();
			Timestamp now = new Timestamp(cur_date.getTime());

			if(end_time.before(now) || start_time.before(now)) {
				v.printMessage("Submission end time and start time must be after the current time", false, out);
				return;
			}
*/
			//end time can't be before start time
			if(end_time.before(start_time)) {
				v.printMessage("Submission end time must be after the start time", false, out);
				return;
			}
			//load the update query
			prepareAddConference.setInt(1, cid);
			prepareAddConference.setString(2, cname);
			prepareAddConference.setTimestamp(3, start_time);
			prepareAddConference.setTimestamp(4, end_time);
			prepareAddConference.setInt(5, reviews_per_paper);
			prepareAddConference.setInt(6, reviews_per_reviewer);

			prepareAddChair.setInt(1, uid);
			prepareAddChair.setInt(2, cid);
			prepareAddChair.setInt(3, 0);

			Statement s1 = con.createStatement();
			ResultSet rs1 = s1.executeQuery("SELECT * FROM Conference WHERE cname=\""+cname+"\"");
			if(rs1.next()){
				v.printMessage("Conference name already exists. Please choose a different conference name.", false, out);
				s0.close();
				rs0.close();
				s1.close();
				rs1.close();
				return;
			}
			//execute the insertion
			prepareAddConference.executeUpdate();
			prepareAddChair.executeUpdate();
			s0.close();
			rs0.close();
			s1.close();
			rs1.close();


			v.printMessage("You have successfully added a new conference", true, out);
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		catch(UnsupportedEncodingException e){
			v.printMessage("Decoding Error", false, out);
		}catch(NumberFormatException e){
			v.printMessage("Please enter a valid number for the field(s) number of reviews per paper or maximum number of reviews per reviewer.", false, out);
			return;
		}catch(IllegalArgumentException e){
			v.printMessage("Please enter a valid date/time.", false, out);
			return;
		}
		catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}
	}
	public void modifyConferenceForm(HttpServletRequest request, Connection con) {
		try{
			//get the session's uid
			int cid = Integer.parseInt(request.getParameter("cid"));

			if(hasEnded(cid, con)) {
				v.printMessage("Cannot make changes after conference has ended.", false, out);
				return;
			}

			//search for the conference id
			Statement s1 = con.createStatement();
			ResultSet userInfo = s1.executeQuery("SELECT * FROM Conference WHERE cid="+ cid);
			if(!userInfo.next())
			{
				v.printMessage("Invalid User", false, out);
				return;
			}
			userInfo.beforeFirst();
			v.printForm(out, userInfo, "submit_modify_conference", "modify_conference_form", "Modify Conference Information");
			s1.close();
			userInfo.close();
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
	}
	public void modifyConference(HttpServletRequest request, Connection con) {
		try{
			//get the conference name
			String cname = v.decodeParameter(request.getParameter("cname"));
			int cid = Integer.parseInt(request.getParameter("cid"));

			//get all the values from the form and store into variables

			java.sql.Timestamp start_time = Timestamp.valueOf(v.decodeParameter(request.getParameter("submission_start_time")));
			java.sql.Timestamp end_time = Timestamp.valueOf(v.decodeParameter(request.getParameter("submission_end_time")));
/*
			//get the current date
			java.util.Date cur_date = new java.util.Date();
			Timestamp now = new Timestamp(cur_date.getTime());
			if(end_time.before(now)) {
				v.printMessage("Submission end time and start time must be after the current time", false, out);
				return;
			}
*/
			//end time can't be before start time
			if(end_time.before(start_time)) {
				v.printMessage("Submission end time must be after the start time", false, out);
				return;
			}
			//check if conference name already exists
			Statement s1 = con.createStatement();
			ResultSet rs1 = s1.executeQuery("SELECT * FROM Conference WHERE cname=\""+cname+"\" AND cid<>"+cid);
			if(rs1.next()){
				v.printMessage("Conference name already exists. Please choose a different conference name.", false, out);
				return;
			}
			//load the update query
			Statement s0 = con.createStatement();
			s0.executeUpdate("UPDATE Conference SET cname=\""+cname+"\", submission_start_time=\'"+start_time+"\', submission_end_time=\'"+end_time+"\' WHERE cid="+cid);


			v.printMessage("You have successfully updated "+cname, true, out);
			s0.close();
			s1.close();
			rs1.close();
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		catch(UnsupportedEncodingException e){
			v.printMessage("Decoding Error", false, out);
		}catch(NumberFormatException e){
			v.printMessage("Please enter a valid number for the fields: number of reviews per paper or maximum number of reviews per reviewer.", false, out);
			return;
		}catch(IllegalArgumentException e){
			v.printMessage("Please enter a valid date/time.", false, out);
			return;
		}
		catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}
	}
	
	public void createPaper(HttpServletRequest request, Connection con) {
		try{
			//get uid
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");
			//prepare to add a new paper
			PreparedStatement prepareAddPaper = con.prepareStatement
			(
				"INSERT INTO Paper VALUES(?, ?, ?, ?, ?)"
			);

			//set the values of each conference attribute

			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT MAX(paper_id) FROM Paper");
			int paper_id = 0;
			if(rs0.next()){
				paper_id = rs0.getInt(1) + 1;
			}
			//get all the values from the form and store into variables
			String title = v.decodeParameter(request.getParameter("title"));
			String abstr = v.decodeParameter(request.getParameter("abstract"));
			String content = v.decodeParameter(request.getParameter("content"));

			//prepare to add an new author entry
			PreparedStatement prepareAddPaperAuthor;
			prepareAddPaperAuthor = con.prepareStatement
			(
				"INSERT INTO Author VALUES(?, ?)"
			);

			//prepare to add a new status entry
			PreparedStatement prepareAddPaperConf;
			prepareAddPaperConf = con.prepareStatement
			(
				"INSERT INTO Status VALUES(?, ?, ?)"
			);

			//get the conference name
			int cid = Integer.parseInt(request.getParameter("cid"));
			
			//load the update query
			prepareAddPaperConf.setInt(1, cid);
			prepareAddPaperConf.setInt(2, paper_id);
			prepareAddPaperConf.setInt(3, 0);

			prepareAddPaperAuthor.setInt(1, uid);
			prepareAddPaperAuthor.setInt(2, paper_id);

			prepareAddPaper.setInt(1, paper_id);
			prepareAddPaper.setString(2, title);
			prepareAddPaper.setString(3, abstr);
			prepareAddPaper.setString(4, content);
			prepareAddPaper.setInt(5, 0);
			//execute the insertion
			prepareAddPaper.executeUpdate();
			prepareAddPaperAuthor.executeUpdate();
			prepareAddPaperConf.executeUpdate();
			
			s0.close();
			rs0.close();

			v.printMessage("You have successfully added a new paper", true, out);
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		catch(UnsupportedEncodingException e){
			v.printMessage("Decoding Error", false, out);
		}
		catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}
	}

	public void modifyPaperForm(HttpServletRequest request, Connection con) {
		try{
			//get the paper ID
			int paper_id = Integer.parseInt(request.getParameter("paper_id"));

			//get the conference associated with that paper
			Statement s0 = con.createStatement();
			ResultSet conference = s0.executeQuery("SELECT cid FROM Status WHERE paper_id="+paper_id);
			int cid = 0;
			if(conference.next()) {
				cid = conference.getInt("cid");
			}
			else 
			{
				v.printMessage("Error: Paper is not associated with conference.", false, out);
				return;
			}

			if(hasEnded(cid, con)) {
				v.printMessage("Cannot make changes after end of submission time.", false, out);
				return;
			}

			//search for the paper
			Statement s2 = con.createStatement();
			ResultSet paperInfo = s2.executeQuery("SELECT * FROM Paper WHERE paper_id="+ paper_id);
			if(!paperInfo.next())
			{
				v.printMessage("Invalid User", false, out);
				return;
			}
			paperInfo.beforeFirst();
			v.printForm(out, paperInfo, "submit_modify_paper", "modify_paper_form", "Modify Paper Information");
			
			s0.close();
			conference.close();
			s2.close();
			paperInfo.close();
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
	}


	public void modifyPaper(HttpServletRequest request, Connection con) {
		try{
			//get the paper id
			int paper_id =Integer.parseInt(request.getParameter("paper_id"));
			
			//get all the values from the form and store into variables
			String title = v.decodeParameter(request.getParameter("title"));
			String abstr = v.decodeParameter(request.getParameter("abstract"));
			String content = v.decodeParameter(request.getParameter("content"));
			
			//update the current entry
			Statement s2 = con.createStatement();
			s2.executeUpdate("UPDATE Paper SET title=\""+title+"\", abstract=\""+abstr+"\", content=\""+content+"\" WHERE paper_id="+paper_id);

			
			s2.close();
			v.printMessage("You have successfully modified "+title, true, out);
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		catch(UnsupportedEncodingException e){
			v.printMessage("Decoding Error", false, out);
		}
		catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}
	}

	public void readPaper(HttpServletRequest request, Connection con) {
		try{
			//get the paper id
			int paper_id =Integer.parseInt(request.getParameter("paper_id"));
			
			//get paper information
			Statement s2 = con.createStatement();
			ResultSet r2 = s2.executeQuery("SELECT * FROM Paper WHERE paper_id="+paper_id);

			v.viewPaper(r2, con, request, out);
			s2.close();
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
	}

	//note: creating review is only called when no review is present in the table
	public void createReview(HttpServletRequest request, Connection con) {
		try{
			//get uid
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");

			//set the values of each Review attribute
			int paper_id = Integer.parseInt(request.getParameter("paper_id"));
		
			//get all the values from the form and store into variables
			int rating = 0;
			try{
				rating = Integer.parseInt(request.getParameter("rating"));
				if(rating<1 || rating>7) {
					v.printMessage("Rating must be within the range of 1-7.", false, out);
					return;
				}

			}catch(NumberFormatException e) {
				v.printMessage("Please enter a valid rating.", false, out);
				return;
			}

			String comments = v.decodeParameter(request.getParameter("comments"));

			
			//update the current entry
			Statement s2 = con.createStatement();
			s2.executeUpdate("UPDATE Reviews SET comments=\""+comments+"\", rating="+rating+" WHERE paper_id="+paper_id+" AND uid="+uid);
			
			s2.close();

			//update the value of num_reviews for the paper
			Statement s1 = con.createStatement();
			ResultSet rs1 = s1.executeQuery("SELECT num_reviews FROM Paper WHERE paper_id="+paper_id);
			int num_reviews = 0;
			if(rs1.next()) {
				num_reviews = rs1.getInt(1);
			}
			num_reviews+=1;
			Statement s3 = con.createStatement();
			s3.executeUpdate("UPDATE Paper SET num_reviews="+num_reviews+" WHERE paper_id="+paper_id);
			v.printMessage("You have successfully added a new review", true, out);

			s1.close();
			rs1.close();
			s2.close();
			s3.close();
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		catch(UnsupportedEncodingException e){
			v.printMessage("Decoding Error", false, out);
		}
		catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}
	}

	public void reviewForm(HttpServletRequest request, Connection con) {
		try{
			//get the paper ID
			int paper_id = Integer.parseInt(request.getParameter("paper_id"));

			//get the conference associated with that paper
			Statement s0 = con.createStatement();
			ResultSet conference = s0.executeQuery("SELECT cid FROM Status WHERE paper_id="+paper_id);
			int cid = 0;
			if(conference.next()) {
				cid = conference.getInt("cid");
			}
			else 
			{
				v.printMessage("Error: Paper is not associated with conference.", false, out);
				return;
			}

			if(hasEnded(cid, con)) {
				v.printMessage("Cannot make changes after end of submission time.", false, out);
				return;
			}

			//get the uid of the reviewer
			HttpSession session = request.getSession();
			int uid =(int) session.getAttribute("uid");
			//search for the conference id
			Statement s2 = con.createStatement();
			ResultSet reviewInfo = s2.executeQuery("SELECT * FROM Reviews WHERE paper_id="+ paper_id+" AND uid="+uid);
			if(!reviewInfo.next())
			{
				v.printMessage("Invalid User", false, out);
				return;
			}
			//creating a new review
			if(reviewInfo.getInt("rating") == 0) {
				reviewInfo.beforeFirst();
				v.printForm(out, reviewInfo, "submit_new_review", "modify_review_form", "Create New Review");
				s0.close();
				conference.close();
				s2.close();
				reviewInfo.close();
				return;
			}
			//modify the review
			reviewInfo.beforeFirst();
			v.printForm(out, reviewInfo, "submit_modify_review", "modify_review_form", "Modify Review Information");
			
			s0.close();
			conference.close();
			s2.close();
			reviewInfo.close();
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
	}
	//note: this function is only called if a review entry already exists
	public void modifyReview(HttpServletRequest request, Connection con) {
		try{
			//get uid
			HttpSession session = request.getSession();
			int uid = (int)session.getAttribute("uid");
			//set the values of each Review attribute
			int paper_id = Integer.parseInt(request.getParameter("paper_id"));
		
			//get all the values from the form and store into variables
			int rating = 0;
			try{
				rating = Integer.parseInt(request.getParameter("rating"));
				if(rating<1 || rating>7) {
					v.printMessage("Rating must be within the range of 1-7.", false, out);
					return;
				}

			}catch(NumberFormatException e) {
				v.printMessage("Please enter a valid rating.", false, out);
				return;
			}


			String comments = v.decodeParameter(request.getParameter("comments"));
			
			//get the conference associated with that paper
			Statement s0 = con.createStatement();
			ResultSet conference = s0.executeQuery("SELECT cid FROM Status WHERE paper_id="+paper_id);
			int cid = 0;
			if(conference.next()) {
				cid = conference.getInt("cid");
			}
			else 
			{
				v.printMessage("Error: Paper is not associated with conference.", false, out);
				return;
			}

			if(hasEnded(cid, con)) {
				v.printMessage("Cannot add a review after end of submission time.", false, out);
				return;
			}


			//update the current entry
			Statement s2 = con.createStatement();
			s2.executeUpdate("UPDATE Reviews SET comments=\""+comments+"\", rating="+rating+" WHERE paper_id="+paper_id+" AND uid="+uid);
			
			
			s0.close();
			conference.close();
			s2.close();

			v.printMessage("You have successfully added a new paper", true, out);
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		catch(UnsupportedEncodingException e){
			v.printMessage("Decoding Error", false, out);
		}catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}

	}
	
	//needs cid to be passed in
	public void addReviewerForm(HttpServletRequest request, Connection con) {
		try{
			int cid = Integer.parseInt(request.getParameter("cid"));

			if(hasEnded(cid, con)) {
				v.printMessage("Cannot add a reviewer after end of submission time.", false, out);
				return;
			}


			//get all available authors who can review this conference
			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT u.uid, u.email FROM User u WHERE u.role<>\"CHAIR\" AND u.uid <> ALL(SELECT r.uid FROM Reviewer r WHERE r.cid="+cid+")"); //listed in drop down
			Statement s2 = con.createStatement();
			ResultSet rs2 = s2.executeQuery("SELECT COUNT(*) FROM User WHERE role<>\"CHAIR\"");
			Statement s3 = con.createStatement();
			ResultSet rs3 = s3.executeQuery("SELECT COUNT(*) FROM Reviewer WHERE cid="+cid);
			int num_authors = 0, num_reviewers = 0;
			if(rs2.next() && rs3.next()) {
				num_authors = rs2.getInt(1);
				num_reviewers =rs3.getInt(1);
			}
			if(rs0.next() || num_authors==num_reviewers) {
				rs0.beforeFirst();
				v.printAddReviewerForm(out, rs0, cid);
			}
			else {
				Statement s1 = con.createStatement();
				ResultSet rs1;
				rs1 = s1.executeQuery("SELECT uid, email FROM User WHERE role<>\"CHAIR\""); //listed in drop down
				v.printAddReviewerForm(out, rs1, cid);
				s1.close();
				rs1.close();
			}
		
			s0.close();
			rs0.close();

		}catch(SQLException e)
		{
			v.printException(e, out);
		}

	}

	public void addReviewer(HttpServletRequest request, Connection con) {
		try{
			int cid = Integer.parseInt(request.getParameter("cid"));
			String email = request.getParameter("email");
			int uid = 0;
			//check to see if author is a reviewer
			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT uid, role FROM User WHERE email=\""+email+"\""); 
			if(rs0.next()) {
				uid = rs0.getInt("uid");
				String role = rs0.getString("role");
				if(role.equals("AUTHOR")) {
					Statement s1 = con.createStatement();
					s1.executeUpdate("UPDATE User SET role=\"REVIEWER\" WHERE uid="+uid);
					s1.close();
				}

			}
			//execute update to add new reviewer
			Statement s2 = con.createStatement();
			s2.executeUpdate("INSERT INTO Reviewer VALUES("+uid+", "+cid+", \""+email+"\", 0)");


			s0.close();
			rs0.close();
			s2.close();

			v.printMessage("You have successfully added a new reviewer", true, out);
				
		}catch(SQLException e)
		{
			v.printException(e, out);
		}

	}

	public void assignReviewerForm(HttpServletRequest request, Connection con) {
		try{
			int cid = Integer.parseInt(request.getParameter("cid"));

			if(hasEnded(cid, con)) {
				v.printMessage("Cannot add a reviewer after end of submission time.", false, out);
				return;
			}


			//get all reviewers in the conference
			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT uid, email FROM Reviewer WHERE cid="+cid); //listed in drop down
			//get all papers in the conference
			Statement s2 = con.createStatement();
			ResultSet rs2 = s2.executeQuery("SELECT p.paper_id, p.title FROM Status s, Paper p WHERE s.cid="+cid+" AND p.paper_id=s.paper_id"); //listed in drop down


			
			v.printAssignReviewerForm(out, rs0, rs2, cid);
			s0.close();
			rs0.close();
			s2.close();
			rs2.close();

			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}

	}

	public void assignReviewer(HttpServletRequest request, Connection con) {
		try{
			//get all parameters
			int cid = Integer.parseInt(request.getParameter("cid"));
			int uid = Integer.parseInt(request.getParameter("uid"));
			int paper_id = Integer.parseInt(request.getParameter("paper_id"));


			//check to make sure that paper is not already assigned to that reviewer
			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT * FROM Reviews WHERE uid="+uid+" AND paper_id="+paper_id); 
			//if the reviewer is already reviewing this paper, return an error
			if(rs0.next()) {
				v.printMessage("Reviewer is already reviewing this paper", false, out);
				return;
			}

			//check to make sure the reviewer is not the author
			Statement s1 = con.createStatement();
			ResultSet rs1 = s1.executeQuery("SELECT uid FROM Author WHERE uid="+uid+" AND paper_id="+paper_id); 
			if(rs1.next()) {
				v.printMessage("You cannot assign a paper to a reviewer in which the reviewer is the author of the paper.", false, out);
				return;
			}

			Statement s2 = con.createStatement();
			ResultSet rs2 = s2.executeQuery("SELECT max_reviews_per_reviewer, num_reviews_per_paper FROM Conference WHERE cid="+cid); 
			int max_reviews_per_reviewer = 0, num_reviews_per_paper = 0;
			if(rs2.next()) {
				max_reviews_per_reviewer = rs2.getInt(1);
				num_reviews_per_paper = rs2.getInt(2);
			}

			//makes sure that the reviewer is not over his total
			Statement s3 = con.createStatement();
			ResultSet rs3 = s3.executeQuery("SELECT num_of_reviews FROM Reviewer WHERE uid="+uid+" AND num_of_reviews<"+max_reviews_per_reviewer); 
			if(!rs3.next()) {
				v.printMessage("This reviewer has viewed his maximum amount of reviews.", false, out);
				return;
			}
			int num_of_reviews = rs3.getInt("num_of_reviews");

			//makes sure that the paper is not over its total
			Statement s4 = con.createStatement();
			ResultSet rs4 = s4.executeQuery("SELECT num_reviews FROM Paper WHERE paper_id="+paper_id+" AND num_reviews<"+num_reviews_per_paper); 
			if(!rs4.next()) {
				v.printMessage("This paper already has enough reviews.", false, out);
				return;
			}
			int num_reviews = rs4.getInt("num_reviews");

			//insert a new review entry
			Statement s5 = con.createStatement();
			s5.executeUpdate("INSERT INTO Reviews VALUES("+uid+", "+paper_id+", \"\", NULL)");

			//increment the count
			Statement s6 = con.createStatement();
			s6.executeUpdate("UPDATE Reviewer SET num_of_reviews="+(num_of_reviews+1)+" WHERE uid="+uid+" AND cid="+cid);

			Statement s7 = con.createStatement();
			s7.executeUpdate("UPDATE Paper SET num_reviews="+num_reviews+" WHERE paper_id="+paper_id);

			s0.close();
			rs0.close();
			s1.close();
			rs1.close();
			s2.close();
			rs2.close();
			s3.close();
			rs3.close();
			s4.close();
			rs4.close();
			s5.close();
			s6.close();
			s7.close();
			v.printMessage("You have successfully assigned a paper to a reviewer.", true, out);
			
		}catch(SQLException e)
		{
			v.printException(e, out);
		}

	}

	public void addCoauthorForm(HttpServletRequest request, Connection con) {
		try{

			int paper_id = Integer.parseInt(request.getParameter("paper_id"));
			//look for conference in which the paper is in
			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT cid FROM Status WHERE paper_id="+paper_id);
			int cid = 0;
			if(rs0.next()) {
				cid = rs0.getInt(1);
			}

			if(hasEnded(cid, con)) {
				v.printMessage("Cannot add a coauthor after end of submission time.", false, out);
				return;
			}

			
			v.printAddCoAuthorForm(out, paper_id);
			s0.close();
			rs0.close();

		}catch(SQLException e)
		{
			v.printException(e, out);
		}

	}

	public void addCoauthor(HttpServletRequest request, Connection con) {
		try{

			int paper_id = Integer.parseInt(request.getParameter("paper_id"));
			String email = request.getParameter("email");
			//gets the id and makes sure email is valid
			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT uid, role FROM User WHERE email=\""+email+"\"");
			int uid = 0;
			if(rs0.next()) {
				uid = rs0.getInt(1);
			}
			else{
				v.printMessage("Invalid email.", false, out);
				return;
			}
			if(rs0.getString(2).equals("CHAIR")) {
				v.printMessage("Chair users can not be authors.", false, out);
				return;
			}
			//makes sure that the author is not already an author of the paper
			Statement s1 = con.createStatement();
			ResultSet rs1 = s1.executeQuery("SELECT * FROM Author WHERE uid="+uid+" AND paper_id="+paper_id);
			if(rs1.next()) {
				v.printMessage("This author is already an author of this paper.", false, out);
				return;
			}
			//insert a new author entry
			Statement s2 = con.createStatement();
			s2.executeUpdate("INSERT INTO Author VALUES("+uid+", "+paper_id+")");

			
			s0.close();
			rs0.close();
			s1.close();
			rs1.close();
			s2.close();
			v.printMessage("You have successfully added a new Coauthor.", true, out);

		}catch(SQLException e)
		{
			v.printException(e, out);
		}catch(NullPointerException e){
			v.printMessage("Please fill in all fields.", false, out);
		}

	}

	public boolean hasEnded(int cid, Connection con) {
		try{
			//check to make sure submission time is not over
			Statement s0 = con.createStatement();
			ResultSet rs0 = s0.executeQuery("SELECT has_ended FROM Chair WHERE cid="+cid);
			if(rs0.next() && rs0.getInt(1)==1) {
				return true;
			}
		}catch(SQLException e)
		{
			v.printException(e, out);
		}
		return false;
	}

	public void triggerFinalDecision(HttpServletRequest request, Connection con) {
		try{
			int cid = Integer.parseInt(request.getParameter("cid"));
			int num_reviews_per_paper = 0;
			String cname = "";
			//gets the current time
			java.util.Date cur_date = new java.util.Date();
			Timestamp now = new Timestamp(cur_date.getTime());

			//check to make sure submission time is not over
			Statement s1 = con.createStatement();
			ResultSet rs1 = s1.executeQuery("SELECT cname, submission_end_time, num_reviews_per_paper FROM Conference WHERE cid="+cid);
			if(rs1.next()) {
				if(now.before(rs1.getTimestamp("submission_end_time")))
				{
					v.printMessage("Current time is before the end of submission.  Therefore you can not make the final decision yet.", false, out);
					return;
				}
				num_reviews_per_paper = rs1.getInt("num_reviews_per_paper");
				cname = rs1.getString("cname");
			}
			else 
			{
				v.printMessage("Error: Paper is not associated with conference.", false, out);
				return;
			}
			//check for each paper, if number of reviewers assigned is less than the required number of reviewers
			Statement s2 = con.createStatement();
			ResultSet r2 = s2.executeQuery("SELECT * FROM Paper p, Status s WHERE s.cid="+cid+" AND s.paper_id=p.paper_id AND p.num_reviews<"+num_reviews_per_paper);
			if(r2.next()) {
				v.printMessage("Some papers have been reviewed by less than the specified number of reviews per paper.", false, out);
				return;
			}

			//check if 1 or more reviewers has not submitted a review
			Statement s3 = con.createStatement();
			ResultSet r3 = s3.executeQuery("SELECT * FROM Reviews r, Status s WHERE s.cid="+cid+" AND s.paper_id=r.paper_id AND r.rating is NULL");
			if(r3.next()) {
				v.printMessage("At least 1 reviewer has not submitted their review.", false, out);
				return;
			}

			//calculate average
			Statement s4 = con.createStatement();
			ResultSet r4 = s4.executeQuery("SELECT paper_id FROM Status WHERE cid="+cid);
			while(r4.next()) {
				int paper_id = r4.getInt(1);
				Statement s5 = con.createStatement();
				ResultSet r5 = s5.executeQuery("SELECT AVG(rating) FROM Reviews WHERE paper_id="+paper_id);
				if(r5.next()) {
					Statement s6 = con.createStatement();
					s6.executeUpdate("UPDATE Status SET decision="+r5.getInt(1)+" WHERE paper_id="+paper_id);
					s6.close();
				}

				s5.close();
				r5.close();
			}

			//update chair
			Statement s7 = con.createStatement();
			s7.executeUpdate("UPDATE Chair SET has_ended=1 WHERE cid="+cid);
			s7.close();
			v.printMessage("You have successfully ended conference:"+cname, true, out);

		}catch(SQLException e)
		{
			v.printException(e, out);
		}
	}



	 //end CMT class
	
}
