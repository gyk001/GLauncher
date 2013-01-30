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

	/* ���ǿ���������onResume��onPause����ֹͣlistene */
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

	/* ��ʼPhoneState���� */
	private class MyPhoneStateListener extends PhoneStateListener {
		/* �ӵõ����ź�ǿ��,ÿ��tiome��Ӧ���и��� */

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
				str += "-��ȫû���źţ�";
			} else if (dBm > -50) {
				str += "-�źż��ã�@@@@@";
			} else if (dBm > -92) {
				str += "-�źŲ���!";
				int star = 5 - (-50 - dBm) / 10;
				while (star > 0) {
					str += "@";
					star--;
				}
			} else {
				str += "-���ź�";
			}
			/*
			 * 
			 * �������������ν��dBm����ֵ��Χ��-XX~0֮�䡣�����Խ���ź�ǿ��Խ�ߡ�����
			 * -50dBm~0dBm��Χ�ڣ���ϲ�㣬����ź��Ѿ��õú��ˡ���˵���վ�ڻ�վ�Ա��ǰɣ���������
			 * -90dBm~-60dBm��ͬ����ϲ�㣬������������ٴ��˵绰�����⡣������˵ģ�����Ӫ�̰ɣ��������ǵ�����
			 */
			Log.d(TAG, "ˢ���ź���ʾ..");
			SignalStengthWidget.this.setText(str);
			Toast.makeText(SignalStengthWidget.this.getContext(), str,
					Toast.LENGTH_SHORT).show();

		}
	};
}
