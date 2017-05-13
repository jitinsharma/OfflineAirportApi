package io.github.jitinsharma.offlineairportapiproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.jitinsharma.offlineairportapi.OfflineAirportApi;
import io.github.jitinsharma.offlineairportapi.model.AirportRequest;
import io.github.jitinsharma.offlineairportapi.model.AirportResponse;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    EditText latitudeInput;
    EditText longitudeInput;
    TextView airportName;
    Button getSingleAirport;
    Button getMultipleAirports;
    ProgressBar progressBar;
    OfflineAirportApi offlineAirportApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        latitudeInput.setText("28.632907");
        longitudeInput.setText("77.219536");
        offlineAirportApi = new OfflineAirportApi(this);
        getSingleAirport.setOnClickListener(this);
        getMultipleAirports.setOnClickListener(this);
    }

    private void bindViews() {
        latitudeInput = (EditText)findViewById(R.id.latitudeInput);
        longitudeInput = (EditText)findViewById(R.id.longitudeInput);
        airportName = (TextView)findViewById(R.id.airportName);
        getSingleAirport = (Button)findViewById(R.id.getSingleAirport);
        getMultipleAirports = (Button)findViewById(R.id.getMultipleAirports);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setLatitude(latitudeInput.getText().toString());
        airportRequest.setLongitude(longitudeInput.getText().toString());
        switch (v.getId()) {
            case R.id.getSingleAirport:
                progressBar.setVisibility(View.VISIBLE);
                singleAirportObservable(airportRequest);
                break;
            case R.id.getMultipleAirports:
                progressBar.setVisibility(View.VISIBLE);
                offlineAirportApi.setDistanceLimit(50000);
                multiAirportObservable(airportRequest);
                break;
        }
    }

    private void singleAirportObservable(final AirportRequest airportRequest) {
        Observable<AirportResponse> observable = Observable.create(new ObservableOnSubscribe<AirportResponse>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<AirportResponse> e) throws Exception {
                e.onNext(offlineAirportApi.getNearestAirport(airportRequest));
                e.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<AirportResponse>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onNext(@NonNull AirportResponse airportResponse) {
                    airportName.setText(airportResponse.getAirportName());
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }

                @Override
                public void onComplete() {
                    progressBar.setVisibility(View.GONE);
                }
            });
    }

    private void multiAirportObservable(final AirportRequest airportRequest) {
        Observable<ArrayList<AirportResponse>> listObservable = Observable.create(
            new ObservableOnSubscribe<ArrayList<AirportResponse>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<ArrayList<AirportResponse>> e) throws Exception {
                    e.onNext(offlineAirportApi.getNearestAirports(airportRequest));
                    e.onComplete();
                }
            });
        listObservable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ArrayList<AirportResponse>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onNext(@NonNull ArrayList<AirportResponse> airportResponses) {
                    airportName.setText("");
                    for (AirportResponse response : airportResponses) {
                        airportName.append(response.getAirportName() + ", ");
                        Log.d(TAG, response.getAirportName());
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }

                @Override
                public void onComplete() {
                    progressBar.setVisibility(View.GONE);
                }
            });
    }
}
