
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.*;

/**
 * Servlet implementation class Scores
 */
@WebServlet("/Scores")
public class Scores extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Scores() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = request.getParameter("email");
		System.out.println(email);
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
        
        Gson gson = new Gson();
        JsonObject res = new JsonObject();
        
        List<Score> arrScore = getData(email);
        
        if (!arrScore.isEmpty()){
        	res.addProperty("existInfo", true);
        	res.addProperty("scores", gson.toJson(arrScore));
        }
        else {
        	res.addProperty("existInfo", false);
        }
        //out.println(res.toString());
        out.println(gson.toJson(arrScore));
        out.close();
	}

	protected List<Score> getData(String email){
		String url = "jdbc:mysql://localhost:3306/scores";
		String username = "root";
		String password = "";
		String query = "";
		List<Score> scores = null;
		Score score;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} 

		try{
		Connection conn = DriverManager.getConnection(url, username, password);
		Statement stmt = conn.createStatement();
		query = "SELECT gam.Description, scg.Score FROM users usr "
				+ "INNER JOIN scoregame scg ON scg.IdUser = usr.idUser "
				+ "INNER JOIN game gam on gam.IdGame = scg.IdGame "
				+ "where usr.Email LIKE '%" + email +"%' "
				+ "ORDER BY scg.Score DESC";
		ResultSet rs = stmt.executeQuery(query);
		System.out.println("conecto");
		scores = new ArrayList<Score>();
		while(rs.next()){
			score = new Score();
			score.setJuego(rs.getString("Description"));
			score.setScore(rs.getInt("Score"));
			scores.add(score);
		}
		//System.out.println(scores.toString());
		rs.close();
		stmt.close();
		stmt = null;
		conn.close();
		conn = null;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return scores;
	}
}
