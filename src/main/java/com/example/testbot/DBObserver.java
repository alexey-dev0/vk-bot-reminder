package com.example.testbot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBObserver implements Runnable {
    @Override
    public void run() {
        try {
            var conn = VKServer.db.getConnection();
            conn.setAutoCommit(false);
            Statement st = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            st.setFetchSize(50);
            ResultSet rs = st.executeQuery("SELECT * FROM reminders");
            /*
            1 - id
            2 - creation_date
            3 - appointment_date
            4 - interval_date
            5 - regular
            6 - user_id
            7 - message
             */
            while (rs.next())
            {
                if (Timestamp.valueOf(LocalDateTime.now()).after(Timestamp.valueOf(rs.getString(3)))) {
                    new VKManager().sendMessage(rs.getString(7), Integer.parseInt(rs.getString(6)));
                    rs.deleteRow();
                }
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
