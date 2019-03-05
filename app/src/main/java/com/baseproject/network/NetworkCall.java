package com.baseproject.network;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.baseproject.R;
import com.baseproject.network.listeners.DefaultActionPerformer;
import com.baseproject.network.listeners.NoInternetListner;
import com.baseproject.network.listeners.RetrofitResponseListener;
import com.baseproject.utils.Logger;
import com.baseproject.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@SuppressWarnings("unused")
public class NetworkCall implements Callback<ResponseBody> {

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    @SuppressWarnings("WeakerAccess")
    public static final int NO_INTERNET_PROMPT_TOAST = 0;
    @SuppressWarnings("WeakerAccess")
    public static final int NO_INTERNET_PROMPT_SNACKBAR = 1;
    @SuppressWarnings("WeakerAccess")
    public static final int NO_INTERNET_PROMPT_ALERT = 2;
    @SuppressWarnings("WeakerAccess")
    public static final int REQUEST_TYPE_GET = 1;
    @SuppressWarnings("WeakerAccess")
    public static final int REQUEST_TYPE_POST = 0;



    private static String BASE_URL = "";

    private  int REQUEST_TYPE = REQUEST_TYPE_POST;




    private String endPoint = "";
    private Context mContext = null;


    private boolean shouldPromptOnNoInternet = true;
    private int noInternetPromptType = NO_INTERNET_PROMPT_TOAST;
    private View snackbarView = null;
    private NoInternetListner noInternetListner = null;

    private RetrofitResponseListener retrofitResponseListener = null;

    private Object requestObject;
    private HashMap<String, String> requestParams = new HashMap<>();
    private HashMap<String, File> requestFiles;
    private HashMap<String, String> headers = new HashMap<>();

    private static DefaultActionPerformer actionPerformer;

    private Call<ResponseBody> call;

    private StringBuilder printBuilder = new StringBuilder("\n API EndPoint : ");
    private static boolean isDubuggable = true;


    public static NetworkCall with(Context context) {
        return new NetworkCall(context);
    }

    private NetworkCall(Context context) {
        this.mContext = context;
    }


    private boolean isMultipartCall = false;


    public NetworkCall setEndPoint(String endPoint) {
        this.endPoint = endPoint;
        return this;
    }

    public NetworkCall setNoInternetPromptType(int noInternetPromptType) {
        this.noInternetPromptType = noInternetPromptType;
        return this;
    }

    public NetworkCall setNoInternetPromptType(int noInternetPromptType, View snackBarView) {
        this.noInternetPromptType = noInternetPromptType;
        this.snackbarView = snackBarView;
        return this;
    }

    public NetworkCall shouldPromptOnNoInternet(boolean shouldPromptOnNoInternet) {
        this.shouldPromptOnNoInternet = shouldPromptOnNoInternet;
        return this;
    }

    public NetworkCall setNoInternetListner(NoInternetListner noInternetListner) {
        this.noInternetListner = noInternetListner;
        return this;
    }

    public NetworkCall setResponseListener(RetrofitResponseListener retrofitRxResponseListener) {
        this.retrofitResponseListener = retrofitRxResponseListener;
        return this;
    }


    public NetworkCall setRequestObject(Object requestObject) {
        this.requestObject = requestObject;
        return this;
    }

    public NetworkCall setRequestParams(HashMap<String, String> params) {
        this.requestParams = params;
        return this;
    }

    public NetworkCall setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public static void setActionPerformer(DefaultActionPerformer actionPerformer) {
        NetworkCall.actionPerformer = actionPerformer;
    }

    public NetworkCall setFiles(HashMap<String, File> fileParams) {
        this.requestFiles = fileParams;
        this.isMultipartCall = true;
        return this;
    }

    public void setMultipartCall(boolean multipartCall) {
        isMultipartCall = multipartCall;
    }

    private void showNoInternetAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.app_name));
        builder.setCancelable(true);

        builder.setMessage(mContext.getString(R.string.str_no_internet));
        builder.setPositiveButton(mContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    /* Internet Handeling*/
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivity != null;
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.isConnected();
    }

    public static void setIsDubuggable(boolean isDubuggable) {
        NetworkCall.isDubuggable = isDubuggable;
    }


    public void makeEmptyRequestCall() {
        requestParams = new HashMap<>();
        makeCall();
    }

    public NetworkCall makeCall() {

        if (requestObject == null && requestParams == null) {
            Logger.Error("No Request Source is Provided");
        } else {
            if (isConnectedToInternet()) {

                Logger.Error("API EndPoint => " + endPoint);
                printBuilder.append(endPoint).append("\n\n").append("Headers\n");

                if (actionPerformer != null) {
                    actionPerformer.onActionPerform(headers, requestParams);
                }

                if (headers.size() > 0) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        Logger.Error(entry.getKey() + "=>" + entry.getValue());
                        printBuilder.append(entry.getKey()).append("=>").append(entry.getValue()).append("\n");
                    }
                } else {
                    Logger.Error("headers are empty");
                    printBuilder.append("Headers are Empty");
                }


                if (retrofitResponseListener != null) {
                    retrofitResponseListener.onPreExecute();
                }


                if (requestObject != null) {
                    makeRequestWithObject(requestObject);
                } else {
                    makeRequestWithParams(requestParams);
                }


            } else {
                if (shouldPromptOnNoInternet) {
                    switch (noInternetPromptType) {
                        case NO_INTERNET_PROMPT_TOAST:
                            Toast.makeText(mContext, mContext.getText(R.string.str_no_internet), Toast.LENGTH_SHORT).show();
                            break;
                        case NO_INTERNET_PROMPT_SNACKBAR:
                            if (snackbarView != null) {
                                Snackbar.make(snackbarView, mContext.getText(R.string.str_no_internet), Snackbar.LENGTH_SHORT).show();
                            }
                            break;
                        case NO_INTERNET_PROMPT_ALERT:
                            showNoInternetAlert();
                            break;
                    }
                }
                if (noInternetListner != null) {
                    noInternetListner.onNoInternet();
                }
            }
        }

        return this;
    }

    public void cancelRequest() {
        call.cancel();
    }

    private void makeRequestWithObject(Object requestClass) {

        printBuilder.append("\n\n").append("Request Object\n");
        printBuilder.append(requestClass.toString()).append("\n\n");
        Logger.verbose(requestClass.toString());

        call = getInstance().APICall(endPoint, headers, requestClass);
        call.enqueue(this);
    }


    private void makeRequestWithParams(HashMap<String, String> requestParams) {

        printBuilder.append("\n\n").append("Request Params\n");

        if (isMultipartCall) {
            HashMap<String, RequestBody> bodyParams = new HashMap<>();

            if (requestParams.size() > 0) {
                for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                    Logger.Error(entry.getKey() + "=>" + entry.getValue());
                    printBuilder.append(entry.getKey()).append("=>").append(entry.getValue()).append("\n");
                    bodyParams.put(entry.getKey(), createPartFromString(entry.getValue()));
                }
            } else {
                Logger.Error("Param are empty");
                printBuilder.append(" Params are empty ");
            }


            if (requestFiles != null && requestFiles.size() > 0) {
                printBuilder.append("\n\n").append("Files to Upload\n");
                for (Map.Entry<String, File> entry : requestFiles.entrySet()) {
                    Logger.Error(entry.getKey() + "=>" + entry.getValue().getPath());
                    printBuilder.append(entry.getKey()).append("=>").append(entry.getValue().getPath()).append("\n");
                    String fileName = entry.getKey() + "\"; filename=\"" + entry.getValue().getName();
                    bodyParams.put(fileName, createPartFromFile(entry.getValue()));
                }
            }

            call = getInstance().APIMultipartCall(endPoint, headers, bodyParams);
            call.enqueue(this);
        } else {

            if (requestParams.size() > 0) {
                for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                    Logger.Error(entry.getKey() + "=>" + entry.getValue());
                    printBuilder.append(entry.getKey()).append("=>").append(entry.getValue()).append("\n");
                }
            } else {
                Logger.Error("Param are empty");
                printBuilder.append(" Params are empty ");
            }
            switch (REQUEST_TYPE) {
                case REQUEST_TYPE_GET:
                    call = getInstance().APICall(endPoint, headers);
                    break;
                default:
                    call = getInstance().APICall(endPoint, headers, requestParams);
                    break;
            }

            call.enqueue(this);
        }

    }


    private RequestBody createPartFromFile(File file) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        handleResponse(response);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        handleError(t);

        printBuilder.append("\n\nResponse\n");
        printBuilder.append("Call to the API Failed");
        printBuilder.append("\n\nThank you\n\n");
        copyToClipBoard();
    }


    private void handleResponse(Response<ResponseBody> response) {

        printBuilder.append("\n\nStatusCode : ").append(String.valueOf(response.code())).append("");

        try {
            if (response.body() != null) {

                String body = response.body().string();
                Logger.Error("Success Response : " + body);

                printBuilder.append("\n\nResponse\n");
                printBuilder.append(body);
                printBuilder.append("\n\n Thank you\n\n");

                copyToClipBoard();

                JSONObject jsonObject = new JSONObject(body);


                if (jsonObject.optBoolean("success") || jsonObject.optString("status").equalsIgnoreCase("200")) {
                    if (retrofitResponseListener != null) {
                        retrofitResponseListener.onSuccess(response.code(), jsonObject, body);
                    }
                } else {
                    String errorMessage = Utils.getMessageFromResponseObject(mContext,jsonObject);
                    if (retrofitResponseListener != null) {
                        retrofitResponseListener.onError(response.code(), errorMessage);
                    }
                }
            } else {
                String body = response.errorBody().string();
                Logger.Error("Error Body =>" + body);

                printBuilder.append("\n\nResponse\n");
                printBuilder.append(body);
                printBuilder.append("\n\nThank you\n\n");

                copyToClipBoard();

                String message = mContext.getString(R.string.str_something_went_wrong);

                // This code just to handle error response which is not there in our application still it is there for future pupose
                try {
                    JSONObject object = new JSONObject(body);
                    message = Utils.getMessageFromResponseObject(mContext,object);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (retrofitResponseListener != null) {
                        retrofitResponseListener.onError(response.code(), message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (retrofitResponseListener != null) {
                retrofitResponseListener.onError(response.code(), mContext.getString(R.string.str_something_went_wrong));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (retrofitResponseListener != null) {
                retrofitResponseListener.onError(response.code(), mContext.getString(R.string.str_something_went_wrong));
            }
        }
    }


    private void handleError(Throwable e) {
        e.printStackTrace();

        String errorMessage;
        if (e.getMessage() != null && e.getMessage().length() > 0) {
            errorMessage = e.getMessage();
        } else {
            errorMessage = mContext.getString(R.string.str_something_went_wrong);
        }

        if (retrofitResponseListener != null) {
            retrofitResponseListener.onError(500, errorMessage);
        }
    }

    private void copyToClipBoard() {
        if (isDubuggable) {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("API Data", printBuilder.toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
        }
    }


    // Retrofit API Interface Instance Handling
    private static ApiInterface apiInterface = null;

    private static ApiInterface getInstance() {

        if (apiInterface == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiInterface = retrofit.create(ApiInterface.class);

        }

        return apiInterface;
    }

    public static void setBASE_URL(String BASE_URL) {
        NetworkCall.BASE_URL = BASE_URL;
    }


    private RequestBody createPartFromString(String value) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), value);
    }

    public NetworkCall setRequestType(int requestType) {
        REQUEST_TYPE = requestType;
        return this;
    }
}



