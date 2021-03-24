package com.codecool.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RadioCharts {

    private static String databaseUrl;
    private static String databaseUserName;
    private static String databasePassWord;

    public RadioCharts(String url, String userName, String passWord) {
        this.databaseUrl = url;
        this.databaseUserName = userName;
        this.databasePassWord = passWord;
    }

    public String getMostPlayedSong() {
        List<String> songsByAirTimeDescending = getSongsByAirtimeDescending();

        if (songsByAirTimeDescending.isEmpty()) {
            return "";
        }
        return songsByAirTimeDescending.get(0);
    }

    public String getMostActiveArtist() {
        List<String> artistsByActivity = getArtistsByActivity();

        if (artistsByActivity.isEmpty()) {
            return "";
        }
        return artistsByActivity.get(0);
    }

    private List<String> getArtistsByActivity() {
        List<String> artists = new ArrayList<>();
        List<Integer> distinctSongs = new ArrayList<>();

        try {
            String SQL = "SELECT " +
                    "artist, COUNT(DISTINCT 'song') AS total_songs " +
                    "FROM music_broadcast " +
                    "GROUP BY artist " +
                    "ORDER BY total_songs";
            PreparedStatement pst = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassWord).prepareStatement(SQL);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                artists.add(rs.getString(1));
                distinctSongs.add(rs.getInt(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return artists;
    }

    private List<String> getSongsByAirtimeDescending() {
        List<String> songNames = new ArrayList<>();
        List<Integer> songAired = new ArrayList<>();

        try {
            String SQL = "SELECT " +
                    "song, SUM(times_aired) AS total_aired " +
                    "FROM music_broadcast " +
                    "GROUP BY song " +
                    "ORDER BY total_aired";
            PreparedStatement pst = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassWord).prepareStatement(SQL);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                songNames.add(rs.getString(1));
                songAired.add(rs.getInt(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return songNames;
    }

}
