package cn.leeq.util.memodemo.ui;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.utils.DataNumFormat;

public class NumDateFormatChinese extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    private EditText etContent;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_date_format_chinese);
        textInputLayout = (TextInputLayout) findViewById(R.id.ndfc_layout_et);
        etContent = (EditText) findViewById(R.id.ndfc_et_content);
        tvResult = (TextView) findViewById(R.id.ndfc_tv_result);
    }

    public void startFormat(View view) {
        Log.e("test", "是否正确  " + isDateEmpty());
        if (isDateEmpty()) {
            String trim = etContent.getText().toString().trim();
            try {
                String month = DataNumFormat.formatStr(trim);
                String substring = month.substring(month.indexOf("年") + 1, month.indexOf("月"));
                Log.e("test", "初の截取 " + month + "\n末の的截取 " + substring);
                tvResult.setText(month);
            } catch (Exception e) {
                Toast.makeText(NumDateFormatChinese.this, "格式不正确", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isDateEmpty() {
        String getDate = etContent.getText().toString().trim();
        if (getDate.isEmpty()) {
            textInputLayout.setError("不是有效的日期");
            requestFocus(etContent);
            return false;
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(EditText etContent) {
        if (etContent.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    private boolean isValidDate(String getDate) {

        return true;
    }
}
