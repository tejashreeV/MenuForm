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
            
            //GETTING HIDDEN FORM FIELD VALUES
            String DeleteFac=request.getParameter("dfac");
            String UpdateFacNo=request.getParameter("ufacno");
            String UpdateFacName=request.getParameter("ufacname");
            
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
                out.print("<script language='JavaScript'>alert('Records Inserted');</script>");
            }
            
            if(s2.equals("DISPLAY")){
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"FormStylesheet.css\">");
                out.println("<h1 id=EmphasiedText>FACILITY TABLE</h1>");
                ResultSet rs=st.executeQuery("select * from \"FacilityTab\"");
                while(rs.next()){
                    int facno=rs.getInt(1);
                    String facname=rs.getString(2);                                        
                        out.println("<table id=Tables align=center>"+"<tr>"+"<td>"+"<input type=checkbox>"+"</td>"+"<td>"+"<input type=text value='"+facno+"'>"+"</td>"+"<td>"+"<input type=text value='"+facname+"'>"+"</tr>");
                        out.println("</table>");                        
                }
            }
            
            if(s2.equals("DELETE")){
                String delQuery="delete from \"FacilityTab\" where \"facility_name\"='"+DeleteFac+"'";
                st.executeUpdate(delQuery);
                out.print("<script language='JavaScript'>alert('Facility Deleted');</script>");
            }
            
            if(s2.equals("UPDATE")){
                String upQuery="update \"FacilityTab\" set \"facility_name\"='"+UpdateFacName+"' where \"facility_no\"='"+UpdateFacNo+"'";
                st.executeUpdate(upQuery);
                out.print("<script language='JavaScript'>alert('Facility Table Updated');</script>");
            }
            
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