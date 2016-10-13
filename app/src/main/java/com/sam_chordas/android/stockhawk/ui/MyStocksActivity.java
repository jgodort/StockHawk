package com.sam_chordas.android.stockhawk.ui;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.melnykov.fab.FloatingActionButton;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.Utilities.Constants;
import com.sam_chordas.android.stockhawk.Utilities.Utils;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.data.StockQuoteProvider;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.rest.RecyclerViewItemClickListener;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.Quote;
import com.sam_chordas.android.stockhawk.service.StockIntentService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.touch_helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sam_chordas.android.stockhawk.Utilities.Constants.HISTORICAL_DATA_BUNDLE_KEY;
import static com.sam_chordas.android.stockhawk.Utilities.Constants.SELECTED_STOCKQUOTE_KEY;

public class MyStocksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MyStocksActivity.class.getName();

    /**
     * The projection to retrieve the historical information from  quotes.
     */
    private static final String[] HISTORICAL_COLUMNS = {
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." + StockQuoteContract.HistoricalQuoteEntry._ID,
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." + StockQuoteContract.HistoricalQuoteEntry.COLUMN_SYMBOL,
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." + StockQuoteContract.HistoricalQuoteEntry.COLUMN_DATE,
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." + StockQuoteContract.HistoricalQuoteEntry.COLUMN_OPEN,
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." + StockQuoteContract.HistoricalQuoteEntry.COLUMN_CLOSE,
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." + StockQuoteContract.HistoricalQuoteEntry.COLUMN_LOW,
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." + StockQuoteContract.HistoricalQuoteEntry.COLUMN_HIGH,
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." + StockQuoteContract.HistoricalQuoteEntry.COLUMN_VOLUME

    };

    /**
     * The projection to retrieve the information from a quote.
     */
    private static final String[] QUOTE_COLUMNS = {
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry._ID,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_NAME,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_HIGH,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_LOW,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_HIGH,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_LOW,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_BID,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE_PERCENT,

    };

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Intent mServiceIntent;
    private ItemTouchHelper mItemTouchHelper;
    private static final int CURSOR_LOADER_ID = 0;
    private QuoteCursorAdapter mCursorAdapter;
    private Context mContext;
    private Cursor mCursor;


    @BindView(R.id.recyclerview_stock_empty)
    TextView mEmptyViewStock;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        mContext = this;
        checkInternetConnectionAvailable();
        setContentView(R.layout.activity_my_stocks);
        ButterKnife.bind(this);

        // The intent service is for executing immediate pulls from the Yahoo API
        // GCMTaskService can only schedule tasks, they cannot execute immediately
        mServiceIntent = new Intent(this, StockIntentService.class);
        if (savedInstanceState == null) {
            // Run the initialize task service so that some stocks appear upon an empty database
            mServiceIntent.putExtra(Constants.TAG_KEY, StockTaskService.INIT_PARAM);
            if (checkInternetConnectionAvailable()) {
                startService(mServiceIntent);
            } else {
                networkToast();
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        mCursorAdapter = new QuoteCursorAdapter(this, null);

        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        prepareDetailIntentData(position);
                    }
                }));
        recyclerView.setAdapter(mCursorAdapter);


        fab.attachToRecyclerView(recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkInternetConnectionAvailable()) {

                    Dialog mDialog = new MaterialDialog.Builder(mContext).title(R.string.symbol_search)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .autoDismiss(true)
                            .positiveText(R.string.add)
                            .negativeText(R.string.disagree)
                            .input(R.string.input_hint, R.string.input_prefill, false,
                                    new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            addQuote(input);
                                        }
                                    }).build();
                    mDialog.show();
                } else {
                    networkToast();
                }

            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        mTitle = getTitle();
        if (checkInternetConnectionAvailable()) {
            configurePeriodicTask();
        }

        Log.d(LOG_TAG, "onCreate: end");
    }

    private void addQuote(CharSequence input) {
        // On FAB click, receive user input. Make sure the stock doesn't already exist
        // in the DB and proceed accordingly
        Cursor c = getContentResolver().query(
                StockQuoteContract.StockQuoteEntry.buildStockQuoteSymbolUri(input.toString().toUpperCase()),
                new String[]{"DISTINCT " + StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL},
                StockQuoteProvider.sStockQuoteSelection,
                null,
                null);
        if (c.getCount() != 0) {
            Toast toast =
                    Toast.makeText(MyStocksActivity.this, "This stock is already saved!",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
            toast.show();
            return;
        } else {
            // Add the stock to DB
            mServiceIntent.putExtra(Constants.TAG_KEY, StockTaskService.ADD_PARAM);
            mServiceIntent.putExtra(Constants.SYMBOL_KEY, input.toString().toUpperCase());
            startService(mServiceIntent);
        }
    }

    private void prepareDetailIntentData(int position) {
        Log.d(LOG_TAG, "prepareDetailIntentData: start ");
        Bundle arguments = new Bundle();

        String selection = StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME + "." +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_QUOTE_ID + "=?";

        Cursor historicData = getContentResolver().query(StockQuoteContract.HistoricalQuoteEntry.CONTENT_URI,
                HISTORICAL_COLUMNS,
                selection,
                new String[]{String.valueOf(mCursorAdapter.getStockId(position))},
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_DATE + " ASC");

        String selectionQuote = StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." +
                StockQuoteContract.StockQuoteEntry._ID + "=?";
        Cursor quoteCursor = getContentResolver().query(StockQuoteContract.StockQuoteEntry.CONTENT_URI,
                QUOTE_COLUMNS,
                selectionQuote,
                new String[]{String.valueOf(mCursorAdapter.getStockId(position))},
                null);


        if (historicData != null && historicData.moveToFirst()) {

            List<HistoricalQuote> historicalQuotes = new ArrayList<HistoricalQuote>();
            for (int i = 0; i < historicData.getCount(); i++) {
                HistoricalQuote hQuote = new HistoricalQuote();
                hQuote.date = historicData.getString(historicData.getColumnIndex(StockQuoteContract.HistoricalQuoteEntry.COLUMN_DATE));
                hQuote.open = historicData.getString(historicData.getColumnIndex(StockQuoteContract.HistoricalQuoteEntry.COLUMN_OPEN));
                hQuote.close = historicData.getString(historicData.getColumnIndex(StockQuoteContract.HistoricalQuoteEntry.COLUMN_CLOSE));
                hQuote.high = historicData.getString(historicData.getColumnIndex(StockQuoteContract.HistoricalQuoteEntry.COLUMN_HIGH));
                hQuote.low = historicData.getString(historicData.getColumnIndex(StockQuoteContract.HistoricalQuoteEntry.COLUMN_LOW));
                hQuote.volume = historicData.getString(historicData.getColumnIndex(StockQuoteContract.HistoricalQuoteEntry.COLUMN_VOLUME));

                historicalQuotes.add(hQuote);
                historicData.moveToNext();
            }
            historicData.close();

            if (!historicalQuotes.isEmpty()) {
                arguments.putParcelableArrayList(HISTORICAL_DATA_BUNDLE_KEY, (ArrayList<? extends Parcelable>) historicalQuotes);
            }
        }


        if (quoteCursor != null && quoteCursor.moveToFirst()) {


            Quote quote = new Quote(
                    quoteCursor.getInt(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry._ID)),
                    quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL)),
                    quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_NAME)),
                    quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_BID)),
                    quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE_PERCENT)),
                    quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE)));
            quote.daysHigh = quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_HIGH));
            quote.daysLow = quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_LOW));
            quote.yearHigh = quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_HIGH));
            quote.yearLow = quoteCursor.getString(quoteCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_LOW));

            arguments.putParcelable(SELECTED_STOCKQUOTE_KEY, quote);

            quoteCursor.close();
        }


        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(SELECTED_STOCKQUOTE_KEY, arguments);

        mContext.startActivity(intent);

        Log.d(LOG_TAG, "prepareDetailIntentData: end ");
    }

    /**
     * Method that configure the synchronization of the information about the Stock.
     */
    private void configurePeriodicTask() {
        Log.d(LOG_TAG, "configurePeriodicTask: start");
        long period = 3600L;
        long flex = 10L;
        String periodicTag = "periodic";

        // create a periodic task to pull stocks once every hour after the app has been opened. This
        // is so Widget data stays up to date.
        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(StockTaskService.class)
                .setPeriod(period)
                .setFlex(flex)
                .setTag(periodicTag)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .build();
        // Schedule task with tag "periodic." This ensure that only the stocks present in the DB
        // are updated.
        GcmNetworkManager.getInstance(this).schedule(periodicTask);
        Log.d(LOG_TAG, "configurePeriodicTask: end");
    }

    /**
     * Method that checks the availability of the connection.
     */
    private boolean checkInternetConnectionAvailable() {
        Log.d(LOG_TAG, "checkInternetConnectionAvailable: start");
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Log.d(LOG_TAG, "checkInternetConnectionAvailable: end");
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume: start");
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        Log.d(LOG_TAG, "onResume: end");
    }

    /**
     * Method that show a Toast message if the network is not available.
     */
    public void networkToast() {
        Log.d(LOG_TAG, "networkToast: start");
        Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "networkToast: end");
    }

    public void restoreActionBar() {
        Log.d(LOG_TAG, "restoreActionBar: start");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        Log.d(LOG_TAG, "restoreActionBar: end");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu: start");
        getMenuInflater().inflate(R.menu.my_stocks, menu);
        restoreActionBar();
        Log.d(LOG_TAG, "onCreateOptionsMenu: end");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected: start");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_change_units) {
            // this is for changing stock changes from percent value to dollar value
            Utils.showPercent = !Utils.showPercent;
            this.getContentResolver().notifyChange(StockQuoteContract.StockQuoteEntry.CONTENT_URI, null);
        }

        Log.d(LOG_TAG, "onOptionsItemSelected: end");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader: start");
        // This narrows the return to only the stocks that are most current.

        String[] projection = new String[]{
                "DISTINCT " + StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL,
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry._ID,
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_NAME,
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_BID,
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_PERCENT_CHANGE,
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE,
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_ISUP};

        Log.d(LOG_TAG, "onCreateLoader: end");
        return new CursorLoader(this, StockQuoteContract.StockQuoteEntry.CONTENT_URI,
                projection,
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished: start");
        mCursorAdapter.swapCursor(data);
        mCursor = data;

        //We need to check if the internet connection is avalibale,
        // if the connection is unavailable, we need to show the proper
        //message.
        if (checkInternetConnectionAvailable()) {

            //If the connection is available, but the cursorAdapter didn´t retrieve
            //any stock quote, we need to show the propper message.
            if (mCursorAdapter.getItemCount() > 0) {
                mEmptyViewStock.setVisibility(View.INVISIBLE);
            } else {
                //Add new Stock quotes message.
                mEmptyViewStock.setText(getString(R.string.no_stocks_display));
                mEmptyViewStock.setVisibility(View.VISIBLE);
            }

        } else {
            //No network message.
            mEmptyViewStock.setText(getString(R.string.no_network_available));
            mEmptyViewStock.setVisibility(View.VISIBLE);
        }

        Log.d(LOG_TAG, "onLoadFinished: end");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        Log.d(LOG_TAG, "onLoaderReset: start");
        mCursorAdapter.swapCursor(null);
        Log.d(LOG_TAG, "onLoaderReset: end");
    }

}
