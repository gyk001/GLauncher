package com.github.gyk001.android.widget;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SignalStengthWidget extends TextView {
	private static final String TAG = SignalStengthWidget.class.getName();

	/* 我们可以用它们onResume和onPause方法停止listene */
	private TelephonyManager Tel;
	private MyPhoneStateListener MyListener;
	private int phoneType;

	public SignalStengthWidget(Context context) {
		super(context);
		Log.d(TAG, "SignalStengthWidget");
		this.setText("xxxx");
	}

	public void reloadWin() {
		Log.d(TAG, "reload");

		Tel.listen(MyListener, PhoneStateListener.LISTEN_NONE);
		Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	public SignalStengthWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		Log.d(TAG, "SignalStengthWidget");
		this.setText("yyy");
		if (!isInEditMode()) {
			MyListener = new MyPhoneStateListener();
			Tel = (TelephonyManager) this.getContext().getSystemService(
					Context.TELEPHONY_SERVICE);
			// Tel.listen(MyListener,
			// PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
			this.phoneType = Tel.getPhoneType();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		if(!isInEditMode()){
			Log.d(TAG, "attached:" + Tel.getSimOperatorName());
			Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		if(!isInEditMode()){
			Log.d(TAG, "detached");
			Tel.listen(MyListener, PhoneStateListener.LISTEN_NONE);
		}
	}

	/* 开始PhoneState听众 */
	private class MyPhoneStateListener extends PhoneStateListener {
		/* 从得到的信号强度,每个tiome供应商有更新 */

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			int asu = 0;
			int dBm = 0;
			String str;
			switch (SignalStengthWidget.this.phoneType) {
			case TelephonyManager.PHONE_TYPE_GSM:
				asu = signalStrength.getGsmSignalStrength();
				dBm = -113 + 2 * asu;
				str = "\nGSM";
				break;
			case TelephonyManager.PHONE_TYPE_CDMA:
				dBm = signalStrength.getCdmaDbm();
				asu = (dBm + 113) / 2;
				str = "\nCDMA";
				break;
			default:
				dBm = 100;
				asu = 1000;
				str = "\nNONE";
				break;
			}
			str += ":" + asu + ":" + dBm;
			;
			if (dBm > 0) {
				str += "-完全没有信号！";
			} else if (dBm > -50) {
				str += "-信号极好！@@@@@";
			} else if (dBm > -92) {
				str += "-信号不错!";
				int star = 5 - (-50 - dBm) / 10;
				while (star > 0) {
					str += "@";
					star--;
				}
			} else {
				str += "-无信号";
			}
			/*
			 * 
			 * 现在来看这个所谓的dBm，数值范围在-XX~0之间。这个数越大，信号强度越高。　　
			 * -50dBm~0dBm范围内，恭喜你，你的信号已经好得很了。话说你就站在基站旁边是吧，哈？　　
			 * -90dBm~-60dBm，同样恭喜你，你基本不会面临打不了电话的问题。如果打不了的，找运营商吧，那是他们的问题
			 */
			Log.d(TAG, "刷新信号显示..");
			SignalStengthWidget.this.setText(str);
			Toast.makeText(SignalStengthWidget.this.getContext(), str,
					Toast.LENGTH_SHORT).show();

		}
	};
}
