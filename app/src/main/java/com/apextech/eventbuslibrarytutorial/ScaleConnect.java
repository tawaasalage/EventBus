package com.apextech.eventbuslibrarytutorial;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.RxBleScanResult;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rx.Observer;
import rx.Subscription;

public class ScaleConnect extends AppCompatActivity {

    /*private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;

    public boolean connectedStatus=false;
    Handler h=new Handler();

    UUID uniuuid;

    private ScanCallback mScanCallback;*/

    RxBleClient rxBleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_connect);

        rxBleClient = RxBleClient.create(ScaleConnect.this);

        Subscription scanSubscription = rxBleClient.scanBleDevices()
                .subscribe(new Observer<RxBleScanResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxBleScanResult rxBleScanResult) {

                        RxBleDevice rxBleDevice=rxBleScanResult.getBleDevice();

                        if(rxBleDevice.getName().contentEquals("EVERIGHT")){}
                        else
                        {
                            return;
                        }

                        if(rxBleDevice.getConnectionState()== RxBleConnection.RxBleConnectionState.DISCONNECTED)
                        {
                            rxBleDevice.establishConnection(ScaleConnect.this, false)
                                    .subscribe(new Observer<RxBleConnection>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.d("ScannedDevices",e.toString());
                                        }

                                        @Override
                                        public void onNext(RxBleConnection rxBleConnection) {



                                            rxBleConnection.discoverServices()
                                                    .subscribe(new Observer<RxBleDeviceServices>() {
                                                        @Override
                                                        public void onCompleted() {

                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {

                                                            Log.d("ScannedDevices",e.toString());
                                                        }

                                                        @Override
                                                        public void onNext(RxBleDeviceServices rxBleDeviceServices) {

                                                            List<BluetoothGattService> ser=rxBleDeviceServices.getBluetoothGattServices();
                                                            for(BluetoothGattService s:ser)
                                                            {
                                                                //Log.d("ScannedDevices",s.toString());

                                                                List<BluetoothGattCharacteristic> chr=s.getCharacteristics();
                                                                for(BluetoothGattCharacteristic c:chr)
                                                                {
                                                                    //Log.d("ScannedDevices",c.toString());
                                                                }

                                                            }

                                                            List<BluetoothGattCharacteristic> chr=ser.get(2).getCharacteristics();

                                                            BluetoothGattCharacteristic c=chr.get(0);

                                                            Log.d("ScannedDevices",byte2HexStr(c.getValue()));

                                                           /* final byte[] data = chr.getValue();

                                                            UUID uniuuid=chr.getUuid();


                                                            final StringBuilder stringBuilder = new StringBuilder(data.length);
                                                            for(byte byteChar : chr.getValue())
                                                                stringBuilder.append(String.format("%02X ", byteChar));


                                                            Log.d("ScannedDevices",stringBuilder.toString());*/

                                                        }
                                                    });

                                        }
                                    });

                            //Log.d("ScannedDevices",rxBleDevice.getName()+" "+rxBleDevice.getConnectionState());
                        }
                        else
                        {
                            return;
                        }


                    }
                });



        /*mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();

        }
        final BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (Build.VERSION.SDK_INT >= 21) {
            startScanInitAPI21();
        }

        h.postDelayed(runnable,2000);

        disconnect();
        startScan();*/
    }

    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (byte b2 : b) {
            String str;
            stmp = Integer.toHexString(b2 & 255);
            if (stmp.length() == 1) {
                str = "0" + stmp;
            } else {
                str = stmp;
            }
            sb.append(str);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

/*

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            h.removeCallbacks(runnable);

            if(connectedStatus)
            {
                mGatt.discoverServices();

                h.postDelayed(runnable,1000);
            }
            else
            {

                h.postDelayed(runnable,2000);
            }

        }
    };


    public void startScan()
    {

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {

        } else
        {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }
            scanLeDevice(true);
        }
    }

    public void holdScan()
    {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    public void disconnect()
    {
        if(mGatt==null)
        {

        }
        else
        {
            mGatt.close();
            mGatt = null;
        }

    }


    @TargetApi(21)
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);

            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }


    @TargetApi(21)
    public void startScanInitAPI21()
    {
        mScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                Log.i("callbackType", String.valueOf(callbackType));
                Log.i("result", result.toString());
                BluetoothDevice btDevice = result.getDevice();
                if (btDevice.getName().contentEquals("OnyxBeacon")) {

                } else if (btDevice.getName().contentEquals("YunChen")) {
                    String uuid=getGuidFromByteArray(result.getScanRecord().getBytes());
                    List<ParcelUuid> p= result.getScanRecord().getServiceUuids();
                    uniuuid=p.get(0).getUuid();

                    Log.i("onCharacteristicRead", uuid +" "+p.get(0).getUuid().toString());
                    connectToDevice(btDevice);

                }
                else if (btDevice.getName().contentEquals("EVERIGHT"))
                {
                    String uuid=getGuidFromByteArray(result.getScanRecord().getBytes());
                    List<ParcelUuid> p= result.getScanRecord().getServiceUuids();
                    uniuuid=p.get(0).getUuid();

                    Log.i("onCharacteristicRead", uuid +" "+p.get(0).getUuid().toString());
                    connectToDevice(btDevice);
                }

            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                for (ScanResult sr : results) {
                    Log.i("ScanResult - Results", sr.toString());


                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e("Scan Failed", "Error Code: " + errorCode);
            }
        };

    }



    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                            connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            mGatt = device.connectGatt(this, false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTING:
                    Log.i("gattCallback", "STATE_CONNECTING");


                    gatt.discoverServices();

                    break;
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    connectedStatus=true;


                    gatt.discoverServices();

                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    Log.i("gattCallback", "STATE_DISCONNECTING");
                    connectedStatus=false;



                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    connectedStatus=false;



                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();

            for(BluetoothGattService ser:services)
            {
                Log.i("onCharacteristicRead", ser.toString());
            }

            //gatt.readCharacteristic(services.get(2).getCharacteristics().get(0));
        }


        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            final byte[] data = characteristic.getValue();

            uniuuid=characteristic.getUuid();

            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String [] arr=stringBuilder.toString().split(" ");

                        Long v=hexToLong(arr[2]+arr[3]);
                        float val=Float.parseFloat(v+"")/100;

                    }
                });

                Log.d("onCharacteristicRead", new String(data) + "\n" + stringBuilder.toString());
            }
            //gatt.disconnect();
        }
    };

    public static long hexToLong(String hex) {
        return Long.parseLong(hex, 16);
    }


    public  String getGuidFromByteArray(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        UUID uuid = new UUID(high, low);
        return uuid.toString();
    }


    public String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(*/
/*YOUR_CHARSET?*//*
)));
    }
*/

}
