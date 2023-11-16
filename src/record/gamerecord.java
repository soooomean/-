package record;
import java.sql.*;
public class gamerecord {
    public static Connection makeConnection(){
        String url =
                "jdbc:mysql://localhost/score?serverTimezone=Asia/Seoul";
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("데이터베이스 연결 중…");
            con = DriverManager.getConnection(url, "root","password");
            System.out.println("데이터베이스 연결 성공");
        }catch (ClassNotFoundException e){
            System.out.println("JDBC 드라이버를 찾지 못했습니다.");
        }catch (SQLException e){
            System.out.println("데이터베이스 연결 실패");
        }
        return con;
    }

    public static void main(String[] args) throws SQLException {
        Connection con = makeConnection();
        Statement stmt = con.createStatement();
        String sql ="create database if not exists score";
        if(stmt.executeUpdate(sql)==1){
            System.out.println("성공");
        }
        else {
            System.out.println("실패");
        }

        sql="use score";
        if(stmt.execute(sql)==true){
            System.out.println("성공");
        }
        else {
            System.out.println("실패");
        }

        sql = "create table if not exists scoreboard(" +
                "name varchar(20)," +
                "score integer," +
                "primary key (name))";
        if(stmt.executeUpdate(sql)==-1){
            System.out.println("성공");
        }
        else {
            System.out.println("실패");
        }

        /*데이터베이스가 없을 시 데이터베이스와 테이블 생성*/
        //StringBuilder sql = new StringBuilder(); /*pstmt 이용 시 */
        /*데이터 들어오면 정렬 후 11번째 데이터가 있을 시 11번째 데이터 삭제*/
        /*sql ="insert into scoreboard values ('name2', 123456)";
        if(stmt.executeUpdate(sql)==1)
            System.out.println("레코드 추가 성공");
        else
            System.out.println("레코드 추가 실패");*/

        con.close();
        stmt.close();

    }
}
