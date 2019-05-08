package in.rajgrihisingh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.fragment.Dashboard;
import in.rajgrihisingh.fragment.Labour_Payment;
import in.rajgrihisingh.fragment.Manufacture;
import in.rajgrihisingh.fragment.MyAccount;
import in.rajgrihisingh.fragment.AttandanceTab;
import in.rajgrihisingh.fragment.Expenses;
import in.rajgrihisingh.fragment.Home;
import in.rajgrihisingh.fragment.PurchaseTab;
import in.rajgrihisingh.fragment.Profile;
import in.rajgrihisingh.fragment.Report;
import in.rajgrihisingh.fragment.Mail;
import in.rajgrihisingh.fragment.Salary;
import in.rajgrihisingh.fragment.Sites;
import in.rajgrihisingh.fragment.Staff;
import in.rajgrihisingh.model.UpdateMeeDialog;
import in.rajgrihisingh.model.VolleySingleton;
import in.rajgrihisingh.pref.SharedPrefManager;

public class RGandSons extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    public static int navItemIndex = 0;
    private static final String TAG_HOME = "Home";
    private static final String TAG_SITES = "Projects";
    private static final String TAG_REPORT = "Manufacture";

    private static final String TAG_Mail = "Mail";
    private static final String TAG_PROFILE = "Profile";
    private static final String TAG_Account = "Account";

    private static final String TAG_Attendance = "Attendance";
    private static final String TAG_PAYMENT = "Purchase";
    private static final String TAG_Staff = "Staff";
    private static final String TAG_Expense= "Expense";
    private static final String TAG_Logout = "Logout";
    private static final String TAG_Salary = " Salary";

    private static final String TAG_Labour = " Labour Payment";
    public static String CURRENT_TAG = TAG_HOME;
    private String[] activityTitles;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String name,email,token;
    String versionName;
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgand_sons);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle=getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            token = bundle.getString("token");
            email = bundle.getString("email");
        }else {
            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            name = sp.getString("keyusername", "");
            token=sp.getString("keyid", "");
            email=sp.getString("email", "");
        }
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles3);

        final PackageManager packageManager = getApplicationContext().getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(getApplicationContext().getPackageName(), 0);
                PACKAGE_NAME = getApplicationContext().getPackageName();
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = null;
            }
        }
        UPDATE();
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle(TAG_PROFILE);
                Fragment fragment = new Profile();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.commitAllowingStateLoss();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        loadNavHeader();
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }
    private void loadNavHeader() {
        txtName.setText(name);
        txtWebsite.setText(email);
    }
    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }
    private Fragment getHomeFragment() {
        switch (navItemIndex) {

            case 0:

                Dashboard dashboardFragment = new Dashboard();
                return dashboardFragment;

            case 1:

                Manufacture manufacture=new Manufacture();
                return manufacture;

            case 2:

                Sites settingsFragment = new Sites();
                return settingsFragment;

            case 3:

                PurchaseTab purchaseFragment = new PurchaseTab();
                return purchaseFragment;

            case 4:

                MyAccount accountFragment = new MyAccount();
                return accountFragment;

            case 5:

                Staff staff = new Staff();
                return staff;

            case 6:

                Salary salary = new Salary();
                return salary;

            case 7:

                Expenses expenses = new Expenses();
                return expenses;

            case 8:

                AttandanceTab attendance = new AttandanceTab();
                return attendance;

            case 9:

                Mail mailFragment = new Mail();
                return mailFragment;

            case 10:

                Labour_Payment labour_payment = new Labour_Payment();
                return labour_payment;

            case 11:

                SharedPrefManager.getInstance(getApplicationContext()).logout();
                Intent intent=new Intent(RGandSons.this,Login.class);
                startActivity(intent);
                finish();

            default:
                return new Home();
        }
    }
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_REPORT;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);

                        break;

                    case R.id.report:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_REPORT;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);

                        break;

                    case R.id.Sites:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SITES;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);

                        break;

                    case R.id.Purchase:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_PAYMENT;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);

                        break;

                    case R.id.account:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_Account;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);

                        break;

                    case R.id.Staff:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_Staff;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);

                        break;

                    case R.id.Salary:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_Salary;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);

                        break;

                    case R.id.Expense:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_Expense;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);
                        break;

                    case R.id.Attendance:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_Attendance;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);
                        break;

                    case R.id.mail:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_Mail;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);
                        break;

                    case R.id.Labour_Payment:
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_Labour;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        break;


                    case R.id.logout:
                        navItemIndex = 11;
                        CURRENT_TAG = TAG_Logout;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot2);

                        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot2);
                        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_dot2);
                        break;

                    default:
                        navItemIndex = 0;
                        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
                }
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }
    private void UPDATE() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GETVERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONObject getversion = obj.getJSONObject("version");
                                String version_code = getversion.getString("version_code");
                                String version_name = getversion.getString("version_name");

                                if (version_name.equals(versionName)) {

                                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();

                                } else {

                                    UpdateMeeDialog updateMeeDialog = new UpdateMeeDialog();
                                    updateMeeDialog.showDialogAddRoute(RGandSons.this, PACKAGE_NAME);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
