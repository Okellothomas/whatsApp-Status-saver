package com.statuses.flavoured.fragments.whatsapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.statuses.flavoured.GenericAdapter;
import com.statuses.flavoured.HelperMethods;
import com.statuses.flavoured.InstanceHandler;
import com.statuses.flavoured.R;
import com.statuses.flavoured.adapter.WhatsappImageAdapter;
import com.statuses.flavoured.model.ImageModel;
import com.statuses.flavoured.recycler.RecyclerClickListener;
import com.statuses.flavoured.recycler.RecyclerTouchListener;
import com.statuses.flavoured.recycler.ToolbarActionModeCallback;
import com.statuses.flavoured.viewer.ImageViewer;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link WhatsappImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatsappImageFragment extends Fragment {
    private static WhatsappImageFragment mInstance;
    RecyclerView recyclerView;
    FragmentActivity activity;
    ProgressBar progressBar;
    private AdView mAdView;
    FloatingActionButton fab;
    WhatsappImageAdapter waImageAdapter;
    private InterstitialAd mInterstitialAd;
    ArrayList<ImageModel> arrayList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private static View v;
    private ActionMode mActionMode;
    Fragment frg;
    FragmentTransaction ft=null;

    FrameLayout frameLayout;
    AdView adView;

    List<File> selected_Files = new ArrayList<>();

    private int STORAGE_PERMISSION_CODE = 11;

    public WhatsappImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_whatsapp_image, container, false);
        activity = getActivity();

      /* MobileAds.initialize(activity, getString(R.string.admob_app_id));
       mInterstitialAd = new InterstitialAd(activity);
       mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
       mInterstitialAd.loadAd(new AdRequest.Builder().build());*/


        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });

        frameLayout = getActivity().findViewById(R.id.homer_banner_container);
        adView = new AdView(getContext());
        adView.setAdUnitId(getString(R.string.banner_id));
        frameLayout.addView(adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);

        mInstance = this;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.ref);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_wa_image);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar_wa);

        populateRecyclerView();
        implementRecyclerViewClickListeners();

      /*  AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");*/

        //Initialise mobile ads
//        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });

//        MobileAds.initialize(getContext());
//        AdLoader adLoader = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110")
//                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
//                    @Override
//                    public void onNativeAdLoaded(NativeAd nativeAd) {
//                        NativeTemplateStyle styles = new
//                                NativeTemplateStyle.Builder().build();
//                        TemplateView template = getActivity().findViewById(R.id.template);
//                        template.setStyles(styles);
//                        template.setNativeAd(nativeAd);
//                    }
//                })
//                .build();
//
//        adLoader.loadAd(new AdRequest.Builder().build());


        //mAdView = v.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
///        mAdView.loadAd(adRequest);

        swipeRefreshLayout.setColorSchemeResources(new int[]{R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark});
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
               /* if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }*/
            }
        });
        /*fab =  v.findViewById(R.id.wa_image_fab_save_all);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, "prob", Toast.LENGTH_SHORT).show();
                saveAll();
              *//*  if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }*//*
            }
        });*/
        return v;
    }

    private AdSize getAdSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        // If the ad hasn't been laid out, default to the full screen width.
        float adWidthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adWidth);
    }

    private void populateRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));

       if(checkPermission()) {
           getStatus();
       }
       else
       {
           requestPermission();
       }
        waImageAdapter = new WhatsappImageAdapter(activity, arrayList);
        recyclerView.setAdapter(waImageAdapter);
        waImageAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    //Implement item click and long click over recycler view
    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (mActionMode != null)
                    onListItemSelect(position);
                else {
                    String str = waImageAdapter.getItem(position).getPath();
                    try {
                        Intent intent = new Intent(getActivity(), ImageViewer.class);
                        intent.putExtra("pos", str);
                        intent.putExtra("position", position);
                        startActivityForResult(intent, 1);
                    } catch (Throwable e) {
                        throw new NoClassDefFoundError(e.getMessage());
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
               mActionMode = null;
               onListItemSelect(position);
            }
        }));
    }


    //List item select method
    private void onListItemSelect(int position) {
        waImageAdapter.toggleSelection(position);//Toggle the selection
        List<Fragment> fragments;
        File[] listFiles1 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();
        File[] listFiles2 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();

        int firstFile = listFiles1.length;
        int secondFile = listFiles2.length;


        File[] listFile3 = new File[firstFile + secondFile];

        int pos = 0;

        for(File item: listFiles1)
        {
            listFile3[pos]=item;
            pos++;
        }
        for(File item: listFiles2)
        {
            listFile3[pos]=item;
            pos++;
        }

        File[] listFiles = listFile3;
        boolean hasCheckedItems = waImageAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbarActionModeCallback(getActivity(), new GenericAdapter<WhatsappImageAdapter>(waImageAdapter), arrayList, new InstanceHandler<WhatsappImageFragment>(mInstance)));
        }
        else if (!hasCheckedItems && mActionMode != null)
        // there no selected items, finish the actionMode
        {
            mActionMode.finish();
            mActionMode=null;
        }

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(waImageAdapter
                    .getSelectedCount()) + " selected");


    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    private void saveAll() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set title
        alertDialogBuilder.setTitle("Save All Status");

        // set dialog message
        alertDialogBuilder
                .setMessage("This Action will Save all the available Image Statuses... \nDo you want to Continue?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        File[] listFiles1 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();
                        File[] listFiles2 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("Android/media/com.whatsapp/WhatsApp/Media/.Statuses/").toString()).listFiles();


                        List list = new ArrayList(Arrays.asList(listFiles1)); //returns a list view of an array
//returns a list view of str2 and adds all elements of str2 into list
                        list.addAll(Arrays.asList(listFiles2));


                        File[] listFiles = (File[]) list.toArray();         //converting list to array

                        if (waImageAdapter.getItemCount() == 0) {
                            Toast.makeText(activity, "No Status available to Save...", Toast.LENGTH_SHORT).show();
                        } else {
                            int i = 0;
                            while (i < listFiles.length) {
                                try {
                                    File file = listFiles[i];
                                    String str = file.getName().toString();
                                    if (str.endsWith(".jpg") || str.endsWith(".jpeg") || str.endsWith(".png")) {
                                        HelperMethods helperMethods = new HelperMethods(activity.getApplicationContext());
                                        HelperMethods.transfer(file);
                                    }
                                    i++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            Toast.makeText(activity, "Images saved.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });


        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black_overlay));
            }
        });

        // show it
        alertDialog.show();
    }


    private void saveSelected() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set title
        alertDialogBuilder.setTitle("Save");

        // set dialog message
        alertDialogBuilder
                .setMessage("This Action will Save all the available Image Statuses... \nDo you want to Continue?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        File[] listFiles = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();
                        if (waImageAdapter.getItemCount() == 0) {
                            Toast.makeText(activity, "No Status available to Save...", Toast.LENGTH_SHORT).show();
                        } else {
                            int i = 0;
                            while (i < selected_Files.size()) {
                                try {
                                    File file = selected_Files.get(i);
                                    String str = file.getName().toString();
                                    if (str.endsWith(".jpg") || str.endsWith(".jpeg") || str.endsWith(".png")) {
                                        HelperMethods helperMethods = new HelperMethods(activity.getApplicationContext());
                                        HelperMethods.transfer(file);
                                    }
                                    i++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            Toast.makeText(activity, "Done :)", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });


        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black_overlay));
            }
        });

        // show it
        alertDialog.show();
    }

    public void getStatus(){
        File[] listFiles ={};

        File[] listFiles1 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();
        File[] listFiles2 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/Android/media/com.whatsapp/WhatsApp/Media/.Statuses/").toString()).listFiles();


        if(listFiles1!=null && listFiles2!=null) {
            listFiles = Arrays.copyOf(listFiles1, listFiles1.length + listFiles2.length);
            System.arraycopy(listFiles2, 0, listFiles, listFiles1.length, listFiles2.length);
        }
        else if(listFiles1==null && listFiles2!=null)
        {
            listFiles = listFiles2;
        }
        else if(listFiles2==null && listFiles1!=null)
        {
            listFiles=listFiles1;
        }
        //converting list to array
        if (listFiles != null && listFiles.length >= 1) {
            Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        }
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png")) {
                    ImageModel model=new ImageModel(file.getAbsolutePath());
                    arrayList.add(model);
                }
            }
        }
    }
//    public void getStatus(){
//        File[] listFiles ={};
//
//        File[] listFiles1 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();
//        File[] listFiles2 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/Android/media/com.whatsapp/WhatsApp/Media/.Statuses/").toString()).listFiles();
//
//
//        if(listFiles1!=null && listFiles2!=null) {
//            listFiles = Arrays.copyOf(listFiles1, listFiles1.length + listFiles2.length);
//            System.arraycopy(listFiles2, 0, listFiles, listFiles1.length, listFiles2.length);
//        }
//        else if(listFiles1==null && listFiles2!=null)
//        {
//            listFiles = listFiles2;
//        }
//        else if(listFiles2==null && listFiles1!=null)
//        {
//            listFiles=listFiles1;
//        }
//               //converting list to array
//        if (listFiles != null && listFiles.length >= 1) {
//            Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
//        }
//        if (listFiles != null) {
//            for (File file : listFiles) {
//                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png")) {
//                    ImageModel model=new ImageModel(file.getAbsolutePath());
//                    arrayList.add(model);
//                }
//            }
//        }
//    }
    public void deleteRows() {
        SparseBooleanArray selected = waImageAdapter
                .getSelectedIds();//Get selected ids

        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                //If current id is selected remove the item via key
                arrayList.remove(selected.keyAt(i));
                waImageAdapter.notifyDataSetChanged();//notify adapter

            }
        }
        Toast.makeText(activity, selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        mActionMode.finish();//Finish action mode after use

    }
    public void refresh() {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
//        WhatsappImageAdapter.notifyDataSetChanged();
        waImageAdapter.updateData(new ArrayList<ImageModel>());
        populateRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null)  {
            if(resultCode == -1)
            {
                refresh();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                refresh();
            }
        }
    }

    public InterstitialAd getmInterstitialAd() {
        return mInterstitialAd;
    }


    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED) || WRITE_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }



    @TargetApi(23)
    public boolean requestPermission() {
        String[] permissions = {

                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        requestPermissions(permissions, STORAGE_PERMISSION_CODE);
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
            getStatus();
        }
    }
}