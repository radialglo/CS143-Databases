# CS143 Databases 
### Fall 2012
##### Developers: 

  Vincent Kyi [@vincekyi](https://github.com/vincekyi)  
  Anthony Su  [@radialglo](https://github.com/radialglo)

##### Project Descriptions: 

###### 1. Project 2
    
  a.Suggest two useful indices for each of the following queries:  
       
      SELECT COUNT(*)
      FROM Author AS A, CoAuthored AS C
      WHERE A.first_name = ‘John’ AND
      C.author1ID = A.ID
     

      SELECT COUNT(*)
      FROM Author
      WHERE first_name = ‘Kevin’;
    

  b. Setup a simple web interface that uses a Java Servlet
       Modify web.xml appropriately for configurations.    

  c. Simple JDBC exercise to print out all papers in the DB  
  d. Write a **web application** using the JDBC(Java Database Connectivity) API for input, browsing, and search functionalities of research papers

###### Implementation Overview:
   Backend implemented using JDBC API  *Scholar.java*  
   Frontend HTML is generated by dynamically by GET requests handled by application  
   Handwritten CSS, icon fonts from [fontello](http://www.fontello.com), and wood pattern from [Subtle Patterns](http://www.subtlepatterns.com)

###### 2. Project 3
  Create a **database design** and implement an **web application** for a simplified Conference Management Tooltkit [CMT](http://cmt.research.microsoft.com/cmt/).  
+ Multiple roles such as Chair, Reviewer, Author  
+ Abstract and paper submission  
+ Management of Reviewers  
+ Assignment of papers to Reviewers  
+ Review submission  
*Refer to ER.pdf in project 3 directory for database design*


###### Implementation Overview:
   Backend implemented using JDBC API  *CMT.java*  
     -- manage user sessions  
     -- POST request to securely authenticate users  
   Frontend HTML is generated by dynamically by GET requests handled by application  *PrintView.java*  
   *PrintView.java is a private object of CMT.java*  
   Handwritten CSS , Twitter [bootstrap](http://twitter.github.com/bootstrap/), and Bedge Grunge from [Subtle Patterns](http://www.subtlepatterns.com)