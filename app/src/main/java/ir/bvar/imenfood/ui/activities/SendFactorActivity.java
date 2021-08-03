package ir.bvar.imenfood.ui.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.bvar.imenfood.App;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.api.FactorApi;
import ir.bvar.imenfood.api.request.GetProductRequest;
import ir.bvar.imenfood.api.request.SendFactorRequest;
import ir.bvar.imenfood.api.response.GetProductsResponse;
import ir.bvar.imenfood.api.response.SendFactorResponse;
import ir.bvar.imenfood.api.response.UploadFactorImageResponse;
import ir.bvar.imenfood.constants.RequestCodes;
import ir.bvar.imenfood.interfaces.SendFactorRequestDialogListener;
import ir.bvar.imenfood.managers.BasePermissionManager;
import ir.bvar.imenfood.managers.PermissionManager;
import ir.bvar.imenfood.models.FactorProduct;
import ir.bvar.imenfood.models.Product;
import ir.bvar.imenfood.models.Provider;
import ir.bvar.imenfood.network.CompressStreamerBitmapObservable;
import ir.bvar.imenfood.network.UploadRequestBody;
import ir.bvar.imenfood.ui.adapters.FactorProductListAdapter;
import ir.bvar.imenfood.ui.dialogs.ConfirmDialog;
import ir.bvar.imenfood.ui.dialogs.MediaTypePickerDialog;
import ir.bvar.imenfood.ui.dialogs.ProductsDialog;
import ir.bvar.imenfood.ui.dialogs.ProvidersDialog;
import ir.bvar.imenfood.utils.BitmapUtility;
import okhttp3.MultipartBody;

/**
 * Created by rezapilehvar on 21/12/2017 AD.
 */

public class SendFactorActivity extends BaseActivity implements SendFactorRequestDialogListener,
        ProvidersDialog.ProviderDialogSelectItemListener,
        ProductsDialog.ProductDialogSelectItemListener,
        ConfirmDialog.ConfirmDialogResponseListener {

    private SendFactorRequest sendFactorRequest = new SendFactorRequest();

    @BindView(R.id.activity_sendfactor_loadingLayout)
    RelativeLayout loadingLayout;

    @BindView(R.id.activity_sendfactor_loadingLayout_progressBar)
    ProgressBar normalProgressbar;

    @BindView(R.id.activity_sendfactor_loadingLayout_uploadProgressBar)
    ProgressBar uploadProgressbar;

    @BindView(R.id.activity_sendfactor_loadingLayout_descTextView)
    AppCompatTextView loadingDescTextView;

    @BindView(R.id.activity_sendfactor_sendButton)
    CardView sendButton;

    @BindView(R.id.activity_sendfactor_uploadFactorImageButton)
    RelativeLayout uploadFactorButton;
    MediaTypePickerDialog mediaTypePickerDialog;

    @BindView(R.id.activity_sendfactor_uploadFactorPreviewImageView)
    AppCompatImageView uploadFactorPreviewImageView;

    @BindView(R.id.activity_sendfactor_mainScrollView)
    NestedScrollView mainScrollView;

    @BindView(R.id.activity_sendfactor_backButton)
    AppCompatImageView backButton;

    @BindView(R.id.activity_sendfactor_addProductButton)
    CardView addProductButton;

    @BindView(R.id.activity_sendfactor_productsRecyclerView)
    RecyclerView productsRecyclerView;
    FactorProductListAdapter factorProductListAdapter;
    List<FactorProduct> factorProductList = new ArrayList<>();

    private ProductsDialog productsDialog;
    private ProvidersDialog providersDialog;
    private ConfirmDialog stayOrDoneDialog;

    private List<Provider> providerList = new ArrayList<>();

    private Disposable getProvidersDisposable;
    private Disposable getProductsDisposable;
    private Disposable uploadFactorImageDisposable;
    private Disposable sendFactorDisposable;
    private Disposable createUploadPartDisposable;

    private Bitmap loadedBitmap;

    private PermissionManager permissionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendfactor);
        ButterKnife.bind(this);
        permissionManager = new PermissionManager(this);
        initViews();
    }

    private void initViews() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (mediaTypePickerDialog == null) {
            mediaTypePickerDialog = new MediaTypePickerDialog(this);
        }
        uploadFactorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaTypePickerDialog.show(SendFactorActivity.this, permissionManager);
            }
        });

        if (providersDialog == null) {
            providersDialog = new ProvidersDialog(this, providerList);
            providersDialog.setCancelable(true);
        }

        if (productsDialog == null) {
            productsDialog = new ProductsDialog(this);
            productsDialog.setCancelable(true);
        }

        if (stayOrDoneDialog == null) {
            stayOrDoneDialog = new ConfirmDialog(this);
        }

        factorProductList.add(new FactorProduct());

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                factorProductList.add(new FactorProduct());

                if (factorProductList.size() > 1) {
                    factorProductListAdapter.notifyItemInserted(factorProductList.size() - 1);
                } else {
                    factorProductListAdapter.notifyItemInserted(1);
                }

                mainScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mainScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendFactorRequest.getImagePath() != null && !sendFactorRequest.getImagePath().isEmpty()) {

                    for (FactorProduct factorProduct : factorProductList) {
                        if (factorProduct.getSelectedProduct() != null) {
                            sendFactorRequest.addProduct(factorProduct.getSelectedProduct(), factorProduct.getSelectedProvider());
                        }

                        if (sendFactorRequest.getProductProviderList().size() > 0) {
                            sendFactorRequest(sendFactorRequest);
                        } else {
                            Toast.makeText(SendFactorActivity.this, "لیست محصولات نمیتواند خالی باشد", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(SendFactorActivity.this, "لطفا عکس فاکتور را اپلود کنید.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (factorProductListAdapter == null) {
            factorProductListAdapter = new FactorProductListAdapter(this, factorProductList, this);
            factorProductListAdapter.setHasStableIds(true);
        }

        if (productsRecyclerView.getAdapter() == null) {
            productsRecyclerView.setAdapter(factorProductListAdapter);
            productsRecyclerView.setNestedScrollingEnabled(false);
            productsRecyclerView.setItemViewCacheSize(100);
        }

        if (productsRecyclerView.getLayoutManager() == null) {
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }


        updateData();
    }

    @Override
    public void updateData() {
        super.updateData();
        getProviders();
    }

    private void getProviders() {
        getProvidersDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(FactorApi.class)
                .getProviders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Provider>>() {

                    @Override
                    public void onNext(List<Provider> providerList) {
                        SendFactorActivity.this.providerList.clear();
                        SendFactorActivity.this.providerList.addAll(providerList);
                        providersDialog.notifyAdapterDataChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SendFactorActivity.this, "Error while getting providers , please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getProducts(final FactorProduct requestedFactorProduct) {
        if (requestedFactorProduct.getSelectedProvider() != null) {

            if (getProductsDisposable != null && !getProductsDisposable.isDisposed()) {
                getProductsDisposable.dispose();
            }

            getProductsDisposable = App.getInstance()
                    .getRetrofitInstance()
                    .create(FactorApi.class)
                    .getProducts(new GetProductRequest(requestedFactorProduct.getSelectedProvider().getID()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<GetProductsResponse>() {
                        @Override
                        public void onNext(GetProductsResponse productsResponse) {
                            if (productsResponse != null && productsResponse.getProductList() != null) {
                                requestedFactorProduct.setLoadedProductList(productsResponse.getProductList());

                                int indexOfFactorProduct = factorProductList.indexOf(requestedFactorProduct);
                                factorProductListAdapter.notifyItemChanged(indexOfFactorProduct);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Toast.makeText(SendFactorActivity.this, "Error while getting products , please try again", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void onProviderDialogSelectedItemListener(Provider provider, FactorProduct requestedFactorProduct) {
        requestedFactorProduct.setSelectedProvider(provider);
        requestedFactorProduct.getLoadedProductList().clear();
        requestedFactorProduct.setSelectedProduct(null);
        providersDialog.dismiss();

        int indexOfFactorProduct = factorProductList.indexOf(requestedFactorProduct);
        factorProductListAdapter.notifyItemChanged(indexOfFactorProduct);

        getProducts(requestedFactorProduct);
    }

    @Override
    public void onProductDialogSelectedItemListener(Product product, FactorProduct factorProduct) {
        factorProduct.setSelectedProduct(product);
        productsDialog.dismiss();

        int indexOfFactorProduct = factorProductList.indexOf(factorProduct);
        factorProductListAdapter.notifyItemChanged(indexOfFactorProduct);
    }

    @Override
    public void requestProvider(FactorProduct factorProduct, int position) {
        providersDialog.show(factorProduct, this);
    }

    @Override
    public void requestProduct(FactorProduct factorProduct, int position) {
        productsDialog.show(factorProduct, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mediaTypePickerDialog.show(this, permissionManager);
                }
                break;
            case BasePermissionManager.PERMISSIONS_REQUEST_READ_EX_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mediaTypePickerDialog.show(this, permissionManager);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodes.PICK_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (mediaTypePickerDialog != null) {
                        Uri selectedImage = mediaTypePickerDialog.getCameraOutPutUri();
                        ContentResolver cr = getContentResolver();

                        Bitmap bitmap;

                        try {

                            if (loadedBitmap != null && !loadedBitmap.isRecycled()) {
                                loadedBitmap.recycle();
                                uploadFactorPreviewImageView.setImageBitmap(null);
                            }

                            bitmap = android.provider.MediaStore.Images.Media
                                    .getBitmap(cr, selectedImage);

                            uploadFactorImage(bitmap);

                        } catch (Exception e) {
                            if (e instanceof IOException) {

                                // get picture from file uri
                                try {
                                    bitmap = BitmapUtility.uriToBitmap(SendFactorActivity.this, mediaTypePickerDialog.getCameraOutPutUri());

                                    if (bitmap != null) {
                                        uploadFactorImage(bitmap);
                                    } else {
                                        Toast.makeText(this, "خطا هنگام دریافت عکس از دوربین", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                } catch (Exception createBitmapFromURIException){
                                    Toast.makeText(this, "خطا هنگام دریافت عکس از دوربین", Toast.LENGTH_SHORT)
                                            .show();
                                    createBitmapFromURIException.printStackTrace();
                                }


                            } else {
                                Toast.makeText(this, "خطا هنگام دریافت عکس از دوربین", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            e.printStackTrace();
                        }
                    }
                }
                break;
            case RequestCodes.PICK_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    //pick image from gallery
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    assert selectedImage != null;
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    bitmap = BitmapFactory.decodeFile(imgDecodableString);

                    if (bitmap != null) {
//                        showImagePreview(bitmap);
                    }

                    uploadFactorImage(bitmap);
                }
                break;
        }
    }

    private void showImagePreview(Bitmap bitmap) {
        uploadFactorPreviewImageView.setImageBitmap(bitmap);
    }

    private void uploadFactorImage(final Bitmap bitmap) {
        if (uploadFactorImageDisposable != null && !uploadFactorImageDisposable.isDisposed()) {
            uploadFactorImageDisposable.dispose();
        }

        Observable<MultipartBody.Part> compressStreamerBitmapObservable = Observable.create(
                new CompressStreamerBitmapObservable(uploadFactorPreviewImageView, bitmap,
                        new UploadRequestBody.UploadRequestStreamerListener() {
                            @Override
                            public void onProgressChanged(int progress) {
                                uploadProgressbar.setProgress(progress);
                            }
                        }));

        if (createUploadPartDisposable != null && !createUploadPartDisposable.isDisposed()) {
            createUploadPartDisposable.dispose();
        }

        createUploadPartDisposable = compressStreamerBitmapObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MultipartBody.Part>() {
                    @Override
                    public void onNext(MultipartBody.Part part) {
                        if (uploadFactorImageDisposable != null && !uploadFactorImageDisposable.isDisposed()) {
                            uploadFactorImageDisposable.dispose();
                        }

                        uploadFactorImageDisposable = App.getInstance()
                                .getRetrofitInstance()
                                .create(FactorApi.class)
                                .uploadFactorImage(part)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<UploadFactorImageResponse>() {

                                    @Override
                                    protected void onStart() {
                                        super.onStart();
                                        loadingLayout.setVisibility(View.VISIBLE);
                                        normalProgressbar.setVisibility(View.GONE);
                                        uploadProgressbar.setVisibility(View.VISIBLE);

                                        loadingDescTextView.setText("در حال آپلود تصویر فاکتور");
                                    }

                                    @Override
                                    public void onNext(UploadFactorImageResponse uploadFactorImageResponse) {
                                        String imagePath = uploadFactorImageResponse.getImagePath();

                                        if (imagePath != null && !imagePath.isEmpty()) {
                                            int imagesWordIndex = imagePath.indexOf(": images/");

                                            if (imagesWordIndex != -1) {
                                                int startOfImagesPath = imagePath.indexOf("images/");
                                                imagePath = imagePath.substring(startOfImagesPath);
                                            }
                                        }


                                        sendFactorRequest.setImagePath(imagePath);
                                        loadingLayout.setVisibility(View.GONE);
                                        normalProgressbar.setVisibility(View.VISIBLE);
                                        uploadProgressbar.setVisibility(View.GONE);

                                        Toast.makeText(SendFactorActivity.this, "آپلود تصویر با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();

                                        if (bitmap != null && !bitmap.isRecycled()) {
                                            bitmap.recycle();
                                        }

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                        uploadFactorPreviewImageView.setImageBitmap(null);
                                        loadingLayout.setVisibility(View.GONE);
                                        normalProgressbar.setVisibility(View.VISIBLE);
                                        uploadProgressbar.setVisibility(View.GONE);

                                        if (bitmap != null && !bitmap.isRecycled()) {
                                            bitmap.recycle();
                                        }

                                        Toast.makeText(SendFactorActivity.this, "خطا هنگام اپلود تصویر ، لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        uploadFactorPreviewImageView.setImageBitmap(null);
                        loadingLayout.setVisibility(View.GONE);
                        normalProgressbar.setVisibility(View.VISIBLE);
                        uploadProgressbar.setVisibility(View.GONE);

                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                        }

                        Toast.makeText(SendFactorActivity.this, "خطا هنگام اپلود تصویر ، لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void sendFactorRequest(final SendFactorRequest sendFactorRequest) {
        if (sendFactorDisposable != null && !sendFactorDisposable.isDisposed()) {
            sendFactorDisposable.dispose();
        }

        sendFactorDisposable = App.getInstance()
                .getRetrofitInstance()
                .create(FactorApi.class)
                .sendFactor(sendFactorRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<SendFactorResponse>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        loadingLayout.setVisibility(View.VISIBLE);
                        normalProgressbar.setVisibility(View.VISIBLE);
                        uploadProgressbar.setVisibility(View.GONE);

                        loadingDescTextView.setText("در حال ارسال فاکتور");
                    }

                    @Override
                    public void onNext(SendFactorResponse sendFactorResponse) {
                        loadingLayout.setVisibility(View.GONE);
                        normalProgressbar.setVisibility(View.VISIBLE);
                        uploadProgressbar.setVisibility(View.GONE);

                        if (sendFactorResponse != null) {
                            stayOrDoneDialog.show(SendFactorActivity.this,
                                    "فاکتور با موفقیت ارسال شد ، آیا میخواهید فاکتور جدید اضافه کنید؟",
                                    "بله",
                                    "خیر،خروج");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loadingLayout.setVisibility(View.GONE);
                        normalProgressbar.setVisibility(View.VISIBLE);
                        uploadProgressbar.setVisibility(View.GONE);

                        Toast.makeText(SendFactorActivity.this, "خطا در ارسال فاکتور ، دوباره تلاش کنید", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getProvidersDisposable != null && !getProvidersDisposable.isDisposed()) {
            getProvidersDisposable.dispose();
        }

        if (getProductsDisposable != null && !getProductsDisposable.isDisposed()) {
            getProductsDisposable.dispose();
        }

        if (uploadFactorImageDisposable != null && !uploadFactorImageDisposable.isDisposed()) {
            uploadFactorImageDisposable.dispose();
        }

        if (sendFactorDisposable != null && !sendFactorDisposable.isDisposed()) {
            sendFactorDisposable.dispose();
        }

        if (createUploadPartDisposable != null && !createUploadPartDisposable.isDisposed()) {
            createUploadPartDisposable.dispose();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SendFactorActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onConfirmDialogPositiveClicked(ConfirmDialog confirmDialog) {
        factorProductList.clear();
        factorProductList.add(new FactorProduct());
        factorProductListAdapter.notifyDataSetChanged();
        uploadFactorPreviewImageView.setImageBitmap(null);
        sendFactorRequest = new SendFactorRequest();
    }

    @Override
    public void onConfirmDialogNegativeClicked(ConfirmDialog confirmDialog) {
        finish();
    }
}
