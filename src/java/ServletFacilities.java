/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class ServletFacilities extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        Connection con=null;
        Statement st=null;
        PrintWriter out = response.getWriter();
        try {           
            
            Class.forName("org.postgresql.Driver");
            String url="jdbc:postgresql://localhost:5432/Services";
            String user="postgres";
            String pass="$rihanna$";
            con=DriverManager.getConnection(url,user,pass);            
            st=con.createStatement();
            
            //GETTING INPUT FROM DYNAMIC ROWS
            String[] Chkbox=request.getParameterValues("chkbox[0]");
            String[] Facility_no=request.getParameterValues("textbox[0]");
            String[] Facility_name=request.getParameterValues("textbox[1]");
            
            String s2=request.getParameter("s1");            
            if(s2.equals("SAVE") && Chkbox!=null){                
                for(int i=0;i<Chkbox.length;i++){                
                    //st.executeUpdate("insert into Orders values('"+Item_no[i]+"','"+Item_desc[i]+"','"+Qty[i]+"','"+Rate[i]+"')");                                        
                    PreparedStatement pstmt;
                    pstmt=con.prepareStatement("insert into \"FacilityTab\" values(?,?)");
                    pstmt.setInt(1,Integer.parseInt(Facility_no[i]));
                    pstmt.setString(2,Facility_name[i]);                    
                    pstmt.executeUpdate();
                }
                out.println("<h1>Dynamic Records Inserted<h1>");
            }
            if(s2.equals("DISPLAY")){
                ResultSet rs=st.executeQuery("select * from \"FacilityTab\"");
//                ResultSetMetaData rsmd=rs.getMetaData();
//                int cols=rsmd.getColumnCount();
                while(rs.next()){
                    int facno=rs.getInt(1);
                    String facname=rs.getString(2);                                        
//                    for(int i=3;i<cols;i++){
                        out.println("<table>"+"<tr>"+"<td>"+"<input type=checkbox>"+"</td>"+"<td>"+"<input type=text value='"+facno+"'>"+"</td>"+"<td>"+"<input type=text value='"+facname+"'>"+"</tr>");
                        out.println("</table>");
//                    }
                }
            }
            
//            String s3=request.getParameter("s1");
//            if(s3.equals("DELETE") && Chkbox!=null){
//                for(int i=0;i<Chkbox.length;i++){
//                    String chk_count=Chkbox[i];
//                    PreparedStatement pstmt;
//                    pstmt=con.prepareStatement("delete from \"FacilityTab\" where \"facility_no\"='chk_count'");
//                    pstmt.executeUpdate();
//                }
//            }
            
            st.close();
            con.close();
        }
        catch(SQLException se){
            out.println(se.getMessage());
        }
        catch(Exception e){
            out.println(e.getMessage());
        }
    }
}