import com.google.gson.Gson;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            PreparedStatement preparedStatement = con.prepareStatement("SELECT CardID FROM ygodata WHERE SCCardType=?;");
            preparedStatement.setString(1, "通常怪兽");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> normalMonsterIDList = new ArrayList<>();
            while (resultSet.next()) {
                normalMonsterIDList.add(resultSet.getInt(1));
            }
            List<Integer> resultList = new ArrayList<>();
            for (int i = 0;i < 40;i ++){
                int idx = new Random().nextInt(normalMonsterIDList.size());
                resultList.add(normalMonsterIDList.remove(idx));
            }
            System.out.println(new Gson().toJson(resultList));
        } catch (SQLException se) {
            System.out.println("数据库连接失败！");
            se.printStackTrace();
        }
    }
}
