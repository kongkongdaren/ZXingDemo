package com.software.wen.zxingdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTvResult;
    private EditText mEtText;
    private ImageView mIvPhoto;
    private CheckBox mCbLogo;
    private int PERMISSION_REQUEST_CODE=0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mEtText = (EditText) findViewById(R.id.et_text);
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo);
        mCbLogo = (CheckBox) findViewById(R.id.cb_logo);
    }
    //扫描二维码
    public void scanOnClick(View view){
            //检查app是否有访问相机的权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                //如果没有就进行申请
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }else{
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                //第二个参数是请求码
                startActivityForResult(intent, 0);
            }
    }
    //生成二维码
    public void makeOnClick(View view){
        String input=mEtText.getText().toString().trim();
        if (input.equals("")){
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
        }else{

            Bitmap bitmap = EncodingUtils.createQRCode(input,
                    500,500,mCbLogo.isChecked()?
                            BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher):null);
            mIvPhoto.setImageBitmap(bitmap);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
             Bundle bundle=data.getExtras();
             String result=bundle.getString("result");
             mTvResult.setText(result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                //第二个参数是请求码
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();

            }
            return;
        }
    }
}
