package com.example.navi_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.util.ArrayList;

public class DatabaseHelp extends SQLiteOpenHelper {

    private static String DB_NAME = "covNavi.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DatabaseHelp(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }
    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    public ArrayList<Building> getBuildings() {
        ArrayList<Building> buildings = new ArrayList<>();

        String query = "SELECT * FROM Buildings";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                int newBuildingID = cursor.getInt(cursor.getColumnIndex("ID"));
                String newBuildingName = cursor.getString(cursor.getColumnIndex("Name"));

                Building newBuilding = new Building(newBuildingID, newBuildingName, mContext);
                buildings.add(newBuilding);
            } while (cursor.moveToNext());
            db.close();
        }
        return buildings;
    }

    public ArrayList<Level> getLevels(int buildingID) {
        ArrayList<Level> levels = new ArrayList<>();

        String query = "SELECT * FROM Levels WHERE Building = '" + String.valueOf(buildingID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                int newLevelID = cursor.getInt(cursor.getColumnIndex("ID"));
                String newLevelName = cursor.getString(cursor.getColumnIndex("Name"));

                Level newLevel = new Level(newLevelID, newLevelName, mContext);
                levels.add(newLevel);
            } while (cursor.moveToNext());
            db.close();
        }
        return levels;
    }

    public Location getLocation(Node searchNode) {
        String query = "SELECT * FROM Nodes WHERE Address = '" + searchNode.address + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                int newNodeLocation = cursor.getInt(cursor.getColumnIndex("Location"));

                query = "SELECT * FROM Locations WHERE ID = '" + newNodeLocation + "'";
                cursor = db.rawQuery(query, null);
                if(cursor != null && cursor.moveToFirst()) {
                    do {
                        int newLocationID = cursor.getInt(cursor.getColumnIndex("ID"));
                        String newLocationName = cursor.getString(cursor.getColumnIndex("Name"));
                        String newLocationType = cursor.getString(cursor.getColumnIndex("Type"));
                        String newLocationWorkspaces = cursor.getString(cursor.getColumnIndex("Workspaces"));
                        String newLocationComputers = cursor.getString(cursor.getColumnIndex("Computers"));
                        String newLocationFood = cursor.getString(cursor.getColumnIndex("Food"));

                        Location newLocation = new Location(newLocationID, newLocationName, newLocationType, newLocationComputers, newLocationWorkspaces, newLocationFood, mContext);
                        db.close();
                        return newLocation;

                    } while (cursor.moveToNext());
                }

            } while (cursor.moveToNext());
        }
        return null;
    }

    public ArrayList<Location> getLocations(int levelID) {
        ArrayList<Location> locations = new ArrayList<>();

        String query = "SELECT * FROM Locations WHERE Level = '" + String.valueOf(levelID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int newLocationID = cursor.getInt(cursor.getColumnIndex("ID"));
                String newLocationName = cursor.getString(cursor.getColumnIndex("Name"));
                String newLocationType = cursor.getString(cursor.getColumnIndex("Type"));
                String newLocationWorkspaces = cursor.getString(cursor.getColumnIndex("Workspaces"));
                String newLocationComputers = cursor.getString(cursor.getColumnIndex("Computers"));
                String newLocationFood = cursor.getString(cursor.getColumnIndex("Food"));

                Location newLocation = new Location(newLocationID, newLocationName, newLocationType, newLocationComputers, newLocationWorkspaces, newLocationFood, mContext);
                locations.add(newLocation);
            } while (cursor.moveToNext());
            db.close();
        }
        return locations;
    }

    public Node getNode(String searchNodeAddress) {
        String query = "SELECT * FROM Nodes WHERE Address = '" + searchNodeAddress + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String newNodeAddress = cursor.getString(cursor.getColumnIndex("Address"));

                Node newNode = new Node(newNodeAddress, mContext);
                db.close();
                return newNode;
            } while (cursor.moveToNext());
        }
        return null;
    }

    public ArrayList<Node> getNodes(int locationID) {
        ArrayList<Node> nodes = new ArrayList<>();

        String query = "SELECT * FROM Nodes WHERE Location = '" + String.valueOf(locationID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String newNodeAddress = cursor.getString(cursor.getColumnIndex("Address"));

                Node newNode = new Node(newNodeAddress, mContext);
                nodes.add(newNode);
            } while (cursor.moveToNext());
            db.close();
        }
        return nodes;
    }

    public ArrayList<Connection> getConnections(String nodeAddress) {
        ArrayList<Connection> connections = new ArrayList<>();

        String query = "SELECT * FROM Connections WHERE NodeA = '" + nodeAddress + "' OR NodeB = '" + nodeAddress + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String newConnectionNodeA = cursor.getString(cursor.getColumnIndex("NodeA"));
                String newConnectionNodeB = cursor.getString(cursor.getColumnIndex("NodeB"));
                int newConnectionWeight = cursor.getInt(cursor.getColumnIndex("Weight"));

                if (nodeAddress.equals(newConnectionNodeA)) {
                    Connection newConnection = new Connection(newConnectionNodeB, newConnectionWeight, mContext);
                    connections.add(newConnection);
                } else {
                    Connection newConnection = new Connection(newConnectionNodeA, newConnectionWeight, mContext);
                    connections.add(newConnection);
                }
            } while (cursor.moveToNext());
            db.close();
        }
        return connections;
    }

    /**
     * login
     * login to the system by passing and email and password to this function, and if the login is successful then return a User object
     * @param email login email
     * @param password login password
     * @return User object if the users login details are entered correctly, null if not
     */
    public User login(String email, String password) {

        // Create new string for sql search query
        String query = "SELECT * FROM Users Where Email = '" + email + "'";
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Run query
        Cursor cursor = db.rawQuery(query, null);

        // If the query did not return will null, move to first record
        if (cursor != null && cursor.moveToNext()) {

            // Do while cursor can move to next record
            do {

                // Get new users email from database record
                String newUserEmail = cursor.getString(cursor.getColumnIndex("Email"));
                // Get new users type from database record
                String newUserType = cursor.getString(cursor.getColumnIndex("Type"));
                // Get new users password from database record
                String newUserPassword = cursor.getString(cursor.getColumnIndex("Password"));

                // Check if login password equals the new password returned from the database
                if(password.equals(newUserPassword)) {

                    // Create new user
                    User newUser = new User(newUserEmail, newUserType);
                    // Return new user
                    return newUser;

                }
            } while (cursor.moveToNext());

        }

        // Return null
        return null;

    }

    /**
     * registerUser
     * refisterUser takes data about a new user and allows them to register by updating the database
     * @param email new users email address
     * @param password new users password
     * @param type new user type
     * @return if error will return false, if not true is returned
     */
    public boolean registerUser(String email, String password, String type) {

        // Try
        try {

            // Get writable database
            SQLiteDatabase db = this.getWritableDatabase();

            // Create new values object
            ContentValues values = new ContentValues();
            // Add new users data to values object
            values.put("Email", email);
            values.put("Password", password);
            values.put("Type", type);

            // Insert new record into users tables of database
            db.insert("Users", null, values);

            // Close the database
            db.close();

            // Return true
            return true;
        } catch (SQLException e) {

            // Return false
            return false;
        }
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

}




