package io.github.sher1234.service.database;

public interface Queries {
    String CreateUser =
            "CREATE TABLE User (" +
                    "UserID varchar(64) PRIMARY KEY, " +
                    "Email varchar(50) NOT NULL UNIQUE, " +
                    "Phone text, " +
                    "Name text, " +
                    "EmployeeID varchar(32) NOT NULL UNIQUE, " +
                    "Password varchar(32) NOT NULL, " +
                    "IsAdmin tinyint(4) NOT NULL DEFAULT 0, " +
                    "IsRegistered tinyint(4) NOT NULL DEFAULT 0" +
                    ")";

    String CreateLogins =
            "CREATE TABLE Logins (" +
                    "UserID varchar(64) PRIMARY KEY, " +
                    "Token varchar(64)," +
                    "Recent datetime," +
                    "Expiry datetime" +
                    ")";

    String CreateCalls =
            "CREATE TABLE Calls (" +
                    "CallID varchar(20) NOT NULL PRIMARY KEY, " +
                    "ComplaintType text, " +
                    "ConcernName text NOT NULL, " +
                    "ConcernPhone text NOT NULL, " +
                    "CustomerName text NOT NULL, " +
                    "DateTime datetime NOT NULL, " +
                    "IsCompleted tinyint(1) NOT NULL DEFAULT 0, " +
                    "NatureOfSite text, " +
                    "ProductNumber text, " +
                    "ProductDetail text, " +
                    "SiteDetails text, " +
                    "Warranty text NOT NULL, " +
                    "ID1 varchar(64) NOT NULL, " +
                    "ID2 varchar(64)" +
                    ")";

    String CreateCalls2 =
            "CREATE TABLE Calls2 (" +
                    "CallID varchar(20) NOT NULL PRIMARY KEY, " +
                    "ComplaintType text, " +
                    "ConcernName text NOT NULL, " +
                    "ConcernPhone text NOT NULL, " +
                    "CustomerName text NOT NULL, " +
                    "DateTime datetime NOT NULL, " +
                    "IsCompleted tinyint(1) NOT NULL DEFAULT 0, " +
                    "NatureOfSite text, " +
                    "ProductNumber text, " +
                    "ProductDetail text, " +
                    "SiteDetails text, " +
                    "Warranty text NOT NULL, " +
                    "ID1 varchar(64) NOT NULL, " +
                    "ID2 varchar(64)" +
                    ")";

    String CreateVisits =
            "CREATE TABLE Visits (" +
                    "VisitID varchar(20) NOT NULL PRIMARY KEY," +
                    "CallID varchar(20) NOT NULL," +
                    "Name text," +
                    "Email text," +
                    "Phone text," +
                    "Action text," +
                    "Observation," +
                    "Start datetime," +
                    "End datetime," +
                    "Satisfaction int(5) DEFAULT 0," +
                    "Feedback text," +
                    "Location text," +
                    "Signature text," +
                    "ID varchar(64) NOT NULL" +
                    ")";

    String CreateVisits2 =
            "CREATE TABLE Visits2 (" +
                    "VisitID varchar(20) NOT NULL PRIMARY KEY," +
                    "CallID varchar(20) NOT NULL," +
                    "Name text," +
                    "Email text," +
                    "Phone text," +
                    "Action text," +
                    "Observation," +
                    "Start datetime," +
                    "End datetime," +
                    "Satisfaction int(5) DEFAULT 0," +
                    "Feedback text," +
                    "Location text," +
                    "Signature text," +
                    "ID varchar(64) NOT NULL" +
                    ")";

    String DeleteUser = "DELETE FROM User";

    String DeleteToken = "DELETE FROM Logins";
}
