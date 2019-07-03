package mc.alk.arena.serializers;

import mc.alk.arena.util.Log;
import mc.alk.v1r9.serializers.SQLSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLInstance extends SQLSerializer {

    public static String URL = "localhost";
    public static String PORT = "3306";
    public static String USERNAME = "root";
    public static String PASSWORD = "";

    public static String DB = "BattleArena";

    public static final String TABLE_PREFIX = "ba_";

    public static final String SERVER = "Server"; // The server name
    public static final String GAME = "Game"; // The game
    public static final String ARENA = "ArenaName"; // The arena name
    public static final String QUEUED_PLAYERS = "QueuedPlayers"; // The players queued
    public static final String STATE = "State"; // The state of the arena
    public static final String MAX_PLAYERS = "MaxPlayers"; // The maximum amount of players that can join
    public static final String ENABLED = "Enabled"; // If the arena is enabled

    private String activeTable;

    private String tableName;

    private String createTable;
    private String insertTable;
    private String updateTable;

    private String getServer;
    private String getGame;
    private String getArena;
    private String getQueuedPlayers;
    private String getState;
    private String getMaxPlayers;
    private String getEnabled;

    private String type;

    public SQLInstance(String tableName, String type) {
        this.type = type;

        setTable(tableName);
    }

    public void setTable(String tableName) {
        this.tableName = tableName;

        this.activeTable = TABLE_PREFIX + tableName + "_active_" + type;
    }

    public String getTable(){
        return tableName;
    }

    @Override
    public boolean init() {
        setDB(DB);
        setType(SQLSerializer.SQLType.MYSQL);
        setURL(URL);
        setPort(PORT);
        setUsername(USERNAME);
        setPassword(PASSWORD);

        super.init();

        createTable = "CREATE TABLE IF NOT EXISTS " + activeTable
                + " (" + SERVER + " VARCHAR(100), " + GAME + " VARCHAR(100),"
                + ARENA + " VARCHAR(100), " + QUEUED_PLAYERS + " VARCHAR(40000), "
                + STATE + " VARCHAR(100), " + MAX_PLAYERS + " VARCHAR(100), "
                + ENABLED + " VARCHAR(100))";

        insertTable = "INSERT INTO " + activeTable + " VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE "
                + SERVER + " = VALUES(" + SERVER + "), " + GAME + " = VALUES(" + GAME + "), "
                + ARENA + " = VALUES(" + ARENA + "), " + QUEUED_PLAYERS + " = VALUES(" + QUEUED_PLAYERS + "), "
                + STATE + " = VALUES(" + STATE + "), " + MAX_PLAYERS + " = VALUES(" + MAX_PLAYERS + "), "
                + ENABLED + " = VALUES(" + ENABLED + ")";

        updateTable = "UPDATE " + activeTable + " SET " + SERVER + " = ?, " + GAME + " = ?, "
                + ARENA + " = ?, " + QUEUED_PLAYERS + " = ?, " + STATE + " = ?, "
                + MAX_PLAYERS + " = ?," + ENABLED  + " = ? WHERE " + ARENA + " = ?";

        getServer = "SELECT * FROM " + activeTable + " WHERE " + SERVER + " = ?";
        getGame = "SELECT * FROM " + activeTable + " WHERE " + GAME + " = ?";
        getArena = "SELECT * FROM " + activeTable + " WHERE " + ARENA + " = ?";
        getQueuedPlayers = "SELECT * FROM " + activeTable + " WHERE " + QUEUED_PLAYERS + " = ?";
        getState = "SELECT * FROM " + activeTable + " WHERE " + STATE + " = ?";
        getMaxPlayers = "SELECT * FROM " + activeTable + " WHERE " + MAX_PLAYERS + " = ?";
        getEnabled = "SELECT * FROM " + activeTable + " WHERE " + ENABLED + " = ?";

        try {
            createTable(activeTable, createTable);
        } catch (Exception ex) {
            Log.err("Could not create table for " + tableName + "! Please read the error log below!");
            Log.printStackTrace(ex);
        }
        return true;
    }

    // TODO: Find a way to break method parameters into its own object?
    public void insertTable(String server, String arena, String queuedPlayers,
                            String state, String maxPlayers, String enabled)  {

        List<List<Object>> batch = new ArrayList<List<Object>>();
        batch.add(Arrays.asList(new Object[] {server, tableName, arena, queuedPlayers, state, maxPlayers, enabled}));

        try {
            executeBatch(true, insertTable, batch);
        } catch (Exception ex) {
            Log.err("Failed to insert into table!");
            Log.err("Tried to run insert: " + insertTable);
            Log.err("Inserted batch: '" + server + ", " + tableName + ", " + arena + ", "
                + queuedPlayers + ", " + state + ", "  + maxPlayers + ", " + enabled + "'");

            ex.printStackTrace();
        }
    }

    public void updateTable(String server, String arena, String queuedPlayers,
                            String state, String maxPlayers, String enabled) {

        List<List<Object>> batch = new ArrayList<List<Object>>();
        batch.add(Arrays.asList(new Object[] {server, tableName, arena, queuedPlayers, state, maxPlayers, enabled, arena}));

        executeBatch(true, updateTable, batch);
    }
}
