package com.example.user.letsgotoateith.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


/**
 * Created by user on 16/4/2015.
 */
public class TransfersProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TransfersDbHelper mOpenHelper;

    static final int USER_PROFILE = 300;

    static final int TRANSPORT_DATA_S = 100;
    static final int USER_PROFILE_S = 101;
    static final int TRANSPORT_CHOOSE_S = 102;

    static final int TRANSPORT_DATA_I = 200;
    static final int USER_PROFILE_I = 201;
    static final int TRANSPORT_CHOOSE_I = 202;

    private static final SQLiteQueryBuilder sQueryBuilder;

    static{
        sQueryBuilder = new SQLiteQueryBuilder();


        //reg_cars show only entries with not all people found
        sQueryBuilder.setTables(
                TransfersContract.CarsEntry.TABLE_NAME //+ " WHERE " +
                //                TransfersContract.CarsEntry.COLUMN_PEOPLE_REG +
                //               " != " + TransfersContract.CarsEntry.COLUMN_PEOPLE
        );
    }

    private static final SQLiteQueryBuilder sSearchTranspStatusQueryBuilder;

    static{
        sSearchTranspStatusQueryBuilder = new SQLiteQueryBuilder();

        //weather INNER JOIN location ON weather.location_id = location._id
        sSearchTranspStatusQueryBuilder.setTables(
                TransfersContract.TransportsEntry.TABLE_NAME + " INNER JOIN " +
                        TransfersContract.CarsEntry.TABLE_NAME +
                        " ON " + TransfersContract.CarsEntry.TABLE_NAME+
                        "."+TransfersContract.CarsEntry._ID+
                        " = "+TransfersContract.TransportsEntry.TABLE_NAME+
                        "."+TransfersContract.TransportsEntry.COLUMN_REG_CAR_ID+" INNER JOIN " +
                        TransfersContract.TransportsEntry.TABLE_NAME +
                        " ON " + TransfersContract.TransportsEntry.TABLE_NAME+
                        "."+TransfersContract.TransportsEntry.COLUMN_USER_ID+
                        " = "+TransfersContract.UsersEntry.TABLE_NAME+
                        "."+TransfersContract.UsersEntry._ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TransfersDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TRANSPORT_DATA_S:
                return TransfersContract.CarsEntry.CONTENT_TYPE;
            case USER_PROFILE_S:
                return TransfersContract.UsersEntry.CONTENT_TYPE;
            case TRANSPORT_CHOOSE_S:
                return TransfersContract.TransportsEntry.CONTENT_TYPE;
            case TRANSPORT_DATA_I:
                return TransfersContract.CarsEntry.CONTENT_TYPE;
            case USER_PROFILE_I:
                return TransfersContract.UsersEntry.CONTENT_TYPE;
            case TRANSPORT_CHOOSE_I:
                return TransfersContract.TransportsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TRANSPORT_DATA_I:

                rowsUpdated = db.update(TransfersContract.CarsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case USER_PROFILE_I:
                rowsUpdated = db.update(TransfersContract.UsersEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TRANSPORT_CHOOSE_I:
                rowsUpdated = db.update(TransfersContract.TransportsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TRANSPORT_DATA_I: {

                long _id = db.insert(TransfersContract.CarsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TransfersContract.CarsEntry.buildCarUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER_PROFILE_I: {
                long _id = db.insert(TransfersContract.UsersEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TransfersContract.UsersEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRANSPORT_CHOOSE_I:{
                long _id = db.insert(TransfersContract.TransportsEntry.TABLE_NAME, TransfersContract.UsersEntry._ID, values);
                if ( _id > 0 )
                    returnUri = TransfersContract.TransportsEntry.buildTranspUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TransfersContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        //matcher.addURI(authority, TransfersContract.PATH_USERS, USER_PROFILE);
        matcher.addURI(authority, TransfersContract.PATH_USERS, USER_PROFILE_I);
        matcher.addURI(authority, TransfersContract.PATH_REG_CARS + "/#/#/#/#/#/#", TRANSPORT_DATA_I);
        matcher.addURI(authority, TransfersContract.PATH_TRANSP + "/#/#/#/#", TRANSPORT_CHOOSE_I);
        matcher.addURI(authority, TransfersContract.PATH_USERS + "/#", USER_PROFILE_S);
        matcher.addURI(authority, TransfersContract.PATH_REG_CARS + "/#/#/#/#/#", TRANSPORT_DATA_S);
        matcher.addURI(authority, TransfersContract.PATH_TRANSP + "/#/#", TRANSPORT_CHOOSE_S);

        return matcher;
    }

    private static final String sSearchCar =
            TransfersContract.CarsEntry.TABLE_NAME +
                    "." + TransfersContract.CarsEntry.COLUMN_DAY+ " = ? AND " +
                    TransfersContract.CarsEntry.TABLE_NAME +
                    "."+TransfersContract.CarsEntry.COLUMN_DEP_TIME + " = ? "+
                    TransfersContract.CarsEntry.TABLE_NAME +
                    "."+TransfersContract.CarsEntry.COLUMN_RET_TIME + " = ? "+
                    TransfersContract.CarsEntry.TABLE_NAME +
                    "."+TransfersContract.CarsEntry.COLUMN_FREQ + " = ? "+
                    TransfersContract.CarsEntry.TABLE_NAME +
                    "."+TransfersContract.CarsEntry.COLUMN_AREA + " = ? ";

    private static final String sGetTransport =
            TransfersContract.TransportsEntry.TABLE_NAME +
                    "." + TransfersContract.TransportsEntry.COLUMN_REG_CAR_ID+ " = ? AND " +
                    TransfersContract.TransportsEntry.TABLE_NAME +
                    TransfersContract.TransportsEntry.COLUMN_USER_ID + " = ? ";

    private static final String sUser =
            TransfersContract.UsersEntry.TABLE_NAME+
                    "." + TransfersContract.UsersEntry._ID + " = ? ";

    private Cursor getCarsFromSearch(Uri uri, String[] projection, String sortOrder) {
        int day = TransfersContract.CarsEntry.getDayFromUri(uri);
        int dep = TransfersContract.CarsEntry.getDepFromUri(uri);
        int ret=TransfersContract.CarsEntry.getRetFromUri(uri);
        int freq=TransfersContract.CarsEntry.getFreqFromUri(uri);
        String area=TransfersContract.CarsEntry.getAreaFromUri(uri);

        return sQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sSearchCar,
                new String[]{String.valueOf(day),String.valueOf(dep),String.valueOf(ret),String.valueOf(freq),area},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case TRANSPORT_DATA_S:
            {
                retCursor = getCarsFromSearch(uri, projection, sortOrder);
                break;
            }
            case USER_PROFILE_I: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TransfersContract.UsersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRANSPORT_CHOOSE_S: {
                retCursor = getTransportStatus(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getTransportStatus(Uri uri, String[] projection, String sortOrder) {

        int reg_car= TransfersContract.TransportsEntry.getRegCarIdFromUri(uri);
        int user_id = TransfersContract.TransportsEntry.getUserIdFromUri(uri);

        return sSearchTranspStatusQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sGetTransport,
                new String[]{String.valueOf(reg_car),String.valueOf(user_id)},
                null,
                null,
                sortOrder
        );
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case TRANSPORT_DATA_S:
                rowsDeleted = db.delete(
                        TransfersContract.CarsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_PROFILE_S:
                rowsDeleted = db.delete(
                        TransfersContract.UsersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRANSPORT_CHOOSE_S:
                rowsDeleted = db.delete(
                        TransfersContract.TransportsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

}