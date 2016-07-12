package cn.leeq.util.memodemo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.yoojia.zxing.qrcode.Encoder;

import cn.leeq.util.memodemo.R;

public class QRDemo extends AppCompatActivity {

    private EditText etContent;
    private Button btnPositive;
    private RadioButton rbBmp;
    private ImageView ivQr;
    private Encoder encoderBuild;
    private RadioButton rbScan;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdemo);
        init();

        btnPositive.setClickable(false);
        etContent.setVisibility(View.GONE);
        rbScan.setChecked(true);
        ivQr.setVisibility(View.GONE);
        tvResult.setVisibility(View.GONE);
    }

    private void init() {
        etContent = (EditText) findViewById(R.id.qr_et_content);
        btnPositive = (Button) findViewById(R.id.qr_btn_positive);
        rbBmp = (RadioButton) findViewById(R.id.qr_rb_makeqr_bmp);
        ivQr = (ImageView) findViewById(R.id.qr_iv_bmp);
        rbScan = (RadioButton) findViewById(R.id.qr_rb_scan);
        tvResult = (TextView) findViewById(R.id.qr_tv_result);
        encoderBuild = new Encoder.Builder()
                .setBackgroundColor(Color.WHITE)
                .setCodeColor(Color.BLACK)
                .setOutputBitmapHeight(500)
                .setOutputBitmapWidth(500)
                .setOutputBitmapPadding(1)
                .build();
    }

    public void startQR(View view) {
        switch (view.getId()) {
            case R.id.qr_btn_positive:
                if (rbBmp.isChecked() && etContent.getVisibility() == View.VISIBLE) {
                    if (!TextUtils.isEmpty(etContent.getText())) {
                        String getContent = etContent.getText().toString();
                        //Toast.makeText(QRDemo.this, getContent, Toast.LENGTH_SHORT).show();
                        ivQr.setVisibility(View.VISIBLE);
                        tvResult.setVisibility(View.GONE);
                        createQRcodeImg(getContent);
                        etContent.setText("");
                    }
                }else{
                    startActivityForResult(new Intent(this,ScanQRCode.class),200);
                }
                break;
            case R.id.qr_rb_makeqr_bmp:
                btnPositive.setClickable(true);
                etContent.setVisibility(View.VISIBLE);
                break;
            case R.id.qr_rb_scan:
                btnPositive.setClickable(true);
                etContent.setVisibility(View.GONE);
                break;
        }
    }

    private void createQRcodeImg(String getContent) {
        Bitmap encode = encoderBuild.encode(getContent);
        ivQr.setImageBitmap(encode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == 201) {
                ivQr.setVisibility(View.GONE);
                tvResult.setVisibility(View.VISIBLE);
                String scanResult = data.getStringExtra("scanResult");
                tvResult.setText(scanResult);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
