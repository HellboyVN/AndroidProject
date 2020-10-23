package hellboy.com.testlib;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.proframeapps.videoframeplayer.util.NativeHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    String ip, db, username, pass;
    Button btnLogIn;
    TextView info;
    Connection con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NativeHelper.d("haha");
        ip = "db4free.net:3306/";
        db = "hazel_humax";
        username = "hellboy";
        pass = "12345678";
        btnLogIn = (Button) findViewById(R.id.btn_login);
        info = (TextView) findViewById(R.id.tv_info);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                con = connectionClass(username,pass,db,ip);
                if(con == null){
                    info.setText("null");
                }else{
                    String querry = "select * from age where username= 'levan'";
                    Statement statement = null;

                    statement = con.createStatement();
                    ResultSet rs = statement.executeQuery(querry);
                    if (rs.next()){
                        info.setText("Age = " + rs.getInt("age"));
                    }else {
                        info.setText("failed");
                    }
                }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public Connection connectionClass (String user, String pass, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = "";

        try {
            Log.e("hellboy","here1");
            Class.forName("com.mysql.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + server + database + ";user=" + user + ";password=" +pass +";";
            connection = DriverManager.getConnection("jdbc:mysql://localhost/hazel_humax", "root", "");
            Log.e("hellboy","here");
        } catch (SQLException ae)
        {
            Log.e("error here 1 : ", ae.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception ex)
        {
            Log.e("error here 3 : ", ex.getMessage());
        }
        return connection;
    }
}
