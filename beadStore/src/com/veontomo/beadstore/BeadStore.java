package com.veontomo.beadstore;

import java.util.HashMap;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Represents the bead stand
 * 
 * @author veontomo@gmail.com
 * @since 0.2
 */
public class BeadStore extends SQLiteOpenHelper {

	private final String TAG = "BeadStore";

	/**
	 * Default value of bead sachets
	 */
	private final static int MAXQUANTITY = 10;

	/**
	 * Database holder
	 * 
	 * @since 0.7
	 */
	private SQLiteDatabase database;
	
	private String[] allColumns = {COLUMN_ID, COLUMN_COLORCODE, COLUMN_QUANTITY, COLUMN_WING, COLUMN_ROW, COLUMN_WING};

	/**
	 * Bead colors present on the stand
	 * 
	 * @since 0.2
	 */
	private String standContent = "\"A1\"\n"
			+ "00050 10020 10050 10070 10090\n"
			+ "10110 10140 20010 20060 20080\n"
			+ "30030 30050 30080 30100 30110\n"
			+ "40010 50100 50120 50220 50430\n"
			+ "50710 50150 50060 50290 60000\n"
			+ "60010 60030 60150 60100 60300\n"
			+ "80010 80060 90000 90030 90050\n"
			+ "90070 90090 90120 78102 17020\n"
			+ "17050 17070 17090 17110 17140\n"
			+ "27010 27060 27080 37030 37050\n"
			+ "37080 37100 37110 47010 57100\n"
			+ "57120 57220 57430 57710 57150\n"
			+ "57060 57290 67000 67010 67030\n" + "\"A2\"\n"
			+ "67150 67100 67300 87010 87060\n"
			+ "97000 97030 97050 97070 97090\n"
			+ "97120 11050 21060 31050 41010\n"
			+ "51120 51430 61030 61300 81060\n"
			+ "91050 91090 10050/1 20060/1 30050/1\n"
			+ "50120/1 80060/1 90070/1 02090 32010\n"
			+ "52240 03050 13600 13780 23020\n"
			+ "23040 23980 33000 33020 33040\n"
			+ "33050 33060 33070 33080 33210\n"
			+ "33220 43020 53210 53230 53240\n"
			+ "53250 53270 53310 53410 53430\n"
			+ "63000 63020 63030 63050 63080\n"
			+ "63130 83110 83130 93110 93140\n" + "\"B1\"\n"
			+ "93170 93190 93210 93300 93310 \n"
			+ "46205 57205 59205 14600 24020 \n"
			+ "34020 34210 44020 54250 64050 \n"
			+ "84110 94140 94190 13600/1 33210/1\n"
			+ "53250/1 83110/1 93140/1 93190/1 47102 \n"
			+ "57206 47112 47113 47115 47185 \n"
			+ "46112 46113 49102 59115 59135 \n"
			+ "59155 59195 89110 18181 18184 \n"
			+ "18191 18192 18123 18161 18165 \n"
			+ "18131 18134 18151 18154 18112 \n"
			+ "18113 18141 29980 38302 38386 \n"
			+ "38389 38394 38398 38328 38318 \n"
			+ "38342 38365 38336 38356 38358 \n" + "\"B2\"\n"
			+ "01181 01182 01183 01184 01185\n"
			+ "01191 01192 01193 01194 01195\n"
			+ "01121 01122 01123 01131 01132\n"
			+ "01133 01134 01151 01152 01153\n"
			+ "01154 01161 01162 01163 01164\n"
			+ "01165 01111 01112 01113 01141\n"
			+ "18503 18586 18581 18583 18589\n"
			+ "18598 18595 18528 18565 18536\n"
			+ "18556 18558 18542 18549 37383\n"
			+ "37389 37328 37336 37356 37358\n"
			+ "37342 02153 02154 02161 02163\n"
			+ "02184 02191 02192 02231 02281\n"
			+ "02292 98140 07331 07332 07631\n" + "\"C1\"\n"
			+ "16389 16398 17383 17836 17897\n"
			+ "17899 37188 37398 38318/1 38325/1 \n"
			+ "38336/1 38342/1 38394/1 38395/1 38398/1\n"
			+ "02165 02111 02291 02141 02122\n"
			+ "02121 02123 03281 03282 03283\n"
			+ "03284 03285 03291 03292 03293\n"
			+ "03294 03295 03221 03222 03223 \n"
			+ "03231 03232 03233 03234 03251\n"
			+ "03252 03253 03254 03261 03262 \n"
			+ "03263 03264 03265 03211 03212 \n"
			+ "03213 03241 08283 08286 08288 \n"
			+ "08289 08273 08275 08277 08225 \n"
			+ "08228 08298 08256 08258 08265 \n" + "\"C2\"\n"
			+ "08236 68105 68106 17325 17328\n"
			+ "78181 78182 78183 78184 78185\n"
			+ "78191 78192 78193 78194 78195\n"
			+ "78121 78122 78123 78131 78132\n"
			+ "78133 78134 78151 78152 78153\n"
			+ "78154 78161 78162 78163 78164\n"
			+ "78165 78111 78112 78113 78141\n"
			+ "37136 37154 37156 37173 37177\n"
			+ "37185 37186 46318 37325 16020\n"
			+ "16050 16218 16249 16286 11020\n"
			+ "17708 36110 38149 38349 46102\n"
			+ "57102 46316 17292 17398 17783\n"
			+ "02133 02151 02293 03194 03441\n";

	/**
	 * A mapping from color code to location
	 * 
	 * @since 0.2
	 */
	private HashMap<String, Location> colorToLocation = new HashMap<String, Location>();

	/**
	 * Constructor
	 */
	public BeadStore(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		initialize();
		open();
		loadDb();
		Log.i(TAG,
				"Initialization is done. There are " + colorToLocation.size()
						+ " records.");
	}

	/**
	 * Opens database
	 * 
	 * @since 0.7
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = this.getWritableDatabase();
	}

	/**
	 * Closes db
	 * @since 0.7
	 */
	public void close() {
		super.close();
	}
	
//	 public BeadInfo createBeadInfo(String ) {
//		    ContentValues values = new ContentValues();
//		    values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
//		    long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
//		        values);
//		    Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
//		        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
//		        null, null, null);
//		    cursor.moveToFirst();
//		    Comment newComment = cursorToComment(cursor);
//		    cursor.close();
//		    return newComment;
//		  }


	/**
	 * Loads locations and available quantity of beads into db
	 * 
	 * @since 0.7
	 */
	private void loadDb() {
		ContentValues values;
		long insertId;
		for (Entry<String, Location> infoLine : colorToLocation.entrySet()){
			values = new ContentValues();
			String colorCode = infoLine.getKey();
			Location location = infoLine.getValue();
		    values.put(COLUMN_COLORCODE, colorCode);
		    values.put(COLUMN_QUANTITY, MAXQUANTITY);
		    values.put(COLUMN_ROW, location.getRow());
		    values.put(COLUMN_COLUMN, location.getCol());
		    values.put(COLUMN_WING, location.getWing());
		    insertId = database.insert(TABLE_NAME, null, values);
		    if (insertId == -1){
		    	Log.i(TAG, "problem with inserting bead " + colorCode + " located at " + location.toString());
		    }
		    

			
		}
//	    Cursor cursor = database.query(TABLE_NAME, allColumns, COLUMN_ID + " = " + insertId, null,
//	        null, null, null);
//	    cursor.moveToFirst();
//	    Comment newComment = cursorToComment(cursor);
//	    cursor.close();
	}

	/**
	 * Returns location of the bead with given color code.
	 * 
	 * @param colorCode
	 * @return Location
	 * @since 0.2
	 */
	public Location getByColor(String colorCode) {
		if (colorToLocation.containsKey(colorCode)) {
			return colorToLocation.get(colorCode);
		}
		return null;
	}

	/**
	 * Reads the string with bead content of the stand and prepares a hash map
	 * in order to facilitate future search requests
	 * 
	 * @since 0.2
	 * @see BeadStore#standContent
	 */
	private void initialize() {
		String[] lines = this.standContent.split("\\n");
		Integer linesNum = lines.length;
		String line;
		String currentMarker = null;
		Integer currentRow = 1;
		Integer pointer, rowLen, linesCounter;
		String[] colors;
		String key;
		for (linesCounter = 0; linesCounter < linesNum; linesCounter++) {
			line = lines[linesCounter].trim();
			if (line.equals("")) {
				continue;
			}
			if (line.matches("\".*\"")) {
				currentMarker = line.replace("\"", "");
				currentRow = 1;
				continue;
			}
			colors = line.split("\\s+");
			rowLen = colors.length;
			for (pointer = 0; pointer < rowLen; pointer++) {
				key = colors[pointer];
				this.colorToLocation.put(key, new Location(currentMarker,
						currentRow, pointer + 1));
			}
			currentRow++;
		}
	}

	/**
	 * Returns number of sachets of bead of given color present in the store.
	 * 
	 * @param colorCode
	 * @return int
	 */
	public int getQuantity(String colorCode) {
		// / !!! stub
		return 5;
	}

	public static final String TABLE_NAME = "BeadStore";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COLORCODE = "color_code";
	public static final String COLUMN_QUANTITY = "quantity";

	private static final String DATABASE_NAME = "BeadStore";
	private static final int DATABASE_VERSION = 1;

	private static final String COLUMN_WING = "wing";

	private static final String COLUMN_ROW = "row";

	private static final String COLUMN_COLUMN = "col";
	
	/**
	 * Maximal number of characters in color code string
	 * @since 0.8
	 */
	private static final int COLORCODE_MAX_LEN = 7;
	
	/**
	 * Maximal number of characters in string describing wing
	 * @since 0.8
	 */
	private static final int WING_MAX_LEN = 2;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_COLORCODE + " varchar(" + String.valueOf(COLORCODE_MAX_LEN) + ") not null unique, " 
			+ COLUMN_QUANTITY + " integer, " 
			+ COLUMN_WING + " char(" + String.valueOf(WING_MAX_LEN) + "), " 
			+ COLUMN_ROW + " tinyint unsigned, "
			+ COLUMN_COLUMN + " tinyint unsigned, " 
			+ "unique (" + COLUMN_WING + ", " + COLUMN_ROW + ", " + COLUMN_COLUMN + "));";

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.i(TAG, "creating db");
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(BeadStore.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
