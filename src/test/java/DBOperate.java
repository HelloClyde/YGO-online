import com.google.gson.Gson;
import org.junit.Test;

import java.sql.*;
import java.util.List;

/**
 * Created by HelloClyde on 2017/3/17.
 */
public class DBOperate {
    @Test
    public void createEnableCards() {
        try {
            //加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace();
        }
        String url = "jdbc:mysql://127.0.0.1:3306/ygo?characterEncoding=UTF-8";
        String username = "root";
        String password = "root";
        try {
            Connection con =
                    DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT cards FROM user WHERE id=1;");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                List<Integer> cardIds = new Gson().fromJson(resultSet.getString("cards"), List.class);
                System.out.println(cardIds);
            }
        } catch (SQLException se) {
            System.out.println("数据库连接失败！");
            se.printStackTrace();
        }
    }
}
