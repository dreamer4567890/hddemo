package com.example.demo.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPersonalDataActivity extends BaseUiActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int CHOOSE_PHOTO = 3;

    private Uri imageUri;

    private EditText etName;
    private Button btName;
    private CircleImageView ivHead;

    private TextView tvCamera;
    private TextView tvPicture;
    private TextView tvCancel;

    private Dialog dialog;
    private View inflate;
    //private AlertDialog alertDialog;

    @Override
    protected int getLayoutId(){
        return R.layout.activity_edit_personal_data;
    }

    @Override
    protected void initData(){
        setMyActionBar("个人资料",false);
        etName = findViewById(R.id.et_name);
        btName = findViewById(R.id.bt_name);
        ivHead = findViewById(R.id.iv_head);
        Intent intent = getIntent();
        etName.setText(intent.getStringExtra("nickname"));
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraDialog(view);
            }
        });
        btName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = etName.getText().toString();
                if(!TextUtils.isEmpty(nickname)){
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("nickname",nickname);
                    editor.apply();

                    Intent intent = new Intent();
                    intent.putExtra("name",nickname);
                    //intent.putExtra("head",ivHead.getTag().toString());
                    setResult(2,intent);
                    Toast.makeText(EditPersonalDataActivity.this,"保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void showCameraDialog(View view){
        dialog = new Dialog(this);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_camera,null);
        tvCamera = (TextView) inflate.findViewById(R.id.tv_camera);
        tvPicture = (TextView) inflate.findViewById(R.id.tv_picture);
        tvCancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        setDialogLister();
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void setDialogLister(){
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File saveImage = new File(Environment.getExternalStorageDirectory(), "saveImage.jpg");
                try {
                    if (saveImage.exists()) {
                        saveImage.delete();
                    }
                    saveImage.createNewFile();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }

                imageUri = Uri.fromFile(saveImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        tvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;

            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ivHead.setImageBitmap(bitmap);
                    }
                    catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    handleImage(data);
                }
                break;

            default:
                break;
        }
    }

    // 只在Android4.4及以上版本使用
    @TargetApi(19)
    private void handleImage(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 通过document id来处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                // 解析出数字id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        else if ("content".equals(uri.getScheme())) {
            // 如果不是document类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }

        // 根据图片路径显示图片
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ivHead.setImageBitmap(bitmap);
        }
        else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showToast(String message){
        Toast.makeText(EditPersonalDataActivity.this, message, Toast.LENGTH_LONG).show();
    }

}
