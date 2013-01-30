package com.github.gyk001.android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class BatteryStateWidget extends TextView {
	private static final String TAG = BatteryStateWidget.class.getName();
	public BatteryStateWidget(Context context) {
		super(context);
		Log.d(TAG,"BatteryStateWidget");
	}

	public BatteryStateWidget(Context context, AttributeSet attrs){
		super(context,attrs);

		Log.d(TAG,"BatteryStateWidget");
		this.setText("yyy");
	}
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		this.getContext() .registerReceiver(mBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();

		this.getContext().unregisterReceiver(mBatteryInfoReceiver);
	}
	
	

	
	private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(BatteryStateWidget.this!=null){
				String action = intent.getAction();
				if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
					
					int level = intent.getIntExtra("level", 0);
					int scale = intent.getIntExtra("scale", 100);
					int batteryStatus = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
					String status;
					switch(batteryStatus){
					case BatteryManager.BATTERY_STATUS_CHARGING :
						status = "正在充电:";
						break;
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						status="剩余电量:";
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						status="电量满：";
						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						status="正在放电：";
						break;
					default:
							status = "";
					}
					BatteryStateWidget.this.setText(status + String.valueOf(level * 100 / scale) + "%");
				}
			}
		}
	};


}
